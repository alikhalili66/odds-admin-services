package ir.khalili.products.odds.core.dao;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
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

public class DAO_League {

    private static final Logger logger = LogManager.getLogger(DAO_League.class);
    private static final DateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
    
    public static Future<Boolean> checkCustomerValidTo(SQLConnection sqlConnection, Long customerId, Long serviceId) {
        Promise<Boolean> promise = Promise.promise();
        return promise.future();
    }
    
    public static Future<Void> save(SQLConnection sqlConnection, JsonObject message) {

		Promise<Void> promise = Promise.promise();
		
		JsonArray params = new JsonArray();
		
		try {
			params.add(message.getString("name"));
			params.add(message.getString("symbol"));
			params.add(message.getString("image"));
			params.add(CalenderUtil.toDate(message.getString("activeFrom")));
			params.add(CalenderUtil.toDate(message.getString("activeTo")));
			params.add(CalenderUtil.toDate(message.getString("oddsFrom")));
			params.add(CalenderUtil.toDate(message.getString("oddsTo")));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		sqlConnection.updateWithParams(""
				+ "insert into toppleague("
				+ "ID,"
				+ "NAME,"
				+ "SYMBOL,"
				+ "IMAGE,"
				+ "ACTIVEFROM,"
				+ "ACTIVETO,"
				+ "oddsfrom,"
				+ "oddsTo,"
				+ "creationDate,"
				+ "createdBy_id)"
				+ "values(soppleague.nextval,?,?,?,?,?,sysdate,?)", params, resultHandler->{
			if(resultHandler.failed()) {
				logger.error("Unable to get accessQueryResult:", resultHandler.cause());
				promise.fail(new DAOEXCP_Internal(-100, "خطای داخلی. با راهبر سامانه تماس بگیرید."));
				return;
			}
			
			logger.trace("SaveLeagueSuccessful");
			promise.complete();
			
		});
		
		return promise.future();
	}
    public static Future<Void> update(SQLConnection sqlConnection, JsonObject message) {

		Promise<Void> promise = Promise.promise();
		
		JsonArray params = new JsonArray();
		
		try {
			params.add(message.getString("name"));
			params.add(message.getString("symbol"));
			params.add(message.getString("image"));
			params.add(CalenderUtil.toDate(message.getString("activeFrom")));
			params.add(CalenderUtil.toDate(message.getString("activeTo")));
			params.add(CalenderUtil.toDate(message.getString("oddsFrom")));
			params.add(CalenderUtil.toDate(message.getString("oddsTo")));
			params.add(message.getInteger("leagueId"));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		sqlConnection.updateWithParams(""
				+ "update toppleague l set "
				+ "l.NAME=?,"
				+ "l.SYMBOL=?,"
				+ "l.IMAGE=?,"
				+ "l.ACTIVEFROM=?,"
				+ "l.ACTIVETO=?,"
				+ "l.oddsfrom=?,"
				+ "l.oddsTo=? "
				+ " where l.id=? ", params, resultHandler->{
			if(resultHandler.failed()) {
				logger.error("Unable to get accessQueryResult:", resultHandler.cause());
				promise.fail(new DAOEXCP_Internal(-100, "خطای داخلی. با راهبر سامانه تماس بگیرید."));
				return;
			}
			
			logger.trace("UpdateLeagueSuccessful");
			promise.complete();
			
		});
		
		return promise.future();
	}
    
    public static Future<JsonObject> delete(SQLConnection sqlConnection, Integer leagueId) {
        Promise<JsonObject> promise = Promise.promise();
        JsonArray params = new JsonArray();
        params.add(leagueId);
        
        sqlConnection.updateWithParams("update toppleague set dto=sysdate WHERE id=?", params, handler -> {
			if(handler.failed()) {
				logger.error("Unable to get accessQueryResult:", handler.cause());
				promise.fail(new DAOEXCP_Internal(-100, "خطای داخلی. با راهبر سامانه تماس بگیرید."));
				return;
			}
			
			logger.trace("deleteLeagueByIdSuccessful");
			promise.complete();
			
		});
		return promise.future();
    }
    
    public static Future<List<JsonObject>> fetchAll(SQLConnection sqlConnection) {
        Promise<List<JsonObject>> promise = Promise.promise();
        
        sqlConnection.query("SELECT "
        		+ "l.id,"
        		+ "l.NAME,"
        		+ "l.SYMBOL,"
        		+ "l.IMAGE,"
        		+ "To_Char(l.ACTIVEFROM,'yyyy/mm/dd HH:MM:SS','nls_calendar=persian') ACTIVE_FROM,"
        		+ "To_Char(l.ACTIVETO,'yyyy/mm/dd HH:MM:SS','nls_calendar=persian') ACTIVE_TO,"
        		+ "To_Char(l.creationdate,'yyyy/mm/dd HH:MM:SS','nls_calendar=persian') creation_date"
        		+ "  FROM toppleague l WHERE l.dto is null", handler -> {
            if (handler.failed()) {
                promise.fail(new DAOEXCP_Internal(-100, "خطای داخلی. با راهبر سامانه تماس بگیرید."));
            } else {
                if (null == handler.result() || null == handler.result().getRows() || handler.result().getRows().isEmpty()) {
                    promise.fail(new DAOEXCP_Internal(-100, "داده ای یافت نشد"));
                } else {
                    logger.trace("fetchAllLeagueSuccessful");
                    promise.complete(handler.result().getRows());
                }
            
            }
        });

        return promise.future();
    }
        
    public static Future<JsonObject> fetchById(SQLConnection sqlConnection, Integer leagueId) {
        Promise<JsonObject> promise = Promise.promise();
        JsonArray params = new JsonArray();
        params.add(leagueId);
        
        sqlConnection.queryWithParams("SELECT "
        		+ "l.id,"
        		+ "l.NAME,"
        		+ "l.SYMBOL,"
        		+ "l.IMAGE,"
        		+ "To_Char(l.ACTIVEFROM,'yyyy/mm/dd HH:MM:SS','nls_calendar=persian') ACTIVE_FROM,"
        		+ "To_Char(l.ACTIVETO,'yyyy/mm/dd HH:MM:SS','nls_calendar=persian') ACTIVE_TO,"
        		+ "To_Char(l.creationdate,'yyyy/mm/dd HH:MM:SS','nls_calendar=persian') creation_date"
        		+ "  FROM toppleague l WHERE l.id=? and l.dto is null", params, handler -> {
            if (handler.failed()) {
                promise.fail(new DAOEXCP_Internal(-100, "خطای داخلی. با راهبر سامانه تماس بگیرید."));
            } else {
                if (null == handler.result() || null == handler.result().getRows() || handler.result().getRows().isEmpty()) {
                    promise.fail(new DAOEXCP_Internal(-100, "داده ای یافت نشد"));
                } else {
                    logger.trace("fetchAllLeagueByIdSuccessful");
                    promise.complete(handler.result().getRows().get(0));
                }
            
            }
        });

        return promise.future();
    }
      
}