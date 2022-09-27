package ir.khalili.products.odds.core.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.sql.ResultSet;
import io.vertx.ext.sql.SQLConnection;
import ir.khalili.products.odds.core.excp.dao.DAOEXCP_Internal;
import ir.khalili.products.odds.core.excp.dao.DAOEXCP_NoDataFound;

public class DAO_Customer {

    private static final Logger logger = LogManager.getLogger(DAO_Customer.class);
    
    public static Future<Boolean> checkCustomerValidTo(SQLConnection sqlConnection, Long customerId, Long serviceId) {
        Promise<Boolean> promise = Promise.promise();
        return promise.future();
    }
    
    public static Future<JsonObject> fetchCustomerById(SQLConnection sqlConnection, Long customerId) {
        Promise<JsonObject> promise = Promise.promise();
        JsonArray params = new JsonArray();
        params.add(customerId);
        sqlConnection.queryWithParams("SELECT * FROM tnascustomer WHERE id = ? and dto is null", params, handler -> {
            if (handler.failed()) {
                promise.fail(new DAOEXCP_Internal(-100, "خطای داخلی. با راهبر سامانه تماس بگیرید."));
            } else {

                if (null == handler.result() || null == handler.result().getRows() || handler.result().getRows().isEmpty()) {
                    promise.fail(new DAOEXCP_Internal(-100, "مشتری مورد نظر موجود نمی باشد."));
                } else {
                    logger.trace("updateCustomerLocationInfoSuccessful");
                    promise.complete(handler.result().getRows().get(0));
                }
            
            }
        });

        return promise.future();
    }
    
}
