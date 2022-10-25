package ir.khalili.products.odds.core.biz.user;

import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import io.vertx.core.AsyncResult;
import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.sql.SQLConnection;
import ir.khalili.products.odds.core.dao.DAO_User;

public class Biz_01_UserFetchAll {

    private static final Logger logger = LogManager.getLogger(Biz_01_UserFetchAll.class);

    public static void fetchAll(SQLConnection sqlConnection, JsonObject message, Handler<AsyncResult<JsonObject>> resultHandler) {

    	Future<Integer> fetchCountAll = DAO_User.fetchCountAll(sqlConnection, message);
    	Future<List<JsonObject>> fetchAll = DAO_User.fetchAll(sqlConnection, message);
    	
    	CompositeFuture.all(fetchCountAll, fetchAll).onComplete(result -> {
            if (result.failed()) {
            	logger.error("Unable to complete result: " + result.cause());
                resultHandler.handle(Future.failedFuture(result.cause()));
                return;
            }
            
            JsonObject joOutput = new JsonObject();
			joOutput.put("TotalCount", fetchCountAll.result());
			joOutput.put("List", fetchAll.result());
			
            logger.trace("USER_FETCH_ALL_RESULT : " + fetchAll.result());
			resultHandler.handle(Future.succeededFuture(
					new JsonObject()
					.put("resultCode", 1)
					.put("resultMessage", "عملیات با موفقیت انجام شد.")
					.put("info", joOutput)
					));

        });
    }
}
