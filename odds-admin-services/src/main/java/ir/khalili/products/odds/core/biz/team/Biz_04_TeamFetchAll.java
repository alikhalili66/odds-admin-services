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

public class Biz_04_TeamFetchAll {

    private static final Logger logger = LogManager.getLogger(Biz_04_TeamFetchAll.class);

    public static void fetchAll(Vertx vertx, SQLConnection sqlConnection, JsonObject message, Handler<AsyncResult<JsonObject>> resultHandler) {

        DAO_Team.fetchAll(sqlConnection, message).onComplete(result -> {
            if (result.failed()) {
            	logger.error("Unable to complete result: " + result.cause());
                resultHandler.handle(Future.failedFuture(result.cause()));
                return;
            }
            
//            HelperImage.getImage(vertx, result.result()).onComplete(result0 -> {
//                if (result0.failed()) {
//                	logger.error("Unable to complete result0: " + result0.cause());
//                    resultHandler.handle(Future.failedFuture(result0.cause()));
//                    return;
//                }
                logger.trace("COMPETITION_FETCH_ALL_RESULT : " + result.result());
                
                resultHandler.handle(Future.succeededFuture(
                		new JsonObject()
                		.put("resultCode", 1)
                		.put("resultMessage", "عملیات با موفقیت انجام شد.")
                		.put("info", result.result())
                		));
//            });

        });

    }

}
