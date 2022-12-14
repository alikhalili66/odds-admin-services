package ir.khalili.products.odds.core.biz.team;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import io.vertx.core.AsyncResult;
import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.sql.SQLConnection;
import ir.khalili.products.odds.core.dao.DAO_League;
import ir.khalili.products.odds.core.dao.DAO_Team;
import ir.khalili.products.odds.core.enums.HistoryEnum;
import ir.khalili.products.odds.core.service.ClientMinIO;

public class Biz_06_TeamImageUpdate {

    private static final Logger logger = LogManager.getLogger(Biz_06_TeamImageUpdate.class);

    public static void updateImage(Vertx vertx, SQLConnection sqlConnection, JsonObject message, Handler<AsyncResult<JsonObject>> resultHandler) {

        logger.trace("inputMessage:" + message);

        DAO_Team.fetchById(sqlConnection, message.getInteger("teamId")).onComplete(teamHandler -> {
        	if (teamHandler.failed()) {
        		logger.error("Unable to complete handler0: " + teamHandler.cause());
        		resultHandler.handle(Future.failedFuture(teamHandler.cause()));
        		return;
        	}
        	
        	JsonObject joTeam = teamHandler.result();
        	
        	DAO_League.fetchById(sqlConnection, joTeam.getInteger("LEAGUE_ID")).onComplete(leagueHandler->{
        		
        		if (leagueHandler.failed()) {
                	logger.error("Unable to complete leagueHandler: " + leagueHandler.cause());
                    resultHandler.handle(Future.failedFuture(leagueHandler.cause()));
                    return;
                }
        		
            	ClientMinIO.saveTeamImage(vertx, leagueHandler.result().getString("SYMBOL"), joTeam.getString("SYMBOL"), message.getString("image")).onComplete(minIOHandler -> {
            		
            		if (minIOHandler.failed()) {
            			logger.error("Unable to complete minIOHandler: " + minIOHandler.cause());
            			resultHandler.handle(Future.failedFuture(minIOHandler.cause()));
            			return;
            		}
            		
            		message.put("image", minIOHandler.result());
            		joTeam.put("IMAGE", minIOHandler.result());
            		
                    Future<Void> futUpdateTeamImage = DAO_Team.updateImage(sqlConnection, message);
                    Future<Void> futSaveTeamHistory = DAO_Team.saveTeamHistory(sqlConnection,joTeam,HistoryEnum.UPDATE.getSymbol()," ", message.getInteger("userId"));
                    
                    CompositeFuture.all(futUpdateTeamImage, futSaveTeamHistory).onComplete(handler -> {
            			if (handler.failed()) {
            				logger.error("Unable to complete handler: " + handler.cause());
            				resultHandler.handle(Future.failedFuture(handler.cause()));
            				return;
            			}
            			
            			resultHandler.handle(Future.succeededFuture(
            					new JsonObject()
            					.put("resultCode", 1)
            					.put("resultMessage", "???????????? ???? ???????????? ?????????? ????.")
            					));
            			
            		});
            	});
            	
        	});
        	
        });
    }

}
