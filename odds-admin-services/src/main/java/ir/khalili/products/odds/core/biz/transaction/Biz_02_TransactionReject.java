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
import ir.khalili.products.odds.core.dao.DAO_Transaction;
import ir.khalili.products.odds.core.enums.TransactionStatus;
import ir.khalili.products.odds.core.helper.HelperPayPod;

public class Biz_02_TransactionReject {

    private static final Logger logger = LogManager.getLogger(Biz_02_TransactionReject.class);

    public static void reject(Vertx vertx, SQLConnection sqlConnection, JsonObject message, Handler<AsyncResult<JsonObject>> resultHandler) {

    	Future<JsonObject> futTransactionFetchByTransactionId = DAO_Transaction.fetchTransactionById(sqlConnection, message.getInteger("id"));
    	Future<Void> futUpdateTransactionStatus = DAO_Transaction.updateTransactionStatus(sqlConnection, message.getInteger("id"), TransactionStatus.R.name());
    	
    	CompositeFuture.all(futTransactionFetchByTransactionId, futUpdateTransactionStatus).onComplete(result -> {
            if (result.failed()) {
                resultHandler.handle(Future.failedFuture(result.cause()));
                return;
            }
            
        	HelperPayPod.rejectTransaction(futTransactionFetchByTransactionId.result().getString("USERNAME"), futTransactionFetchByTransactionId.result().getString("TRANSACTIONID")).onComplete(result1 -> {
                if (result1.failed()) {
                    resultHandler.handle(Future.failedFuture(result1.cause()));
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
    }
}
