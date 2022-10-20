package ir.khalili.products.odds.core.biz.transaction;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.sql.SQLConnection;
import ir.khalili.products.odds.core.dao.DAO_Transaction;
import ir.khalili.products.odds.core.enums.TransactionStatus;

public class Biz_02_TransactionReject {

    private static final Logger logger = LogManager.getLogger(Biz_02_TransactionReject.class);

    public static void reject(Vertx vertx, SQLConnection sqlConnection, JsonObject message, Handler<AsyncResult<JsonObject>> resultHandler) {

        DAO_Transaction.doUpdateTransactionStatus(sqlConnection, message.getInteger("transactionId"), TransactionStatus.R.name()).onComplete(result -> {
            if (result.failed()) {
                resultHandler.handle(Future.failedFuture(result.cause()));
                return;
            }
            
            logger.trace("TRANSACTION_REJECT_DONE");
            
			resultHandler.handle(Future.succeededFuture(
					new JsonObject()
					.put("resultCode", 1)
					.put("resultMessage", "عملیات با موفقیت انجام شد.")
			));
        });
    }
}
