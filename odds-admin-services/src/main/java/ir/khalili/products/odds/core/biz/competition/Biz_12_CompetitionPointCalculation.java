package ir.khalili.products.odds.core.biz.competition;

import java.util.ArrayList;
import java.util.Arrays;
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
import ir.khalili.products.odds.core.dao.DAO_Odds;
import ir.khalili.products.odds.core.dao.DAO_User;

public class Biz_12_CompetitionPointCalculation {

    private static final Logger logger = LogManager.getLogger(Biz_12_CompetitionPointCalculation.class);

    @SuppressWarnings("rawtypes")
	public static void calculate(SQLConnection sqlConnection, JsonObject message, Handler<AsyncResult<JsonObject>> resultHandler) {

        logger.trace("inputMessage:" + message);

        final Integer competitionId = message.getInteger("competitionId");

        logger.trace("CALCULATE_TOTAL_POINT_FOR_THIS_COMPETITION_PER_QUESTION_ID");
        
        Future<JsonObject> futCompetition = DAO_Competition.fetchById(sqlConnection, competitionId);
        Future<List<JsonObject>> futTotalPointResult = DAO_Competition.fetchOddsTotalPointByCompetitionId(sqlConnection, competitionId);
        
        CompositeFuture.join(futCompetition, futTotalPointResult).onComplete(joinHandler01 -> {
            if (joinHandler01.failed()) {
            	logger.error("Unable to complete joinHandler01: " + joinHandler01.cause());
                resultHandler.handle(Future.failedFuture(joinHandler01.cause()));
                return;
            }
            
            List<JsonObject> totalPointList = futTotalPointResult.result();
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
            
            CompositeFuture.all(futList).onComplete(joinHandler02 -> {
                if (joinHandler02.failed()) {
                	logger.error("Unable to complete result1: " + joinHandler02.cause());
                    resultHandler.handle(Future.failedFuture(joinHandler02.cause()));
                    return;
                }
                
                List<Future> futList2 = new ArrayList<>();
//                Map<Integer, Future<JsonObject>> mapWinnerUsers = new HashMap<>();
                
                // Update reward point for winner and loser users that 0 will be set to loser users
                for (Iterator<JsonObject> obj = totalPointList.iterator(); obj.hasNext();) {
    				JsonObject joTotalPoint = (JsonObject) obj.next();
    				
    				List<JsonObject> winnerUserList = mapWinners.get(joTotalPoint.getInteger("QUESTION_ID")).result();
    				if (winnerUserList.size() > 0) {
						
    					long totalWinnerPoint = 0;
    					
    					for (JsonObject joWinnerUser : winnerUserList) {
    						totalWinnerPoint = totalWinnerPoint + joWinnerUser.getInteger("POINT");
						}
    					
    					Long rewardPoint = joTotalPoint.getLong("TOTAL_POINT").longValue() / totalWinnerPoint;
    					
    					int coefficient = (int) Math.ceil(rewardPoint) <= 2 ? 2 : (int) Math.ceil(rewardPoint);
    					List<Integer> winnerUserIdList = new ArrayList<>();
    					
    					for (Iterator<JsonObject> obj2 = winnerUserList.iterator(); obj2.hasNext();) {
    						JsonObject joWinnerUser = (JsonObject) obj2.next();
    						winnerUserIdList.add(joWinnerUser.getInteger("USER_ID"));
    					}
    					futList2.add(DAO_Competition.updateRewardPointForCalculation(sqlConnection, coefficient, joTotalPoint.getInteger("QUESTION_ID"), competitionId, winnerUserIdList));
    					
					}
                    List<JsonObject> loserUserList = mapLosers.get(joTotalPoint.getInteger("QUESTION_ID")).result();
                    if (loserUserList.size() > 0) {
                    	List<Integer> loserUserIdList = new ArrayList<>();
                    	for (JsonObject joLoserUser : loserUserList) {
                    		loserUserIdList.add(joLoserUser.getInteger("USER_ID"));
                    	}
                    	futList2.add(DAO_Competition.updateRewardPointForCalculation(sqlConnection, 0, joTotalPoint.getInteger("QUESTION_ID"), competitionId, loserUserIdList));
                    }
                }
                
                final String historyDescription = "پیشبینی " + futCompetition.result().getString("TEAM1_NAME") + "-" + futCompetition.result().getString("TEAM2_NAME");
                
                CompositeFuture.all(futList2).onComplete(joinHandler03 -> {
                	
                    if (joinHandler03.failed()) {
                    	logger.error("Unable to complete result2: " + joinHandler03.cause());
                        resultHandler.handle(Future.failedFuture(joinHandler03.cause()));
                        return;
                    }

                    Future<List<JsonObject>> futRewardPoint = DAO_Competition.fetchTotalUserPointOfCompetition(sqlConnection, competitionId);
                    
                    CompositeFuture.join(Arrays.asList(futRewardPoint)).onComplete(joinHandler04->{
                    	
                    	if (joinHandler03.failed()) {
                        	logger.error("Unable to complete joinHandler04: " + joinHandler04.cause());
                            resultHandler.handle(Future.failedFuture(joinHandler04.cause()));
                            return;
                        }
                    	
                    	// Update user point and save userPoint history
                    	Future<Void> futHistory = DAO_User.saveUserPointHistory(sqlConnection, futRewardPoint.result(), "W", historyDescription, competitionId);
                    	Future<Void> futCalculate = DAO_Competition.updateUserPointForCalculation(sqlConnection, futRewardPoint.result(), competitionId);
                    	Future<Void> futUpdateCorrectAnswers = DAO_Odds.updateCorrectAnswers(sqlConnection, competitionId);

                        CompositeFuture.all(futHistory, futCalculate, futUpdateCorrectAnswers).onComplete(joinHandler05 -> {
                            if (joinHandler05.failed()) {
                            	logger.error("Unable to complete joinHandler05: " + joinHandler05.cause());
                                resultHandler.handle(Future.failedFuture(joinHandler05.cause()));
                                return;
                            }
                            
                            Future<Void> futDelete = DAO_User.deleteUserPointHistory(sqlConnection, futRewardPoint.result(), competitionId);
                            
                            CompositeFuture.join(Arrays.asList(futDelete)).onComplete(joinHandler06->{
                            	
                            	if (joinHandler06.failed()) {
                                	logger.error("Unable to complete joinHandler06: " + joinHandler06.cause());
                                    resultHandler.handle(Future.failedFuture(joinHandler06.cause()));
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
            });
        });
    }
}
