package ir.khalili.products.odds.core.biz.league;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.sql.SQLConnection;
import ir.khalili.products.odds.core.dao.DAO_League;

public class Biz_05_LeagueFetchById {

    private static final Logger logger = LogManager.getLogger(Biz_05_LeagueFetchById.class);

    public static void fetchById(SQLConnection sqlConnection, JsonObject message, Handler<AsyncResult<JsonObject>> resultHandler) {

		logger.trace("inputMessage:" + message);

		final Integer leagueId = message.getInteger("leagueId");

		DAO_League.fetchById(sqlConnection, leagueId).onComplete(result -> {
			if (result.failed()) {
				logger.error("Unable to complete result: " + result.cause());
				resultHandler.handle(Future.failedFuture(result.cause()));
				return;
			}

			logger.trace("LEAGUE_FETCH_BY_ID_RESULT : " + result.result());

			resultHandler.handle(Future.succeededFuture(
					new JsonObject()
					.put("resultCode", 1)
					.put("resultMessage", "عملیات با موفقیت انجام شد.")
					.put("info", result.result())));

		});

    }

}
