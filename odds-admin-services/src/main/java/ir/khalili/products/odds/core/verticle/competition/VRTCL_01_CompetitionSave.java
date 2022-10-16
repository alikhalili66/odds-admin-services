package ir.khalili.products.odds.core.verticle.competition;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.sql.SQLConnection;
import ir.khalili.products.odds.core.biz.competition.Biz_01_CompetitionSave;
import ir.khalili.products.odds.core.constants.AppConstants;
import ir.khalili.products.odds.core.helper.HelperConnection;
import ir.khalili.products.odds.core.utils.Configuration;

public class VRTCL_01_CompetitionSave extends AbstractVerticle {
	private Logger logger = LogManager.getLogger(VRTCL_01_CompetitionSave.class);
	
	@Override
    public void start(Promise<Void> startPromise) throws Exception {
    	logger.info("Starting verticle "+this.getClass().getSimpleName());
    	
		Configuration.config = config();
				
    	try {
    		
        	vertx.eventBus().consumer(AppConstants.EVNT_BUS_ADR_SRVCS_ODDS_COMPETITION_SAVE, message -> {
        		
        		logger.trace("Event "+AppConstants.EVNT_BUS_ADR_SRVCS_ODDS_COMPETITION_SAVE+" recieved with message:"+((JsonObject)(message.body())));
    				
        		new HelperConnection() {
					@Override
					public void callBiz(SQLConnection sqlConnection, Handler<AsyncResult<JsonObject>> resultHandler) {
						Biz_01_CompetitionSave.save(sqlConnection, (JsonObject)(message.body()), resultHandler);
					}
        		}.getConnection(vertx, this.getClass().getSimpleName(), message);
        		
        	});
        	logger.info("Event Bus Handler "+AppConstants.EVNT_BUS_ADR_SRVCS_ODDS_COMPETITION_SAVE+" ready to reply.");
        	startPromise.complete();
		} catch (Exception e) {
			logger.error("EXCEPTION DETECTED STARTING",e);
			startPromise.fail(e);
		}
    	
    }
    
}