package ir.khalili.products.odds.core.biz.transaction;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import io.vertx.core.AsyncResult;
import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.sql.SQLConnection;
import ir.khalili.products.odds.core.dao.DAO_Config;
import ir.khalili.products.odds.core.dao.DAO_League;
import ir.khalili.products.odds.core.dao.DAO_Transaction;
import ir.khalili.products.odds.core.dao.DAO_User;
import ir.khalili.products.odds.core.excp.dao.DAOEXCP_Internal;

public class Biz_04_TransactionSave {

    private static final Logger logger = LogManager.getLogger(Biz_04_TransactionSave.class);

    public static void confirm(Vertx vertx, SQLConnection sqlConnection, JsonObject message, Handler<AsyncResult<JsonObject>> resultHandler) {

        logger.trace("inputMessage:" + message);

        final String applicationCode = message.getString("applicationCode");
        final Integer amount = message.getInteger("amount");
        final String invoiceId = message.getString("invoiceId");
        final String description = message.getString("description");
        final String UGID = message.getString("userId");
        final String date = message.getString("date");
        
        if (!applicationCode.matches("operator_charge_service|bill_pay_service")) {
        	logger.error("INALID_APPLICATIONCODE:" + applicationCode);
        	resultHandler.handle(Future.succeededFuture(
					new JsonObject()
					.put("resultCode", 1)
					.put("resultMessage", "عملیات با موفقیت انجام شد.")
					));
       	 	return;
       
		}
        
        DAO_Config.fetchActiveLeague(sqlConnection).onComplete(leagueHandler->{
        	
        	if (leagueHandler.failed()) {
                logger.error("Unable to complete leagueHandler: " + leagueHandler.cause());
                resultHandler.handle(Future.failedFuture(leagueHandler.cause()));
                return;
            }

        	final Integer leagueId = leagueHandler.result();
        	
        	Future<JsonObject> futPoint = DAO_Config.fetchBySymbol(sqlConnection, leagueId, "POINTS_PER_TRANSACTION");
        	Future<JsonObject> futAmount = DAO_Config.fetchBySymbol(sqlConnection, leagueId, "AMOUNT_PER_TRANSACTION");
        	Future<JsonObject> futLeague = DAO_League.fetchValidLeagueById(sqlConnection, leagueId);
        	Future<JsonObject> futUser = DAO_User.fetchUserByUGID(sqlConnection, leagueId, UGID);
        	
        	CompositeFuture.join(futPoint, futAmount, futLeague, futUser).onComplete(joinHandler01 -> {
        		
        		if (joinHandler01.failed()) {
        			logger.error("Unable to complete joinHandler01: " + joinHandler01.cause());
        			resultHandler.handle(Future.failedFuture(joinHandler01.cause()));
        			return;
        		}
        		
    			logger.trace("AMOUNT_PER_TRANSACTION:" + futAmount.result().getString("VALUE") + ",POINTS_PER_TRANSACTION:"+ futPoint.result().getString("VALUE"));

    			
        		
        		int minAmount = Integer.parseInt(futAmount.result().getString("VALUE"));
        		int point = (amount/ minAmount) * Integer.parseInt(futPoint.result().getString("VALUE"));

        		if(minAmount > amount) {
                	logger.error("INALID_AMOUNT:" + amount);
                	resultHandler.handle(Future.succeededFuture(
        					new JsonObject()
        					.put("resultCode", 1)
        					.put("resultMessage", "عملیات با موفقیت انجام شد.")
        					));
               	 	return;
               
        		}
        		
    			sqlConnection.setAutoCommit(false, autoCommitHandler -> {

    				if (autoCommitHandler.failed()) {
    					logger.error("Unable to setAutoCommit set to false" + autoCommitHandler.cause());
    					resultHandler.handle(Future.failedFuture(new DAOEXCP_Internal(-100, "خطای داخلی. با راهبر سامانه تماس بگیرید.")));
    					return;
    				}

    				Future<Integer> futTransaction = DAO_Transaction.saveTransaction(sqlConnection, leagueId, point, amount, futUser.result().getInteger("ID"), applicationCode, invoiceId, description, date);
            		Future<Void> futSaveUserPointHistory = DAO_User.saveUserPointHistory(sqlConnection, new JsonObject().put("ID", futUser.result().getInteger("ID")).put("AMOUNT", amount).put("POINT", point), "T", description);
            		Future<Void> futUpdateUserPointAndAmount = DAO_User.updateUserPointAndAmount(sqlConnection, point, 0l, futUser.result().getInteger("ID"));
            		
            		CompositeFuture.all(futTransaction , futSaveUserPointHistory, futUpdateUserPointAndAmount).onComplete(joinHandler04 -> {
            			
            			if (joinHandler04.failed()) {
            				sqlConnection.rollback(rollBackHandler -> {
    							if (rollBackHandler.failed()) {
    								logger.error("RollBackFailed", rollBackHandler.cause());
    							}
            				});
            				resultHandler.handle(Future.failedFuture(joinHandler04.cause()));
            				return;
            			}
            			
            			sqlConnection.commit(commitHandler -> {
							if (commitHandler.failed()) {

								logger.error("Unable to get accessQueryResult:", commitHandler.cause());

								sqlConnection.rollback(rollBackHandler -> {
									if (rollBackHandler.failed()) {
										logger.error("RollBackFailed", rollBackHandler.cause());
									}
								});

								resultHandler.handle(Future.failedFuture(new DAOEXCP_Internal(-100, "خطای داخلی. با راهبر سامانه تماس بگیرید.")));
								return;
							} 

							logger.trace("TRANSACTION_CONFIRM_DONE");
	            			resultHandler.handle(Future.succeededFuture(
	            					new JsonObject()
	            					.put("resultCode", 1)
	            					.put("resultMessage", "عملیات با موفقیت انجام شد.")
	            					));

						});
            			
            		});
    				
    			});
        		
//        		Future<Integer> futTransaction = DAO_Transaction.saveTransaction(sqlConnection, point, amount, futUser.result().getInteger("ID"), applicationCode, invoiceId, description, date);
//        		Future<Void> futSaveUserPointHistory = DAO_User.saveUserPointHistory(sqlConnection, new JsonObject().put("ID", futUser.result().getInteger("ID")).put("AMOUNT", amount).put("POINT", point), "T", description);
//        		Future<Void> futUpdateUserPointAndAmount = DAO_Competition.updateUserPointAndAmount(sqlConnection, point, 0l, futUser.result().getInteger("ID"));
//        		
//        		CompositeFuture.all(futTransaction , futSaveUserPointHistory, futUpdateUserPointAndAmount).onComplete(joinHandler04 -> {
//        			
//        			if (joinHandler04.failed()) {
//        				resultHandler.handle(Future.failedFuture(joinHandler04.cause()));
//        				return;
//        			}
//        			logger.trace("TRANSACTION_CONFIRM_DONE");
//        			resultHandler.handle(Future.succeededFuture(
//        					new JsonObject()
//        					.put("resultCode", 1)
//        					.put("resultMessage", "عملیات با موفقیت انجام شد.")
//        					));
//        		});
        		
        	});
        });
        
    }

}
