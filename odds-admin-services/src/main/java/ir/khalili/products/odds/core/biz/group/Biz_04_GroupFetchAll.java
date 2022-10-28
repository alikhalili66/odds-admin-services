package ir.khalili.products.odds.core.biz.group;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.sql.SQLConnection;
import ir.khalili.products.odds.core.dao.DAO_Group;

public class Biz_04_GroupFetchAll {

    private static final Logger logger = LogManager.getLogger(Biz_04_GroupFetchAll.class);

    public static void groupFetchAll(SQLConnection sqlConnection, JsonObject message, Handler<AsyncResult<JsonObject>> resultHandler) {

        DAO_Group.fetchAll(sqlConnection, message).onComplete(result -> {
            if (result.failed()) {
            	logger.error("Unable to complete result: " + result);
                resultHandler.handle(Future.failedFuture(result.cause()));
                return;
            }
            
            logger.trace("COMPETITION_FETCH_ALL_RESULT : " + result.result());
            
			resultHandler.handle(Future.succeededFuture(
					new JsonObject()
					.put("resultCode", 1)
					.put("resultMessage", "عملیات با موفقیت انجام شد.")
					.put("info", result.result())
					));

        });

    }

}
