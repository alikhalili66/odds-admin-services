package ir.khalili.products.odds.core.biz.team;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.sql.SQLConnection;
import ir.khalili.products.odds.core.dao.DAO_Team;
import ir.khalili.products.odds.core.helper.HelperImage;

public class Biz_05_TeamFetchById {

    private static final Logger logger = LogManager.getLogger(Biz_05_TeamFetchById.class);

    public static void fetchById(Vertx vertx, SQLConnection sqlConnection, JsonObject message, Handler<AsyncResult<JsonObject>> resultHandler) {

		logger.trace("inputMessage:" + message);

		final Integer teamId = message.getInteger("teamId");

		DAO_Team.fetchById(sqlConnection, teamId).onComplete(result -> {
			if (result.failed()) {
				resultHandler.handle(Future.failedFuture(result.cause()));
				return;
			}

            HelperImage.getImage(vertx, result.result().getString("IMAGE")).onComplete(result0 -> {
                if (result0.failed()) {
                    resultHandler.handle(Future.failedFuture(result0.cause()));
                    return;
                }
                result.result().put("IMAGE", result0.result());
                
                logger.trace("TEAM_FETCH_BY_ID_RESULT : " + result.result());
                
                resultHandler.handle(Future.succeededFuture(
                		new JsonObject()
                		.put("resultCode", 1)
                		.put("resultMessage", "عملیات با موفقیت انجام شد.")
                		.put("info", result.result())));
            });

		});
    }

}
