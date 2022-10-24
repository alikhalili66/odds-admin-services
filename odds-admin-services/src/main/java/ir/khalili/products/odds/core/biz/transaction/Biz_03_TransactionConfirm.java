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
import ir.khalili.products.odds.core.dao.DAO_Competition;
import ir.khalili.products.odds.core.dao.DAO_Transaction;
import ir.khalili.products.odds.core.dao.DAO_User;
import ir.khalili.products.odds.core.enums.TransactionStatus;
import ir.khalili.products.odds.core.helper.HelperPayPod;

public class Biz_03_TransactionConfirm {

    private static final Logger logger = LogManager.getLogger(Biz_03_TransactionConfirm.class);

    public static void confirm(Vertx vertx, SQLConnection sqlConnection, JsonObject message, Handler<AsyncResult<JsonObject>> resultHandler) {
    	
    	Future<JsonObject> futTransactionFetchByTransactionId = DAO_Transaction.fetchTransactionById(sqlConnection, message.getInteger("id"));
    	Future<Void> futUpdateTransactionStatus = DAO_Transaction.updateTransactionStatus(sqlConnection, message.getInteger("id"), TransactionStatus.C.name());
    	
    	CompositeFuture.all(futTransactionFetchByTransactionId, futUpdateTransactionStatus).onComplete(result -> {
            if (result.failed()) {
                resultHandler.handle(Future.failedFuture(result.cause()));
                return;
            }
            
            Future<JsonObject> futFetchById = DAO_User.fetchById(sqlConnection, futTransactionFetchByTransactionId.result().getInteger("USER_ID"));
        	Future<Void> futRejectTransaction = HelperPayPod.rejectTransaction(futTransactionFetchByTransactionId.result().getString("USERNAME"), futTransactionFetchByTransactionId.result().getString("TRANSACTIONID"));
        	
        	CompositeFuture.all(futFetchById, futRejectTransaction).onComplete(result1 -> {
                if (result1.failed()) {
                    resultHandler.handle(Future.failedFuture(result1.cause()));
                    return;
                }
                
                Future<Void> futSaveUserPointHistory = DAO_User.saveUserPointHistory(sqlConnection, new JsonObject().put("ID", futFetchById.result().getInteger("ID")).put("AMOUNT", futFetchById.result().getLong("AMOUNT")).put("POINT", futFetchById.result().getInteger("POINT")));
                Future<Void> futUpdateUserPointAndAmount = DAO_Competition.updateUserPointAndAmount(sqlConnection, futFetchById.result().getInteger("POINT"), futFetchById.result().getLong("AMOUNT"), futFetchById.result().getInteger("ID"));
                
            	CompositeFuture.all(futSaveUserPointHistory, futUpdateUserPointAndAmount).onComplete(result2 -> {
                    if (result2.failed()) {
                        resultHandler.handle(Future.failedFuture(result2.cause()));
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
    }
}
