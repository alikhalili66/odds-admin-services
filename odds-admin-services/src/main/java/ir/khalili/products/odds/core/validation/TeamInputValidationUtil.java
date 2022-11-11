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

public final class TeamInputValidationUtil {

    private static Logger logger = LoggerFactory.getLogger(TeamInputValidationUtil.class);

    private TeamInputValidationUtil() {
    }


	public static void validateSave(RoutingContext context, Handler<AsyncResult<JsonObject>> resultHandler) {

		InputValidationUtil.validateToken(context, AccessLockIn.ODDS_TEAM_SAVE).onComplete(handler -> {

			if (handler.failed()) {
				resultHandler.handle(Future.failedFuture(handler.cause()));
				return;
			}

			final JsonObject joToken = handler.result();

			Integer leagueId;
			String name;
			String symbol;
			String image;

	        try {
	            final JsonObject inputParameters = InputValidationUtil.validate(context);

	            leagueId = inputParameters.getInteger("leagueId");
	            name = inputParameters.getString("name");
	            symbol = inputParameters.getString("symbol");
	            image = inputParameters.getString("image");

	            if (null == leagueId || leagueId < 1) {
	                throw new EXCP_RtMgr_Validation(-603, "شناسه لیگ معتبر نمی باشد");
	            }
	            
	            if (null == name || name.isEmpty()) {
	                throw new EXCP_RtMgr_Validation(-603, "فیلد نام معتبر نمی باشد");
	            }

	            if (null == symbol || symbol.isEmpty()) {
	                throw new EXCP_RtMgr_Validation(-603, "فیلد نماد معتبر نمی باشد");
	            }

	            if (null == image || image.isEmpty()) {
	                throw new EXCP_RtMgr_Validation(-603, "فیلد تصویر معتبر نمی باشد");
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
			joResult.put("name", name);
			joResult.put("symbol", symbol);
			joResult.put("image", image);
			joResult.put("userId", joToken.getInteger("id"));
			joResult.put("clientInfo", context.request().getHeader("User-Agent"));
			joResult.put("ip", context.request().remoteAddress().host());

			resultHandler.handle(Future.succeededFuture(joResult));

		});

    }

	public static void validateImageUpdate(RoutingContext context, Handler<AsyncResult<JsonObject>> resultHandler) {

		InputValidationUtil.validateToken(context, AccessLockIn.ODDS_TEAM_IMAGE_UPDATE).onComplete(handler -> {

			if (handler.failed()) {
				resultHandler.handle(Future.failedFuture(handler.cause()));
				return;
			}

			final JsonObject joToken = handler.result();

			Integer teamId;
			String image;

	        try {
	            final JsonObject inputParameters = InputValidationUtil.validate(context);

	            teamId = inputParameters.getInteger("teamId");
	            image = inputParameters.getString("image");

	            if (null == teamId || teamId < 1) {
	                throw new EXCP_RtMgr_Validation(-603, "شناسه تیم معتبر نمی باشد");
	            }
	            
	            if (null == image || image.isEmpty()) {
	                throw new EXCP_RtMgr_Validation(-603, "فیلد نام معتبر نمی باشد");
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
			joResult.put("teamId", teamId);
			joResult.put("image", image);
			joResult.put("userId", joToken.getInteger("id"));
			joResult.put("clientInfo", context.request().getHeader("User-Agent"));
			joResult.put("ip", context.request().remoteAddress().host());

			resultHandler.handle(Future.succeededFuture(joResult));

		});

    }
	
	
	public static void validateUpdate(RoutingContext context, Handler<AsyncResult<JsonObject>> resultHandler) {

		InputValidationUtil.validateToken(context, AccessLockIn.ODDS_TEAM_UPDATE).onComplete(handler -> {

			if (handler.failed()) {
				resultHandler.handle(Future.failedFuture(handler.cause()));
				return;
			}

			final JsonObject joToken = handler.result();

			Integer teamId;
			Integer leagueId;
			String name;
			String symbol;

	        try {
	            final JsonObject inputParameters = InputValidationUtil.validate(context);

	            teamId = inputParameters.getInteger("teamId");
	            leagueId = inputParameters.getInteger("leagueId");
	            name = inputParameters.getString("name");
	            symbol = inputParameters.getString("symbol");

	            if (null == teamId || teamId < 1) {
	                throw new EXCP_RtMgr_Validation(-603, "شناسه تیم معتبر نمی باشد");
	            }
	            
	            if (null == leagueId || leagueId < 1) {
	                throw new EXCP_RtMgr_Validation(-603, "شناسه لیگ معتبر نمی باشد");
	            }
	            
	            if (null == name || name.isEmpty()) {
	                throw new EXCP_RtMgr_Validation(-603, "فیلد نام معتبر نمی باشد");
	            }

	            if (null == symbol || symbol.isEmpty()) {
	                throw new EXCP_RtMgr_Validation(-603, "فیلد نماد معتبر نمی باشد");
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
			joResult.put("teamId", teamId);
			joResult.put("leagueId", leagueId);
			joResult.put("name", name);
			joResult.put("symbol", symbol);
			joResult.put("userId", joToken.getInteger("id"));
			joResult.put("clientInfo", context.request().getHeader("User-Agent"));
			joResult.put("ip", context.request().remoteAddress().host());

			resultHandler.handle(Future.succeededFuture(joResult));

		});

    }
	
	public static void validateDelete(RoutingContext context, Handler<AsyncResult<JsonObject>> resultHandler) {

		InputValidationUtil.validateToken(context, AccessLockIn.ODDS_TEAM_DELETE).onComplete(handler -> {

			if (handler.failed()) {
				resultHandler.handle(Future.failedFuture(handler.cause()));
				return;
			}

			final JsonObject joToken = handler.result();

			Integer teamId;

	        try {
	            final JsonObject inputParameters = InputValidationUtil.validate(context);

	            teamId = inputParameters.getInteger("teamId");
	            
	            if (null == teamId || teamId < 1) {
	                throw new EXCP_RtMgr_Validation(-603, "شناسه تیم معتبر نمی باشد");
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
			joResult.put("teamId", teamId);
			joResult.put("userId", joToken.getInteger("id"));
			joResult.put("clientInfo", context.request().getHeader("User-Agent"));
			joResult.put("ip", context.request().remoteAddress().host());

			resultHandler.handle(Future.succeededFuture(joResult));

		});

    }
	
	public static void validateMemberDelete(RoutingContext context, Handler<AsyncResult<JsonObject>> resultHandler) {

		InputValidationUtil.validateToken(context, AccessLockIn.ODDS_TEAM_MEMBER_DELETE).onComplete(handler -> {

			if (handler.failed()) {
				resultHandler.handle(Future.failedFuture(handler.cause()));
				return;
			}

			final JsonObject joToken = handler.result();

			Integer memberId;

	        try {
	            final JsonObject inputParameters = InputValidationUtil.validate(context);

	            memberId = inputParameters.getInteger("memberId");
	            
	            if (null == memberId || memberId < 1) {
	                throw new EXCP_RtMgr_Validation(-603, "شناسه عضو معتبر نمی باشد");
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
			joResult.put("memberId", memberId);
			joResult.put("userId", joToken.getInteger("id"));
			joResult.put("clientInfo", context.request().getHeader("User-Agent"));
			joResult.put("ip", context.request().remoteAddress().host());

			resultHandler.handle(Future.succeededFuture(joResult));

		});

    }
	
	public static void validateFetchAll(RoutingContext context, Handler<AsyncResult<JsonObject>> resultHandler) {

		InputValidationUtil.validateToken(context, AccessLockIn.ODDS_TEAM_FETCH_ALL).onComplete(handler -> {

			if (handler.failed()) {
				resultHandler.handle(Future.failedFuture(handler.cause()));
				return;
			}

			final JsonObject joToken = handler.result();

			Integer leagueId;

	        try {
	            final JsonObject inputParameters = InputValidationUtil.validate(context);

	            leagueId = inputParameters.getInteger("leagueId");
	            
	            if (null != leagueId && leagueId < 1) {
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
			
			joResult.put("userId", joToken.getInteger("id"));
			joResult.put("clientInfo", context.request().getHeader("User-Agent"));
			joResult.put("ip", context.request().remoteAddress().host());

			resultHandler.handle(Future.succeededFuture(joResult));

		});

    }
	
	public static void validateFetchById(RoutingContext context, Handler<AsyncResult<JsonObject>> resultHandler) {

		InputValidationUtil.validateToken(context, AccessLockIn.ODDS_TEAM_FETCH_BY_ID).onComplete(handler -> {

			if (handler.failed()) {
				resultHandler.handle(Future.failedFuture(handler.cause()));
				return;
			}

			final JsonObject joToken = handler.result();

			Integer teamId;

	        try {
	            final JsonObject inputParameters = InputValidationUtil.validate(context);

	            teamId = inputParameters.getInteger("teamId");
	            
	            if (null == teamId || teamId < 1) {
	                throw new EXCP_RtMgr_Validation(-603, "شناسه تیم معتبر نمی باشد");
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
			joResult.put("teamId", teamId);
			joResult.put("userId", joToken.getInteger("id"));
			joResult.put("clientInfo", context.request().getHeader("User-Agent"));
			joResult.put("ip", context.request().remoteAddress().host());

			resultHandler.handle(Future.succeededFuture(joResult));

		});

    }
	
	public static void validateMemberFetchAll(RoutingContext context, Handler<AsyncResult<JsonObject>> resultHandler) {

		InputValidationUtil.validateToken(context, AccessLockIn.ODDS_TEAM_MEMBER_FETCH_ALL).onComplete(handler -> {

			if (handler.failed()) {
				resultHandler.handle(Future.failedFuture(handler.cause()));
				return;
			}

			final JsonObject joToken = handler.result();

			Integer teamId;

	        try {
	            final JsonObject inputParameters = InputValidationUtil.validate(context);

	            teamId = inputParameters.getInteger("teamId");
	            
	            if (null == teamId || teamId < 1) {
	                throw new EXCP_RtMgr_Validation(-603, "شناسه تیم معتبر نمی باشد");
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
			joResult.put("teamId", teamId);
			joResult.put("userId", joToken.getInteger("id"));
			joResult.put("clientInfo", context.request().getHeader("User-Agent"));
			joResult.put("ip", context.request().remoteAddress().host());

			resultHandler.handle(Future.succeededFuture(joResult));

		});

    }
	
	public static void validateMemberUpdate(RoutingContext context, Handler<AsyncResult<JsonObject>> resultHandler) {

		InputValidationUtil.validateToken(context, AccessLockIn.ODDS_TEAM_MEMBER_UPDATE).onComplete(handler -> {

			if (handler.failed()) {
				resultHandler.handle(Future.failedFuture(handler.cause()));
				return;
			}

			final JsonObject joToken = handler.result();

			Integer memberId;
			Integer teamId;
			String name;
			Integer count;
			String position;

	        try {
	            final JsonObject inputParameters = InputValidationUtil.validate(context);

	            memberId = inputParameters.getInteger("memberId");
	            teamId = inputParameters.getInteger("teamId");
	            name = inputParameters.getString("name");
	            count = inputParameters.getInteger("count");
	            position = inputParameters.getString("position");

	            if (null == memberId || memberId < 1) {
	                throw new EXCP_RtMgr_Validation(-603, "شناسه عضو معتبر نمی باشد");
	            }
	            
	            if (null == teamId || teamId < 1) {
	                throw new EXCP_RtMgr_Validation(-603, "شناسه تیم معتبر نمی باشد");
	            }
	            
	            if (null == name || name.isEmpty()) {
	                throw new EXCP_RtMgr_Validation(-603, "فیلد نام عضو معتبر نمی باشد");
	            }

	            if (null == count || count < 1) {
	                throw new EXCP_RtMgr_Validation(-603, "فیلد تعداد معتبر نمی باشد");
	            }
	            
	            if (null == position || position.isEmpty()) {
	                throw new EXCP_RtMgr_Validation(-603, "فیلد پست معتبر نمی باشد");
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
			joResult.put("memberId", memberId);
			joResult.put("teamId", teamId);
			joResult.put("name", name);
			joResult.put("count", count);
			joResult.put("position", position);
			
			joResult.put("userId", joToken.getInteger("id"));
			joResult.put("clientInfo", context.request().getHeader("User-Agent"));
			joResult.put("ip", context.request().remoteAddress().host());

			resultHandler.handle(Future.succeededFuture(joResult));

		});

    }
	
	public static void validateMemberSave(RoutingContext context, Handler<AsyncResult<JsonObject>> resultHandler) {

		InputValidationUtil.validateToken(context, AccessLockIn.ODDS_TEAM_MEMBER_SAVE).onComplete(handler -> {

			if (handler.failed()) {
				resultHandler.handle(Future.failedFuture(handler.cause()));
				return;
			}

			final JsonObject joToken = handler.result();

			Integer teamId;
			String name;
			Integer count;
			String position;

	        try {
	            final JsonObject inputParameters = InputValidationUtil.validate(context);

	            teamId = inputParameters.getInteger("teamId");
	            name = inputParameters.getString("name");
	            count = inputParameters.getInteger("count");
	            position = inputParameters.getString("position");

	            if (null == teamId || teamId < 1) {
	                throw new EXCP_RtMgr_Validation(-603, "شناسه تیم معتبر نمی باشد");
	            }
	            
	            if (null == name || name.isEmpty()) {
	                throw new EXCP_RtMgr_Validation(-603, "فیلد نام عضو معتبر نمی باشد");
	            }

	            if (null == count || count < 1) {
	                throw new EXCP_RtMgr_Validation(-603, "فیلد تعداد معتبر نمی باشد");
	            }
	            
	            if (null == position || position.isEmpty()) {
	                throw new EXCP_RtMgr_Validation(-603, "فیلد پست معتبر نمی باشد");
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
			joResult.put("teamId", teamId);
			joResult.put("name", name);
			joResult.put("count", count);
			joResult.put("position", position);
			
			joResult.put("userId", joToken.getInteger("id"));
			joResult.put("clientInfo", context.request().getHeader("User-Agent"));
			joResult.put("ip", context.request().remoteAddress().host());

			resultHandler.handle(Future.succeededFuture(joResult));

		});

    }
}
