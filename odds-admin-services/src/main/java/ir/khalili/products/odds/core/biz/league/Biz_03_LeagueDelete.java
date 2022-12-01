package ir.khalili.products.odds.core.biz.league;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import io.vertx.core.AsyncResult;
import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.sql.SQLConnection;
import ir.khalili.products.odds.core.dao.DAO_League;
import ir.khalili.products.odds.core.enums.HistoryEnum;

public class Biz_03_LeagueDelete {

    private static final Logger logger = LogManager.getLogger(Biz_03_LeagueDelete.class);

    public static void delete(SQLConnection sqlConnection, JsonObject message, Handler<AsyncResult<JsonObject>> resultHandler) {

        logger.trace("inputMessage:" + message);

        final Integer leagueId = message.getInteger("leagueId");

        DAO_League.fetchById(sqlConnection, leagueId).onComplete(handler0 -> {
            if (handler0.failed()) {
            	logger.error("Unable to complete handler0: " + handler0.cause());
                resultHandler.handle(Future.failedFuture(handler0.cause()));
                return;
            }
            
            JsonObject joLeague = handler0.result();
            
            Future<Void> futDeleteLeague = DAO_League.delete(sqlConnection, leagueId);
            Future<Void> futSaveLeagueHistory = DAO_League.saveHistory(sqlConnection,joLeague,HistoryEnum.DELETE.getSymbol()," ", message.getInteger("userId"));
            
            CompositeFuture.all(futDeleteLeague, futSaveLeagueHistory).onComplete(handler -> {
            	if (handler.failed()) {
            		logger.error("Unable to complete handler: " + handler.cause());
            		resultHandler.handle(Future.failedFuture(handler.cause()));
            		return;
            	}
            	logger.trace("LEAGUE_DELETE_SUCCESSFULL.");
            	
            	resultHandler.handle(Future.succeededFuture(
            			new JsonObject()
            			.put("resultCode", 1)
            			.put("resultMessage", "عملیات با موفقیت انجام شد.")
            			));
            	
            });
        });
    }

}
