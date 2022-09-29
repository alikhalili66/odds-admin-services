package ir.khalili.products.odds.core.verticle.auth;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;
import ir.khalili.products.odds.core.biz.auth.Biz_02_AuthOTP;
import ir.khalili.products.odds.core.constants.AppConstants;
import ir.khalili.products.odds.core.utils.Configuration;

public class VRTCL_02_AuthOTP extends AbstractVerticle {
	private Logger logger = LogManager.getLogger(VRTCL_02_AuthOTP.class);
	
	@Override
    public void start(Promise<Void> startPromise) throws Exception {
    	logger.info("Starting verticle "+this.getClass().getSimpleName());
    	
		Configuration.config = config();
		
    	try {
        	
        	vertx.eventBus().consumer(AppConstants.EVNT_BUS_ADR_SRVCS_ODDS_AUTH_OTP, message -> {
        		
        		logger.trace("Event "+AppConstants.EVNT_BUS_ADR_SRVCS_ODDS_AUTH_OTP+" recieved with message:"+((JsonObject)(message.body())));
    				
        		Biz_02_AuthOTP.doAuth((JsonObject)(message.body()), resultHandler -> {

					if (resultHandler.succeeded()) {
						logger.trace("AVTCL01,Succeeded:"+resultHandler.result());
						message.reply(resultHandler.result());
					} else {
						JsonObject excp = JsonObject.mapFrom(resultHandler.cause());
						logger.error("AVTCL01,Failed:"+excp);
						message.fail(excp.getInteger("resultCode"), excp.getString("resultMessage"));
					}
					
				});
    				
        	});
        	logger.info("Event Bus Handler "+AppConstants.EVNT_BUS_ADR_SRVCS_ODDS_AUTH_OTP+" ready to reply.");
        	startPromise.complete();
		} catch (Exception e) {
			logger.error("EXCEPTION DETECTED STARTING",e);
			startPromise.fail(e);
		}
    	
    }
    
}