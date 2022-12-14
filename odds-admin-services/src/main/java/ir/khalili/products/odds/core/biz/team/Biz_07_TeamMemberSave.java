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

public class Biz_07_TeamMemberSave {

    private static final Logger logger = LogManager.getLogger(Biz_07_TeamMemberSave.class);

    public static void save(Vertx vertx, SQLConnection sqlConnection, JsonObject message, Handler<AsyncResult<JsonObject>> resultHandler) {

        logger.trace("inputMessage:" + message);
        
        DAO_Team.saveMember(sqlConnection, message).onComplete(handler -> {
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
    }

}
