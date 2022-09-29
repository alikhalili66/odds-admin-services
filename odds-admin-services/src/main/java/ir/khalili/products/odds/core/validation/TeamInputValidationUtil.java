package ir.khalili.products.odds.core.validation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import ir.khalili.products.odds.core.excp.validation.EXCP_RtMgr_Validation;

public final class TeamInputValidationUtil {

    private static Logger logger = LoggerFactory.getLogger(TeamInputValidationUtil.class);

    private TeamInputValidationUtil() {
    }


	public static void validateSave(RoutingContext context, Handler<AsyncResult<JsonObject>> resultHandler) {

		InputValidationUtil.validateToken(context).onComplete(handler -> {

			if (handler.failed()) {
				resultHandler.handle(Future.failedFuture(handler.cause()));
				return;
			}

			final JsonObject joSession = handler.result();

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

			Integer teamId;
			Integer leagueId;
			String name;
			String symbol;
			String image;

	        try {
	            final JsonObject inputParameters = InputValidationUtil.validate(context);

	            teamId = inputParameters.getInteger("teamId");
	            leagueId = inputParameters.getInteger("leagueId");
	            name = inputParameters.getString("name");
	            symbol = inputParameters.getString("symbol");
	            image = inputParameters.getString("image");

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
			joResult.put("teamId", teamId);
			joResult.put("leagueId", leagueId);
			joResult.put("name", name);
			joResult.put("symbol", symbol);
			joResult.put("image", image);
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
			joResult.put("clientInfo", context.request().getHeader("User-Agent"));
			joResult.put("ip", context.request().remoteAddress().host());

			resultHandler.handle(Future.succeededFuture(joResult));

		});

    }
	
	
}
