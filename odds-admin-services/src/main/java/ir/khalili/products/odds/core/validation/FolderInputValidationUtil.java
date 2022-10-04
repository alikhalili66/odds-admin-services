package ir.khalili.products.odds.core.validation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import ir.khalili.products.odds.core.excp.validation.EXCP_RtMgr_Validation;

public final class FolderInputValidationUtil {

    private static Logger logger = LoggerFactory.getLogger(FolderInputValidationUtil.class);

    private FolderInputValidationUtil() {
    }

	public static void validateSave(RoutingContext context, Handler<AsyncResult<JsonObject>> resultHandler) {

		InputValidationUtil.validateToken(context).onComplete(handler -> {

			if (handler.failed()) {
				resultHandler.handle(Future.failedFuture(handler.cause()));
				return;
			}

			final JsonObject joToken = handler.result();

			Integer parentId;
			Integer leagueId;
			String name;
			
	        try {
	            final JsonObject inputParameters = InputValidationUtil.validate(context);

	            parentId = inputParameters.getInteger("parentId");
	            leagueId = inputParameters.getInteger("leagueId");
	            name = inputParameters.getString("name");
	            
	            if (null != parentId && parentId < 1) {
	                throw new EXCP_RtMgr_Validation(-603, "شناسه پوشه پدر معتبر نیست");
	            }
	            
	            if (null == leagueId || leagueId < 1) {
	                throw new EXCP_RtMgr_Validation(-603, "شناسه لیگ معتبر نمی باشد");
	            }
	            
	            if (null == name || name.isEmpty()) {
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
			joResult.put("parentId", parentId);
			joResult.put("leagueId", leagueId);
			joResult.put("name", name);

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

			Integer folderId;
			Integer parentId;
			Integer leagueId;
			String name;
			
	        try {
	            final JsonObject inputParameters = InputValidationUtil.validate(context);

	            folderId = inputParameters.getInteger("folderId");
	            parentId = inputParameters.getInteger("parentId");
	            leagueId = inputParameters.getInteger("leagueId");
	            name = inputParameters.getString("name");

	            if (null == folderId || folderId < 1) {
	                throw new EXCP_RtMgr_Validation(-603, "شناسه پوشه معتبر نیست");
	            }
	            
	            if (null != parentId && parentId < 1) {
	                throw new EXCP_RtMgr_Validation(-603, "شناسه پوشه پدر معتبر نیست");
	            }
	            
	            if (null == leagueId || leagueId < 1) {
	                throw new EXCP_RtMgr_Validation(-603, "شناسه لیگ معتبر نمی باشد");
	            }
	            
	            if (null == name || name.isEmpty()) {
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
			joResult.put("folderId", folderId);
			joResult.put("parentId", parentId);
			joResult.put("leagueId", leagueId);
			joResult.put("name", name);

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

			Integer folderId;

	        try {
	            final JsonObject inputParameters = InputValidationUtil.validate(context);

	            folderId = inputParameters.getInteger("folderId");
	            
	            if (null == folderId || folderId < 1) {
	                throw new EXCP_RtMgr_Validation(-603, "شناسه پوشه صحیح نمی باشد");
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
			joResult.put("folderId", folderId);
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

		InputValidationUtil.validateToken(context).onComplete(handler -> {

			if (handler.failed()) {
				resultHandler.handle(Future.failedFuture(handler.cause()));
				return;
			}

			final JsonObject joToken = handler.result();

			Integer folderId;

	        try {
	            final JsonObject inputParameters = InputValidationUtil.validate(context);

	            folderId = inputParameters.getInteger("folderId");
	            
	            if (null == folderId || folderId < 1) {
	                throw new EXCP_RtMgr_Validation(-603, "شناسه پوشه صحیح نمی باشد");
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
			joResult.put("folderId", folderId);
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

			Integer folderId;
			Integer questionId;

	        try {
	            final JsonObject inputParameters = InputValidationUtil.validate(context);

	            folderId = inputParameters.getInteger("folderId");
	            questionId = inputParameters.getInteger("questionId");
	            
	            if (null == folderId || folderId < 1) {
	                throw new EXCP_RtMgr_Validation(-603, "شناسه پوشه معتبر نمی باشد");
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
			joResult.put("folderId", folderId);
			joResult.put("questionId", questionId);

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

			Integer folderId;
			Integer questionId;

	        try {
	            final JsonObject inputParameters = InputValidationUtil.validate(context);

	            folderId = inputParameters.getInteger("folderId");
	            questionId = inputParameters.getInteger("questionId");
	            
	            if (null == folderId || folderId < 1) {
	                throw new EXCP_RtMgr_Validation(-603, "شناسه پوشه معتبر نمی باشد");
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
			joResult.put("folderId", folderId);
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

			Integer folderId;

	        try {
	            final JsonObject inputParameters = InputValidationUtil.validate(context);

	            folderId = inputParameters.getInteger("folderId");
	            
	            if (null == folderId || folderId < 1) {
	                throw new EXCP_RtMgr_Validation(-603, "شناسه پوشه صحیح نمی باشد");
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
			joResult.put("folderId", folderId);
			joResult.put("userId", joToken.getInteger("id"));
			joResult.put("clientInfo", context.request().getHeader("User-Agent"));
			joResult.put("ip", context.request().remoteAddress().host());

			resultHandler.handle(Future.succeededFuture(joResult));

		});

    }
		
	
}
