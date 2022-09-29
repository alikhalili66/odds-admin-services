package ir.khalili.products.odds.core.validation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import ir.khalili.products.odds.core.excp.validation.EXCP_RtMgr_Validation;

public final class QuestionInputValidationUtil {

    private static Logger logger = LoggerFactory.getLogger(QuestionInputValidationUtil.class);

    private QuestionInputValidationUtil() {
    }


	public static void validateSave(RoutingContext context, Handler<AsyncResult<JsonObject>> resultHandler) {

		InputValidationUtil.validateToken(context).onComplete(handler -> {

			if (handler.failed()) {
				resultHandler.handle(Future.failedFuture(handler.cause()));
				return;
			}

			final JsonObject joSession = handler.result();

			Integer leagueId;
			String question;
			String answers;
			String type;
			Integer minPoint;

	        try {
	            final JsonObject inputParameters = InputValidationUtil.validate(context);

	            leagueId = inputParameters.getInteger("leagueId");
	            question = inputParameters.getString("question");
	            answers = inputParameters.getString("answers");
	            type = inputParameters.getString("type");
	            minPoint = inputParameters.getInteger("minPoint");
	            
	            if (null == leagueId || leagueId < 1) {
	                throw new EXCP_RtMgr_Validation(-603, "شناسه لیگ معتبر نمی باشد");
	            }

	            if (null == question || question.isEmpty()) {
	                throw new EXCP_RtMgr_Validation(-603, "فیلد سوال معتبر نمی باشد");
	            }

	            if (null == answers || answers.isEmpty()) {
	                throw new EXCP_RtMgr_Validation(-603, "فیلد پاسخ معتبر نمی باشد");
	            }

	            if (null == type || type.isEmpty()) {
	                throw new EXCP_RtMgr_Validation(-603, "فیلد نوع معتبر نمی باشد");
	            }

	            if (null == minPoint || minPoint < 1) {
	                throw new EXCP_RtMgr_Validation(-603, "فیلد minPoint معتبر نمی باشد");
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
			joResult.put("leagueId", leagueId);
			joResult.put("question", question);
			joResult.put("answers", answers);
			joResult.put("type", type);
			joResult.put("minPoint", minPoint);
			joResult.put("userId", joSession.getInteger("userId"));
			joResult.put("clientInfo", context.request().getHeader("User-Agent"));
			joResult.put("ip", context.request().remoteAddress().host());

			resultHandler.handle(Future.succeededFuture(joResult));

		});

    }
	
	public static void validateUpdate(RoutingContext context, Handler<AsyncResult<JsonObject>> resultHandler) {

		InputValidationUtil.validateToken(context).onComplete(handler -> {

			if (handler.failed()) {
				resultHandler.handle(Future.failedFuture(handler.cause()));
				return;
			}

			final JsonObject joSession = handler.result();

			Integer questionId;
			Integer leagueId;
			String question;
			String answers;
			String type;
			Integer minPoint;

	        try {
	            final JsonObject inputParameters = InputValidationUtil.validate(context);

	            questionId = inputParameters.getInteger("questionId");
	            leagueId = inputParameters.getInteger("leagueId");
	            question = inputParameters.getString("question");
	            answers = inputParameters.getString("answers");
	            type = inputParameters.getString("type");
	            minPoint = inputParameters.getInteger("minPoint");

	            if (null == questionId || questionId < 1) {
	                throw new EXCP_RtMgr_Validation(-603, "شناسه سوال معتبر نمی باشد");
	            }

	            if (null == leagueId || leagueId < 1) {
	                throw new EXCP_RtMgr_Validation(-603, "شناسه لیگ معتبر نمی باشد");
	            }

	            if (null == question || question.isEmpty()) {
	                throw new EXCP_RtMgr_Validation(-603, "فیلد سوال معتبر نمی باشد");
	            }

	            if (null == answers || answers.isEmpty()) {
	                throw new EXCP_RtMgr_Validation(-603, "فیلد پاسخ معتبر نمی باشد");
	            }

	            if (null == type || type.isEmpty()) {
	                throw new EXCP_RtMgr_Validation(-603, "فیلد نوع معتبر نمی باشد");
	            }

	            if (null == minPoint || minPoint < 1) {
	                throw new EXCP_RtMgr_Validation(-603, "فیلد minPoint معتبر نمی باشد");
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
			joResult.put("questionId", questionId);
			joResult.put("leagueId", leagueId);
			joResult.put("question", question);
			joResult.put("answers", answers);
			joResult.put("type", type);
			joResult.put("minPoint", minPoint);
			
			joResult.put("userId", joSession.getInteger("userId"));
			joResult.put("clientInfo", context.request().getHeader("User-Agent"));
			joResult.put("ip", context.request().remoteAddress().host());

			resultHandler.handle(Future.succeededFuture(joResult));

		});

    }
	
	public static void validateDelete(RoutingContext context, Handler<AsyncResult<JsonObject>> resultHandler) {

		InputValidationUtil.validateToken(context).onComplete(handler -> {

			if (handler.failed()) {
				resultHandler.handle(Future.failedFuture(handler.cause()));
				return;
			}

			final JsonObject joSession = handler.result();

			Integer questionId;

	        try {
	            final JsonObject inputParameters = InputValidationUtil.validate(context);

	            questionId = inputParameters.getInteger("questionId");
	            
	            if (null == questionId || questionId < 1) {
	                throw new EXCP_RtMgr_Validation(-603, "شناسه سوال معتبر نمی باشد");
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
			joResult.put("questionId", questionId);
			joResult.put("clientInfo", context.request().getHeader("User-Agent"));
			joResult.put("ip", context.request().remoteAddress().host());

			resultHandler.handle(Future.succeededFuture(joResult));

		});

    }
	
	public static void validateFetchAll(RoutingContext context, Handler<AsyncResult<JsonObject>> resultHandler) {

		InputValidationUtil.validateToken(context).onComplete(handler -> {

			if (handler.failed()) {
				resultHandler.handle(Future.failedFuture(handler.cause()));
				return;
			}

			final JsonObject joSession = handler.result();

			final JsonObject joResult = new JsonObject();
			joResult.put("clientInfo", context.request().getHeader("User-Agent"));
			joResult.put("ip", context.request().remoteAddress().host());

			resultHandler.handle(Future.succeededFuture(joResult));

		});

    }
	
	public static void validateFetchById(RoutingContext context, Handler<AsyncResult<JsonObject>> resultHandler) {

		InputValidationUtil.validateToken(context).onComplete(handler -> {

			if (handler.failed()) {
				resultHandler.handle(Future.failedFuture(handler.cause()));
				return;
			}

			final JsonObject joSession = handler.result();

			Integer questionId;

	        try {
	            final JsonObject inputParameters = InputValidationUtil.validate(context);

	            questionId = inputParameters.getInteger("questionId");
	            
	            if (null == questionId || questionId < 1) {
	                throw new EXCP_RtMgr_Validation(-603, "شناسه سوال معتبر نمی باشد");
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
			joResult.put("questionId", questionId);
			joResult.put("clientInfo", context.request().getHeader("User-Agent"));
			joResult.put("ip", context.request().remoteAddress().host());

			resultHandler.handle(Future.succeededFuture(joResult));

		});

    }
	
	
}