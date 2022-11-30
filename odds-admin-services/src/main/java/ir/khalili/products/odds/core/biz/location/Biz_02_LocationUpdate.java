package ir.khalili.products.odds.core.biz.location;

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
import ir.khalili.products.odds.core.dao.DAO_Location;
import ir.khalili.products.odds.core.enums.HistoryEnum;
import ir.khalili.products.odds.core.service.ClientMinIO;

public class Biz_02_LocationUpdate {

    private static final Logger logger = LogManager.getLogger(Biz_02_LocationUpdate.class);

    public static void update(Vertx vertx, SQLConnection sqlConnection, JsonObject message, Handler<AsyncResult<JsonObject>> resultHandler) {

        logger.trace("inputMessage:" + message);

        DAO_Location.fetchById(sqlConnection, message.getInteger("locationId")).onComplete(locationHandler -> {
        	
        	if (locationHandler.failed()) {
        		logger.error("Unable to complete handler0: " + locationHandler.cause());
        		resultHandler.handle(Future.failedFuture(locationHandler.cause()));
        		return;
        	}
        	
        	JsonObject joLocation = locationHandler.result();
        	
        	DAO_League.fetchById(sqlConnection, joLocation.getInteger("LEAGUE_ID")).onComplete(leagueHandler->{
        		
        		if (leagueHandler.failed()) {
                	logger.error("Unable to complete leagueHandler: " + leagueHandler.cause());
                    resultHandler.handle(Future.failedFuture(leagueHandler.cause()));
                    return;
                }
        		
            	ClientMinIO.saveLocationImage(vertx, leagueHandler.result().getString("SYMBOL"), message.getString("name"), message.getString("image")).onComplete(minIOHandler -> {
            		
            		if (minIOHandler.failed()) {
            			logger.error("Unable to complete minIOHandler: " + minIOHandler.cause());
            			resultHandler.handle(Future.failedFuture(minIOHandler.cause()));
            			return;
            		}
            		
            		message.put("image", minIOHandler.result());
            		
                    Future<Void> futUpdateLocation = DAO_Location.update(sqlConnection, message);
                    Future<Void> futSaveLocationHistory = DAO_Location.saveHistory(sqlConnection, joLocation,HistoryEnum.UPDATE.getSymbol()," ", message.getInteger("userId"));
                    
                    CompositeFuture.all(futUpdateLocation, futSaveLocationHistory).onComplete(handler -> {
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
        });
        
    }

}
