package ir.khalili.products.odds.core.biz.location;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.sql.SQLConnection;
import ir.khalili.products.odds.core.dao.DAO_Location;
import ir.khalili.products.odds.core.helper.HelperImage;

public class Biz_01_LocationSave {

    private static final Logger logger = LogManager.getLogger(Biz_01_LocationSave.class);

    public static void save(Vertx vertx, SQLConnection sqlConnection, JsonObject message, Handler<AsyncResult<JsonObject>> resultHandler) {

        logger.trace("inputMessage:" + message);

        HelperImage.saveImage(vertx, message.getString("image"), message.getString("name")).onComplete(result -> {
            if (result.failed()) {
            	logger.error("Unable to complete result: " + result.cause());
                resultHandler.handle(Future.failedFuture(result.cause()));
                return;
            }
            message.put("image", result.result());
            DAO_Location.save(sqlConnection, message).onComplete(handler -> {
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
    }

}
