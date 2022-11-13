package ir.khalili.products.odds.core.biz.config;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.sql.SQLConnection;
import ir.khalili.products.odds.core.dao.DAO_Config;
import ir.khalili.products.odds.core.dao.DAO_League;
import ir.khalili.products.odds.core.service.ClientMinIO;
import ir.khalili.products.odds.core.utils.Helper;

public class Biz_01_ConfigUpdate {

    private static final Logger logger = LogManager.getLogger(Biz_01_ConfigUpdate.class);

    public static void update(SQLConnection sqlConnection, JsonObject message, Handler<AsyncResult<JsonObject>> resultHandler) {

        logger.trace("inputMessage:" + message);

		final Integer configId = message.getInteger("configId");
		
		DAO_Config.fetchById(sqlConnection, configId).onComplete(configHandler -> {
			if (configHandler.failed()) {
				logger.error("Unable to complete configHandler: " + configHandler.cause());
				resultHandler.handle(Future.failedFuture(configHandler.cause()));
				return;
			}

			logger.trace("CONFIG_FETCH_BY_ID_RESULT : " + configHandler.result());

			JsonObject joConfig = configHandler.result();
        	
			Future<JsonObject> futLeague;
			if(null != joConfig.getInteger("LEAGUE_ID")) {
				futLeague = DAO_League.fetchById(sqlConnection, joConfig.getInteger("LEAGUE_ID"));
			}else {
				futLeague = Helper.createFuture(new JsonObject().put("SYMBOL", "LEAGUE"));
			}
			
			futLeague.onComplete(leagueHandler->{
        		
        		if (leagueHandler.failed()) {
                	logger.error("Unable to complete leagueHandler: " + leagueHandler.cause());
                    resultHandler.handle(Future.failedFuture(leagueHandler.cause()));
                    return;
                }
        		
    			if(joConfig.getString("TYPE").equals("File")) {
                    message.put("value", ClientMinIO.saveFile(leagueHandler.result().getString("SYMBOL"), joConfig.getString("SYMBOL"), message.getString("value")));
    			}
    			
    	        DAO_Config.update(sqlConnection, configId, message.getString("value")).onComplete(updateHandler -> {
    	            if (updateHandler.failed()) {
    	            	logger.error("Unable to complete handler: " + updateHandler.cause());
    	                resultHandler.handle(Future.failedFuture(updateHandler.cause()));
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
