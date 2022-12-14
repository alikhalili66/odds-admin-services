package ir.khalili.products.odds.core.biz.league;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import io.vertx.core.AsyncResult;
import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.sql.SQLConnection;
import ir.khalili.products.odds.core.dao.DAO_Config;
import ir.khalili.products.odds.core.dao.DAO_League;

public class Biz_01_LeagueSave {

    private static final Logger logger = LogManager.getLogger(Biz_01_LeagueSave.class);

    public static void save(SQLConnection sqlConnection, JsonObject message, Handler<AsyncResult<JsonObject>> resultHandler) {

        logger.trace("inputMessage:" + message);

        DAO_League.fetchSequence(sqlConnection).onComplete(handlerSeq->{
        	if (handlerSeq.failed()) {
        		logger.error("Unable to complete handlerSeq: " + handlerSeq.cause());
                resultHandler.handle(Future.failedFuture(handlerSeq.cause()));
                return;
            }
        	
        	Future<Void> futSave = DAO_League.save(sqlConnection, handlerSeq.result(), message);
        	Future<Void> futConfig = DAO_Config.doSaveConfigLeague(sqlConnection, handlerSeq.result());
            
            CompositeFuture.join(futSave, futConfig).onComplete(handler -> {
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
