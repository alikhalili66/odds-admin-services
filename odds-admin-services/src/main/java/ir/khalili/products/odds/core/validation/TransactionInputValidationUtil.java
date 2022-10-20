package ir.khalili.products.odds.core.validation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import ir.khalili.products.odds.core.excp.validation.EXCP_RtMgr_Validation;

public final class TransactionInputValidationUtil {

    private static Logger logger = LoggerFactory.getLogger(TransactionInputValidationUtil.class);

    private TransactionInputValidationUtil() {
    	logger.trace("");
    }

	public static void validateTransactionConfirm(RoutingContext context, Handler<AsyncResult<JsonObject>> resultHandler) {

		InputValidationUtil.validateToken(context).onComplete(handler -> {

			if (handler.failed()) {
				resultHandler.handle(Future.failedFuture(handler.cause()));
				return;
			}
			
			final JsonObject joToken = handler.result();

			Integer transactionId;

	        try {
	            final JsonObject inputParameters = InputValidationUtil.validate(context);

	            transactionId = inputParameters.getInteger("transactionId");
	            
	            if (null != transactionId && transactionId < 1) {
	                throw new EXCP_RtMgr_Validation(-603, "شناسه تراکنش معتبر نمی باشد");
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
			joResult.put("transactionId", transactionId);
			
			joResult.put("userId", joToken.getInteger("id"));
			joResult.put("clientInfo", context.request().getHeader("User-Agent"));
			joResult.put("ip", context.request().remoteAddress().host());

			resultHandler.handle(Future.succeededFuture(joResult));

		});

    }
	
	public static void validateTransactionReject(RoutingContext context, Handler<AsyncResult<JsonObject>> resultHandler) {

		InputValidationUtil.validateToken(context).onComplete(handler -> {

			if (handler.failed()) {
				resultHandler.handle(Future.failedFuture(handler.cause()));
				return;
			}
			
			final JsonObject joToken = handler.result();

			Integer transactionId;

	        try {
	            final JsonObject inputParameters = InputValidationUtil.validate(context);

	            transactionId = inputParameters.getInteger("transactionId");
	            
	            if (null != transactionId && transactionId < 1) {
	                throw new EXCP_RtMgr_Validation(-603, "شناسه تراکنش معتبر نمی باشد");
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
			joResult.put("transactionId", transactionId);
			
			joResult.put("userId", joToken.getInteger("id"));
			joResult.put("clientInfo", context.request().getHeader("User-Agent"));
			joResult.put("ip", context.request().remoteAddress().host());

			resultHandler.handle(Future.succeededFuture(joResult));

		});

    }
	
	public static void validateTransactionFetchAll(RoutingContext context, Handler<AsyncResult<JsonObject>> resultHandler) {

		InputValidationUtil.validateToken(context).onComplete(handler -> {

			if (handler.failed()) {
				resultHandler.handle(Future.failedFuture(handler.cause()));
				return;
			}
			
			final JsonObject joToken = handler.result();

			Integer transactionId = null; 	// Optional
			Integer userId = null;			// Optional
			String status = null;			// Optional
			Integer startIndex = null;
			Integer endIndex = null;

	        try {
	            final JsonObject inputParameters = InputValidationUtil.validate(context);

	            transactionId = inputParameters.getInteger("transactionId");
	            userId = inputParameters.getInteger("userId");
	            status = inputParameters.getString("status");
				startIndex = inputParameters.getInteger("startIndex");
				endIndex = inputParameters.getInteger("endIndex");
				
	            if (null != transactionId && transactionId < 1) {
	                throw new EXCP_RtMgr_Validation(-603, "شناسه تراکنش معتبر نمی باشد");
	            }

	            if (null != userId && userId < 1) {
	                throw new EXCP_RtMgr_Validation(-603, "شناسه کاربر معتبر نمی باشد");
	            }
	            
	            if (null != status && status.trim().isEmpty()) {
	                throw new EXCP_RtMgr_Validation(-603, "وضعیت تراکنش معتبر نمی باشد");
	            }
	            
				if (startIndex== null || startIndex < 0) {
					throw new EXCP_RtMgr_Validation(-602, "اندیس شروع صحیح نمی باشد.");
				}
				
				if (endIndex== null || endIndex < 0) {
					throw new EXCP_RtMgr_Validation(-602, "اندیس پایان صحیح نمی باشد.");
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
			joResult.put("transactionId", transactionId);
			joResult.put("userId", userId);
			joResult.put("status", status);
			joResult.put("startIndex", startIndex);
			joResult.put("endIndex", endIndex);
			
			joResult.put("clientInfo", context.request().getHeader("User-Agent"));
			joResult.put("ip", context.request().remoteAddress().host());

			resultHandler.handle(Future.succeededFuture(joResult));

		});

    }
	
	
}
