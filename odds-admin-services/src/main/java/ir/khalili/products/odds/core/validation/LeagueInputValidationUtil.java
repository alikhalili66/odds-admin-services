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

public final class LeagueInputValidationUtil {

    private static Logger logger = LoggerFactory.getLogger(LeagueInputValidationUtil.class);

    private LeagueInputValidationUtil() {
    }


	public static void validateSave(RoutingContext context, Handler<AsyncResult<JsonObject>> resultHandler) {

		InputValidationUtil.validateToken(context, AccessLockIn.ODDS_LEAGUE_SAVE).onComplete(handler -> {

			if (handler.failed()) {
				resultHandler.handle(Future.failedFuture(handler.cause()));
				return;
			}

			final JsonObject joToken = handler.result();

			String name;
			String symbol;
			String image;
			String activeFrom;
			String activeTo;
			String oddsFrom;
			String oddsTo;

	        try {
	            final JsonObject inputParameters = InputValidationUtil.validate(context);

	            name = inputParameters.getString("name");
	            symbol = inputParameters.getString("symbol");
	            image = inputParameters.getString("image");
	            activeFrom = inputParameters.getString("activeFrom");
	            activeTo = inputParameters.getString("activeTo");
	            oddsFrom = inputParameters.getString("oddsFrom");
	            oddsTo = inputParameters.getString("oddsTo");
	            
	            if (null == name || name.isEmpty()) {
	                throw new EXCP_RtMgr_Validation(-603, "فیلد نام معتبر نمی باشد");
	            }
	            
	            if (null == symbol || symbol.isEmpty()) {
	            	throw new EXCP_RtMgr_Validation(-603, "فیلد نماد معتبر نمی باشد");
	            }
	            
	            if (null == image || image.isEmpty()) {
	            	throw new EXCP_RtMgr_Validation(-603, "فیلد تصویر معتبر نمی باشد");
	            }
	            
	            if (null == activeFrom || activeFrom.isEmpty()) {
	                throw new EXCP_RtMgr_Validation(-603, "فیلد 'فعال از' معتبر نمی باشد");
	            }
	            
	            if (null == activeTo || activeTo.isEmpty()) {
	                throw new EXCP_RtMgr_Validation(-603, "فیلد 'فعال تا' معتبر نمی باشد");
	            }
	            
	            if (null == oddsFrom || oddsFrom.isEmpty()) {
	                throw new EXCP_RtMgr_Validation(-603, "فیلد 'شروع پیش بینی' معتبر نمی باشد");
	            }
	            
	            if (null == oddsTo || oddsTo.isEmpty()) {
	                throw new EXCP_RtMgr_Validation(-603, "فیلد 'پایان پیش بینی' معتبر نمی باشد");
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
			joResult.put("name", name);
			joResult.put("symbol", symbol);
			joResult.put("image", image);
			joResult.put("activeFrom", activeFrom.split(" GMT")[0]);
			joResult.put("activeTo", activeTo.split(" GMT")[0]);
			joResult.put("oddsFrom", oddsFrom.split(" GMT")[0]);
			joResult.put("oddsTo", oddsTo.split(" GMT")[0]);
			
			joResult.put("userId", joToken.getInteger("id"));
			joResult.put("clientInfo", context.request().getHeader("User-Agent"));
			joResult.put("ip", context.request().remoteAddress().host());

			resultHandler.handle(Future.succeededFuture(joResult));

		});

    }
	
	public static void validateUpdate(RoutingContext context, Handler<AsyncResult<JsonObject>> resultHandler) {

		InputValidationUtil.validateToken(context, AccessLockIn.ODDS_LEAGUE_UPDATE).onComplete(handler -> {

			if (handler.failed()) {
				resultHandler.handle(Future.failedFuture(handler.cause()));
				return;
			}

			final JsonObject joToken = handler.result();

			Integer leagueId;
			String name;
			String symbol;
			String image;
			String activeFrom;
			String activeTo;
			String oddsFrom;
			String oddsTo;

	        try {
	            final JsonObject inputParameters = InputValidationUtil.validate(context);

	            leagueId = inputParameters.getInteger("leagueId");
	            name = inputParameters.getString("name");
	            symbol = inputParameters.getString("symbol");
	            image = inputParameters.getString("image");
	            activeFrom = inputParameters.getString("activeFrom");
	            activeTo = inputParameters.getString("activeTo");
	            oddsFrom = inputParameters.getString("oddsFrom");
	            oddsTo = inputParameters.getString("oddsTo");

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
	            
	            if (null == activeFrom || activeFrom.isEmpty()) {
	                throw new EXCP_RtMgr_Validation(-603, "فیلد 'فعال از' معتبر نمی باشد");
	            }
	            
	            if (null == activeTo || activeTo.isEmpty()) {
	                throw new EXCP_RtMgr_Validation(-603, "فیلد 'فعال تا' معتبر نمی باشد");
	            }
	            
	            if (null == oddsFrom || oddsFrom.isEmpty()) {
	                throw new EXCP_RtMgr_Validation(-603, "فیلد 'شروع پیش بینی' معتبر نمی باشد");
	            }
	            
	            if (null == oddsTo || oddsTo.isEmpty()) {
	                throw new EXCP_RtMgr_Validation(-603, "فیلد 'پایان پیش بینی' معتبر نمی باشد");
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
			joResult.put("activeFrom", activeFrom.split(" GMT")[0]);
			joResult.put("activeTo", activeTo.split(" GMT")[0]);
			joResult.put("oddsFrom", oddsFrom.split(" GMT")[0]);
			joResult.put("oddsTo", oddsTo.split(" GMT")[0]);
			
			joResult.put("userId", joToken.getInteger("id"));
			joResult.put("clientInfo", context.request().getHeader("User-Agent"));
			joResult.put("ip", context.request().remoteAddress().host());

			resultHandler.handle(Future.succeededFuture(joResult));

		});

    }
	
	public static void validateDelete(RoutingContext context, Handler<AsyncResult<JsonObject>> resultHandler) {

		InputValidationUtil.validateToken(context, AccessLockIn.ODDS_LEAGUE_DELETE).onComplete(handler -> {

			if (handler.failed()) {
				resultHandler.handle(Future.failedFuture(handler.cause()));
				return;
			}

			final JsonObject joToken = handler.result();

			Integer leagueId;

	        try {
	            final JsonObject inputParameters = InputValidationUtil.validate(context);

	            leagueId = inputParameters.getInteger("leagueId");
	            
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
			joResult.put("userId", joToken.getInteger("id"));
			joResult.put("clientInfo", context.request().getHeader("User-Agent"));
			joResult.put("ip", context.request().remoteAddress().host());

			resultHandler.handle(Future.succeededFuture(joResult));

		});

    }
	
	public static void validateFetchAll(RoutingContext context, Handler<AsyncResult<JsonObject>> resultHandler) {

		InputValidationUtil.validateToken(context, AccessLockIn.ODDS_LEAGUE_FETCH_ALL).onComplete(handler -> {

			if (handler.failed()) {
				resultHandler.handle(Future.failedFuture(handler.cause()));
				return;
			}

			final JsonObject joToken = handler.result();

			final JsonObject joResult = new JsonObject();
			joResult.put("userId", joToken.getInteger("id"));
			joResult.put("clientInfo", context.request().getHeader("User-Agent"));
			joResult.put("ip", context.request().remoteAddress().host());

			resultHandler.handle(Future.succeededFuture(joResult));

		});

    }
	
	public static void validateFetchById(RoutingContext context, Handler<AsyncResult<JsonObject>> resultHandler) {

		InputValidationUtil.validateToken(context, AccessLockIn.ODDS_LEAGUE_FETCH_BY_ID).onComplete(handler -> {

			if (handler.failed()) {
				resultHandler.handle(Future.failedFuture(handler.cause()));
				return;
			}

			final JsonObject joToken = handler.result();

			Integer leagueId;

	        try {
	            final JsonObject inputParameters = InputValidationUtil.validate(context);

	            leagueId = inputParameters.getInteger("leagueId");
	            
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
			joResult.put("userId", joToken.getInteger("id"));
			joResult.put("clientInfo", context.request().getHeader("User-Agent"));
			joResult.put("ip", context.request().remoteAddress().host());

			resultHandler.handle(Future.succeededFuture(joResult));

		});

    }
	
	
}
