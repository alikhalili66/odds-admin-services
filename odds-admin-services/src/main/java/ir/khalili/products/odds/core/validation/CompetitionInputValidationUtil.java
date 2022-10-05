package ir.khalili.products.odds.core.validation;

import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import ir.khalili.products.odds.core.excp.validation.EXCP_RtMgr_Validation;

public final class CompetitionInputValidationUtil {

    private static Logger logger = LoggerFactory.getLogger(CompetitionInputValidationUtil.class);

    private CompetitionInputValidationUtil() {
    }

	
	public static void validateSave(RoutingContext context, Handler<AsyncResult<JsonObject>> resultHandler) {

		InputValidationUtil.validateToken(context).onComplete(handler -> {

			if (handler.failed()) {
				resultHandler.handle(Future.failedFuture(handler.cause()));
				return;
			}

			final JsonObject joToken = handler.result();

			Integer leagueId;
			Integer teamId1;
			Integer teamId2;
			Integer groupId;
			String activeFrom;
			String activeTo;
			String oddsFrom;
			String oddsTo;
			String competitionDate;

	        try {
	            final JsonObject inputParameters = InputValidationUtil.validate(context);

	            leagueId = inputParameters.getInteger("leagueId");
	            teamId1 = inputParameters.getInteger("teamId1");
	            teamId2 = inputParameters.getInteger("teamId2");
	            groupId = inputParameters.getInteger("groupId");
	            activeFrom = inputParameters.getString("activeFrom");
	            activeTo = inputParameters.getString("activeTo");
	            oddsFrom = inputParameters.getString("oddsFrom");
	            oddsTo = inputParameters.getString("oddsTo");
	            competitionDate = inputParameters.getString("competitionDate");
	            
	            if (null == leagueId || leagueId < 1) {
	                throw new EXCP_RtMgr_Validation(-603, "شناسه لیگ معتبر نمی باشد");
	            }

	            if (null == teamId1 || teamId1 < 1) {
	                throw new EXCP_RtMgr_Validation(-603, "شناسه تیم اول معتبر نمی باشد");
	            }
	            
	            if (null == teamId2 || teamId2 < 1) {
	                throw new EXCP_RtMgr_Validation(-603, "شناسه تیم دوم معتبر نمی باشد");
	            }
	            
	            if (null == groupId || groupId < 1) {
	                throw new EXCP_RtMgr_Validation(-603, "شناسه گروه معتبر نمی باشد");
	            }
	            
	            if (null == activeFrom || activeFrom.isEmpty() || !activeFrom.matches("[0-9]{4}-[0-9]{2}-[0-9]{2}T[0-9]{2}:[0-9]{2}:[0-9]{2}Z")) {
	                throw new EXCP_RtMgr_Validation(-603, "فیلد 'فعال از' معتبر نمی باشد");
	            }
	            
	            if (null == activeTo || activeTo.isEmpty() || !activeTo.matches("[0-9]{4}-[0-9]{2}-[0-9]{2}T[0-9]{2}:[0-9]{2}:[0-9]{2}Z")) {
	                throw new EXCP_RtMgr_Validation(-603, "فیلد 'فعال تا' معتبر نمی باشد");
	            }
	            
	            if (null == oddsFrom || oddsFrom.isEmpty() || !oddsFrom.matches("[0-9]{4}-[0-9]{2}-[0-9]{2}T[0-9]{2}:[0-9]{2}:[0-9]{2}Z")) {
	                throw new EXCP_RtMgr_Validation(-603, "فیلد 'شروع پیش بینی' معتبر نمی باشد");
	            }
	            
	            if (null == oddsTo || oddsTo.isEmpty() || !oddsTo.matches("[0-9]{4}-[0-9]{2}-[0-9]{2}T[0-9]{2}:[0-9]{2}:[0-9]{2}Z")) {
	                throw new EXCP_RtMgr_Validation(-603, "فیلد 'پایان پیش بینی' معتبر نمی باشد");
	            }

	            if (null == competitionDate || competitionDate.isEmpty() || !competitionDate.matches("[0-9]{4}-[0-9]{2}-[0-9]{2}T[0-9]{2}:[0-9]{2}:[0-9]{2}Z")) {
	                throw new EXCP_RtMgr_Validation(-603, "فیلد 'تاریخ مسابقه' معتبر نمی باشد");
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
			joResult.put("teamId1", teamId1);
			joResult.put("teamId2", teamId2);
			joResult.put("groupId", groupId);
			joResult.put("activeFrom", activeFrom);
			joResult.put("activeTo", activeTo);
			joResult.put("oddsFrom", oddsFrom);
			joResult.put("oddsTo", oddsTo);
			joResult.put("competitionDate", competitionDate);

			joResult.put("userId", joToken.getInteger("id"));
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

			final JsonObject joToken = handler.result();

			Integer competitionId;
			Integer leagueId;
			Integer teamId1;
			Integer teamId2;
			Integer groupId;
			String activeFrom;
			String activeTo;
			String oddsFrom;
			String oddsTo;
			String competitionDate;

	        try {
	            final JsonObject inputParameters = InputValidationUtil.validate(context);

	            competitionId = inputParameters.getInteger("competitionId");
	            leagueId = inputParameters.getInteger("leagueId");
	            teamId1 = inputParameters.getInteger("teamId1");
	            teamId2 = inputParameters.getInteger("teamId2");
	            groupId = inputParameters.getInteger("groupId");
	            activeFrom = inputParameters.getString("activeFrom");
	            activeTo = inputParameters.getString("activeTo");
	            oddsFrom = inputParameters.getString("oddsFrom");
	            oddsTo = inputParameters.getString("oddsTo");
	            competitionDate = inputParameters.getString("competitionDate");

	            if (null == competitionId || competitionId < 1) {
	                throw new EXCP_RtMgr_Validation(-603, "شناسه مسابقه معتبر نمی باشد");
	            }
	            
	            if (null == leagueId || leagueId < 1) {
	                throw new EXCP_RtMgr_Validation(-603, "شناسه لیگ معتبر نمی باشد");
	            }

	            if (null == teamId1 || teamId1 < 1) {
	                throw new EXCP_RtMgr_Validation(-603, "شناسه تیم اول معتبر نمی باشد");
	            }
	            
	            if (null == teamId2 || teamId2 < 1) {
	                throw new EXCP_RtMgr_Validation(-603, "شناسه تیم دوم معتبر نمی باشد");
	            }
	            
	            if (null == groupId || groupId < 1) {
	                throw new EXCP_RtMgr_Validation(-603, "شناسه گروه معتبر نمی باشد");
	            }
	            
	            if (null == activeFrom || activeFrom.isEmpty() || !activeFrom.matches("[0-9]{4}-[0-9]{2}-[0-9]{2}T[0-9]{2}:[0-9]{2}:[0-9]{2}Z")) {
	                throw new EXCP_RtMgr_Validation(-603, "فیلد 'فعال از' معتبر نمی باشد");
	            }
	            
	            if (null == activeTo || activeTo.isEmpty() || !activeTo.matches("[0-9]{4}-[0-9]{2}-[0-9]{2}T[0-9]{2}:[0-9]{2}:[0-9]{2}Z")) {
	                throw new EXCP_RtMgr_Validation(-603, "فیلد 'فعال تا' معتبر نمی باشد");
	            }
	            
	            if (null == oddsFrom || oddsFrom.isEmpty() || !oddsFrom.matches("[0-9]{4}-[0-9]{2}-[0-9]{2}T[0-9]{2}:[0-9]{2}:[0-9]{2}Z")) {
	                throw new EXCP_RtMgr_Validation(-603, "فیلد 'شروع پیش بینی' معتبر نمی باشد");
	            }
	            
	            if (null == oddsTo || oddsTo.isEmpty() || !oddsTo.matches("[0-9]{4}-[0-9]{2}-[0-9]{2}T[0-9]{2}:[0-9]{2}:[0-9]{2}Z")) {
	                throw new EXCP_RtMgr_Validation(-603, "فیلد 'پایان پیش بینی' معتبر نمی باشد");
	            }

	            if (null == competitionDate || competitionDate.isEmpty() || !competitionDate.matches("[0-9]{4}-[0-9]{2}-[0-9]{2}T[0-9]{2}:[0-9]{2}:[0-9]{2}Z")) {
	                throw new EXCP_RtMgr_Validation(-603, "فیلد 'تاریخ مسابقه' معتبر نمی باشد");
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
			joResult.put("leagueId", leagueId);
			joResult.put("teamId1", teamId1);
			joResult.put("teamId2", teamId2);
			joResult.put("groupId", groupId);
			joResult.put("activeFrom", activeFrom);
			joResult.put("activeTo", activeTo);
			joResult.put("oddsFrom", oddsFrom);
			joResult.put("oddsTo", oddsTo);
			joResult.put("competitionDate", competitionDate);

			joResult.put("userId", joToken.getInteger("id"));
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

			final JsonObject joToken = handler.result();

			Integer competitionId;

	        try {
	            final JsonObject inputParameters = InputValidationUtil.validate(context);

	            competitionId = inputParameters.getInteger("competitionId");
	            
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
			joResult.put("userId", joToken.getInteger("id"));
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

			final JsonObject joToken = handler.result();
			
			Integer leagueId;
			Integer groupId;

	        try {
	            final JsonObject inputParameters = InputValidationUtil.validate(context);

	            leagueId = inputParameters.getInteger("leagueId");
	            groupId = inputParameters.getInteger("groupId");
	            
	            if (null != leagueId && leagueId < 1) {
	                throw new EXCP_RtMgr_Validation(-603, "شناسه لیگ معتبر نمی باشد");
	            }

	            if (null != groupId && groupId < 1) {
	                throw new EXCP_RtMgr_Validation(-603, "شناسه گروه معتبر نمی باشد");
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
			joResult.put("leagueId", leagueId);
			joResult.put("userId", joToken.getInteger("id"));
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

			final JsonObject joToken = handler.result();

			Integer competitionId;

	        try {
	            final JsonObject inputParameters = InputValidationUtil.validate(context);

	            competitionId = inputParameters.getInteger("competitionId");
	            
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
			joResult.put("userId", joToken.getInteger("id"));
			joResult.put("clientInfo", context.request().getHeader("User-Agent"));
			joResult.put("ip", context.request().remoteAddress().host());

			resultHandler.handle(Future.succeededFuture(joResult));

		});

    }
	
	public static void validateGroupFetch(RoutingContext context, Handler<AsyncResult<JsonObject>> resultHandler) {

		InputValidationUtil.validateToken(context).onComplete(handler -> {

			if (handler.failed()) {
				resultHandler.handle(Future.failedFuture(handler.cause()));
				return;
			}

			final JsonObject joToken = handler.result();

			Integer competitionId;

	        try {
	            final JsonObject inputParameters = InputValidationUtil.validate(context);

	            competitionId = inputParameters.getInteger("competitionId");
	            
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

			joResult.put("userId", joToken.getInteger("id"));
			joResult.put("clientInfo", context.request().getHeader("User-Agent"));
			joResult.put("ip", context.request().remoteAddress().host());

			resultHandler.handle(Future.succeededFuture(joResult));

		});

    }
	
	public static void validateQuestionAssign(RoutingContext context, Handler<AsyncResult<JsonObject>> resultHandler) {

		InputValidationUtil.validateToken(context).onComplete(handler -> {

			if (handler.failed()) {
				resultHandler.handle(Future.failedFuture(handler.cause()));
				return;
			}

			final JsonObject joToken = handler.result();

			Integer competitionId;
			Integer questionId;
			Integer norder;

	        try {
	            final JsonObject inputParameters = InputValidationUtil.validate(context);

	            competitionId = inputParameters.getInteger("competitionId");
	            questionId = inputParameters.getInteger("questionId");
	            norder = inputParameters.getInteger("norder");
	            
	            if (null == competitionId || competitionId < 1) {
	                throw new EXCP_RtMgr_Validation(-603, "شناسه مسابقه معتبر نمی باشد");
	            }

	            if (null == questionId || questionId < 1) {
	                throw new EXCP_RtMgr_Validation(-603, "شناسه سوال معتبر نمی باشد");
	            }

	            if (null == norder || norder < 1) {
	                throw new EXCP_RtMgr_Validation(-603, "ترتیب سوال معتبر نمی باشد");
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
			joResult.put("questionId", questionId);
			joResult.put("norder", norder);

			joResult.put("userId", joToken.getInteger("id"));
			joResult.put("clientInfo", context.request().getHeader("User-Agent"));
			joResult.put("ip", context.request().remoteAddress().host());

			resultHandler.handle(Future.succeededFuture(joResult));

		});

    }
	
	public static void validateQuestionUnAssign(RoutingContext context, Handler<AsyncResult<JsonObject>> resultHandler) {

		InputValidationUtil.validateToken(context).onComplete(handler -> {

			if (handler.failed()) {
				resultHandler.handle(Future.failedFuture(handler.cause()));
				return;
			}

			final JsonObject joToken = handler.result();

			Integer competitionId;
			Integer questionId;

	        try {
	            final JsonObject inputParameters = InputValidationUtil.validate(context);

	            competitionId = inputParameters.getInteger("competitionId");
	            questionId = inputParameters.getInteger("questionId");
	            
	            if (null == competitionId || competitionId < 1) {
	                throw new EXCP_RtMgr_Validation(-603, "شناسه مسابقه معتبر نمی باشد");
	            }

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
			joResult.put("competitionId", competitionId);
			joResult.put("questionId", questionId);

			joResult.put("userId", joToken.getInteger("id"));
			joResult.put("clientInfo", context.request().getHeader("User-Agent"));
			joResult.put("ip", context.request().remoteAddress().host());

			resultHandler.handle(Future.succeededFuture(joResult));

		});

    }
	
	public static void validateQuestionFetch(RoutingContext context, Handler<AsyncResult<JsonObject>> resultHandler) {

		InputValidationUtil.validateToken(context).onComplete(handler -> {

			if (handler.failed()) {
				resultHandler.handle(Future.failedFuture(handler.cause()));
				return;
			}

			final JsonObject joToken = handler.result();

			Integer competitionId;

	        try {
	            final JsonObject inputParameters = InputValidationUtil.validate(context);

	            competitionId = inputParameters.getInteger("competitionId");
	            
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

			joResult.put("userId", joToken.getInteger("id"));
			joResult.put("clientInfo", context.request().getHeader("User-Agent"));
			joResult.put("ip", context.request().remoteAddress().host());

			resultHandler.handle(Future.succeededFuture(joResult));

		});

    }
	
	public static void validateResultRegister(RoutingContext context, Handler<AsyncResult<JsonObject>> resultHandler) {

		InputValidationUtil.validateToken(context).onComplete(handler -> {

			if (handler.failed()) {
				resultHandler.handle(Future.failedFuture(handler.cause()));
				return;
			}

			final JsonObject joToken = handler.result();

			Integer competitionId;
			String result;

	        try {
	            final JsonObject inputParameters = InputValidationUtil.validate(context);

	            competitionId = inputParameters.getInteger("competitionId");
	            result = inputParameters.getString("result");

	            if (null == competitionId || competitionId < 1) {
	                throw new EXCP_RtMgr_Validation(-603, "شناسه مسابقه معتبر نمی باشد");
	            }
	            
	            if (null == result || result.isEmpty() || result.length() > 5) {
	                throw new EXCP_RtMgr_Validation(-603, "فیلد نتیجه معتبر نمی باشد");
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
			joResult.put("result", result);

			joResult.put("userId", joToken.getInteger("id"));
			joResult.put("clientInfo", context.request().getHeader("User-Agent"));
			joResult.put("ip", context.request().remoteAddress().host());

			resultHandler.handle(Future.succeededFuture(joResult));

		});

    }

	public static void validateQuestionResultRegister(RoutingContext context, Handler<AsyncResult<JsonObject>> resultHandler) {

		InputValidationUtil.validateToken(context).onComplete(handler -> {

			if (handler.failed()) {
				resultHandler.handle(Future.failedFuture(handler.cause()));
				return;
			}

			final JsonObject joToken = handler.result();

			Integer competitionId = null;
			JsonArray results = new JsonArray();
			
			try {

				final JsonObject inputParameters = InputValidationUtil.validate(context);
				
				competitionId = inputParameters.getInteger("competitionId");
				results = inputParameters.getJsonArray("results");
				
				if (null == competitionId || competitionId < 1) {
					throw new EXCP_RtMgr_Validation(-602, "شناسه مسابقه معتبر نمی باشد.");
				}
				
				if (results == null || results.isEmpty() || results.size()==0) {
					throw new EXCP_RtMgr_Validation(-602, "لیست پاسخ سوالات معتبر نمی باشد");
				}
				
				for (Iterator<Object> iterator = results.iterator(); iterator.hasNext();) {
					JsonObject joResult = (JsonObject) iterator.next();
					if (!joResult.containsKey("questionId") ||
						joResult.getInteger("questionId") < 1 ||
						!joResult.containsKey("result") ||
						joResult.getString("result") == null ||
						joResult.getString("result").isEmpty() ||
						joResult.getString("result").length() == 0 ||
						joResult.getString("result").length() > 10) {
						
						throw new EXCP_RtMgr_Validation(-602, "لیست پاسخ سوالات معتبر نمی باشد");
					}
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
			
			joResult.put("competitionId", competitionId);
			joResult.put("results", results);

			joResult.put("userId", joToken.getInteger("id"));
			joResult.put("clientInfo", context.request().getHeader("User-Agent"));
			joResult.put("ip", context.request().remoteAddress().host());

			resultHandler.handle(Future.succeededFuture(joResult));

		});

    }

	public static void validatePointCalculation(RoutingContext context, Handler<AsyncResult<JsonObject>> resultHandler) {

		InputValidationUtil.validateToken(context).onComplete(handler -> {

			if (handler.failed()) {
				resultHandler.handle(Future.failedFuture(handler.cause()));
				return;
			}

			final JsonObject joToken = handler.result();

			Integer competitionId;

	        try {
	            final JsonObject inputParameters = InputValidationUtil.validate(context);

	            competitionId = inputParameters.getInteger("competitionId");
	            
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
			joResult.put("userId", joToken.getInteger("id"));
			joResult.put("clientInfo", context.request().getHeader("User-Agent"));
			joResult.put("ip", context.request().remoteAddress().host());

			resultHandler.handle(Future.succeededFuture(joResult));

		});

    }
	
}
