package ir.khalili.products.odds.core.biz.auth;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import ir.khalili.products.odds.core.helper.HelperLockIn;

public class Biz_02_AuthOTP {

	private static final Logger logger = LogManager.getLogger(Biz_02_AuthOTP.class);

	public static void doAuth(JsonObject message, Handler<AsyncResult<JsonObject>> resultHandler) {

		logger.trace("inputMessage:"+message);
		
		final long cellphone = message.getLong("cellphone");
		final int code = message.getInteger("code");
		
		HelperLockIn.checkOTP(cellphone, code).onComplete(handler->{
			
			if(handler.failed()) {
				logger.error("Unable to complete handle: " + handler.cause());
				resultHandler.handle(Future.failedFuture(handler.cause()));
				return;
			}
			
			JsonObject joLogin = handler.result();
			
//			if(!joLogin.getString("type").matches("S|A|O")) {
//				resultHandler.handle(Future.failedFuture(new BIZEXCP_Auth(-1, "نوع کاربر صحیح نمی باشد.")));
//				return;
//			}
			
			JsonObject joInfo = new JsonObject();
			joInfo.put("token", joLogin.getString("token"));
			joInfo.put("id", joLogin.getInteger("id"));
			joInfo.put("name", joLogin.getString("name"));
			joInfo.put("lastName", joLogin.getString("lastName"));
			joInfo.put("status", joLogin.getString("status"));
			joInfo.put("type", joLogin.getString("type"));
			joInfo.put("cellphone", joLogin.getLong("cellphone"));
			joInfo.put("isOtpEnable", joLogin.getBoolean("isOtpEnable"));
			joInfo.put("accesses", joLogin.getJsonArray("accesses"));
			joInfo.put("roles", joLogin.getJsonArray("accesses"));
			joInfo.put("groups", joLogin.getJsonArray("accesses"));
			
			resultHandler.handle(Future.succeededFuture(
					new JsonObject()
					.put("resultCode", 1)
					.put("resultMessage", "عملیات با موفقیت انجام شد.")
					.put("info", joInfo)
					));
		});
		
	}

}
