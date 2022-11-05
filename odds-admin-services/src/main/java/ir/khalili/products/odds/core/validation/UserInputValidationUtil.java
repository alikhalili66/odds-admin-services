package ir.khalili.products.odds.core.validation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import ir.khalili.products.odds.core.enums.AccessLockIn;
import ir.khalili.products.odds.core.excp.validation.EXCP_RtMgr_Validation;
import ir.khalili.products.odds.core.utils.NationalNumberChecker;

public final class UserInputValidationUtil {

    private static Logger logger = LoggerFactory.getLogger(UserInputValidationUtil.class);

    private UserInputValidationUtil() {
    }


	public static void validateFetchAll(RoutingContext context, Handler<AsyncResult<JsonObject>> resultHandler) {

		InputValidationUtil.validateToken(context, AccessLockIn.ODDS_USER_FETCH_ALL).onComplete(handler -> {

			if (handler.failed()) {
				resultHandler.handle(Future.failedFuture(handler.cause()));
				return;
			}

			final JsonObject joToken = handler.result();
			Integer leagueId;
			String nationalNumber = null; // optional
			Long cellphone = null; // optional
			Integer startIndex = null;
			Integer endIndex = null;
			
	        try {
	            final JsonObject inputParameters = InputValidationUtil.validate(context);

	            leagueId = inputParameters.getInteger("leagueId");
	            nationalNumber = inputParameters.getString("nationalNumber");
	            cellphone = inputParameters.getLong("cellphone");
				startIndex = inputParameters.getInteger("startIndex");
				endIndex = inputParameters.getInteger("endIndex");

				if (nationalNumber != null && (nationalNumber.isEmpty() || !NationalNumberChecker.isValid(nationalNumber))) {
					throw new EXCP_RtMgr_Validation(-602, "شماره ملی صحیح نمی باشد");
				}

				if (cellphone != null && cellphone < 0) {
					throw new EXCP_RtMgr_Validation(-602, "شماره موبایل صحیح نمی باشد");
				}
				
				if (startIndex== null || startIndex < 0) {
					throw new EXCP_RtMgr_Validation(-602, "اندیس شروع صحیح نمی باشد.");
				}
				
				if (endIndex== null || endIndex < 0) {
					throw new EXCP_RtMgr_Validation(-602, "اندیس پایان صحیح نمی باشد.");
				}
				
				if (null == leagueId || leagueId < 1) {
	                throw new EXCP_RtMgr_Validation(-603, "شناسه لیگ معتبر نمی باشد");
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
			joResult.put("nationalNumber", nationalNumber);
			joResult.put("cellphone", cellphone);
			joResult.put("startIndex", startIndex);
			joResult.put("endIndex", endIndex);
			
			joResult.put("userId", joToken.getInteger("id"));
			joResult.put("clientInfo", context.request().getHeader("User-Agent"));
			joResult.put("ip", context.request().remoteAddress().host());

			resultHandler.handle(Future.succeededFuture(joResult));

		});

    }
	
	public static void validateFetchById(RoutingContext context, Handler<AsyncResult<JsonObject>> resultHandler) {

		InputValidationUtil.validateToken(context, AccessLockIn.ODDS_USER_FETCH_BY_ID).onComplete(handler -> {

			if (handler.failed()) {
				resultHandler.handle(Future.failedFuture(handler.cause()));
				return;
			}

			final JsonObject joToken = handler.result();

			Integer id;

	        try {
	            final JsonObject inputParameters = InputValidationUtil.validate(context);

	            id = inputParameters.getInteger("id");
	            
	            if (null == id || id < 1) {
	                throw new EXCP_RtMgr_Validation(-603, "شناسه کاربر معتبر نمی باشد");
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
			joResult.put("id", id);
			joResult.put("userId", joToken.getInteger("id"));
			joResult.put("clientInfo", context.request().getHeader("User-Agent"));
			joResult.put("ip", context.request().remoteAddress().host());

			resultHandler.handle(Future.succeededFuture(joResult));

		});

    }
	
	public static void validateFetchOdds(RoutingContext context, Handler<AsyncResult<JsonObject>> resultHandler) {

		InputValidationUtil.validateToken(context, AccessLockIn.ODDS_USER_FETCH_ODDS).onComplete(handler -> {

			if (handler.failed()) {
				resultHandler.handle(Future.failedFuture(handler.cause()));
				return;
			}

			final JsonObject joToken = handler.result();

			Integer id;
			Integer startIndex = null;
			Integer endIndex = null;
			
	        try {
	            final JsonObject inputParameters = InputValidationUtil.validate(context);

	            id = inputParameters.getInteger("id");
				startIndex = inputParameters.getInteger("startIndex");
				endIndex = inputParameters.getInteger("endIndex");
				
	            if (null == id || id < 1) {
	                throw new EXCP_RtMgr_Validation(-603, "شناسه کاربر معتبر نمی باشد");
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
			joResult.put("id", id);
			joResult.put("startIndex", startIndex);
			joResult.put("endIndex", endIndex);
			
			joResult.put("userId", joToken.getInteger("id"));
			joResult.put("clientInfo", context.request().getHeader("User-Agent"));
			joResult.put("ip", context.request().remoteAddress().host());

			resultHandler.handle(Future.succeededFuture(joResult));

		});

    }
	
	public static void validateUserFetchQuestionAnswer(RoutingContext context, Handler<AsyncResult<JsonObject>> resultHandler) {

		InputValidationUtil.validateToken(context, AccessLockIn.ODDS_USER_FETCH_QUESTION_ANSWER).onComplete(handler -> {

			if (handler.failed()) {
				resultHandler.handle(Future.failedFuture(handler.cause()));
				return;
			}

			Integer userId;
			Integer competitionId;

	        try {
	            final JsonObject inputParameters = InputValidationUtil.validate(context);

	            userId = inputParameters.getInteger("userId");
	            competitionId = inputParameters.getInteger("competitionId");
	            
	            if (null == userId || userId < 1) {
	                throw new EXCP_RtMgr_Validation(-603, "شناسه کاربر معتبر نمی باشد");
	            }

	            if (null == competitionId || competitionId < 1) {
	                throw new EXCP_RtMgr_Validation(-603, "شناسه مسابقه معتبر نمی باشد");
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
			joResult.put("competitionId", competitionId);
			joResult.put("userId", userId);
			joResult.put("clientInfo", context.request().getHeader("User-Agent"));
			joResult.put("ip", context.request().remoteAddress().host());

			resultHandler.handle(Future.succeededFuture(joResult));

		});

    }
	
	public static void validateUserPointHistory(RoutingContext context, Handler<AsyncResult<JsonObject>> resultHandler) {

		InputValidationUtil.validateToken(context, AccessLockIn.ODDS_USER_FETCH_POINT_HISTORY).onComplete(handler -> {

			if (handler.failed()) {
				resultHandler.handle(Future.failedFuture(handler.cause()));
				return;
			}

			Integer userId;
			Integer startIndex = null;
			Integer endIndex = null;
			
	        try {
	            final JsonObject inputParameters = InputValidationUtil.validate(context);

	            userId = inputParameters.getInteger("userId");
	            startIndex = inputParameters.getInteger("startIndex");
	            endIndex = inputParameters.getInteger("endIndex");
	            
	            if (null == userId || userId < 1) {
	                throw new EXCP_RtMgr_Validation(-603, "شناسه کاربر معتبر نمی باشد");
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
			joResult.put("userId", userId);
			joResult.put("startIndex", startIndex);
			joResult.put("endIndex", endIndex);
			
			joResult.put("clientInfo", context.request().getHeader("User-Agent"));
			joResult.put("ip", context.request().remoteAddress().host());

			resultHandler.handle(Future.succeededFuture(joResult));

		});

    }
	
}
