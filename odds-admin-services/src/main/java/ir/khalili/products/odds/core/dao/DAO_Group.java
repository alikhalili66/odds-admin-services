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

public class DAO_Group {

    private static final Logger logger = LogManager.getLogger(DAO_Group.class);
    
    public static Future<Void> save(SQLConnection sqlConnection, JsonObject message) {

		Promise<Void> promise = Promise.promise();
		
		JsonArray params = new JsonArray();
		
		params.add(message.getInteger("leagueId"));
		params.add(message.getString("name"));
		params.add(message.getString("activeFrom"));
		params.add(message.getString("activeTo"));
		params.add(message.getInteger("userId"));
		
		sqlConnection.updateWithParams(""
				+ "insert into toppgroup("
				+ "ID,"
				+ "LEAGUE_ID,"
				+ "NAME,"
				+ "ACTIVEFROM,"
				+ "ACTIVETO,"
				+ "creationDate,"
				+ "createdBy_id)"
				+ "values(soppgroup.nextval,?,?,TO_DATE(?, 'Dy Mon DD YYYY HH24:MI:SS'),TO_DATE(?, 'Dy Mon DD YYYY HH24:MI:SS'),sysdate,?)", params, resultHandler->{
			if(resultHandler.failed()) {
				logger.error("Unable to get accessQueryResult:", resultHandler.cause());
				if(resultHandler.cause().getMessage().toUpperCase().contains("UN_GROUP_NAME")) {
					logger.error("UN_GROUP_NAME_UNIQUE_CONSTRAINT");
					promise.fail(new DAOEXCP_Internal(-100, "نام تکراری است."));
				}else if(resultHandler.cause().getMessage().toUpperCase().contains("UN_GROUP_SYMBOL")) {
					logger.error("UN_GROUP_SYMBOL_UNIQUE_CONSTRAINT");
					promise.fail(new DAOEXCP_Internal(-100, "نماد تکراری است."));
				}else {
					logger.error("SAVE_GROUP_FAILED.");
					promise.fail(new DAOEXCP_Internal(-100, "خطای داخلی. با راهبر سامانه تماس بگیرید."));
				}
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
		
		params.add(message.getInteger("leagueId"));
		params.add(message.getString("name"));
		params.add(message.getString("activeFrom"));
		params.add(message.getString("activeTo"));
		params.add(message.getInteger("groupId"));
		
		sqlConnection.updateWithParams(""
				+ "update toppgroup g set "
				+ "LEAGUE_ID=?,"
				+ "NAME=?,"
				+ "ACTIVEFROM=TO_DATE(?, 'Dy Mon DD YYYY HH24:MI:SS'),"
				+ "ACTIVETO=TO_DATE(?, 'Dy Mon DD YYYY HH24:MI:SS') "
				+ " where g.id=?", params, resultHandler->{
			if(resultHandler.failed()) {
				logger.error("Unable to get accessQueryResult:", resultHandler.cause());
				if(resultHandler.cause().getMessage().toUpperCase().contains("UN_GROUP_NAME")) {
					logger.error("UN_GROUP_NAME_UNIQUE_CONSTRAINT");
					promise.fail(new DAOEXCP_Internal(-100, "نام تکراری است."));
				}else if(resultHandler.cause().getMessage().toUpperCase().contains("UN_GROUP_SYMBOL")) {
					logger.error("UN_GROUP_SYMBOL_UNIQUE_CONSTRAINT");
					promise.fail(new DAOEXCP_Internal(-100, "نماد تکراری است."));
				}else {
					logger.error("UPDATE_GROUP_FAILED.");
					promise.fail(new DAOEXCP_Internal(-100, "خطای داخلی. با راهبر سامانه تماس بگیرید."));
				}
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
    
    public static Future<List<JsonObject>> fetchAll(SQLConnection sqlConnection, JsonObject message) {
        Promise<List<JsonObject>> promise = Promise.promise();
        JsonArray params = new JsonArray();
        params.add(message.getInteger("leagueId"));
        
        sqlConnection.queryWithParams("SELECT "
        		+ "g.id,"
        		+ "g.LEAGUE_ID,"
        		+ "g.NAME,"
        		+ "To_Char(g.ACTIVEFROM, 'Dy Mon DD YYYY HH24:MI:SS')|| ' GMT+0330' ACTIVE_FROM,"
        		+ "To_Char(g.ACTIVETO, 'Dy Mon DD YYYY HH24:MI:SS')|| ' GMT+0330' ACTIVE_TO,"
        		+ "To_Char(g.creationdate, 'Dy Mon DD YYYY HH24:MI:SS')|| ' GMT+0330' creation_date"
        		+ "  FROM toppgroup g WHERE g.LEAGUE_ID=nvl(?, g.LEAGUE_ID) and g.dto is null", params, handler -> {
            if (handler.failed()) {
            	logger.error("Unable to get accessQueryResult:", handler.cause());
                promise.fail(new DAOEXCP_Internal(-100, "خطای داخلی. با راهبر سامانه تماس بگیرید."));
            } else {
                if (null == handler.result() || null == handler.result().getRows() || handler.result().getRows().isEmpty()) {
                	logger.error("fetchAllGroupNoDataFound");
                	promise.complete(new ArrayList<>());
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
        		+ "To_Char(g.ACTIVEFROM, 'Dy Mon DD YYYY HH24:MI:SS')|| ' GMT+0330' ACTIVE_FROM,"
        		+ "To_Char(g.ACTIVETO, 'Dy Mon DD YYYY HH24:MI:SS')|| ' GMT+0330' ACTIVE_TO,"
        		+ "To_Char(g.creationdate, 'Dy Mon DD YYYY HH24:MI:SS')|| ' GMT+0330' creation_date"
        		+ "  FROM toppgroup g WHERE g.id=? and g.dto is null", params, handler -> {
            if (handler.failed()) {
            	logger.error("Unable to get accessQueryResult:", handler.cause());
                promise.fail(new DAOEXCP_Internal(-100, "خطای داخلی. با راهبر سامانه تماس بگیرید."));
            } else {
                if (null == handler.result() || null == handler.result().getRows() || handler.result().getRows().isEmpty()) {
                	logger.error("fetchGroupByIdNoDataFound");
                    promise.fail(new DAOEXCP_Internal(-100, "داده ای یافت نشد"));
                } else {
                    logger.trace("fetchAllGroupByIdSuccessful");
                    promise.complete(handler.result().getRows().get(0));
                }
            
            }
        });

        return promise.future();
    }
    
    public static Future<Void> assignQuestion(SQLConnection sqlConnection, JsonObject message) {
        Promise<Void> promise = Promise.promise();
        JsonArray params = new JsonArray();
        params.add(message.getInteger("teamId"));
        params.add(message.getInteger("groupId"));
        
		sqlConnection.updateWithParams("insert into toppteamgroup (TEAM_ID, GROUP_ID) values(?,?)", params, resultHandler->{
			if(resultHandler.failed()) {
				logger.error("Unable to get accessQueryResult:", resultHandler.cause());
				promise.fail(new DAOEXCP_Internal(-100, "خطای داخلی. با راهبر سامانه تماس بگیرید."));
				return;
			}
			
			logger.trace("assignQuestionSuccessful");
			promise.complete();
			
		});
        return promise.future();
    }
    
    public static Future<Void> unAssignQuestion(SQLConnection sqlConnection, JsonObject message) {
        Promise<Void> promise = Promise.promise();
        JsonArray params = new JsonArray();
        params.add(message.getInteger("teamId"));
        params.add(message.getInteger("groupId"));
        
        sqlConnection.updateWithParams("delete from toppteamgroup where TEAM_ID=? and GROUP_ID=?", params, handler -> {
			if(handler.failed()) {
				logger.error("Unable to get accessQueryResult:", handler.cause());
				promise.fail(new DAOEXCP_Internal(-100, "خطای داخلی. با راهبر سامانه تماس بگیرید."));
				return;
			}
			
			logger.trace("unAssignQuestionSuccessful");
			promise.complete();
			
		});
		return promise.future();
    }
     
    public static Future<List<JsonObject>> fetchTeam(SQLConnection sqlConnection, JsonObject message) {

        Promise<List<JsonObject>> promise = Promise.promise();
        JsonArray params = new JsonArray();
        params.add(message.getInteger("groupId"));
        
        sqlConnection.queryWithParams(""
        		+ "select "
        		+ "t.id,"
        		+ "t.league_id,"
        		+ "t.name,"
        		+ "t.symbol,"
        		+ "t.image "
        		+ "from "
        		+ "toppTeam t, toppteamgroup tg "
        		+ "where "
        		+ "tg.group_ID=? and tg.team_ID=t.id and t.dto is null", params, handler -> {
            if (handler.failed()) {
            	logger.error("Unable to get accessQueryResult:", handler.cause());
                promise.fail(new DAOEXCP_Internal(-100, "خطای داخلی. با راهبر سامانه تماس بگیرید."));
            } else {

                if (null == handler.result() || null == handler.result().getRows() || handler.result().getRows().isEmpty()) {
                	logger.error("fetchTeamNoDataFound");
                	promise.complete(new ArrayList<>());
                } else {
                    logger.trace("TeamfetchQuestionSuccessful");
                    promise.complete(handler.result().getRows());
                }
            
            }
        });

        return promise.future();
    
    }
        
}
