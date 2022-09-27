package ir.khalili.products.odds.core.biz.group;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.sql.SQLConnection;
import ir.khalili.products.odds.core.dao.DAO_Customer;

public class Biz_07_GroupTeamUnAssign {

    private static final Logger logger = LogManager.getLogger(Biz_07_GroupTeamUnAssign.class);

    public static void createAccount(SQLConnection sqlConnection, JsonObject message, Handler<AsyncResult<JsonObject>> resultHandler) {

        logger.trace("inputMessage:" + message);

        final Long customerId = message.getLong("customerId");
        final Long serviceId = message.getLong("serviceId");

        DAO_Customer.checkCustomerValidTo(sqlConnection, customerId, serviceId).onComplete(resultSet -> {
            if (resultSet.failed()) {
                resultHandler.handle(Future.failedFuture(resultSet.cause()));
                return;
            }
            if (resultSet.result()) {
                DAO_Customer.fetchCustomerById(sqlConnection, customerId).onComplete(fetchResult -> {
                    if (fetchResult.failed()) {
                        resultHandler.handle(Future.failedFuture(fetchResult.cause()));
                        return;
                    }
                    
                });
            }

        });

    }

}
