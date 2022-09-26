package ir.khalili.products.odds.core.validation;

import static ir.khalili.products.odds.core.helper.SecurityUtil.agentKey;
import static ir.khalili.products.odds.core.helper.SecurityUtil.customerKey;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import ir.khalili.products.odds.core.excp.util.EXCPSEC_UnableToDecrypt;
import ir.khalili.products.odds.core.excp.validation.EXCP_RtMgr_Validation;
import ir.khalili.products.odds.core.helper.HelperLockIn;
import ir.khalili.products.odds.core.helper.SecurityUtil;
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

	public static Future<JsonObject> validateToken(String token) {

		Promise<JsonObject> promise = Promise.promise();

		if (null == token || token.isEmpty()) {
			logger.error("INVALID Token : " + token);
			promise.fail(new EXCP_RtMgr_Validation(-3, "توکن معتبر نمی باشد."));
			return promise.future();
		}

		Future<Void> futVoid = Helper.createFutureVoid();
		Future<JsonObject> futToken = HelperLockIn.checkToken("Bearer " + token);

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

	public static Future<JsonObject> validateAgentSession(RoutingContext context) {

		Promise<JsonObject> promise = Promise.promise();

		String agentSession = context.request().getHeader("agentSession");

		if (null == agentSession || agentSession.trim().isEmpty()) {
			logger.error("agentSessionIsNull");
			promise.fail(new EXCP_RtMgr_Validation(-4, "نشست عامل معتبر نمی باشد."));
			return promise.future();
		}

		JsonObject joAgentSession;
		try {
			joAgentSession = new JsonObject(SecurityUtil.decrypt(agentSession, agentKey));
		} catch (EXCPSEC_UnableToDecrypt e) {
			logger.error("agentSessionIsNull");
			promise.fail(new EXCP_RtMgr_Validation(-4, "نشست عامل معتبر نمی باشد."));
			return promise.future();
		}

		validateToken(joAgentSession.getString("token")).onComplete(tokenHandler -> {

			if (tokenHandler.failed()) {
				promise.fail(tokenHandler.cause());
				return;
			}

			promise.complete(joAgentSession);

		});

		return promise.future();
	}

	public static Future<JsonObject> validateCustomerSession(RoutingContext context) {

		Promise<JsonObject> promise = Promise.promise();

		String agentSession = context.request().getHeader("agentSession");
		String customerSession = context.request().getHeader("customerSession");
		
		if (null == agentSession || agentSession.trim().isEmpty()) {
			logger.error("agentSessionIsNull");
			promise.fail(new EXCP_RtMgr_Validation(-4, "نشست عامل معتبر نمی باشد."));
			return promise.future();
		}

		if (null == customerSession || customerSession.trim().isEmpty()) {
			logger.error("customerSessionIsNull");
			promise.fail(new EXCP_RtMgr_Validation(-4, "نشست مشتری معتبر نمی باشد."));
			return promise.future();
		}
		
		JsonObject joAgentSession;
		try {
			joAgentSession = new JsonObject(SecurityUtil.decrypt(agentSession, agentKey));
		} catch (EXCPSEC_UnableToDecrypt e) {
			logger.error("agentSessionIsNull");
			promise.fail(new EXCP_RtMgr_Validation(-4, "نشست عامل معتبر نمی باشد."));
			return promise.future();
		}

		JsonObject joCustomerSession;
		try {
			joCustomerSession = new JsonObject(SecurityUtil.decrypt(customerSession, customerKey));
		} catch (EXCPSEC_UnableToDecrypt e) {
			logger.error("agentSessionIsNull");
			promise.fail(new EXCP_RtMgr_Validation(-4, "نشست عامل معتبر نمی باشد."));
			return promise.future();
		}
		
		if (joCustomerSession.getInteger("agentId") != joAgentSession.getInteger("agentId")) {
			logger.error("INVALID Agent :  : " + joCustomerSession.getInteger("agentId") + " : " + joAgentSession.getInteger("agentId"));
			promise.fail(new EXCP_RtMgr_Validation(-4, "نشست مشتری متعلق به این عامل نمی باشد."));
			return promise.future();
		}

		Future<JsonObject> futAgentToken = validateToken(joAgentSession.getString("token"));
		Future<JsonObject> futCustomerToken = validateToken(joCustomerSession.getString("token"));
		
		CompositeFuture.all(futAgentToken, futCustomerToken).onComplete(tokenHandler -> {

			if (tokenHandler.failed()) {
				promise.fail(tokenHandler.cause());
				return;
			}

			promise.complete(joCustomerSession);

		});

		return promise.future();
	}
	
}