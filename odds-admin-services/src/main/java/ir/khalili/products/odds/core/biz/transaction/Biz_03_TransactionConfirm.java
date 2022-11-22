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
import ir.khalili.products.odds.core.biz.excp.BIZEXCP_Transaction;
import ir.khalili.products.odds.core.dao.DAO_Competition;
import ir.khalili.products.odds.core.dao.DAO_Transaction;
import ir.khalili.products.odds.core.dao.DAO_User;
import ir.khalili.products.odds.core.enums.TransactionStatus;
import ir.khalili.products.odds.core.helper.HelperPayPod;
import ir.khalili.products.odds.core.utils.Helper;

public class Biz_03_TransactionConfirm {

    private static final Logger logger = LogManager.getLogger(Biz_03_TransactionConfirm.class);

    public static void confirm(Vertx vertx, SQLConnection sqlConnection, JsonObject message, Handler<AsyncResult<JsonObject>> resultHandler) {
    	
    	logger.trace("inputMessage:" + message);
    	
    	final int id = message.getInteger("id");
    	
    	Future<JsonObject> futTransaction = DAO_Transaction.fetchTransactionById(sqlConnection, id);
    	
    	CompositeFuture.all(futTransaction, Helper.createFutureVoid()).onComplete(joinHandler01 -> {
            if (joinHandler01.failed()) {
            	logger.error("Unable to complete joinHandler01: " + joinHandler01.cause());
                resultHandler.handle(Future.failedFuture(joinHandler01.cause()));
                return;
            }
            
            JsonObject joTransaction = futTransaction.result();
          
            if(!joTransaction.getString("STATUS").equals(TransactionStatus.pending.getStatus())) {
            	logger.error("TRANSACTION_STATUS_FAILED");
           	 	resultHandler.handle(Future.failedFuture(new BIZEXCP_Transaction(-100, "وضعیت تراکنش در حال بررسی نمی باشد.")));
           	 	return;
           }
            
            final String TRANSACTION_ID = joTransaction.getString("TRANSACTIONID");
            
            Future<String> futCheck = HelperPayPod.checkTransaction(joTransaction.getString("INVOICEID"), joTransaction.getString("USERNAME"));
            
            futCheck.onComplete(joinHandler02->{
            	
            	if (joinHandler02.failed()) {
            		logger.error("Unable to complete joinHandler02: " + joinHandler02.cause());
                    resultHandler.handle(Future.failedFuture(joinHandler02.cause()));
                    return;
                }
            	
            	Future<JsonObject> futFetchById = DAO_User.fetchById(sqlConnection, joTransaction.getInteger("USER_ID"));
            	Future<Void> futConfirmTransaction = HelperPayPod.confirmTransaction(joTransaction.getString("USERNAME"), TRANSACTION_ID);
            	
            	CompositeFuture.all(futFetchById, futConfirmTransaction).onComplete(joinHandler03 -> {
                    if (joinHandler03.failed()) {
                    	logger.error("Unable to complete joinHandler03: " + joinHandler03.cause());
                        resultHandler.handle(Future.failedFuture(joinHandler03.cause()));
                        return;
                    }
                  
                    JsonObject joUserInfo = new JsonObject()
                    		.put("ID", futFetchById.result().getInteger("ID"))
                    		.put("AMOUNT", joTransaction.getLong("AMOUNT"))
                    		.put("POINT", joTransaction.getInteger("POINT"));
                    
                    Future<Void> futSaveUserPointHistory = DAO_User.saveUserPointHistory(sqlConnection, joUserInfo, "B", joTransaction.getString("DESCRIPTION"));
                    Future<Void> futUpdateUserPointAndAmount = DAO_Competition.updateUserPointAndAmount(sqlConnection, joTransaction.getInteger("POINT"), joTransaction.getLong("AMOUNT"), futFetchById.result().getInteger("ID"));
                    Future<Void> futUpdateTransactionStatus = DAO_Transaction.updateTransactionStatus(sqlConnection, id, TransactionStatus.confirm.getStatus());
                    
                	CompositeFuture.all(futSaveUserPointHistory, futUpdateUserPointAndAmount, futUpdateTransactionStatus).onComplete(joinHandler04 -> {
                		if (joinHandler04.failed()) {
                			logger.error("Unable to complete joinHandler04: " + joinHandler04.cause());
                            resultHandler.handle(Future.failedFuture(joinHandler04.cause()));
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
            
        });
    }
}
