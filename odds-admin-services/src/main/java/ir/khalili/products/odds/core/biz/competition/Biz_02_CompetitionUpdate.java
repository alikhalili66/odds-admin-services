package ir.khalili.products.odds.core.biz.competition;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import io.vertx.core.AsyncResult;
import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.sql.SQLConnection;
import ir.khalili.products.odds.core.dao.DAO_Competition;
import ir.khalili.products.odds.core.enums.HistoryEnum;

public class Biz_02_CompetitionUpdate {

    private static final Logger logger = LogManager.getLogger(Biz_02_CompetitionUpdate.class);

    public static void update(SQLConnection sqlConnection, JsonObject message, Handler<AsyncResult<JsonObject>> resultHandler) {

        logger.trace("inputMessage:" + message);
        
        DAO_Competition.fetchById(sqlConnection, message.getInteger("competitionId")).onComplete(handler0 -> {
            if (handler0.failed()) {
            	logger.error("Unable to complete handler0: " + handler0.cause());
                resultHandler.handle(Future.failedFuture(handler0.cause()));
                return;
            }
            
            JsonObject joCompetition = handler0.result();
            
            Future<Void> futUpdateCompetition = DAO_Competition.update(sqlConnection, message);
            Future<Void> futSaveCompetitionHistory = DAO_Competition.saveHistory(sqlConnection,joCompetition,HistoryEnum.UPDATE.getSymbol()," ", message.getInteger("userId"));
            
            CompositeFuture.all(futUpdateCompetition, futSaveCompetitionHistory).onComplete(handler -> {
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
