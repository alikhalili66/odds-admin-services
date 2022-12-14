package ir.khalili.products.odds.core.biz.transaction;

import java.util.List;

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

public class Biz_01_TransactionFetchAll {

    private static final Logger logger = LogManager.getLogger(Biz_01_TransactionFetchAll.class);

    public static void fetchAll(Vertx vertx, SQLConnection sqlConnection, JsonObject message, Handler<AsyncResult<JsonObject>> resultHandler) {

    	logger.trace("inputMessage:" + message);
    	
    	Future<Integer> futFetchCountAllTransaction = DAO_Transaction.fetchCountAllTransaction(sqlConnection, message);
    	Future<List<JsonObject>> futFetchAllTransaction = DAO_Transaction.fetchAllTransaction(sqlConnection, message);
    	
    	CompositeFuture.all(futFetchAllTransaction, futFetchCountAllTransaction).onComplete(result -> {
            if (result.failed()) {
            	logger.error("Unable to complete result: " + result.cause());
                resultHandler.handle(Future.failedFuture(result.cause()));
                return;
            }
            
            logger.trace("TRANSACTION_FETCH_ALL_RESULT : " + result.result());
            
            JsonObject joOutput = new JsonObject();
			joOutput.put("TotalCount", futFetchCountAllTransaction.result());
			joOutput.put("List", futFetchAllTransaction.result());
			
			resultHandler.handle(Future.succeededFuture(
					new JsonObject()
					.put("resultCode", 1)
					.put("resultMessage", "عملیات با موفقیت انجام شد.")
					.put("info", joOutput)
			));
        });
    }
}
