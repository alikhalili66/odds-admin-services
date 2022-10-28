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

public class Biz_06_TeamImageUpdate {

    private static final Logger logger = LogManager.getLogger(Biz_06_TeamImageUpdate.class);

    public static void updateImage(Vertx vertx, SQLConnection sqlConnection, JsonObject message, Handler<AsyncResult<JsonObject>> resultHandler) {

        logger.trace("inputMessage:" + message);

        DAO_Team.fetchById(sqlConnection, message.getInteger("teamId")).onComplete(handler0 -> {
        	if (handler0.failed()) {
        		logger.error("Unable to complete handler0: " + handler0.cause());
        		resultHandler.handle(Future.failedFuture(handler0.cause()));
        		return;
        	}
        	HelperImage.saveImage(vertx, message.getString("image"), handler0.result().getString("NAME")).onComplete(result -> {
        		if (result.failed()) {
        			logger.error("Unable to complete result: " + result.cause());
        			resultHandler.handle(Future.failedFuture(result.cause()));
        			return;
        		}
        		message.put("image", result.result());
        		DAO_Team.updateImage(sqlConnection, message).onComplete(handler -> {
        			if (handler.failed()) {
        				logger.error("Unable to complete handler: " + handler.cause());
        				resultHandler.handle(Future.failedFuture(handler.cause()));
        				return;
        			}
        			
        			resultHandler.handle(Future.succeededFuture(
        					new JsonObject()
        					.put("resultCode", 1)
        					.put("resultMessage", "عملیات با موفقیت انجام شد.")
        					));
        			
        		});
        	});
        });
    }

}
