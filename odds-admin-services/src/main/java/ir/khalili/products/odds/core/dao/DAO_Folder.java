package ir.khalili.products.odds.core.dao;

import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.sql.SQLConnection;
import ir.khalili.products.odds.core.excp.dao.DAOEXCP_Internal;

public class DAO_Folder {

    private static final Logger logger = LogManager.getLogger(DAO_Folder.class);
    
    public static Future<Boolean> checkCustomerValidTo(SQLConnection sqlConnection, Long customerId, Long serviceId) {
        Promise<Boolean> promise = Promise.promise();
        return promise.future();
    }
    
    public static Future<Void> save(SQLConnection sqlConnection, JsonObject message) {

		Promise<Void> promise = Promise.promise();
		
		JsonArray params = new JsonArray();
		params.add(message.getInteger("parentId"));
		params.add(message.getInteger("leagueId"));
		params.add(message.getInteger("name"));
		params.add(message.getInteger("userId"));
		
		sqlConnection.updateWithParams(""
				+ "insert into toppfolder("
				+ "id,"
				+ "parent_id,"
				+ "league_id,"
				+ "name,"
				+ "creationDate,"
				+ "createdBy_id)"
				+ "values(soppfolder.nextval,?,?,?,sysdate,?)", params, resultHandler->{
			if(resultHandler.failed()) {
				logger.error("Unable to get accessQueryResult:", resultHandler.cause());
				promise.fail(new DAOEXCP_Internal(-100, "خطای داخلی. با راهبر سامانه تماس بگیرید."));
				return;
			}
			
			logger.trace("SaveFolderSuccessful");
			promise.complete();
			
		});
		
		return promise.future();
	}

    public static Future<Void> update(SQLConnection sqlConnection, JsonObject message) {

		Promise<Void> promise = Promise.promise();
		
		JsonArray params = new JsonArray();
		params.add(message.getInteger("parentId"));
		params.add(message.getInteger("leagueId"));
		params.add(message.getInteger("name"));
		params.add(message.getInteger("folderId"));
		
		sqlConnection.updateWithParams(""
				+ "update toppfolder f set "
				+ "f.parent_id=?,"
				+ "f.league_id=?,"
				+ "f.name=? "
				+ " where f.id=?", params, resultHandler->{
			if(resultHandler.failed()) {
				logger.error("Unable to get accessQueryResult:", resultHandler.cause());
				promise.fail(new DAOEXCP_Internal(-100, "خطای داخلی. با راهبر سامانه تماس بگیرید."));
				return;
			}
			
			logger.trace("UpdateFolderSuccessful");
			promise.complete();
			
		});
		
		return promise.future();
	
    }
    
    public static Future<JsonObject> delete(SQLConnection sqlConnection, Integer folderId) {
        Promise<JsonObject> promise = Promise.promise();
        JsonArray params = new JsonArray();
        params.add(folderId);
        
        sqlConnection.updateWithParams("update toppfolder set dto=sysdate WHERE id=?", params, handler -> {
			if(handler.failed()) {
				logger.error("Unable to get accessQueryResult:", handler.cause());
				promise.fail(new DAOEXCP_Internal(-100, "خطای داخلی. با راهبر سامانه تماس بگیرید."));
				return;
			}
			
			logger.trace("deleteFolderByIdSuccessful");
			promise.complete();
			
		});
		return promise.future();
    }
    
    public static Future<List<JsonObject>> fetchAll(SQLConnection sqlConnection) {
        Promise<List<JsonObject>> promise = Promise.promise();
        
        sqlConnection.query("SELECT "
        		+ "f.id,"
        		+ "f.PARENT_ID,"
        		+ "f.LEAGUE_ID,"
        		+ "f.NAME,"
        		+ "To_Char(f.creationdate,'yyyy/mm/dd HH:MM:SS','nls_calendar=persian') creation_date"
        		+ "  FROM toppfolder f WHERE f.dto is null CONNECT BY PRIOR f.id = f.parent_id", handler -> {
            if (handler.failed()) {
                promise.fail(new DAOEXCP_Internal(-100, "خطای داخلی. با راهبر سامانه تماس بگیرید."));
            } else {
                if (null == handler.result() || null == handler.result().getRows() || handler.result().getRows().isEmpty()) {
                    promise.fail(new DAOEXCP_Internal(-100, "داده ای یافت نشد"));
                } else {
                    logger.trace("fetchAllFolderSuccessful");
                    promise.complete(handler.result().getRows());
                }
            
            }
        });

        return promise.future();
    }
        
    public static Future<JsonObject> fetchById(SQLConnection sqlConnection, Integer folderId) {
        Promise<JsonObject> promise = Promise.promise();
        JsonArray params = new JsonArray();
        params.add(folderId);
        
        sqlConnection.queryWithParams("SELECT "
        		+ "f.id,"
        		+ "f.PARENT_ID,"
        		+ "f.LEAGUE_ID,"
        		+ "f.NAME,"
        		+ "To_Char(f.creationdate,'yyyy/mm/dd HH:MM:SS','nls_calendar=persian') creation_date"
        		+ "  FROM toppfolder f WHERE f.id=? and f.dto is null CONNECT BY PRIOR f.id = f.parent_id", params, handler -> {
            if (handler.failed()) {
                promise.fail(new DAOEXCP_Internal(-100, "خطای داخلی. با راهبر سامانه تماس بگیرید."));
            } else {
                if (null == handler.result() || null == handler.result().getRows() || handler.result().getRows().isEmpty()) {
                    promise.fail(new DAOEXCP_Internal(-100, "داده ای یافت نشد"));
                } else {
                    logger.trace("fetchAllFolderByIdSuccessful");
                    promise.complete(handler.result().getRows().get(0));
                }
            
            }
        });
        return promise.future();
    }
    
    public static Future<JsonObject> assignQuestion(SQLConnection sqlConnection, Long customerId) {
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
    
    public static Future<JsonObject> unAssignQuestion(SQLConnection sqlConnection, Long customerId) {
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
    
    public static Future<JsonObject> fetchQuestion(SQLConnection sqlConnection, Long customerId) {
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