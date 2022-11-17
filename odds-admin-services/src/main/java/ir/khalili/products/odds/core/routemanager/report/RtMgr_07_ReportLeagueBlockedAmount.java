package ir.khalili.products.odds.core.routemanager.report;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import io.vertx.core.eventbus.ReplyException;
import io.vertx.core.eventbus.ReplyFailure;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import ir.khalili.products.odds.core.constants.AppConstants;
import ir.khalili.products.odds.core.excp.validation.EXCP_RtMgr_Validation;
import ir.khalili.products.odds.core.validation.ReportInputValidationUtil;

/**
 * @author A.KH
 */
public class RtMgr_07_ReportLeagueBlockedAmount   {
	
	private static								Logger 			logger 								= LogManager.getLogger(RtMgr_07_ReportLeagueBlockedAmount.class);
	
	public static void handler(RoutingContext context) {
		
		ReportInputValidationUtil.validateLeagueBlockedAmount(context, validateHandler->{
			
			if(validateHandler.failed()) {

    			context.response()
	    	      .putHeader("content-type", "application/json; charset=utf-8")
	    	      .end(Json.encodePrettily(new JsonObject()
				  			.put("resultCode"	, ((EXCP_RtMgr_Validation)validateHandler.cause()).getResultCode())
				  			.put("resultMessage", ((EXCP_RtMgr_Validation)validateHandler.cause()).getResultMessage())));
    			return;
    		
			}
			
			context.vertx().eventBus().request(AppConstants.EVNT_BUS_ADR_SRVCS_ODDS_REPORT_LEAGUE_BLOCKED_AMOUNT, validateHandler.result(),replyHandler->{
				
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
						resMessage	=	" ،برای رسیدگی به این درخواست ثبت نشده، لطفا با راهبر سامانه تماس بگیرید EventBus آدرسی در";
					} else if (((ReplyException)replyHandler.cause()).failureType() == ReplyFailure.TIMEOUT) {
						logger.error("NO REPLY WAS RECEIVED BEFORE THE TIMEOUT TIME.");
						resMessage	=	".عدم فراخوانی سرویس در زمان مناسب";
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
