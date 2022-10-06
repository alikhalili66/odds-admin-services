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

public class DAO_User {

    private static final Logger logger = LogManager.getLogger(DAO_User.class);
    
    public static Future<List<JsonObject>> fetchAll(SQLConnection sqlConnection, JsonObject message) {
        Promise<List<JsonObject>> promise = Promise.promise();
        JsonArray params = new JsonArray();
        
        params.add(message.getLong("cellphone"));
        params.add(message.getString("nationalNumber"));
        params.add(message.getInteger("startIndex"));
        params.add(message.getInteger("endIndex"));
        
        sqlConnection.queryWithParams("SELECT * FROM (SELECT "
        		+ "u.id,"
        		+ "u.NAME,"
        		+ "u.LASTNAME,"
        		+ "u.NIKENAME,"
        		+ "u.AVATAR,"
        		+ "u.POINT,"
        		+ "u.AMOUNT,"
        		+ "u.CELLPHONE,"
        		+ "u.NATIONALNUMBER,"
        		+ "To_Char(u.BIRTHDATE,'YYYY-MM-DD\"T\"HH24:MI:SS\"Z\"') BIRTH_DATE,"
        		+ "u.PROVINCE,"
        		+ "u.CITY,"
        		+ "u.POSTCODE,"
        		+ "u.ADDRESS,"
        		+ "To_Char(u.creationdate,'YYYY-MM-DD\"T\"HH24:MI:SS\"Z\"') creation_date, "
        		+ "row_number() over (ORDER BY u.id desc) line_number"
        		+ "  FROM toppuser u where u.CELLPHONE=nvl(?,u.CELLPHONE) and u.NATIONALNUMBER=nvl(?,u.NATIONALNUMBER)) WHERE line_number BETWEEN ? AND ?",params,  handler -> {
            if (handler.failed()) {
                promise.fail(new DAOEXCP_Internal(-100, "خطای داخلی. با راهبر سامانه تماس بگیرید."));
            } else {
                if (null == handler.result() || null == handler.result().getRows() || handler.result().getRows().isEmpty()) {
                	promise.complete(new ArrayList<>());
                } else {
                    logger.trace("fetchAllUserSuccessful");
                    promise.complete(handler.result().getRows());
                }
            
            }
        });

        return promise.future();
    }
    
    public static Future<JsonObject> fetchById(SQLConnection sqlConnection, Integer userId) {
        Promise<JsonObject> promise = Promise.promise();
        JsonArray params = new JsonArray();
        params.add(userId);
        
        sqlConnection.queryWithParams("SELECT "
        		+ "u.id,"
        		+ "u.NAME,"
        		+ "u.LASTNAME,"
        		+ "u.NIKENAME,"
        		+ "u.AVATAR,"
        		+ "u.POINT,"
        		+ "u.AMOUNT,"
        		+ "u.NATIONALNUMBER,"
        		+ "To_Char(u.BIRTHDATE,'YYYY-MM-DD\"T\"HH24:MI:SS\"Z\"') BIRTH_DATE,"
        		+ "u.PROVINCE,"
        		+ "u.CITY,"
        		+ "u.POSTCODE,"
        		+ "u.ADDRESS,"
        		+ "To_Char(u.creationdate,'YYYY-MM-DD\"T\"HH24:MI:SS\"Z\"') creation_date "
        		+ "  FROM toppuser u where u.id=?", params, handler -> {
            if (handler.failed()) {
                promise.fail(new DAOEXCP_Internal(-100, "خطای داخلی. با راهبر سامانه تماس بگیرید."));
            } else {
                if (null == handler.result() || null == handler.result().getRows() || handler.result().getRows().isEmpty()) {
                    promise.fail(new DAOEXCP_Internal(-100, "داده ای یافت نشد"));
                } else {
                    logger.trace("fetchAllUserByIdSuccessful");
                    promise.complete(handler.result().getRows().get(0));
                }
            
            }
        });

        return promise.future();
    }
    
    public static Future<List<JsonObject>> fetchOdds(SQLConnection sqlConnection, JsonObject message) {
        Promise<List<JsonObject>> promise = Promise.promise();
        JsonArray params = new JsonArray();
        params.add(message.getInteger("id"));
        params.add(message.getInteger("startIndex"));
        params.add(message.getInteger("endIndex"));
        
        sqlConnection.queryWithParams("SELECT * FROM (SELECT "
        		+ "o.id,"
        		+ "o.LEAGUE_ID,"
        		+ "l.name league_name,"
        		+ "o.COMPETITION_ID,"
        		+ "o.TEAM1_ID,"
        		+ "(select name from toppteam t where t.id=o.TEAM1_ID) TEAM1_name,"
        		+ "(select image from toppteam t where t.id=o.TEAM1_ID) TEAM1_image,"
        		+ "o.TEAM2_ID,"
        		+ "(select name from toppteam t where t.id=o.TEAM2_ID) TEAM2_name,"
        		+ "(select image from toppteam t where t.id=o.TEAM2_ID) TEAM2_image,"
        		+ "o.GROUP_ID,"
        		+ "g.name group_name,"
        		+ "o.QUESTION_ID,"
        		+ "q.question,"
        		+ "o.ANSWER,"
        		+ "o.POINT,"
        		+ "o.CORRECTANSWER CORRECT_ANSWER,"
        		+ "o.REWARDPOINT REWARD_POINT,"
        		+ "o.COUNT,"
        		+ "row_number() over (ORDER BY o.id desc) line_number"
        		+ "  FROM toppodds o, toppleague l, toppgroup g, toppquestion q where o.QUESTION_ID=q.id and o.GROUP_ID=g.id and o.LEAGUE_ID=l.id and o.id=?) WHERE line_number BETWEEN ? AND ?", params, handler -> {
            if (handler.failed()) {
                promise.fail(new DAOEXCP_Internal(-100, "خطای داخلی. با راهبر سامانه تماس بگیرید."));
            } else {

                if (null == handler.result() || null == handler.result().getRows() || handler.result().getRows().isEmpty()) {
                	promise.complete(new ArrayList<>());
                } else {
                    logger.trace("fetchOddsSuccessful");
                    promise.complete(handler.result().getRows());
                }
            
            }
        });

        return promise.future();
    }
    
    public static Future<Void> saveUserPointHistory(SQLConnection sqlConnection, JsonObject message) {

    	Promise<Void> promise = Promise.promise();
		
		JsonArray params = new JsonArray();
		params.add(message.getInteger("ID"));
		params.add(message.getInteger("POINT"));
		params.add("I");
		params.add(null);
		params.add(message.getLong("AMOUNT"));
		
		sqlConnection.updateWithParams(""
				+ "insert into toppuserpointhistory("
				+ "ID,"
				+ "USER_ID,"
				+ "POINT,"
				+ "HISTORYTYPE,"
				+ "HISTORYDESCRIPTION,"
				+ "HISTORYDATE,"
				+ "AMOUNT)"
				+ "values("
				+ "soppuserpointhistory.nextval,"
				+ "?,"
				+ "?,"
				+ "?,"
				+ "?,"
				+ "sysdate,"
				+ "?)", params, resultHandler->{
			if(resultHandler.failed()) {
				logger.error("Unable to get accessQueryResult:", resultHandler.cause());
				promise.fail(new DAOEXCP_Internal(-100, "خطای داخلی. با راهبر سامانه تماس بگیرید."));
				return;
			}
			
			logger.trace("saveUserPointHistorySuccessful");
			promise.complete();
			
		});
		
		return promise.future();
	}

    public static Future<List<JsonObject>> fetchQuestionAnswer(SQLConnection sqlConnection, JsonObject message) {
        Promise<List<JsonObject>> promise = Promise.promise();
        JsonArray params = new JsonArray();
        params.add(message.getInteger("userId"));
        params.add(message.getInteger("competitionId"));
        
        sqlConnection.queryWithParams("SELECT " + 
        		"    q.question, " + 
        		"    o.answer " + 
        		"FROM " + 
        		"    toppodds o, " + 
        		"    toppquestion q " + 
        		"WHERE " + 
        		"    o.user_id = ? " + 
        		"    AND o.competition_id = ? " + 
        		"    AND o.question_id=q.id", params, handler -> {
            if (handler.failed()) {
                promise.fail(new DAOEXCP_Internal(-100, "خطای داخلی. با راهبر سامانه تماس بگیرید."));
            } else {
                if (null == handler.result() || null == handler.result().getRows() || handler.result().getRows().isEmpty()) {
                    promise.fail(new DAOEXCP_Internal(-100, "داده ای یافت نشد"));
                } else {
                    logger.trace("fetchQuestionAnswerSuccessful");
                    promise.complete(handler.result().getRows());
                }
            
            }
        });

        return promise.future();
    }
 
    public static Future<List<JsonObject>> fetchUserPointHistory(SQLConnection sqlConnection, JsonObject message) {
        Promise<List<JsonObject>> promise = Promise.promise();
        JsonArray params = new JsonArray();
        
        params.add(message.getInteger("userId"));
        params.add(message.getInteger("startIndex"));
        params.add(message.getInteger("endIndex"));
        
        sqlConnection.queryWithParams("SELECT * FROM (SELECT "
        		+ "u.id,"
        		+ "u.user_id,"
        		+ "u.point,"
        		+ "u.amount,"
        		+ "u.historytype history_type,"
        		+ "u.historydescription history_description,"
        		+ "To_Char(u.historydate,'YYYY-MM-DD\"T\"HH24:MI:SS\"Z\"') history_date, "
        		+ "row_number() over (ORDER BY u.id desc) line_number"
        		+ "  FROM toppuserpointhistory u where u.user_id=?) WHERE line_number BETWEEN ? AND ?",params,  handler -> {
            if (handler.failed()) {
                promise.fail(new DAOEXCP_Internal(-100, "خطای داخلی. با راهبر سامانه تماس بگیرید."));
            } else {
                if (null == handler.result() || null == handler.result().getRows() || handler.result().getRows().isEmpty()) {
                	promise.complete(new ArrayList<>());
                } else {
                    logger.trace("fetchUserPointHistorySuccessful");
                    promise.complete(handler.result().getRows());
                }
            
            }
        });

        return promise.future();
    }
    
      
}
