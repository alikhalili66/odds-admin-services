package ir.khalili.products.odds.core.biz.config;

import java.io.BufferedReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.sql.SQLConnection;
import ir.khalili.products.odds.core.biz.excp.BIZEXCP_Invalid;
import ir.khalili.products.odds.core.dao.DAO_Config;

public class Biz_03_ConfigFetchById {

    private static final Logger logger = LogManager.getLogger(Biz_03_ConfigFetchById.class);

    public static void fetchById(SQLConnection sqlConnection, JsonObject message, Handler<AsyncResult<JsonObject>> resultHandler) {

		logger.trace("inputMessage:" + message);

		final Integer configId = message.getInteger("configId");

		DAO_Config.fetchById(sqlConnection, configId).onComplete(result -> {
			if (result.failed()) {
				logger.error("Unable to complete result: " + result.cause());
				resultHandler.handle(Future.failedFuture(result.cause()));
				return;
			}

			logger.trace("CONFIG_FETCH_BY_ID_RESULT : " + result.result());

			if(result.result().getString("TYPE").equals("File")) {
				StringBuffer buffer = new StringBuffer();

				try {

					try(BufferedReader reader = Files.newBufferedReader(Paths.get(result.result().getString("VALUE")), StandardCharsets.UTF_8)){
						int ch = 0;
						while ((ch = reader.read()) != -1) {
							buffer.append((char) ch + reader.readLine());
						}
					}

					result.result().put("VALUE", buffer.toString());

				} catch (Exception e) {
					logger.error("ERROR_WHEN_CONVERTING_FILE");
					e.printStackTrace();
					resultHandler.handle(Future.failedFuture(new BIZEXCP_Invalid("خطای در تبدیل فایل. با راهبر سامانه تماس بگیرید.")));
					return;
				}
			}
			
			resultHandler.handle(Future.succeededFuture(
					new JsonObject()
					.put("resultCode", 1)
					.put("resultMessage", "عملیات با موفقیت انجام شد.")
					.put("info", result.result())));

		});

    }

}
