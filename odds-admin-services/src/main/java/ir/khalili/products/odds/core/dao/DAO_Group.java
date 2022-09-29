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
import ir.khalili.products.odds.core.utils.CalenderUtil;

public class DAO_Group {

    private static final Logger logger = LogManager.getLogger(DAO_Group.class);
    
    public static Future<Boolean> checkCustomerValidTo(SQLConnection sqlConnection, Long customerId, Long serviceId) {
        Promise<Boolean> promise = Promise.promise();
        return promise.future();
    }
    
    public static Future<Void> save(SQLConnection sqlConnection, JsonObject message) {

		Promise<Void> promise = Promise.promise();
		
		JsonArray params = new JsonArray();
		
		try {
			params.add(message.getInteger("leagueId"));
			params.add(message.getString("name"));
			params.add(CalenderUtil.toDate(message.getString("activeFrom")));
			params.add(CalenderUtil.toDate(message.getString("activeTo")));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		sqlConnection.updateWithParams(""
				+ "insert into toppgroup("
				+ "ID,"
				+ "LEAGUE_ID,"
				+ "NAME,"
				+ "ACTIVEFROM,"
				+ "ACTIVETO,"
				+ "creationDate,"
				+ "createdBy_id)"
				+ "values(soppgroup.nextval,?,?,?,?,sysdate,?)", params, resultHandler->{
			if(resultHandler.failed()) {
				logger.error("Unable to get accessQueryResult:", resultHandler.cause());
				promise.fail(new DAOEXCP_Internal(-100, "خطای داخلی. با راهبر سامانه تماس بگیرید."));
				return;
			}
			
			logger.trace("SaveGroupSuccessful");
			promise.complete();
			
		});
		
		return promise.future();
	}
    public static Future<Void> update(SQLConnection sqlConnection, JsonObject message) {

		Promise<Void> promise = Promise.promise();
		
		JsonArray params = new JsonArray();
		
		try {
			params.add(message.getInteger("leagueId"));
			params.add(message.getString("name"));
			params.add(CalenderUtil.toDate(message.getString("activeFrom")));
			params.add(CalenderUtil.toDate(message.getString("activeTo")));
			params.add(message.getInteger("groupId"));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		sqlConnection.updateWithParams(""
				+ "update toppgroup g set "
				+ "LEAGUE_ID=?,"
				+ "NAME=?,"
				+ "ACTIVEFROM=?,"
				+ "ACTIVETO=? "
				+ " where g.id=?", params, resultHandler->{
			if(resultHandler.failed()) {
				logger.error("Unable to get accessQueryResult:", resultHandler.cause());
				promise.fail(new DAOEXCP_Internal(-100, "خطای داخلی. با راهبر سامانه تماس بگیرید."));
				return;
			}
			
			logger.trace("UpdateGroupSuccessful");
			promise.complete();
			
		});
		
		return promise.future();
	}
    
    public static Future<JsonObject> delete(SQLConnection sqlConnection, Integer groupId) {
        Promise<JsonObject> promise = Promise.promise();
        JsonArray params = new JsonArray();
        params.add(groupId);
        
        sqlConnection.updateWithParams("update toppgroup set dto=sysdate WHERE id=?", params, handler -> {
			if(handler.failed()) {
				logger.error("Unable to get accessQueryResult:", handler.cause());
				promise.fail(new DAOEXCP_Internal(-100, "خطای داخلی. با راهبر سامانه تماس بگیرید."));
				return;
			}
			
			logger.trace("deleteGroupByIdSuccessful");
			promise.complete();
			
		});
		return promise.future();
    }
    
    public static Future<List<JsonObject>> fetchAll(SQLConnection sqlConnection) {
        Promise<List<JsonObject>> promise = Promise.promise();
        
        sqlConnection.query("SELECT "
        		+ "g.id,"
        		+ "g.LEAGUE_ID,"
        		+ "g.NAME,"
        		+ "To_Char(g.ACTIVEFROM,'yyyy/mm/dd HH:MM:SS','nls_calendar=persian') ACTIVE_FROM,"
        		+ "To_Char(g.ACTIVETO,'yyyy/mm/dd HH:MM:SS','nls_calendar=persian') ACTIVE_TO,"
        		+ "To_Char(g.creationdate,'yyyy/mm/dd HH:MM:SS','nls_calendar=persian') creation_date"
        		+ "  FROM toppgroup g WHERE g.dto is null", handler -> {
            if (handler.failed()) {
                promise.fail(new DAOEXCP_Internal(-100, "خطای داخلی. با راهبر سامانه تماس بگیرید."));
            } else {
                if (null == handler.result() || null == handler.result().getRows() || handler.result().getRows().isEmpty()) {
                    promise.fail(new DAOEXCP_Internal(-100, "داده ای یافت نشد"));
                } else {
                    logger.trace("fetchAllGroupSuccessful");
                    promise.complete(handler.result().getRows());
                }
            
            }
        });

        return promise.future();
    }
        
    public static Future<JsonObject> fetchById(SQLConnection sqlConnection, Integer groupId) {
        Promise<JsonObject> promise = Promise.promise();
        JsonArray params = new JsonArray();
        params.add(groupId);
        
        sqlConnection.queryWithParams("SELECT "
        		+ "g.id,"
        		+ "g.LEAGUE_ID,"
        		+ "g.NAME,"
        		+ "To_Char(g.ACTIVEFROM,'yyyy/mm/dd HH:MM:SS','nls_calendar=persian') ACTIVE_FROM,"
        		+ "To_Char(g.ACTIVETO,'yyyy/mm/dd HH:MM:SS','nls_calendar=persian') ACTIVE_TO,"
        		+ "To_Char(g.creationdate,'yyyy/mm/dd HH:MM:SS','nls_calendar=persian') creation_date"
        		+ "  FROM toppgroup g WHERE g.id=? and g.dto is null", params, handler -> {
            if (handler.failed()) {
                promise.fail(new DAOEXCP_Internal(-100, "خطای داخلی. با راهبر سامانه تماس بگیرید."));
            } else {
                if (null == handler.result() || null == handler.result().getRows() || handler.result().getRows().isEmpty()) {
                    promise.fail(new DAOEXCP_Internal(-100, "داده ای یافت نشد"));
                } else {
                    logger.trace("fetchAllGroupByIdSuccessful");
                    promise.complete(handler.result().getRows().get(0));
                }
            
            }
        });

        return promise.future();
    }
    
    public static Future<JsonObject> assignTeam(SQLConnection sqlConnection, Long customerId) {
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
    
    public static Future<JsonObject> unAssignTeam(SQLConnection sqlConnection, Long customerId) {
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
    
    public static Future<JsonObject> fetchTeam(SQLConnection sqlConnection, Long customerId) {
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
