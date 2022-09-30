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

public class DAO_User {

    private static final Logger logger = LogManager.getLogger(DAO_User.class);
    
    public static Future<List<JsonObject>> fetchAll(SQLConnection sqlConnection) {
        Promise<List<JsonObject>> promise = Promise.promise();
        
        sqlConnection.query("SELECT "
        		+ "u.id,"
        		+ "u.NAME,"
        		+ "u.LASTNAME,"
        		+ "u.NIKENAME,"
        		+ "u.AVATAR,"
        		+ "u.POINT,"
        		+ "u.AMOUNT,"
        		+ "u.NATIONALNUMBER,"
        		+ "To_Char(u.BIRTHDATE,'yyyy/mm/dd HH:MM:SS','nls_calendar=persian') BIRTH_DATE,"
        		+ "u.PROVINCE,"
        		+ "u.CITY,"
        		+ "u.POSTCODE,"
        		+ "u.ADDRESS,"
        		+ "To_Char(u.creationdate,'yyyy/mm/dd HH:MM:SS','nls_calendar=persian') creation_date "
        		+ "  FROM toppuser u", handler -> {
            if (handler.failed()) {
                promise.fail(new DAOEXCP_Internal(-100, "خطای داخلی. با راهبر سامانه تماس بگیرید."));
            } else {
                if (null == handler.result() || null == handler.result().getRows() || handler.result().getRows().isEmpty()) {
                    promise.fail(new DAOEXCP_Internal(-100, "داده ای یافت نشد"));
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
        		+ "To_Char(u.BIRTHDATE,'yyyy/mm/dd HH:MM:SS','nls_calendar=persian') BIRTH_DATE,"
        		+ "u.PROVINCE,"
        		+ "u.CITY,"
        		+ "u.POSTCODE,"
        		+ "u.ADDRESS,"
        		+ "To_Char(u.creationdate,'yyyy/mm/dd HH:MM:SS','nls_calendar=persian') creation_date "
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
    
    public static Future<List<JsonObject>> fetchOdds(SQLConnection sqlConnection, Integer userId) {
        Promise<List<JsonObject>> promise = Promise.promise();
        JsonArray params = new JsonArray();
        params.add(userId);
        
        sqlConnection.queryWithParams("SELECT "
        		+ "o.id,"
        		+ "o.LEAGUE_ID,"
        		+ "l.name league_name,"
        		+ "o.COMPETITION_ID,"
        		+ "o.TEAM1_ID,"
        		+ "(select name from toppteam t where t.id=o.TEAM1_ID) TEAM1_name,"
        		+ "o.TEAM2_ID,"
        		+ "(select name from toppteam t where t.id=o.TEAM2_ID) TEAM2_name,"
        		+ "o.GROUP_ID,"
        		+ "g.name group_name,"
        		+ "o.QUESTION_ID,"
        		+ "q.question,"
        		+ "o.ANSWER,"
        		+ "o.POINT,"
        		+ "o.CORRECTANSWER CORRECT_ANSWER,"
        		+ "o.REWARDPOINT REWARD_POINT,"
        		+ "o.COUNT"
        		+ "  FROM toppodds o, toppleague l, toppgroup g, toppquestion q where o.QUESTION_ID=q.id and o.GROUP_ID=g.id and o.LEAGUE_ID=l.id and o.id=?", params, handler -> {
            if (handler.failed()) {
                promise.fail(new DAOEXCP_Internal(-100, "خطای داخلی. با راهبر سامانه تماس بگیرید."));
            } else {

                if (null == handler.result() || null == handler.result().getRows() || handler.result().getRows().isEmpty()) {
                    promise.fail(new DAOEXCP_Internal(-100, "داده ای یافت نشد"));
                } else {
                    logger.trace("fetchOddsSuccessful");
                    promise.complete(handler.result().getRows());
                }
            
            }
        });

        return promise.future();
    }
    
    
      
}
