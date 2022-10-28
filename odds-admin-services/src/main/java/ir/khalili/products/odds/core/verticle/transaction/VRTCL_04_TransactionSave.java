package ir.khalili.products.odds.core.verticle.transaction;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.sql.SQLConnection;
import ir.khalili.products.odds.core.biz.transaction.Biz_04_TransactionSave;
import ir.khalili.products.odds.core.constants.AppConstants;
import ir.khalili.products.odds.core.utils.Configuration;

public class VRTCL_04_TransactionSave extends AbstractVerticle {
	private Logger logger = LogManager.getLogger(VRTCL_04_TransactionSave.class);
	
	@Override
    public void start(Promise<Void> startPromise) throws Exception {
    	logger.info("Starting verticle "+this.getClass().getSimpleName());
    	
		Configuration.config = config();
		
    	try {
    		JDBCClient ircJDBC = JDBCClient.createShared(vertx, Configuration.getDataBaseConfig(),AppConstants.APP_DS_ODDS);
    		
        	vertx.eventBus().consumer(AppConstants.EVNT_BUS_ADR_SRVCS_ODDS_TRANSACTION_SAVE, message -> {
        		
        		logger.trace("Event "+AppConstants.EVNT_BUS_ADR_SRVCS_ODDS_TRANSACTION_SAVE+" recieved with message:"+((JsonObject)(message.body())));
    				
        		ircJDBC.getConnection(connection -> {
					
		    		if (connection.failed()) {
		    			
		    			logger.error("Unable to get connection from database:", connection.cause());
		    			message.fail(-12020, "امکان برقراری ارتباط با بانک اطلاعاتی نیست");
		    			return;
		    		}
		    		
		    		SQLConnection sqlConnection = connection.result();

					Biz_04_TransactionSave.confirm(vertx, sqlConnection, (JsonObject)(message.body()), resultHandler -> {
	
						if (resultHandler.succeeded()) {
							logger.trace("AVTCL08,Succeeded:"+resultHandler.result());
							message.reply(resultHandler.result());
						} else {
							JsonObject excp = JsonObject.mapFrom(resultHandler.cause());
							logger.error("AVTCL08,Failed:"+excp);
							message.fail(excp.getInteger("resultCode"), excp.getString("resultMessage"));
						}

    					sqlConnection.close(handler -> {
							if(handler.failed()) {
								logger.error("IncompleteSessionClose", handler.cause());
							}
						});
		    		});
				});
        	});
        	logger.info("Event Bus Handler "+AppConstants.EVNT_BUS_ADR_SRVCS_ODDS_TRANSACTION_SAVE+" ready to reply.");
        	startPromise.complete();
		} catch (Exception e) {
			logger.error("EXCEPTION DETECTED STARTING",e);
			startPromise.fail(e);
		}
    	
    }
    
}