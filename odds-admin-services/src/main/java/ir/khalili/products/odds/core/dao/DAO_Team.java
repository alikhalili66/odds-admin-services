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

public class DAO_Team {

    private static final Logger logger = LogManager.getLogger(DAO_Team.class);
    
    public static Future<Void> save(SQLConnection sqlConnection, JsonObject message) {

		Promise<Void> promise = Promise.promise();
		
		JsonArray params = new JsonArray();
		params.add(message.getString("leagueId"));
		params.add(message.getString("name"));
		params.add(message.getString("symbol"));
		params.add(message.getString("image"));
		params.add(message.getInteger("userId"));
		
		sqlConnection.updateWithParams(""
				+ "insert into toppteam("
				+ "id,"
				+ "league_id,"
				+ "name,"
				+ "symbol,"
				+ "image,"
				+ "creationDate,"
				+ "createdBy_id)"
				+ "values(soppteam.nextval,?,?,?,?,sysdate,?)", params, resultHandler->{
			if(resultHandler.failed()) {
				logger.error("Unable to get accessQueryResult:", resultHandler.cause());
				promise.fail(new DAOEXCP_Internal(-100, "خطای داخلی. با راهبر سامانه تماس بگیرید."));
				return;
			}
			
			logger.trace("SaveTeamSuccessful");
			promise.complete();
			
		});
		
		return promise.future();
	}
  
    public static Future<Void> update(SQLConnection sqlConnection, JsonObject message) {

		Promise<Void> promise = Promise.promise();
		
		JsonArray params = new JsonArray();
		params.add(message.getString("leagueId"));
		params.add(message.getString("name"));
		params.add(message.getString("symbol"));
		params.add(message.getInteger("teamId"));
		
		sqlConnection.updateWithParams(""
				+ "update toppteam t set "
				+ "t.league_id=?,"
				+ "t.name=?,"
				+ "t.symbol=?"
				+ " where t.id=?", params, resultHandler->{
			if(resultHandler.failed()) {
				logger.error("Unable to get accessQueryResult:", resultHandler.cause());
				promise.fail(new DAOEXCP_Internal(-100, "خطای داخلی. با راهبر سامانه تماس بگیرید."));
				return;
			}
			
			logger.trace("UpdateTeamSuccessful");
			promise.complete();
			
		});
		
		return promise.future();
	}
    
    public static Future<Void> updateImage(SQLConnection sqlConnection, JsonObject message) {

		Promise<Void> promise = Promise.promise();
		
		JsonArray params = new JsonArray();
		params.add(message.getString("image"));
		params.add(message.getInteger("teamId"));
		
		sqlConnection.updateWithParams(""
				+ "update toppteam t set "
				+ "t.IMAGE=? "
				+ " where t.id=?", params, resultHandler->{
			if(resultHandler.failed()) {
				logger.error("Unable to get accessQueryResult:", resultHandler.cause());
				promise.fail(new DAOEXCP_Internal(-100, "خطای داخلی. با راهبر سامانه تماس بگیرید."));
				return;
			}
			
			logger.trace("UpdateImageTeamSuccessful");
			promise.complete();
			
		});
		
		return promise.future();
	}
    
    public static Future<JsonObject> delete(SQLConnection sqlConnection, Integer teamId) {
        Promise<JsonObject> promise = Promise.promise();
        JsonArray params = new JsonArray();
        params.add(teamId);
        
        sqlConnection.updateWithParams("update toppteam set dto=sysdate WHERE id=?", params, handler -> {
			if(handler.failed()) {
				logger.error("Unable to get accessQueryResult:", handler.cause());
				promise.fail(new DAOEXCP_Internal(-100, "خطای داخلی. با راهبر سامانه تماس بگیرید."));
				return;
			}
			
			logger.trace("deleteTeamByIdSuccessful");
			promise.complete();
			
		});
		return promise.future();
    }
    
    public static Future<List<JsonObject>> fetchAll(SQLConnection sqlConnection) {
        Promise<List<JsonObject>> promise = Promise.promise();
        
        sqlConnection.query("SELECT "
        		+ "t.id,"
        		+ "t.NAME,"
        		+ "t.SYMBOL,"
        		+ "t.IMAGE,"
        		+ "t.LEAGUE_ID,"
        		+ "To_Char(t.creationdate,'YYYY-MM-DD\"T\"HH24:MI:SS\"Z\"') creation_date"
        		+ "  FROM toppteam t WHERE t.dto is null", handler -> {
            if (handler.failed()) {
                promise.fail(new DAOEXCP_Internal(-100, "خطای داخلی. با راهبر سامانه تماس بگیرید."));
            } else {
                if (null == handler.result() || null == handler.result().getRows() || handler.result().getRows().isEmpty()) {
                	promise.complete(new ArrayList<>());
                } else {
                    logger.trace("fetchAllCompetitionSuccessful");
                    promise.complete(handler.result().getRows());
                }
            
            }
        });

        return promise.future();
    }
        
    public static Future<JsonObject> fetchById(SQLConnection sqlConnection, Integer teamId) {
        Promise<JsonObject> promise = Promise.promise();
        JsonArray params = new JsonArray();
        params.add(teamId);
        
        sqlConnection.queryWithParams("SELECT "
        		+ "t.id,"
        		+ "t.NAME,"
        		+ "t.SYMBOL,"
        		+ "t.IMAGE,"
        		+ "t.LEAGUE_ID,"
        		+ "To_Char(t.creationdate,'YYYY-MM-DD\"T\"HH24:MI:SS\"Z\"') creation_date"
        		+ "  FROM toppteam t WHERE t.id=? and t.dto is null", params, handler -> {
            if (handler.failed()) {
                promise.fail(new DAOEXCP_Internal(-100, "خطای داخلی. با راهبر سامانه تماس بگیرید."));
            } else {
                if (null == handler.result() || null == handler.result().getRows() || handler.result().getRows().isEmpty()) {
                    promise.fail(new DAOEXCP_Internal(-100, "داده ای یافت نشد"));
                } else {
                    logger.trace("fetchAllTeamByIdSuccessful");
                    promise.complete(handler.result().getRows().get(0));
                }
            
            }
        });

        return promise.future();
    }
      
}
