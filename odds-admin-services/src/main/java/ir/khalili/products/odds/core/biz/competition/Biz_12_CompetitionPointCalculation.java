package ir.khalili.products.odds.core.biz.competition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import io.vertx.core.AsyncResult;
import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.sql.SQLConnection;
import ir.khalili.products.odds.core.dao.DAO_Competition;
import ir.khalili.products.odds.core.dao.DAO_User;

public class Biz_12_CompetitionPointCalculation {

    private static final Logger logger = LogManager.getLogger(Biz_12_CompetitionPointCalculation.class);

    @SuppressWarnings("rawtypes")
	public static void calculate(SQLConnection sqlConnection, JsonObject message, Handler<AsyncResult<JsonObject>> resultHandler) {

        logger.trace("inputMessage:" + message);

        final Integer competitionId = message.getInteger("competitionId");

        logger.trace("CALCULATE_TOTAL_POINT_FOR_THIS_COMPETITION_PER_QUESTION_ID");
        
        Future<JsonObject> futCompetition = DAO_Competition.fetchById(sqlConnection, competitionId);
        Future<List<JsonObject>> futTtalPointResult = DAO_Competition.fetchOddsTotalPointByCompetitionId(sqlConnection, competitionId);
        
        CompositeFuture.join(futCompetition, futTtalPointResult).onComplete(joinHandler01 -> {
            if (joinHandler01.failed()) {
            	logger.error("Unable to complete joinHandler01: " + joinHandler01.cause());
                resultHandler.handle(Future.failedFuture(joinHandler01.cause()));
                return;
            }
            
            List<JsonObject> totalPointList = futTtalPointResult.result();
            logger.trace("TOTAL_POINT_LIST : " + totalPointList);
            
            Map<Integer, Future<List<JsonObject>>> mapWinners = new HashMap<>();
            Map<Integer, Future<List<JsonObject>>> mapLosers = new HashMap<>();
            List<Future> futList = new ArrayList<>();
            
            logger.trace("PREPARE_LIST_OF_FUTURES_THAT_WILL_FETCH_WINNER_AND_LOSER_USERS_PER_QUESTION_ID");
            for (Iterator<JsonObject> obj = totalPointList.iterator(); obj.hasNext();) {
				JsonObject joTotalPoint = (JsonObject) obj.next();
				
				Future<List<JsonObject>> futFetchOddsWinnerUsersByCompetitionIdAndQuestionId = DAO_Competition.fetchOddsWinnerUsersByCompetitionIdAndQuestionId(
						sqlConnection,
						competitionId,
						joTotalPoint.getInteger("QUESTION_ID"));
				
				futList.add(futFetchOddsWinnerUsersByCompetitionIdAndQuestionId);
				mapWinners.put(joTotalPoint.getInteger("QUESTION_ID"), futFetchOddsWinnerUsersByCompetitionIdAndQuestionId);
				
				Future<List<JsonObject>> futFetchOddsLoserUsersByCompetitionIdAndQuestionId = DAO_Competition.fetchOddsLoserUsersByCompetitionIdAndQuestionId(
						sqlConnection,
						competitionId,
						joTotalPoint.getInteger("QUESTION_ID"));
				
				futList.add(futFetchOddsLoserUsersByCompetitionIdAndQuestionId);
				mapLosers.put(joTotalPoint.getInteger("QUESTION_ID"), futFetchOddsLoserUsersByCompetitionIdAndQuestionId);
				
			}
            
            CompositeFuture.all(futList).onComplete(result1 -> {
                if (result1.failed()) {
                	logger.error("Unable to complete result1: " + result1.cause());
                    resultHandler.handle(Future.failedFuture(result1.cause()));
                    return;
                }
                
                List<Future> futList2 = new ArrayList<>();
                Map<Integer, Future<JsonObject>> mapWinnerUsers = new HashMap<>();
                
                // Update reward point for winner and loser users that 0 will be set to loser users
                for (Iterator<JsonObject> obj = totalPointList.iterator(); obj.hasNext();) {
    				JsonObject joTotalPoint = (JsonObject) obj.next();
    				
    				List<JsonObject> winnerUserList = mapWinners.get(joTotalPoint.getInteger("QUESTION_ID")).result();
    				if (winnerUserList.size() > 0) {
						
    					long totalWinnerPoint = 0;
    					
    					for (Iterator<JsonObject> obj2 = winnerUserList.iterator(); obj2.hasNext();) {
    						JsonObject joWinnerUser = (JsonObject) obj2.next();
    						totalWinnerPoint = totalWinnerPoint + joWinnerUser.getInteger("POINT");
    					}
    					
    					Long rewardPoint = joTotalPoint.getLong("TOTAL_POINT").longValue() / totalWinnerPoint;
    					
    					int coefficient = (int) Math.ceil(rewardPoint) <= 2 ? 2 : (int) Math.ceil(rewardPoint);
    					List<Integer> winnerUserIdList = new ArrayList<>();
    					
    					for (Iterator<JsonObject> obj2 = winnerUserList.iterator(); obj2.hasNext();) {
    						JsonObject joWinnerUser = (JsonObject) obj2.next();
    						
    						Future<JsonObject> futWinnerUser = DAO_User.fetchById(sqlConnection, joWinnerUser.getInteger("USER_ID"));
    						futList2.add(futWinnerUser);
    						mapWinnerUsers.put(joWinnerUser.getInteger("USER_ID"), futWinnerUser);
    						winnerUserIdList.add(joWinnerUser.getInteger("USER_ID"));
    					}
    					Future<Void> futUpdateRewardPointForWinners = DAO_Competition.updateRewardPointForCalculation(sqlConnection, coefficient, joTotalPoint.getInteger("QUESTION_ID"), competitionId, winnerUserIdList);
    					futList2.add(futUpdateRewardPointForWinners);
    					
					}
                    List<JsonObject> loserUserList = mapLosers.get(joTotalPoint.getInteger("QUESTION_ID")).result();
                    List<Integer> loserUserIdList = new ArrayList<>();
                    for (Iterator<JsonObject> obj2 = loserUserList.iterator(); obj2.hasNext();) {
        				JsonObject joLoserUser = (JsonObject) obj2.next();
        				loserUserIdList.add(joLoserUser.getInteger("USER_ID"));
                    }
                    Future<Void> futUpdateRewardPointForLosers = DAO_Competition.updateRewardPointForCalculation(sqlConnection, 0, joTotalPoint.getInteger("QUESTION_ID"), competitionId, loserUserIdList);
                    futList2.add(futUpdateRewardPointForLosers);
                }
                
                final String historyDescription = "پیشبینی " + futCompetition.result().getString("TEAM1_NAME") + "-" + futCompetition.result().getString("TEAM2_NAME");
                
                CompositeFuture.all(futList2).onComplete(result2 -> {
                    if (result2.failed()) {
                    	logger.error("Unable to complete result2: " + result2.cause());
                        resultHandler.handle(Future.failedFuture(result2.cause()));
                        return;
                    }

                    // Update user point and save userPoint history
                    List<Future> futList3 = new ArrayList<>();
                    for (Iterator<JsonObject> obj = totalPointList.iterator(); obj.hasNext();) {
        				JsonObject joTotalPoint = (JsonObject) obj.next();
        				List<JsonObject> winnerUserList = mapWinners.get(joTotalPoint.getInteger("QUESTION_ID")).result();
        				
        				List<Integer> winnerUserIdList = new ArrayList<>();
        				List<JsonObject> winnerUsers = new ArrayList<>();
        				
                        for (Iterator<JsonObject> obj2 = winnerUserList.iterator(); obj2.hasNext();) {
            				JsonObject joWinnerUser = (JsonObject) obj2.next();
            				winnerUserIdList.add(joWinnerUser.getInteger("USER_ID"));
            				winnerUsers.add(mapWinnerUsers.get(joWinnerUser.getInteger("USER_ID")).result());
                        }
                        Future<Void> futUpdateRewardPointForWinners = DAO_Competition.updateUserPointForCalculation(sqlConnection, joTotalPoint.getInteger("QUESTION_ID"), competitionId, winnerUserIdList);
                        futList3.add(futUpdateRewardPointForWinners);
                        Future<Void> futSaveUserPointHistory = DAO_User.saveUserPointHistory(sqlConnection, winnerUsers, "W", historyDescription, competitionId);
                        futList3.add(futSaveUserPointHistory);
                    }
                    
                    CompositeFuture.all(futList3).onComplete(result3 -> {
                        if (result3.failed()) {
                        	logger.error("Unable to complete result3: " + result3.cause());
                            resultHandler.handle(Future.failedFuture(result3.cause()));
                            return;
                        }
                        
                        logger.trace("COMPETITION_POINT_CALCULATION_SUCCESSFULL.");
                        resultHandler.handle(Future.succeededFuture(
                        		new JsonObject()
                        		.put("resultCode", 1)
                        		.put("resultMessage", "عملیات با موفقیت انجام شد.")
                        		));
                    });
                });
            });
        });
    }
}
