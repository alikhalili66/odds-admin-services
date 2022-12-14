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
import ir.khalili.products.odds.core.dao.DAO_Transaction;
import ir.khalili.products.odds.core.enums.TransactionStatus;
import ir.khalili.products.odds.core.helper.HelperPayPod;
import ir.khalili.products.odds.core.utils.Helper;

public class Biz_02_TransactionReject {

    private static final Logger logger = LogManager.getLogger(Biz_02_TransactionReject.class);

    public static void reject(Vertx vertx, SQLConnection sqlConnection, JsonObject message, Handler<AsyncResult<JsonObject>> resultHandler) {

    	logger.trace("inputMessage:" + message);
    	
    	final int id = message.getInteger("id");
    	
    	Future<JsonObject> futTransactionFetchByTransactionId = DAO_Transaction.fetchTransactionById(sqlConnection, id);
    	
    	CompositeFuture.all(futTransactionFetchByTransactionId, Helper.createFutureVoid()).onComplete(joinHandler01 -> {
            if (joinHandler01.failed()) {
            	logger.error("Unable to complete joinHandler01: " + joinHandler01.cause());
                resultHandler.handle(Future.failedFuture(joinHandler01.cause()));
                return;
            }
            
            JsonObject joTransaction = futTransactionFetchByTransactionId.result();
            
            logger.trace("transaction:" + joTransaction);
            
            if(!joTransaction.getString("STATUS").equals(TransactionStatus.pending.getStatus())) {
            	logger.error("TRANSACTION_STATUS_FAILED");
            	resultHandler.handle(Future.failedFuture(new BIZEXCP_Transaction(-100, "وضعیت تراکنش در حال بررسی نمی باشد.")));
                return;
            }
            
            Future<String> futCheck = HelperPayPod.checkTransaction(joTransaction.getString("INVOICEID"), joTransaction.getString("USERNAME"));

            futCheck.onComplete(joinHandler02->{
            	if (joinHandler02.failed()) {
            		logger.error("Unable to complete joinHandler02: " + joinHandler02.cause());
                    resultHandler.handle(Future.failedFuture(joinHandler02.cause()));
                    return;
                }
            	
            	HelperPayPod.rejectTransaction(joTransaction.getString("USERNAME"), joTransaction.getString("USERNAME")).onComplete(joinHandler03 -> {
                   
            		if (joinHandler03.failed()) {
            			logger.error("Unable to complete joinHandler03: " + joinHandler03.cause());
                        resultHandler.handle(Future.failedFuture(joinHandler03.cause()));
                        return;
                    }
                    
                    DAO_Transaction.updateTransactionStatus(sqlConnection, id, TransactionStatus.reject.getStatus()).onComplete(joinHandler04->{
                    	 if (joinHandler04.failed()) {
                    		 logger.error("Unable to complete joinHandler04: " + joinHandler04.cause());
                             resultHandler.handle(Future.failedFuture(joinHandler04.cause()));
                             return;
                         }
                    	 
                    	 logger.trace("TRANSACTION_REJECT_DONE");
                    	 
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
