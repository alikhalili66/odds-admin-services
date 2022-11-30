package ir.khalili.products.odds.core.biz.user;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.sql.SQLConnection;
import ir.khalili.products.odds.core.dao.DAO_User;

public class Biz_06_UserEditNikeName {

    private static final Logger logger = LogManager.getLogger(Biz_06_UserEditNikeName.class);

    public static void doEdit(SQLConnection sqlConnection, JsonObject message, Handler<AsyncResult<JsonObject>> resultHandler) {

		logger.trace("inputMessage:" + message);

		int id = message.getInteger("id");
		String nikename = message.getString("nikename");
		
		DAO_User.fetchById(sqlConnection, id).onComplete(result -> {
			if (result.failed()) {
				logger.error("Unable to complete result: " + result.cause());
				resultHandler.handle(Future.failedFuture(result.cause()));
				return;
			}

			DAO_User.updateNikeName(sqlConnection, id, nikename).onComplete(updateHandler->{
				
				if (updateHandler.failed()) {
					logger.error("Unable to complete updateHandler: " + updateHandler.cause());
					resultHandler.handle(Future.failedFuture(updateHandler.cause()));
					return;
				}
				
				logger.trace("USER_FETCH_BY_ID_RESULT : " + result.result());

				resultHandler.handle(Future.succeededFuture(
						new JsonObject()
						.put("resultCode", 1)
						.put("resultMessage", "عملیات با موفقیت انجام شد.")
						));
				
			});

		});

    }

}
