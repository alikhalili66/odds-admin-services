package ir.khalili.products.odds.core.biz.group;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.sql.SQLConnection;
import ir.khalili.products.odds.core.dao.DAO_Competition;

public class Biz_09_GroupCompetitionFetch {

    private static final Logger logger = LogManager.getLogger(Biz_09_GroupCompetitionFetch.class);

    public static void groupFetch(SQLConnection sqlConnection, JsonObject message, Handler<AsyncResult<JsonObject>> resultHandler) {

        logger.trace("inputMessage:" + message);

        DAO_Competition.fetchCompetitionByGroupId(sqlConnection, message.getInteger("groupId")).onComplete(handler -> {
            if (handler.failed()) {
                resultHandler.handle(Future.failedFuture(handler.cause()));
                return;
            }
            
			resultHandler.handle(Future.succeededFuture(
					new JsonObject()
					.put("resultCode", 1)
					.put("resultMessage", "عملیات با موفقیت انجام شد.")
					.put("info", handler.result())
					));

        });
    }

}
