package ir.khalili.products.odds.core.validation;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import ir.khalili.products.odds.core.enums.AccessLockIn;
import ir.khalili.products.odds.core.excp.validation.EXCP_RtMgr_Validation;
import ir.khalili.products.odds.core.helper.HelperLockIn;
import ir.khalili.products.odds.core.utils.Helper;;

/**
 * @author A.KH
 */
public final class InputValidationUtil {

	private static Logger logger = LogManager.getLogger(InputValidationUtil.class);

	/**
	 * This method to call locally without RoutingContext object and in main method
	 */
	public static JsonObject validate(RoutingContext context) throws EXCP_RtMgr_Validation {

		final JsonObject inputParameters;

		try {
			inputParameters = context.getBodyAsJson();
			logger.trace("VALID JSON FORMAT FOR INPUT : " + inputParameters);
		} catch (Exception e) {
			logger.error("INVALID JSON FORMAT FOR INPUT : " + context.getBodyAsString());
			throw new EXCP_RtMgr_Validation(-498, "اطلاعات ورودی ارسالی در قالب فرمت استاندارد جی سان نمی باشد.");
		}

		return inputParameters;
	}

	public static Future<JsonObject> validateToken(RoutingContext context) {

		Promise<JsonObject> promise = Promise.promise();
		
		String token = context.request().getHeader("Authorization");
		
		if (null == token || token.isEmpty()) {
			logger.error("INVALID Token : " + token);
			promise.fail(new EXCP_RtMgr_Validation(-3, "توکن معتبر نمی باشد."));
			return promise.future();
		}
		
		if (!token.startsWith("Bearer ")) {
			logger.error("INVALID Token : " + token);
			promise.fail(new EXCP_RtMgr_Validation(-3, "توکن معتبر نمی باشد."));
			return promise.future();
		}
		
		
		Future<Void> futVoid = Helper.createFutureVoid();
		Future<JsonObject> futToken = HelperLockIn.checkToken(token);
		
		CompositeFuture.join(futVoid, futToken).onComplete(joinHandler -> {
			
			if (joinHandler.failed()) {
				logger.error("INVALID Token : " + token);
				promise.fail(new EXCP_RtMgr_Validation(-3, "توکن معتبر نمی باشد."));
				return;
			}
			
			logger.trace("tokenInfo: " + futToken.result());
			
			promise.complete(futToken.result());
			
		});

		return promise.future();
	}
	
	public static Future<JsonObject> validateToken(RoutingContext context, AccessLockIn access) {

		Promise<JsonObject> promise = Promise.promise();
		
//		String token = context.request().getHeader("Authorization");
//
//		Future<JsonObject> futToken = validateToken(context);
//		Future<Void> futHasAccess = HelperLockIn.checkAcccess(token, access);
//
//		CompositeFuture.join(futHasAccess, futToken).onComplete(joinHandler -> {
//
//			if (joinHandler.failed()) {
//				promise.fail(joinHandler.cause());
//				return;
//			}
//
//			logger.trace("tokenInfo: " + futToken.result());
//
//			promise.complete(futToken.result());
//
//		});
		promise.complete(new JsonObject());
		return promise.future();
	}
	
}