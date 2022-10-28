package ir.khalili.products.odds.core.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.sql.SQLConnection;
import ir.khalili.products.odds.core.excp.dao.DAOEXCP_Internal;

public class DAO_League {

    private static final Logger logger = LogManager.getLogger(DAO_League.class);
    
    public static Future<Integer> fetchSequence(SQLConnection sqlConnection) {

 		Promise<Integer> promise = Promise.promise();
 		
 		sqlConnection.query("select soppleague.nextval as id from dual", resultHandler->{
 			if(resultHandler.failed()) {
 				logger.error("Unable to get accessQueryResult:", resultHandler.cause());
 				promise.fail(new DAOEXCP_Internal(-100, "خطای داخلی. با راهبر سامانه تماس بگیرید."));
 				return;
 			}
 			
 			logger.trace("SaveLeagueSuccessful");
 			promise.complete(resultHandler.result().getRows().get(0).getInteger("ID"));
 			
 		});
 		
 		return promise.future();
 	}
    
    public static Future<Void> save(SQLConnection sqlConnection, int id, JsonObject message) {

		Promise<Void> promise = Promise.promise();
		
		JsonArray params = new JsonArray();
		params.add(id);
		params.add(message.getString("name"));
		params.add(message.getString("symbol"));
		params.add(message.getString("image"));
		params.add(message.getString("activeFrom"));
		params.add(message.getString("activeTo"));
		params.add(message.getString("oddsFrom"));
		params.add(message.getString("oddsTo"));
		params.add(message.getInteger("userId"));
		
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
				+ "values("
				+ "?,"
				+ "?,"
				+ "?,"
				+ "?,"
				+ "TO_DATE(?,'YYYY-MM-DD\"T\"HH24:MI:SS\"Z\"'),"
				+ "TO_DATE(?,'YYYY-MM-DD\"T\"HH24:MI:SS\"Z\"'),"
				+ "TO_DATE(?,'YYYY-MM-DD\"T\"HH24:MI:SS\"Z\"'),"
				+ "TO_DATE(?,'YYYY-MM-DD\"T\"HH24:MI:SS\"Z\"'),"
				+ "sysdate,"
				+ "?)", params, resultHandler->{
			if(resultHandler.failed()) {
				logger.error("Unable to get accessQueryResult:", resultHandler.cause());
				if(resultHandler.cause().getMessage().toUpperCase().contains("UN_LEAGUE_NAME")) {
					promise.fail(new DAOEXCP_Internal(-100, "نام تکراری است."));
				}else if(resultHandler.cause().getMessage().toUpperCase().contains("UN_LEAGUE_SYMBOL")) {
					promise.fail(new DAOEXCP_Internal(-100, "نماد تکراری است."));
				}else {
					promise.fail(new DAOEXCP_Internal(-100, "خطای داخلی. با راهبر سامانه تماس بگیرید."));
				}
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
		
		params.add(message.getString("name"));
		params.add(message.getString("symbol"));
		params.add(message.getString("image"));
		params.add(message.getString("activeFrom"));
		params.add(message.getString("activeTo"));
		params.add(message.getString("oddsFrom"));
		params.add(message.getString("oddsTo"));
		params.add(message.getInteger("leagueId"));
		
		sqlConnection.updateWithParams(""
				+ "update toppleague l set "
				+ "l.NAME=?,"
				+ "l.SYMBOL=?,"
				+ "l.IMAGE=?,"
				+ "l.ACTIVEFROM=TO_DATE(?,'YYYY-MM-DD\"T\"HH24:MI:SS\"Z\"'),"
				+ "l.ACTIVETO=TO_DATE(?,'YYYY-MM-DD\"T\"HH24:MI:SS\"Z\"'),"
				+ "l.oddsfrom=TO_DATE(?,'YYYY-MM-DD\"T\"HH24:MI:SS\"Z\"'),"
				+ "l.oddsTo=TO_DATE(?,'YYYY-MM-DD\"T\"HH24:MI:SS\"Z\"') "
				+ " where l.id=? ", params, resultHandler->{
			if(resultHandler.failed()) {
				logger.error("Unable to get accessQueryResult:", resultHandler.cause());
				if(resultHandler.cause().getMessage().toUpperCase().contains("UN_LEAGUE_NAME")) {
					promise.fail(new DAOEXCP_Internal(-100, "نام تکراری است."));
				}else if(resultHandler.cause().getMessage().toUpperCase().contains("UN_LEAGUE_SYMBOL")) {
					promise.fail(new DAOEXCP_Internal(-100, "نماد تکراری است."));
				}else {
					promise.fail(new DAOEXCP_Internal(-100, "خطای داخلی. با راهبر سامانه تماس بگیرید."));
				}
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
        		+ "To_Char(l.ACTIVEFROM,'YYYY-MM-DD\"T\"HH24:MI:SS\"Z\"') ACTIVE_FROM,"
        		+ "To_Char(l.ACTIVETO,'YYYY-MM-DD\"T\"HH24:MI:SS\"Z\"') ACTIVE_TO,"
        		+ "To_Char(l.oddsfrom,'YYYY-MM-DD\"T\"HH24:MI:SS\"Z\"') ODDS_FROM,"
        		+ "To_Char(l.oddsTo,'YYYY-MM-DD\"T\"HH24:MI:SS\"Z\"') ODDS_TO,"
        		+ "To_Char(l.creationdate,'YYYY-MM-DD\"T\"HH24:MI:SS\"Z\"') creation_date"
        		+ "  FROM toppleague l WHERE l.dto is null", handler -> {
            if (handler.failed()) {
            	logger.error("Unable to get accessQueryResult:", handler.cause());
                promise.fail(new DAOEXCP_Internal(-100, "خطای داخلی. با راهبر سامانه تماس بگیرید."));
            } else {
                if (null == handler.result() || null == handler.result().getRows() || handler.result().getRows().isEmpty()) {
                	promise.complete(new ArrayList<>());
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
        		+ "To_Char(l.ACTIVEFROM,'YYYY-MM-DD\"T\"HH24:MI:SS\"Z\"') ACTIVE_FROM,"
        		+ "To_Char(l.ACTIVETO,'YYYY-MM-DD\"T\"HH24:MI:SS\"Z\"') ACTIVE_TO,"
        		+ "To_Char(l.oddsfrom,'YYYY-MM-DD\"T\"HH24:MI:SS\"Z\"') ODDS_FROM,"
        		+ "To_Char(l.oddsTo,'YYYY-MM-DD\"T\"HH24:MI:SS\"Z\"') ODDS_TO,"
        		+ "To_Char(l.creationdate,'YYYY-MM-DD\"T\"HH24:MI:SS\"Z\"') creation_date"
        		+ "  FROM toppleague l WHERE l.id=? and l.dto is null", params, handler -> {
            if (handler.failed()) {
            	logger.error("Unable to get accessQueryResult:", handler.cause());
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
      
    public static Future<JsonObject> fetchValidLeagueById(SQLConnection sqlConnection, Integer leagueId) {
        Promise<JsonObject> promise = Promise.promise();
        JsonArray params = new JsonArray();
        params.add(leagueId);
        
        sqlConnection.queryWithParams("SELECT "
        		+ "l.id,"
        		+ "l.NAME,"
        		+ "l.SYMBOL,"
        		+ "l.IMAGE,"
        		+ "To_Char(l.ACTIVEFROM,'YYYY-MM-DD\"T\"HH24:MI:SS\"Z\"') ACTIVE_FROM,"
        		+ "To_Char(l.ACTIVETO,'YYYY-MM-DD\"T\"HH24:MI:SS\"Z\"') ACTIVE_TO,"
        		+ "To_Char(l.oddsfrom,'YYYY-MM-DD\"T\"HH24:MI:SS\"Z\"') ODDS_FROM,"
        		+ "To_Char(l.oddsTo,'YYYY-MM-DD\"T\"HH24:MI:SS\"Z\"') ODDS_TO,"
        		+ "To_Char(l.creationdate,'YYYY-MM-DD\"T\"HH24:MI:SS\"Z\"') creation_date"
        		+ "  FROM toppleague l WHERE l.id=? and sysdate > ACTIVEFROM and sysdate < ACTIVETO and l.dto is null", params, handler -> {
            if (handler.failed()) {
            	logger.error("Unable to get accessQueryResult:", handler.cause());
                promise.fail(new DAOEXCP_Internal(-100, "خطای داخلی. با راهبر سامانه تماس بگیرید."));
            } else {
                if (null == handler.result() || null == handler.result().getRows() || handler.result().getRows().isEmpty()) {
                    promise.fail(new DAOEXCP_Internal(-100, "لیگ معتبر نمی باشد."));
                } else {
                    logger.trace("fetchAllLeagueByIdSuccessful");
                    promise.complete(handler.result().getRows().get(0));
                }
            
            }
        });

        return promise.future();
    }
    
}
