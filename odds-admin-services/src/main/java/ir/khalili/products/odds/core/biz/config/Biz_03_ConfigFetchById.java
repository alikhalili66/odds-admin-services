package ir.khalili.products.odds.core.biz.config;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.sql.SQLConnection;
import ir.khalili.products.odds.core.dao.DAO_Config;

public class Biz_03_ConfigFetchById {

    private static final Logger logger = LogManager.getLogger(Biz_03_ConfigFetchById.class);

    public static void fetchById(SQLConnection sqlConnection, JsonObject message, Handler<AsyncResult<JsonObject>> resultHandler) {

		logger.trace("inputMessage:" + message);

		final Integer configId = message.getInteger("configId");

		DAO_Config.fetchById(sqlConnection, configId).onComplete(result -> {
			if (result.failed()) {
				resultHandler.handle(Future.failedFuture(result.cause()));
				return;
			}

			logger.trace("CONFIG_FETCH_BY_ID_RESULT : " + result.result());

			resultHandler.handle(Future.succeededFuture(
					new JsonObject()
					.put("resultCode", 1)
					.put("resultMessage", "عملیات با موفقیت انجام شد.")
					.put("info", result.result())));

		});

    }

}
