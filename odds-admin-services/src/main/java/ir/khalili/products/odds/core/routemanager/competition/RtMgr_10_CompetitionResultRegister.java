package ir.khalili.products.odds.core.routemanager.competition;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import io.vertx.core.eventbus.ReplyException;
import io.vertx.core.eventbus.ReplyFailure;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import ir.khalili.products.odds.core.constants.AppConstants;
import ir.khalili.products.odds.core.excp.validation.EXCP_RtMgr_Validation;
import ir.khalili.products.odds.core.validation.CompetitionInputValidationUtil;

/**
 * @author A.KH
 */
public class RtMgr_10_CompetitionResultRegister   {
	
	private static								Logger 			logger 								= LogManager.getLogger(RtMgr_10_CompetitionResultRegister.class);
	
	public static void handler(RoutingContext context) {
		
		CompetitionInputValidationUtil.validateResultRegister(context, validateHandler->{
			
			if(validateHandler.failed()) {

    			context.response()
	    	      .putHeader("content-type", "application/json; charset=utf-8")
	    	      .end(Json.encodePrettily(new JsonObject()
				  			.put("resultCode"	, ((EXCP_RtMgr_Validation)validateHandler.cause()).getResultCode())
				  			.put("resultMessage", ((EXCP_RtMgr_Validation)validateHandler.cause()).getResultMessage())));
    			return;
    		
			}
			
			context.vertx().eventBus().request(AppConstants.EVNT_BUS_ADR_SRVCS_ODDS_COMPETITION_RESULT_REGISTER, validateHandler.result(),replyHandler->{
				
				if (replyHandler.succeeded()) {
					logger.trace("jobs done:"+replyHandler.result().body());
					
					context.response()
					.putHeader("content-type", "application/json; charset=utf-8")
					.end(Json.encodePrettily(replyHandler.result().body()));
					
				} else {// Unable to communicate with event bus
					
					logger.error("UNABLE TO COMMUNICATE WITH EVENT BUS!");
					String resMessage = "";
					if (((ReplyException)replyHandler.cause()).failureType() == ReplyFailure.NO_HANDLERS) {
						logger.error("NO HANDLERS WERE AVAILABLE TO HANDLE THE MESSAGE.");
						resMessage	=	" ?????????? ???????????? ???? ?????? ?????????????? ?????? ?????????? ???????? ???? ?????????? ???????????? ???????? ???????????? EventBus ?????????? ????";
					} else if (((ReplyException)replyHandler.cause()).failureType() == ReplyFailure.TIMEOUT) {
						logger.error("NO REPLY WAS RECEIVED BEFORE THE TIMEOUT TIME.");
						resMessage	=	".?????? ???????????????? ?????????? ???? ???????? ??????????";
					} else {
						logger.error("THE RECIPIENT ACTIVELY SENT BACK A FAILURE (REJECTED THE MESSAGE).");
						resMessage	=	replyHandler.cause().getMessage();
					}
					logger.error("EVENT BUS ERROR CAUSE IS : " + replyHandler.cause().toString());
					
					context.response()
					.putHeader("content-type", "application/json; charset=utf-8")
					.end(Json.encodePrettily(new JsonObject()
							.put("resultCode"	, ((ReplyException)replyHandler.cause()).failureCode())
							.put("resultMessage", resMessage)));
					
				}
				
			});
		});
	}

}
