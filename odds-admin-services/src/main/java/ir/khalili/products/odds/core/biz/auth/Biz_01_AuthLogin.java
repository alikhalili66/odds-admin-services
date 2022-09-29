package ir.khalili.products.odds.core.biz.auth;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.sql.SQLConnection;
import ir.khalili.products.odds.core.biz.excp.BIZEXCP_Auth;
import ir.khalili.products.odds.core.helper.HelperLockIn;

public class Biz_01_AuthLogin {

	private static final Logger logger = LogManager.getLogger(Biz_01_AuthLogin.class);

	public static void doAuth(SQLConnection sqlConnection, JsonObject message, Handler<AsyncResult<JsonObject>> resultHandler) {

		logger.trace("inputMessage:"+message);
		
		final String username = message.getString("username");
		final String password = message.getString("password");
		
		HelperLockIn.doLogin(username, password).onComplete(lockinHandler->{
			
			if(lockinHandler.failed()) {
				logger.error("Unable to complete lockinHandler: " + lockinHandler.cause());
				resultHandler.handle(Future.failedFuture(lockinHandler.cause()));
				return;
			}
			
			JsonObject joLogin = lockinHandler.result();
			
			if(!joLogin.getString("type").matches("A")) {
				resultHandler.handle(Future.failedFuture(new BIZEXCP_Auth(-1, "نوع کاربر صحیح نمی باشد.")));
				return;
			}
			
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
			joInfo.put("roles", joLogin.getJsonArray("roles"));
			joInfo.put("groups", joLogin.getJsonArray("groups"));
			
			resultHandler.handle(Future.succeededFuture(
					new JsonObject()
					.put("resultCode", 1)
					.put("resultMessage", "عملیات با موفقیت انجام شد.")
					.put("info", joInfo)
					));
		
		});
		
	}

}
