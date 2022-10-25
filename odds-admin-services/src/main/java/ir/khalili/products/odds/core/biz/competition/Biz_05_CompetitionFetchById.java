package ir.khalili.products.odds.core.biz.competition;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.sql.SQLConnection;
import ir.khalili.products.odds.core.dao.DAO_Competition;

public class Biz_05_CompetitionFetchById {

    private static final Logger logger = LogManager.getLogger(Biz_05_CompetitionFetchById.class);

    public static void fetchById(SQLConnection sqlConnection, JsonObject message, Handler<AsyncResult<JsonObject>> resultHandler) {

        logger.trace("inputMessage:" + message);

        final Integer competitionId = message.getInteger("competitionId");

        DAO_Competition.fetchById(sqlConnection, competitionId).onComplete(result -> {
            if (result.failed()) {
            	logger.error("Unable to complete handle: " + result.cause());
                resultHandler.handle(Future.failedFuture(result.cause()));
                return;
            }
            
            logger.trace("COMPETITION_FETCH_BY_ID_RESULT : " + result.result());
            
			resultHandler.handle(Future.succeededFuture(
					new JsonObject()
					.put("resultCode", 1)
					.put("resultMessage", "عملیات با موفقیت انجام شد.")
					.put("info", result.result())
					));

        });

    }

}
