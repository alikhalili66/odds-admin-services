package ir.khalili.products.odds.core.validation;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import ir.khalili.products.odds.core.excp.validation.EXCP_RtMgr_Validation;

/**
 * @author A.KH
 */
public final class AuthInputValidationUtil {

	private static Logger logger = LogManager.getLogger(AuthInputValidationUtil.class);

	private AuthInputValidationUtil() {
	}

	/**
	 * This method to call locally without RoutingContext object and in main method
	 */

	public static void validateAgent(RoutingContext context, Handler<AsyncResult<JsonObject>> resultHandler) {

		String username;
		String password;

		try {

			final JsonObject inputParameters = InputValidationUtil.validate(context);
			
			username = inputParameters.getString("username");
			password = inputParameters.getString("password");

			if (null == username || username.isEmpty() || username.length() > 100) {
				throw new EXCP_RtMgr_Validation(-601, "نام کاربری معتبر نمی باشد.");
			}
			if (null == password || password.isEmpty() || password.length() > 100) {
				throw new EXCP_RtMgr_Validation(-602, "رمز عبور معتبر نمی باشد.");
			}

		} catch (EXCP_RtMgr_Validation e) {
			resultHandler.handle(Future.failedFuture(e));
			return;
		} catch (Exception e) {
			logger.error("INPUT TYPE VALIDATION FAILED.", e);
			resultHandler.handle(Future.failedFuture(new EXCP_RtMgr_Validation(-499,
					"نوع داده اقلام ارسال شده معتبر نیست. به سند راهنما رجوع کنید ")));
			return;
		}

		final JsonObject joResult = new JsonObject();
		joResult.put("username", username);
		joResult.put("password", password);
		joResult.put("clientInfo", context.request().getHeader("User-Agent"));
		joResult.put("ip", context.request().remoteAddress().host());

		resultHandler.handle(Future.succeededFuture(joResult));

	}

	public static void validateCustomer(RoutingContext context, Handler<AsyncResult<JsonObject>> resultHandler) {

		InputValidationUtil.validateToken(context).onComplete(handler -> {

			if (handler.failed()) {
				resultHandler.handle(Future.failedFuture(handler.cause()));
				return;
			}

			final JsonObject joSession = handler.result();

			String username;
			String password;
			
			try {

				final JsonObject inputParameters = InputValidationUtil.validate(context);
				
				username = inputParameters.getString("username");
				password = inputParameters.getString("password");

				if (null == username || username.isEmpty() || username.length() > 100) {
					throw new EXCP_RtMgr_Validation(-601, "نام کاربری معتبر نمی باشد.");
				}
				if (null == password || password.isEmpty() || password.length() > 100) {
					throw new EXCP_RtMgr_Validation(-602, "رمز عبور معتبر نمی باشد.");
				}

			} catch (EXCP_RtMgr_Validation e) {
				resultHandler.handle(Future.failedFuture(e));
				return;
			} catch (Exception e) {
				logger.error("INPUT TYPE VALIDATION FAILED.", e);
				resultHandler.handle(Future.failedFuture(new EXCP_RtMgr_Validation(-499,
						"نوع داده اقلام ارسال شده معتبر نیست. به سند راهنما رجوع کنید ")));
				return;
			}

			final JsonObject joResult = new JsonObject();
			joResult.put("username", username);
			joResult.put("password", password);
			
			joResult.put("userId", joSession.getInteger("userId"));
			joResult.put("agentId", joSession.getInteger("agentId"));
			joResult.put("clientInfo", context.request().getHeader("User-Agent"));
			joResult.put("ip", context.request().remoteAddress().host());

			resultHandler.handle(Future.succeededFuture(joResult));

		});

	}
	

	
}