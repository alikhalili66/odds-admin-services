package ir.khalili.products.odds.core.validation;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import ir.khalili.products.odds.core.excp.validation.EXCP_RtMgr_Validation;
import ir.khalili.products.odds.core.utils.VerhoeffCheckDigitAlgorithm;

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

	public static void validateLogin(RoutingContext context, Handler<AsyncResult<JsonObject>> resultHandler) {

		String username;
		String password;

		try {

			final JsonObject inputParameters = InputValidationUtil.validate(context);
			
			username = inputParameters.getString("username");
			password = inputParameters.getString("password");

			if (null == username || username.isEmpty() || username.length() > 100) {
				throw new EXCP_RtMgr_Validation(-601, "نام کاربری صحیح نمی باشد.");
			}
			if (null == password || password.isEmpty() || password.length() > 100) {
				throw new EXCP_RtMgr_Validation(-602, "رمز عبور صحیح نمی باشد.");
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

	public static void validateOTP(RoutingContext context, Handler<AsyncResult<JsonObject>> resultHandler) {

		Long cellphone;
		Integer code;

		try {

			final JsonObject inputParameters = InputValidationUtil.validate(context);
			
			cellphone = inputParameters.getLong("cellphone");
			code = inputParameters.getInteger("code");

			if (null == cellphone || !String.valueOf(cellphone).startsWith("9") || String.valueOf(cellphone).length() != 10) {
                throw new EXCP_RtMgr_Validation(-1, "شماره تلفن صحیح نمی باشد.");
            }
            
            if (null == code || String.valueOf(code).length() != 6) {
                throw new EXCP_RtMgr_Validation(-1, "کد صحیح نمی باشد.");
            }

            if (!VerhoeffCheckDigitAlgorithm.validateVerhoeff(String.valueOf(code))) {
                throw new EXCP_RtMgr_Validation(-1, "کد صحیح نمی باشد.");
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
		joResult.put("cellphone", cellphone);
		joResult.put("code", code);
		
		joResult.put("clientInfo", context.request().getHeader("User-Agent"));
		joResult.put("ip", context.request().remoteAddress().host());

		resultHandler.handle(Future.succeededFuture(joResult));

	}
	
}