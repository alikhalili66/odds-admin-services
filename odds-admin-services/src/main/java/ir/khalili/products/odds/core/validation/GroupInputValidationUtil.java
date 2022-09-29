package ir.khalili.products.odds.core.validation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import ir.khalili.products.odds.core.excp.validation.EXCP_RtMgr_Validation;

public final class GroupInputValidationUtil {

    private static Logger logger = LoggerFactory.getLogger(GroupInputValidationUtil.class);

    private GroupInputValidationUtil() {
    }

	public static void validateSave(RoutingContext context, Handler<AsyncResult<JsonObject>> resultHandler) {

		InputValidationUtil.validateToken(context).onComplete(handler -> {

			if (handler.failed()) {
				resultHandler.handle(Future.failedFuture(handler.cause()));
				return;
			}

			final JsonObject joToken = handler.result();

			Integer leagueId;
			String name;
			String activeFrom;
			String activeTo;

	        try {
	            final JsonObject inputParameters = InputValidationUtil.validate(context);

	            leagueId = inputParameters.getInteger("leagueId");
	            name = inputParameters.getString("name");
	            activeFrom = inputParameters.getString("activeFrom");
	            activeTo = inputParameters.getString("activeTo");

	            if (null == name || name.isEmpty()) {
	                throw new EXCP_RtMgr_Validation(-603, "فیلد نام معتبر نمی باشد");
	            }
	            
	            if (null == leagueId || leagueId < 1) {
	            	throw new EXCP_RtMgr_Validation(-603, "شناسه لیگ معتبر نمی باشد");
	            }
	            
	            if (null == activeFrom || activeFrom.isEmpty()) {
	                throw new EXCP_RtMgr_Validation(-603, "فیلد 'فعال از' معتبر نمی باشد");
	            }
	            
	            if (null == activeTo || activeTo.isEmpty()) {
	                throw new EXCP_RtMgr_Validation(-603, "فیلد 'فعال تا' معتبر نمی باشد");
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
			joResult.put("leagueId", leagueId);
			joResult.put("activeFrom", activeFrom);
			joResult.put("activeTo", activeTo);

			joResult.put("userId", joToken.getInteger("userId"));
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

			Integer groupId;
			Integer leagueId;
			String name;
			String activeFrom;
			String activeTo;

	        try {
	            final JsonObject inputParameters = InputValidationUtil.validate(context);

	            groupId = inputParameters.getInteger("groupId");
	            leagueId = inputParameters.getInteger("leagueId");
	            name = inputParameters.getString("name");
	            activeFrom = inputParameters.getString("activeFrom");
	            activeTo = inputParameters.getString("activeTo");

	            if (null == groupId || groupId < 1) {
	                throw new EXCP_RtMgr_Validation(-603, "شناسه گروه معتبر نمی باشد");
	            }
	            
	            if (null == name || name.isEmpty()) {
	                throw new EXCP_RtMgr_Validation(-603, "فیلد نام معتبر نمی باشد");
	            }
	            
	            if (null == leagueId || leagueId < 1) {
	            	throw new EXCP_RtMgr_Validation(-603, "شناسه لیگ معتبر نمی باشد");
	            }
	            
	            if (null == activeFrom || activeFrom.isEmpty()) {
	                throw new EXCP_RtMgr_Validation(-603, "فیلد 'فعال از' معتبر نمی باشد");
	            }
	            
	            if (null == activeTo || activeTo.isEmpty()) {
	                throw new EXCP_RtMgr_Validation(-603, "فیلد 'فعال تا' معتبر نمی باشد");
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
			joResult.put("groupId", groupId);
			joResult.put("name", name);
			joResult.put("leagueId", leagueId);
			joResult.put("activeFrom", activeFrom);
			joResult.put("activeTo", activeTo);

			joResult.put("userId", joToken.getInteger("userId"));
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

			Integer groupId;

	        try {
	            final JsonObject inputParameters = InputValidationUtil.validate(context);

	            groupId = inputParameters.getInteger("groupId");
	            
	            if (null == groupId || groupId < 1) {
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
			joResult.put("groupId", groupId);
			joResult.put("userId", joToken.getInteger("userId"));
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

			final JsonObject joResult = new JsonObject();
			joResult.put("userId", joToken.getInteger("userId"));
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

			Integer groupId;

	        try {
	            final JsonObject inputParameters = InputValidationUtil.validate(context);

	            groupId = inputParameters.getInteger("groupId");
	            
	            if (null == groupId || groupId < 1) {
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
			joResult.put("groupId", groupId);
			joResult.put("userId", joToken.getInteger("userId"));
			joResult.put("clientInfo", context.request().getHeader("User-Agent"));
			joResult.put("ip", context.request().remoteAddress().host());

			resultHandler.handle(Future.succeededFuture(joResult));

		});

    }
	
	public static void validateTeamAssign(RoutingContext context, Handler<AsyncResult<JsonObject>> resultHandler) {

		InputValidationUtil.validateToken(context).onComplete(handler -> {

			if (handler.failed()) {
				resultHandler.handle(Future.failedFuture(handler.cause()));
				return;
			}

			final JsonObject joToken = handler.result();

			Long cellphone;

	        try {
	            final JsonObject inputParameters = InputValidationUtil.validate(context);

	            cellphone = inputParameters.getLong("cellphone");
	            
	            if (null == cellphone || cellphone < 1) {
	                throw new EXCP_RtMgr_Validation(-603, "شماره تلفن معتبر نمی باشد.");
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

			joResult.put("userId", joToken.getInteger("userId"));
			joResult.put("clientInfo", context.request().getHeader("User-Agent"));
			joResult.put("ip", context.request().remoteAddress().host());

			resultHandler.handle(Future.succeededFuture(joResult));

		});

    }
	
	public static void validateTeamUnAssign(RoutingContext context, Handler<AsyncResult<JsonObject>> resultHandler) {

		InputValidationUtil.validateToken(context).onComplete(handler -> {

			if (handler.failed()) {
				resultHandler.handle(Future.failedFuture(handler.cause()));
				return;
			}

			final JsonObject joToken = handler.result();

			Long cellphone;

	        try {
	            final JsonObject inputParameters = InputValidationUtil.validate(context);

	            cellphone = inputParameters.getLong("cellphone");
	            
	            if (null == cellphone || cellphone < 1) {
	                throw new EXCP_RtMgr_Validation(-603, "شماره تلفن معتبر نمی باشد.");
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

			joResult.put("userId", joToken.getInteger("userId"));
			joResult.put("clientInfo", context.request().getHeader("User-Agent"));
			joResult.put("ip", context.request().remoteAddress().host());

			resultHandler.handle(Future.succeededFuture(joResult));

		});

    }
	
	public static void validateTeamFetch(RoutingContext context, Handler<AsyncResult<JsonObject>> resultHandler) {

		InputValidationUtil.validateToken(context).onComplete(handler -> {

			if (handler.failed()) {
				resultHandler.handle(Future.failedFuture(handler.cause()));
				return;
			}

			final JsonObject joToken = handler.result();

			Long cellphone;

	        try {
	            final JsonObject inputParameters = InputValidationUtil.validate(context);

	            cellphone = inputParameters.getLong("cellphone");
	            
	            if (null == cellphone || cellphone < 1) {
	                throw new EXCP_RtMgr_Validation(-603, "شماره تلفن معتبر نمی باشد.");
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

			joResult.put("userId", joToken.getInteger("userId"));
			joResult.put("clientInfo", context.request().getHeader("User-Agent"));
			joResult.put("ip", context.request().remoteAddress().host());

			resultHandler.handle(Future.succeededFuture(joResult));

		});

    }
		
	
}
