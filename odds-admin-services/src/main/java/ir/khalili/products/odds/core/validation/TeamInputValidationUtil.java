package ir.khalili.products.odds.core.validation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import ir.khalili.products.odds.core.excp.validation.EXCP_RtMgr_Validation;
import ir.khalili.products.odds.core.utils.VerhoeffCheckDigitAlgorithm;

public final class TeamInputValidationUtil {

    private static Logger logger = LoggerFactory.getLogger(TeamInputValidationUtil.class);

    private TeamInputValidationUtil() {
    }

	
	public static void validateUserSendOtp(RoutingContext context, Handler<AsyncResult<JsonObject>> resultHandler) {

		InputValidationUtil.validateAgentSession(context).onComplete(handler -> {

			if (handler.failed()) {
				resultHandler.handle(Future.failedFuture(handler.cause()));
				return;
			}

			final JsonObject joSession = handler.result();

			Long cellphone;

	        try {
	            final JsonObject inputParameters = InputValidationUtil.validate(context);

	            cellphone = inputParameters.getLong("cellphone");
	            
	            if (null == cellphone || cellphone < 1) {
	                throw new EXCP_RtMgr_Validation(-603, "شماره تلفن صحیح نمی باشد.");
	            }

	        } catch (EXCP_RtMgr_Validation e) {
				resultHandler.handle(Future.failedFuture(e));
				return;
			} catch (Exception e) {
				logger.error("INPUT TYPE VALIDATION FAILED.", e);
				resultHandler.handle(Future.failedFuture(new EXCP_RtMgr_Validation(-499, "نوع داده اقلام ارسال شده معتبر نیست. به سند راهنما رجوع کنید ")));
				return;
			}

			final JsonObject joResult = new JsonObject();
			joResult.put("cellphone", cellphone);

			joResult.put("agentId", joSession.getInteger("agentId"));
			joResult.put("clientInfo", context.request().getHeader("User-Agent"));
			joResult.put("ip", context.request().remoteAddress().host());

			resultHandler.handle(Future.succeededFuture(joResult));

		});

    }
	
	public static void validateUserPasswordReset(RoutingContext context, Handler<AsyncResult<JsonObject>> resultHandler) {

		InputValidationUtil.validateAgentSession(context).onComplete(handler -> {

			if (handler.failed()) {
				resultHandler.handle(Future.failedFuture(handler.cause()));
				return;
			}

			final JsonObject joSession = handler.result();

			Long cellphone;
			Integer code;

	        try {
	            final JsonObject inputParameters = InputValidationUtil.validate(context);

	            cellphone = inputParameters.getLong("cellphone");
	            code = inputParameters.getInteger("code");

	            if (null == cellphone || cellphone < 1) {
	                throw new EXCP_RtMgr_Validation(-603, "شماره تلفن صحیح نمی باشد.");
	            }

	            if (null == code || String.valueOf(code).length() != 6) {
					throw new EXCP_RtMgr_Validation(-661, "کد صحیح نمی باشد.");
				}

				if (!VerhoeffCheckDigitAlgorithm.validateVerhoeff(String.valueOf(code))) {
					throw new EXCP_RtMgr_Validation(-661, "کد صحیح نمی باشد.");
				}

	        } catch (EXCP_RtMgr_Validation e) {
				resultHandler.handle(Future.failedFuture(e));
				return;
			} catch (Exception e) {
				logger.error("INPUT TYPE VALIDATION FAILED.", e);
				resultHandler.handle(Future.failedFuture(new EXCP_RtMgr_Validation(-499, "نوع داده اقلام ارسال شده معتبر نیست. به سند راهنما رجوع کنید ")));
				return;
			}

			final JsonObject joResult = new JsonObject();
			joResult.put("cellphone", cellphone);
            joResult.put("code", code);

			joResult.put("agentId", joSession.getInteger("agentId"));
			joResult.put("clientInfo", context.request().getHeader("User-Agent"));
			joResult.put("ip", context.request().remoteAddress().host());

			resultHandler.handle(Future.succeededFuture(joResult));

		});

    }
	
	
}
