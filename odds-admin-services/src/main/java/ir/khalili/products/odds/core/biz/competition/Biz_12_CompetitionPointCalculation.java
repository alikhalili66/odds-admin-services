package ir.khalili.products.odds.core.biz.competition;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
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
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.sql.SQLConnection;
import ir.khalili.products.odds.core.constants.AppConstants;
import ir.khalili.products.odds.core.dao.DAO_Competition;
import ir.khalili.products.odds.core.dao.DAO_Odds;
import ir.khalili.products.odds.core.dao.DAO_User;
import ir.khalili.products.odds.core.utils.Configuration;

public class Biz_12_CompetitionPointCalculation {

    private static final Logger logger = LogManager.getLogger(Biz_12_CompetitionPointCalculation.class);

    @SuppressWarnings({ "rawtypes", "unchecked" })
	public static void calculate(Vertx vertx, SQLConnection sqlConnection, JsonObject message, Handler<AsyncResult<JsonObject>> resultHandler) {

        logger.trace("inputMessage:" + message);

        final Integer competitionId = message.getInteger("competitionId");

        logger.trace("CALCULATE_TOTAL_POINT_FOR_THIS_COMPETITION_PER_QUESTION_ID");
        
        Future<JsonObject> futCompetition = DAO_Competition.fetchById(sqlConnection, competitionId);
        Future<List<JsonObject>> futTotalPointResult = DAO_Competition.fetchOddsTotalPointByCompetitionId(sqlConnection, competitionId);
        
        long timer00 = new Date().getTime();
        
        CompositeFuture.join(futCompetition, futTotalPointResult).onComplete(joinHandler01 -> {
            if (joinHandler01.failed()) {
            	logger.error("Unable to complete joinHandler01: " + joinHandler01.cause());
                resultHandler.handle(Future.failedFuture(joinHandler01.cause()));
                return;
            }
            
            long timer01 = new Date().getTime();
            
            logger.trace("timer00 : " + (new Date().getTime() - timer00));
            
            List<JsonObject> totalPointList = futTotalPointResult.result();
            logger.trace("TOTAL_POINT_LIST : " + totalPointList);
            
            Map<Integer, Future<List<JsonObject>>> mapWinners = new HashMap<>();
            Map<Integer, Future<List<JsonObject>>> mapLosers = new HashMap<>();
            List<Future> futList = new ArrayList<>();
            
            logger.trace("PREPARE_LIST_OF_FUTURES_THAT_WILL_FETCH_WINNER_AND_LOSER_USERS_PER_QUESTION_ID");
            for (Iterator<JsonObject> obj = totalPointList.iterator(); obj.hasNext();) {
				JsonObject joTotalPoint = (JsonObject) obj.next();
				
				Future<List<JsonObject>> futWinner = DAO_Competition.fetchOddsWinnerUsersByCompetitionIdAndQuestionId(
						sqlConnection,
						competitionId,
						joTotalPoint.getInteger("QUESTION_ID"));
				
				futList.add(futWinner);
				mapWinners.put(joTotalPoint.getInteger("QUESTION_ID"), futWinner);
				
				Future<List<JsonObject>> futLoser = DAO_Competition.fetchOddsLoserUsersByCompetitionIdAndQuestionId(
						sqlConnection,
						competitionId,
						joTotalPoint.getInteger("QUESTION_ID"));
				
				futList.add(futLoser);
				mapLosers.put(joTotalPoint.getInteger("QUESTION_ID"), futLoser);
				
			}
            
            CompositeFuture.all(futList).onComplete(joinHandler02 -> {
            	
                if (joinHandler02.failed()) {
                	logger.error("Unable to complete result1: " + joinHandler02.cause());
                    resultHandler.handle(Future.failedFuture(joinHandler02.cause()));
                    return;
                }
                
                long timer02 = new Date().getTime();
                
                logger.trace("timer01 : " + (new Date().getTime() - timer01));
                
                vertx.executeBlocking(blockingHandler -> {
                	
                	List<Future> futList2 = new ArrayList<>();
                    
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
            		
            		blockingHandler.complete(futList2);
                    
                }, resultBlockHandler->{
        			if(resultBlockHandler.failed()) {
        				resultHandler.handle(Future.failedFuture(resultBlockHandler.cause()));
                        return;
        			}
        			

    				
    				long timer03 = new Date().getTime();
                    
                    logger.trace("timer02 : " + (new Date().getTime() - timer02));
    				
    				List<Future> futList2 = (List<Future>) resultBlockHandler.result();
    				
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
                        	
                        	long timer04 = new Date().getTime();
                            
                            logger.trace("timer03 : " + (new Date().getTime() - timer03));
                        	
                        	// Update user point and save userPoint history
                        	Future<Void> futCalculate = DAO_Competition.updateUserPointForCalculation(sqlConnection, futRewardPoint.result(), competitionId);
                        	Future<Void> futUpdateCorrectAnswers = DAO_Odds.updateCorrectAnswers(sqlConnection, competitionId);

                            CompositeFuture.all(futCalculate, futUpdateCorrectAnswers).onComplete(joinHandler05 -> {
                                if (joinHandler05.failed()) {
                                	logger.error("Unable to complete joinHandler05: " + joinHandler05.cause());
                                    resultHandler.handle(Future.failedFuture(joinHandler05.cause()));
                                    return;
                                }
                                
                                long timer05 = new Date().getTime();
                                
                                logger.trace("timer04 : " + (new Date().getTime() - timer04));
                                
                                Future<Void> futDelete = DAO_User.deleteUserPointHistory(sqlConnection, futRewardPoint.result(), competitionId);
                                
                                CompositeFuture.join(Arrays.asList(futDelete)).onComplete(joinHandler06->{
                                	
                                	if (joinHandler06.failed()) {
                                    	logger.error("Unable to complete joinHandler06: " + joinHandler06.cause());
                                        resultHandler.handle(Future.failedFuture(joinHandler06.cause()));
                                        return;
                                    }
                                	
                                	
                                	logger.trace("timer05 : " + (new Date().getTime() - timer05));
                                	
                                	resultHandler.handle(Future.succeededFuture(
                                			new JsonObject()
                                			.put("resultCode", 1)
                                			.put("resultMessage", "???????????? ???? ???????????? ?????????? ????.")
                                			));
                                	
                                	saveHistory(vertx, futCompetition.result(), competitionId, futRewardPoint.result());
                                });
                                
                            });
                        	
                        });
                        
                    });
                    
                });

            });
        });
    }
    
    private static void saveHistory(Vertx vertx, JsonObject joCompetition, int competitionId, List<JsonObject> winnerUsers) {

    	long timer06 = new Date().getTime();

    	JDBCClient ircJDBC = JDBCClient.createShared(vertx, Configuration.getDataBaseConfig(),AppConstants.APP_DS_ODDS);
    	
    	ircJDBC.getConnection(connection -> {
			
    		if (connection.failed()) {
    			logger.error("Unable to get connection from database:", connection.cause());
    			return;
    		}
    		
    		SQLConnection sqlConnection = connection.result();

        	final String historyDescription = "?????????????? " + joCompetition.getString("TEAM1_NAME") + "-" + joCompetition.getString("TEAM2_NAME");
        	
        	Future<Void> futHistory = DAO_User.saveUserPointHistory(sqlConnection, winnerUsers, "W", historyDescription, competitionId);

        	CompositeFuture.join(Arrays.asList(futHistory)).onComplete(joinHandler07->{
            	
        		sqlConnection.close(handler -> {
					if(handler.failed()) {
						logger.error("IncompleteSessionClose", handler.cause());
					}
				});
        		
            	if (joinHandler07.failed()) {
                	logger.error("Unable to complete joinHandler07: " + joinHandler07.cause());
                    return;
                }
            	
                logger.trace("timer06 : " + (new Date().getTime() - timer06));
            	
            	logger.trace("COMPETITION_POINT_CALCULATION_SUCCESSFULL.");
        	
        	});
    		
		});
    }
    
}
