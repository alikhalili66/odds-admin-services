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
				+ "TO_DATE(?, 'Dy Mon DD YYYY HH24:MI:SS'),"
				+ "TO_DATE(?, 'Dy Mon DD YYYY HH24:MI:SS'),"
				+ "TO_DATE(?, 'Dy Mon DD YYYY HH24:MI:SS'),"
				+ "TO_DATE(?, 'Dy Mon DD YYYY HH24:MI:SS'),"
				+ "sysdate,"
				+ "?)", params, resultHandler->{
			if(resultHandler.failed()) {
				logger.error("Unable to get accessQueryResult:", resultHandler.cause());
				if(resultHandler.cause().getMessage().toUpperCase().contains("UN_LEAGUE_NAME")) {
					logger.error("UN_LEAGUE_NAME_UNIQUE_CONSTRAINT");
					promise.fail(new DAOEXCP_Internal(-100, "نام تکراری است."));
				}else if(resultHandler.cause().getMessage().toUpperCase().contains("UN_LEAGUE_SYMBOL")) {
					logger.error("UN_LEAGUE_SYMBOL_UNIQUE_CONSTRAINT");
					promise.fail(new DAOEXCP_Internal(-100, "نماد تکراری است."));
				}else {
					logger.error("SAVE_LEAGUE_FAILED.");
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
				+ "l.ACTIVEFROM=TO_DATE(?, 'Dy Mon DD YYYY HH24:MI:SS'),"
				+ "l.ACTIVETO=TO_DATE(?, 'Dy Mon DD YYYY HH24:MI:SS'),"
				+ "l.oddsfrom=TO_DATE(?, 'Dy Mon DD YYYY HH24:MI:SS'),"
				+ "l.oddsTo=TO_DATE(?, 'Dy Mon DD YYYY HH24:MI:SS') "
				+ " where l.id=? ", params, resultHandler->{
			if(resultHandler.failed()) {
				logger.error("Unable to get accessQueryResult:", resultHandler.cause());
				if(resultHandler.cause().getMessage().toUpperCase().contains("UN_LEAGUE_NAME")) {
					logger.error("UN_LEAGUE_NAME_UNIQUE_CONSTRAINT");
					promise.fail(new DAOEXCP_Internal(-100, "نام تکراری است."));
				}else if(resultHandler.cause().getMessage().toUpperCase().contains("UN_LEAGUE_SYMBOL")) {
					logger.error("UN_LEAGUE_SYMBOL_UNIQUE_CONSTRAINT");
					promise.fail(new DAOEXCP_Internal(-100, "نماد تکراری است."));
				}else {
					logger.error("UPDATE_LEAGUE_FAILED.");
					promise.fail(new DAOEXCP_Internal(-100, "خطای داخلی. با راهبر سامانه تماس بگیرید."));
				}
				return;
			}
			
			logger.trace("UpdateLeagueSuccessful");
			promise.complete();
			
		});
		
		return promise.future();
	}
    
    public static Future<Void> delete(SQLConnection sqlConnection, Integer leagueId) {
        Promise<Void> promise = Promise.promise();
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
        		+ "To_Char(l.ACTIVEFROM, 'Dy Mon DD YYYY HH24:MI:SS')|| ' GMT+0330' ACTIVE_FROM,"
        		+ "To_Char(l.ACTIVETO, 'Dy Mon DD YYYY HH24:MI:SS')|| ' GMT+0330' ACTIVE_TO,"
        		+ "To_Char(l.oddsfrom, 'Dy Mon DD YYYY HH24:MI:SS')|| ' GMT+0330' ODDS_FROM,"
        		+ "To_Char(l.oddsTo, 'Dy Mon DD YYYY HH24:MI:SS')|| ' GMT+0330' ODDS_TO,"
        		+ "To_Char(l.creationdate, 'Dy Mon DD YYYY HH24:MI:SS')|| ' GMT+0330' creation_date"
        		+ "  FROM toppleague l WHERE l.dto is null", handler -> {
            if (handler.failed()) {
            	logger.error("Unable to get accessQueryResult:", handler.cause());
                promise.fail(new DAOEXCP_Internal(-100, "خطای داخلی. با راهبر سامانه تماس بگیرید."));
            } else {
                if (null == handler.result() || null == handler.result().getRows() || handler.result().getRows().isEmpty()) {
                	logger.error("fetchAllLeagueNoDataFound");
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
        		+ "To_Char(l.ACTIVEFROM, 'Dy Mon DD YYYY HH24:MI:SS')|| ' GMT+0330' ACTIVE_FROM,"
        		+ "To_Char(l.ACTIVETO, 'Dy Mon DD YYYY HH24:MI:SS')|| ' GMT+0330' ACTIVE_TO,"
        		+ "To_Char(l.oddsfrom, 'Dy Mon DD YYYY HH24:MI:SS')|| ' GMT+0330' ODDS_FROM,"
        		+ "To_Char(l.oddsTo, 'Dy Mon DD YYYY HH24:MI:SS')|| ' GMT+0330' ODDS_TO,"
        		+ "To_Char(l.creationdate, 'Dy Mon DD YYYY HH24:MI:SS')|| ' GMT+0330' creation_date"
        		+ "  FROM toppleague l WHERE l.id=? and l.dto is null", params, handler -> {
            if (handler.failed()) {
            	logger.error("Unable to get accessQueryResult:", handler.cause());
                promise.fail(new DAOEXCP_Internal(-100, "خطای داخلی. با راهبر سامانه تماس بگیرید."));
            } else {
                if (null == handler.result() || null == handler.result().getRows() || handler.result().getRows().isEmpty()) {
                	logger.error("fetchLeagueByIdNoDataFound");
                    promise.fail(new DAOEXCP_Internal(-100, "داده ای یافت نشد"));
                } else {
                    logger.trace("fetchAllLeagueByIdSuccessful::RESULT: " + handler.result().getRows().get(0));
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
        		+ "To_Char(l.ACTIVEFROM, 'Dy Mon DD YYYY HH24:MI:SS')|| ' GMT+0330' ACTIVE_FROM,"
        		+ "To_Char(l.ACTIVETO, 'Dy Mon DD YYYY HH24:MI:SS')|| ' GMT+0330' ACTIVE_TO,"
        		+ "To_Char(l.oddsfrom, 'Dy Mon DD YYYY HH24:MI:SS')|| ' GMT+0330' ODDS_FROM,"
        		+ "To_Char(l.oddsTo, 'Dy Mon DD YYYY HH24:MI:SS')|| ' GMT+0330' ODDS_TO,"
        		+ "To_Char(l.creationdate, 'Dy Mon DD YYYY HH24:MI:SS')|| ' GMT+0330' creation_date"
        		+ "  FROM toppleague l WHERE l.id=? and sysdate > ACTIVEFROM and sysdate < ACTIVETO and l.dto is null", params, handler -> {
            if (handler.failed()) {
            	logger.error("Unable to get accessQueryResult:", handler.cause());
                promise.fail(new DAOEXCP_Internal(-100, "خطای داخلی. با راهبر سامانه تماس بگیرید."));
            } else {
                if (null == handler.result() || null == handler.result().getRows() || handler.result().getRows().isEmpty()) {
                    promise.fail(new DAOEXCP_Internal(-201, "تورنومنت فعال موجود نمی باشد."));
                } else {
                    logger.trace("fetchAllLeagueByIdSuccessful");
                    promise.complete(handler.result().getRows().get(0));
                }
            
            }
        });

        return promise.future();
    }
    
    public static Future<Void> saveHistory(SQLConnection sqlConnection, JsonObject joLeague, String historyType, String historyDescription, Integer historyById) {

		Promise<Void> promise = Promise.promise();
		JsonArray params = new JsonArray();
		params.add(joLeague.getInteger("ID"));
		params.add(joLeague.getString("NAME"));
		params.add(joLeague.getString("SYMBOL"));
		params.add(joLeague.getString("ACTIVE_FROM").split("GMT")[0]);
		params.add(joLeague.getString("ACTIVE_TO").split("GMT")[0]);
		params.add(joLeague.getString("ODDS_FROM").split("GMT")[0]);
		params.add(joLeague.getString("ODDS_TO").split("GMT")[0]);
		params.add(historyType);
		params.add(historyDescription);
		params.add(historyById);
		
		sqlConnection.updateWithParams(""
				+ "insert into tOPPLeagueHistory("
				+ "ID,"
				+ "LEAGUE_ID,"
				+ "NAME,"
				+ "SYMBOL,"
				+ "ACTIVEFROM,"
				+ "ACTIVETO,"
				+ "ODDSFROM,"
				+ "ODDSTO,"
				+ "HISTORYTYPE,"
				+ "HISTORYDESCRIPTION,"
				+ "HISTORYBY_ID,"
				+ "HISTORYDATE)"
				+ "values("
				+ "soppleaguehistory.nextval,"
				+ "?,"
				+ "?,"
				+ "?,"
				+ "TO_DATE(?, 'Dy Mon DD YYYY HH24:MI:SS'),"
				+ "TO_DATE(?, 'Dy Mon DD YYYY HH24:MI:SS'),"
				+ "TO_DATE(?, 'Dy Mon DD YYYY HH24:MI:SS'),"
				+ "TO_DATE(?, 'Dy Mon DD YYYY HH24:MI:SS'),"
				+ "?,"
				+ "?,"
				+ "?,"
				+ "sysdate)", params, resultHandler->{
			if(resultHandler.failed()) {
				logger.error("Unable to get accessQueryResult:", resultHandler.cause());
				promise.fail(new DAOEXCP_Internal(-100, "خطای داخلی. با راهبر سامانه تماس بگیرید."));
				return;
			}
			
			logger.trace("SaveLeagueHistorySuccessful");
			promise.complete();
			
		});
		
		return promise.future();
	}

    
}
