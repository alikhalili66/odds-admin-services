package ir.khalili.products.odds.core.biz.user;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.sql.SQLConnection;
import ir.khalili.products.odds.core.dao.DAO_User;

public class Biz_03_UserFetchOdds {

    private static final Logger logger = LogManager.getLogger(Biz_03_UserFetchOdds.class);

    public static void fetchOdds(SQLConnection sqlConnection, JsonObject message, Handler<AsyncResult<JsonObject>> resultHandler) {

        logger.trace("inputMessage:" + message);

        final Long customerId = message.getLong("customerId");
        final Long serviceId = message.getLong("serviceId");

        DAO_User.checkCustomerValidTo(sqlConnection, customerId, serviceId).onComplete(resultSet -> {
            if (resultSet.failed()) {
                resultHandler.handle(Future.failedFuture(resultSet.cause()));
                return;
            }

			resultHandler.handle(Future.succeededFuture(
					new JsonObject()
					.put("resultCode", 1)
					.put("resultMessage", "عملیات با موفقیت انجام شد.")
					));

			
        });

    }

}
