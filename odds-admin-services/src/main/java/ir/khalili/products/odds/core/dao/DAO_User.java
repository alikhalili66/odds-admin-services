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

    public static Future<Integer> fetchCountAll(SQLConnection sqlConnection, JsonObject message) {
        Promise<Integer> promise = Promise.promise();
        JsonArray params = new JsonArray();
        params.add(message.getInteger("leagueId"));
        params.add(message.getString("username"));

        
        sqlConnection.queryWithParams("SELECT count(*) CNT FROM toppuser u where u.league_Id = ? and u.username = nvl(?, u.username) ",params,  handler -> {
            if (handler.failed()) {
            	logger.error("Unable to get accessQueryResult:", handler.cause());
                promise.fail(new DAOEXCP_Internal(-100, "خطای داخلی. با راهبر سامانه تماس بگیرید."));
            } else {
                if (null == handler.result() || null == handler.result().getRows() || handler.result().getRows().isEmpty()) {
                	logger.error("fetchCountAllNoDataFound");
                	promise.complete(0);
                } else {
                    logger.trace("fetchCountAllSuccessful");
                    promise.complete(handler.result().getRows().get(0).getInteger("CNT"));
                }
            
            }
        });

        return promise.future();
    }

    
    public static Future<List<JsonObject>> fetchAll(SQLConnection sqlConnection, JsonObject message) {
        Promise<List<JsonObject>> promise = Promise.promise();
        JsonArray params = new JsonArray();
        params.add(message.getInteger("leagueId"));
        params.add(message.getString("username"));
        params.add(message.getInteger("startIndex"));
        params.add(message.getInteger("endIndex"));
        
        sqlConnection.queryWithParams("SELECT * FROM (SELECT "
        		+ "u.id,"
        		+ "u.UGID,"
        		+ "u.NIKENAME,"
        		+ "u.username,"
        		+ "u.name,"
        		+ "u.lastname,"
        		+ "u.POINT,"
        		+ "u.AMOUNT,"
        		+ "To_Char(u.creationdate, 'Dy Mon DD YYYY HH24:MI:SS')|| ' GMT+0330' creation_date, "
        		+ "row_number() over (ORDER BY u.id desc) line_number"
        		+ "  FROM toppuser u where u.league_Id = ? and u.username = nvl(?, u.username)) WHERE line_number BETWEEN ? AND ?",params,  handler -> {
            if (handler.failed()) {
            	logger.error("Unable to get accessQueryResult:", handler.cause());
                promise.fail(new DAOEXCP_Internal(-100, "خطای داخلی. با راهبر سامانه تماس بگیرید."));
            } else {
                if (null == handler.result() || null == handler.result().getRows() || handler.result().getRows().isEmpty()) {
                	logger.error("fetchAllNoDataFound");
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
        		+ "u.UGID,"
        		+ "u.NIKENAME,"
        		+ "u.POINT,"
        		+ "u.AMOUNT,"
        		+ "To_Char(u.creationdate, 'Dy Mon DD YYYY HH24:MI:SS')|| ' GMT+0330' creation_date "
        		+ "  FROM toppuser u where u.id=?", params, handler -> {
            if (handler.failed()) {
            	logger.error("Unable to get accessQueryResult:", handler.cause());
                promise.fail(new DAOEXCP_Internal(-100, "خطای داخلی. با راهبر سامانه تماس بگیرید."));
            } else {
                if (null == handler.result() || null == handler.result().getRows() || handler.result().getRows().isEmpty()) {
                	logger.error("fetchByIdNoDataFound");
                    promise.fail(new DAOEXCP_Internal(-100, "داده ای یافت نشد"));
                } else {
                    logger.trace("fetchAllUserByIdSuccessful");
                    promise.complete(handler.result().getRows().get(0));
                }
            
            }
        });

        return promise.future();
    }
    
    
    public static Future<JsonObject> fetchById(SQLConnection sqlConnection, Integer userId, Long rewardPoint) {
        Promise<JsonObject> promise = Promise.promise();
        JsonArray params = new JsonArray();
        params.add(userId);
        
        sqlConnection.queryWithParams("SELECT "
        		+ "u.id,"
        		+ "u.UGID,"
        		+ "u.NIKENAME,"
        		+ "u.POINT,"
        		+ "u.AMOUNT,"
        		+ "To_Char(u.creationdate, 'Dy Mon DD YYYY HH24:MI:SS')|| ' GMT+0330' creation_date "
        		+ "  FROM toppuser u where u.id=?", params, handler -> {
            if (handler.failed()) {
            	logger.error("Unable to get accessQueryResult:", handler.cause());
                promise.fail(new DAOEXCP_Internal(-100, "خطای داخلی. با راهبر سامانه تماس بگیرید."));
            } else {
                if (null == handler.result() || null == handler.result().getRows() || handler.result().getRows().isEmpty()) {
                	logger.error("fetchByIdNoDataFound");
                    promise.fail(new DAOEXCP_Internal(-100, "داده ای یافت نشد"));
                } else {
                    logger.trace("fetchAllUserByIdSuccessful");
                    JsonObject output = handler.result().getRows().get(0);
                    output.put("REWARD_POINT", rewardPoint);
                    promise.complete(output);
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
        
        String query =
        		""
                		+ ""
                		+ " SELECT id, LEAGUE_ID, league_name, TEAM1_ID, TEAM1_name, TEAM1_image, TEAM2_ID, TEAM2_name, TEAM2_image, GROUP_ID, group_name, COMPETITION_ID, POINT,  REWARD_POINT     FROM ( " + 
                		"  SELECT t1.*, t2.*, row_number() over (ORDER BY t1.COMPETITIONDATE DESC) line_number " + 
                		"  FROM " + 
                		"    (SELECT c.id, " + 
                		"      c.LEAGUE_ID, " + 
                		"      l.name league_name, " + 
                		"      c.TEAM1_ID, " + 
                		"      (SELECT name FROM toppteam t WHERE t.id=c.TEAM1_ID ) TEAM1_name, " + 
                		"      (SELECT image FROM toppteam t WHERE t.id=c.TEAM1_ID ) TEAM1_image, " + 
                		"      c.TEAM2_ID, " + 
                		"      (SELECT name FROM toppteam t WHERE t.id=c.TEAM2_ID ) TEAM2_name, " + 
                		"      (SELECT image FROM toppteam t WHERE t.id=c.TEAM2_ID ) TEAM2_image, " + 
                		"      c.GROUP_ID, " + 
                		"      c.COMPETITIONDATE, " + 
                		"      g.name group_name " + 
                		"    FROM toppCOMPETITION c, toppleague l, toppgroup g " + 
                		"    WHERE c.GROUP_ID=g.id AND c.LEAGUE_ID =l.id ) t1, " + 
                		"    (SELECT o.COMPETITION_ID, " + 
                		"      SUM(o.POINT) POINT , " + 
                		"      NVL(SUM(REWARDPOINT),0) REWARD_POINT " + 
                		"    FROM toppodds o " + 
                		"    WHERE o.user_id=? " + 
                		"    GROUP BY COMPETITION_ID " + 
                		"    ) t2 " + 
                		"  WHERE t1.id = t2.COMPETITION_ID " + 
                		"  ) " + 
                		" WHERE line_number BETWEEN ? AND ? "
                		+ ""
                		+ "";
        
        System.out.println(query);
        
        sqlConnection.queryWithParams(query, params, handler -> {
            if (handler.failed()) {
            	logger.error("Unable to get accessQueryResult:", handler.cause());
                promise.fail(new DAOEXCP_Internal(-100, "خطای داخلی. با راهبر سامانه تماس بگیرید."));
            } else {

                if (null == handler.result() || null == handler.result().getRows() || handler.result().getRows().isEmpty()) {
                	logger.error("fetchOddsNoDataFound");
                	promise.complete(new ArrayList<>());
                } else {
                    logger.trace("fetchOddsSuccessful");
                    promise.complete(handler.result().getRows());
                }
            
            }
        });

        return promise.future();
    }

    public static Future<Void> saveUserPointHistory(SQLConnection sqlConnection, JsonObject message, String historyType, String historyDescription) {

    	Promise<Void> promise = Promise.promise();
		
		JsonArray params = new JsonArray();
		params.add(message.getInteger("ID"));
		params.add(message.getInteger("POINT"));
		params.add(historyType);
		params.add(historyDescription);
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

    
    public static Future<Void> saveUserPointHistory(SQLConnection sqlConnection, List<JsonObject> winnerUsers, String historyType, String historyDescription, Integer competitionId) {

    	Promise<Void> promise = Promise.promise();
		
		List<JsonArray> params = new ArrayList<>();
		winnerUsers.forEach(joUser -> {
			params.add(new JsonArray()
					.add(joUser.getInteger("USER_ID"))
					.add(joUser.getInteger("REWARD_POINT"))
					.add(historyType)
					.add(historyDescription)
					.add(competitionId)
					.add(0));
					
		});
		
		sqlConnection.batchWithParams(""
				+ "insert into toppuserpointhistory("
				+ "ID,"
				+ "USER_ID,"
				+ "POINT,"
				+ "HISTORYTYPE,"
				+ "HISTORYDESCRIPTION,"
				+ "HISTORYDATE,"
				+ "COMPETITION_ID,"
				+ "AMOUNT)"
				+ "values("
				+ "soppuserpointhistory.nextval,"
				+ "?,"
				+ "?,"
				+ "?,"
				+ "?,"
				+ "sysdate,"
				+ "?,"
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
        		"	 o.POINT," + 
        		"	 o.REWARDPOINT," + 
        		"    o.answer, " + 
        		"	 o.CORRECTANSWER " + 
        		"FROM " + 
        		"    toppodds o, " + 
        		"    toppquestion q " + 
        		"WHERE " + 
        		"    o.user_id = ? " + 
        		"    AND o.competition_id = ? " + 
        		"    AND o.question_id=q.id", params, handler -> {
            if (handler.failed()) {
            	logger.error("Unable to get accessQueryResult:", handler.cause());
                promise.fail(new DAOEXCP_Internal(-100, "خطای داخلی. با راهبر سامانه تماس بگیرید."));
            } else {
                if (null == handler.result() || null == handler.result().getRows() || handler.result().getRows().isEmpty()) {
                	logger.error("fetchQuestionAnswerNoDataFound");
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
        		+ "u.competition_Id,"
        		+ "u.historytype history_type,"
        		+ "u.historydescription history_description,"
        		+ "To_Char(u.historydate, 'Dy Mon DD YYYY HH24:MI:SS')|| ' GMT+0330' history_date, "
        		+ "row_number() over (ORDER BY u.id desc) line_number"
        		+ "  FROM toppuserpointhistory u where u.user_id=?) WHERE line_number BETWEEN ? AND ?",params,  handler -> {
            if (handler.failed()) {
            	logger.error("Unable to get accessQueryResult:", handler.cause());
                promise.fail(new DAOEXCP_Internal(-100, "خطای داخلی. با راهبر سامانه تماس بگیرید."));
            } else {
                if (null == handler.result() || null == handler.result().getRows() || handler.result().getRows().isEmpty()) {
                	logger.error("fetchUserPointHistoryNoDataFound");
                	promise.complete(new ArrayList<>());
                } else {
                    logger.trace("fetchUserPointHistorySuccessful");
                    promise.complete(handler.result().getRows());
                }
            
            }
        });

        return promise.future();
    }
    
    public static Future<JsonObject> fetchUserByUGID(SQLConnection sqlConnection, int leagueId, String UGID) {
        Promise<JsonObject> promise = Promise.promise();
        JsonArray params = new JsonArray();
        params.add(leagueId);
//        params.add(UGID);

        sqlConnection.queryWithParams("SELECT " +
                "  TU.ID , " +
                "  TU.NIKENAME, " +
                "  TU.POINT, " +
                "  TU.AMOUNT   " +
                "  FROM TOPPUSER TU " +
                "  WHERE LEAGUE_ID = ? and TU.UGID    = '" + UGID+ "'", params, resultHandler -> {
            if (resultHandler.failed()) {
                logger.error("Unable to get accessQueryResult:", resultHandler.cause());
                promise.fail(new DAOEXCP_Internal(-100, "خطای داخلی. با راهبر سامانه تماس بگیرید."));
            } else {
                if (null == resultHandler.result() || null == resultHandler.result().getRows() || resultHandler.result().getRows().isEmpty()) {
                    promise.fail(new DAOEXCP_Internal(-100, "کاربر مورد نظر موجود نمی باشد."));
                } else {
                    logger.trace("fetchUserByUGID01");
                    promise.complete(resultHandler.result().getRows().get(0));
                }
            }
        });

        return promise.future();
    }
    
    public static Future<Void> deleteUserPointHistory(SQLConnection sqlConnection, List<JsonObject> winnerUsers, Integer competitionId) {

    	Promise<Void> promise = Promise.promise();
		
		List<JsonArray> params = new ArrayList<>();
		winnerUsers.forEach(joUser -> {
			params.add(new JsonArray()
					.add(joUser.getInteger("USER_ID"))
					.add(competitionId)
					);
					
		});
		
		sqlConnection.batchWithParams(""
				+ "delete FROM TOPPUSERPOINTHISTORY where id = (SELECT id from (SELECT id, ROWNUM AS RN FROM TOPPUSERPOINTHISTORY where USER_ID=? and  COMPETITION_ID = ? ORDER BY ID DESC) WHERE RN = 1)"
				, params, resultHandler->{
			if(resultHandler.failed()) {
				logger.error("Unable to get accessQueryResult:", resultHandler.cause());
				promise.fail(new DAOEXCP_Internal(-100, "خطای داخلی. با راهبر سامانه تماس بگیرید."));
				return;
			}
			
			logger.trace("deleteUserPointHistorySuccessful");
			promise.complete();
			
		});
		
		return promise.future();
	}
    
}
