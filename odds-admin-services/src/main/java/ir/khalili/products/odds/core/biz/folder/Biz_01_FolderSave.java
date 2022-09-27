package ir.khalili.products.odds.core.biz.folder;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.sql.SQLConnection;
import ir.khalili.products.odds.core.dao.DAO_Folder;

public class Biz_01_FolderSave {

    private static final Logger logger = LogManager.getLogger(Biz_01_FolderSave.class);

    public static void save(SQLConnection sqlConnection, JsonObject message, Handler<AsyncResult<JsonObject>> resultHandler) {

        logger.trace("inputMessage:" + message);

        final Long customerId = message.getLong("customerId");
        final Long serviceId = message.getLong("serviceId");

        DAO_Folder.checkCustomerValidTo(sqlConnection, customerId, serviceId).onComplete(resultSet -> {
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
