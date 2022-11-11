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

public class DAO_Report {

    private static final Logger logger = LogManager.getLogger(DAO_Report.class);
    
    public static Future<Integer> fetchRegisteredUsersCount(SQLConnection sqlConnection, int leagueId) {
        Promise<Integer> promise = Promise.promise();
        
        JsonArray params = new JsonArray();
        params.add(leagueId);
        
        sqlConnection.queryWithParams("select count(*) USER_COUNT from toppuser where league_Id = ?", params, handler -> {
            if (handler.failed()) {
            	logger.error("Unable to get accessQueryResult:", handler.cause());
                promise.fail(new DAOEXCP_Internal(-100, "خطای داخلی. با راهبر سامانه تماس بگیرید."));
            } else {
                if (null == handler.result() || null == handler.result().getRows() || handler.result().getRows().isEmpty()) {
                	logger.error("fetchRegisteredUsersCountNoDataFound");
                	promise.complete(0);
                } else {
                    logger.trace("fetchRegisteredUsersCountSuccessful");
                    promise.complete(handler.result().getRows().get(0).getInteger("USER_COUNT"));
                }
            
            }
        });

        return promise.future();
    }
    
    public static Future<Integer> fetchCompetitorUsersCount(SQLConnection sqlConnection, int leagueId) {
        Promise<Integer> promise = Promise.promise();
        
        JsonArray params = new JsonArray();
        params.add(leagueId);
        
        sqlConnection.queryWithParams("select count(distinct user_id) COMPETITOR_COUNT from toppodds where league_Id = ?", params, handler -> {
            if (handler.failed()) {
            	logger.error("Unable to get accessQueryResult:", handler.cause());
                promise.fail(new DAOEXCP_Internal(-100, "خطای داخلی. با راهبر سامانه تماس بگیرید."));
            } else {
                if (null == handler.result() || null == handler.result().getRows() || handler.result().getRows().isEmpty()) {
                	logger.error("fetchCompetitorUsersCountNoDataFound");
                	promise.complete(0);
                } else {
                    logger.trace("fetchCompetitorUsersCountSuccessful");
                    promise.complete(handler.result().getRows().get(0).getInteger("COMPETITOR_COUNT"));
                }
            
            }
        });

        return promise.future();
    }
    
    public static Future<Long> fetchCompetitorUsersAmount(SQLConnection sqlConnection, int leagueId) {
        Promise<Long> promise = Promise.promise();
        
        JsonArray params = new JsonArray();
        params.add(leagueId);
        
        sqlConnection.queryWithParams("select sum(amount) TOTAL_AMOUNT from toppuser where league_Id = ?", params, handler -> {
            if (handler.failed()) {
            	logger.error("Unable to get accessQueryResult:", handler.cause());
                promise.fail(new DAOEXCP_Internal(-100, "خطای داخلی. با راهبر سامانه تماس بگیرید."));
            } else {
                if (null == handler.result() || null == handler.result().getRows() || handler.result().getRows().isEmpty()) {
                	logger.error("fetchCompetitorUsersAmountNoDataFound");
                	promise.complete(0L);
                } else {
                    logger.trace("fetchCompetitorUsersAmountSuccessful");
                    promise.complete(handler.result().getRows().get(0).getLong("TOTAL_AMOUNT"));
                }
            
            }
        });

        return promise.future();
    }
    
    public static Future<Integer> fetchOddsCount(SQLConnection sqlConnection, int leagueId) {
        Promise<Integer> promise = Promise.promise();
        
        JsonArray params = new JsonArray();
        params.add(leagueId);
        
        sqlConnection.queryWithParams("select count(*) ODDS_COUNT from toppodds where league_Id = ?", params, handler -> {
            if (handler.failed()) {
            	logger.error("Unable to get accessQueryResult:", handler.cause());
                promise.fail(new DAOEXCP_Internal(-100, "خطای داخلی. با راهبر سامانه تماس بگیرید."));
            } else {
                if (null == handler.result() || null == handler.result().getRows() || handler.result().getRows().isEmpty()) {
                	logger.error("fetchOddsCountNoDataFound");
                	promise.complete(0);
                } else {
                    logger.trace("fetchOddsCountSuccessful");
                    promise.complete(handler.result().getRows().get(0).getInteger("ODDS_COUNT"));
                }
            
            }
        });

        return promise.future();
    }
    
    public static Future<Void> saveReport(SQLConnection sqlConnection, JsonObject message) {

		Promise<Void> promise = Promise.promise();
		
		JsonArray params = new JsonArray();
		
		params.add(message.getInteger("competitionId"));
		params.add(message.getInteger("leagueId"));
		params.add(message.getInteger("type"));
		params.add(message.getInteger("result"));
		
		sqlConnection.updateWithParams(""
				+ "INSERT INTO TOPPREPORT("
				+ "ID,"
				+ "COMPETITION_ID,"
				+ "LEAGUE_ID,"
				+ "TYPE,"
				+ "RESULT,"
				+ "CREATIONDATE)"
				+ "values("
				+ "soppreport.nextval,"
				+ "?,"
				+ "?,"
				+ "?,"
				+ "?,"
				+ "sysdate)", params, resultHandler->{
			if(resultHandler.failed()) {
				logger.error("Unable to get accessQueryResult:", resultHandler.cause());
				promise.fail(new DAOEXCP_Internal(-100, "خطای داخلی. با راهبر سامانه تماس بگیرید."));
				return;
			}
			
			logger.trace("saveReportSuccessful");
			promise.complete();
			
		});
		
		return promise.future();
	}

    public static Future<List<JsonObject>> fetchReportCompetitionWithMaximumOdds(SQLConnection sqlConnection) {
        Promise<List<JsonObject>> promise = Promise.promise();
        
        sqlConnection.query(
        		"SELECT  " + 
        		"    competition_id,  " + 
        		"    cnt, " + 
        		"    (select name from toppteam t where t.id=team1_id) TEAM1_name, " + 
        		"    (select name from toppteam t where t.id=team2_id) TEAM2_name, " + 
        		"    (select g.name from toppgroup g where g.id=group_id) GROUP_name " + 
        		" " + 
        		"FROM " + 
        		"    ( " + 
        		"        SELECT " + 
        		"            o.competition_id, " + 
        		"            o.team1_id, " + 
        		"            o.team2_id, " + 
        		"            o.group_id, " + 
        		"            COUNT(*) cnt " + 
        		"        FROM " + 
        		"            toppodds o " + 
        		"        GROUP BY " + 
        		"            o.competition_id, o.team1_id, o.team2_id, o.group_id " + 
        		"        ORDER BY " + 
        		"            cnt DESC " + 
        		"    ) " + 
        		"WHERE ROWNUM < 6", handler -> {
            if (handler.failed()) {
            	logger.error("Unable to get accessQueryResult:", handler.cause());
                promise.fail(new DAOEXCP_Internal(-100, "خطای داخلی. با راهبر سامانه تماس بگیرید."));
            } else {
                if (null == handler.result() || null == handler.result().getRows() || handler.result().getRows().isEmpty()) {
                	logger.error("fetchReportCompetitionWithMaximumOddsNoDataFound");
                	promise.complete(new ArrayList<>());
                } else {
                    logger.trace("fetchReportCompetitionWithMaximumOddsSuccessful");
                    promise.complete(handler.result().getRows());
                }
            
            }
        });

        return promise.future();
    }
    
    public static Future<List<JsonObject>> fetchReportUsersWithMaximumPoint(SQLConnection sqlConnection) {
        Promise<List<JsonObject>> promise = Promise.promise();
        
        sqlConnection.query("SELECT " + 
        		"    * " + 
        		"FROM " + 
        		"    ( " + 
        		"        SELECT " + 
        		"            u.id      user_id, " + 
        		"            u.name, " + 
        		"            u.lastname, " + 
        		"            u.nikename, " + 
        		"            u.point   total_point " + 
        		"        FROM " + 
        		"            toppuser u " + 
        		"        GROUP BY " + 
        		"            u.id, " + 
        		"            u.name, " + 
        		"            u.lastname, " + 
        		"            u.nikename, " + 
        		"            u.point " + 
        		"        ORDER BY " + 
        		"            u.point DESC " + 
        		"    ) " + 
        		"WHERE " + 
        		"    ROWNUM < 31", handler -> {
            if (handler.failed()) {
            	logger.error("Unable to get accessQueryResult:", handler.cause());
                promise.fail(new DAOEXCP_Internal(-100, "خطای داخلی. با راهبر سامانه تماس بگیرید."));
            } else {
                if (null == handler.result() || null == handler.result().getRows() || handler.result().getRows().isEmpty()) {
                	logger.error("fetchReportUsersWithMaximumPointNoDataFound");
                	promise.complete(new ArrayList<>());
                } else {
                    logger.trace("fetchReportUsersWithMaximumPointSuccessful");
                    promise.complete(handler.result().getRows());
                }
            
            }
        });

        return promise.future();
    }
    

    public static Future<List<JsonObject>> fetchReportQuestionStatisticPerCompetition(SQLConnection sqlConnection, Integer competitionId) {
        Promise<List<JsonObject>> promise = Promise.promise();
        
        JsonArray params = new JsonArray();
        params.add(competitionId);
        
        sqlConnection.queryWithParams("SELECT " + 
        		"    o.question_id, " + 
        		"    sum(o.point) total_point, " + 
        		"    count(distinct o.user_id) user_count, " + 
        		"    round ((SELECT COUNT(DISTINCT odd.user_id) FROM toppodds odd,toppcompetitionquestion cq WHERE odd.competition_id = o.competition_id AND odd.question_id = o.question_id AND cq.competition_id = odd.competition_id AND cq.question_id = odd.question_id AND odd.answer = cq.result) / count(distinct o.user_id)*100, 2) correct_answer_percentage, " + 
        		"    round ((SELECT COUNT(DISTINCT odd.user_id) FROM toppodds odd,toppcompetitionquestion cq WHERE odd.competition_id = o.competition_id AND odd.question_id = o.question_id AND cq.competition_id = odd.competition_id AND cq.question_id = odd.question_id AND odd.answer != cq.result) / count(distinct o.user_id)*100, 2) incorrect_answer_percentage " + 
        		"FROM " + 
        		"    toppodds o " + 
        		"GROUP BY " + 
        		"    o.question_id, o.competition_id " + 
        		"HAVING " + 
        		"    o.competition_id=? " + 
        		"ORDER BY " + 
        		"    total_point DESC", params, handler -> {
            if (handler.failed()) {
            	logger.error("Unable to get accessQueryResult:", handler.cause());
                promise.fail(new DAOEXCP_Internal(-100, "خطای داخلی. با راهبر سامانه تماس بگیرید."));
            } else {
                if (null == handler.result() || null == handler.result().getRows() || handler.result().getRows().isEmpty()) {
                	logger.error("fetchReportQuestionStatisticPerCompetitionNoDataFound");
                	promise.complete(new ArrayList<>());
                } else {
                    logger.trace("fetchReportQuestionStatisticPerCompetitionSuccessful");
                    promise.complete(handler.result().getRows());
                }
            
            }
        });

        return promise.future();
    }
    
    public static Future<List<JsonObject>> fetchReportOddsCountPerCompetition(SQLConnection sqlConnection, Integer competitionId) {
        Promise<List<JsonObject>> promise = Promise.promise();
        JsonArray params = new JsonArray();
        params.add(competitionId);
        
        sqlConnection.queryWithParams("SELECT " + 
        		"    o.competition_id, " + 
        		"    COUNT(DISTINCT o.id) cnt " + 
        		"FROM " + 
        		"    toppodds o " + 
        		"GROUP BY " + 
        		"    o.competition_id " + 
        		"HAVING " + 
        		"    o.competition_id=nvl(?,o.competition_id) " + 
        		"ORDER BY " + 
        		"    cnt DESC", params, handler -> {
            if (handler.failed()) {
            	logger.error("Unable to get accessQueryResult:", handler.cause());
                promise.fail(new DAOEXCP_Internal(-100, "خطای داخلی. با راهبر سامانه تماس بگیرید."));
            } else {
                if (null == handler.result() || null == handler.result().getRows() || handler.result().getRows().isEmpty()) {
                	logger.error("fetchReportOddsCountPerCompetitionNoDataFound");
                	promise.complete(new ArrayList<>());
                } else {
                    logger.trace("fetchReportOddsCountPerCompetitionSuccessful");
                    promise.complete(handler.result().getRows());
                }
            
            }
        });

        return promise.future();
    }
    

    public static Future<List<JsonObject>> fetchReportUsersCountWithOdds(SQLConnection sqlConnection) {
        Promise<List<JsonObject>> promise = Promise.promise();
        
        sqlConnection.query("SELECT " + 
        		"    u.id user_id, " + 
        		"    u.name, " + 
        		"    u.lastname, " + 
        		"    u.nikename, " + 
        		"    u.point total_point, " + 
        		"    u.amount total_amount, " + 
        		"    COUNT(o.user_id) odds_count " + 
        		"FROM " + 
        		"    toppuser   u, " + 
        		"    toppodds   o " + 
        		"GROUP BY " + 
        		"    u.id, " + 
        		"    u.name, " + 
        		"    u.lastname, " + 
        		"    u.nikename, " + 
        		"    u.point, " + 
        		"    u.amount, " + 
        		"    o.user_id " + 
        		"HAVING " + 
        		"    u.id = o.user_id " + 
        		"ORDER BY " + 
        		"    u.point DESC", handler -> {
            if (handler.failed()) {
            	logger.error("Unable to get accessQueryResult:", handler.cause());
                promise.fail(new DAOEXCP_Internal(-100, "خطای داخلی. با راهبر سامانه تماس بگیرید."));
            } else {
                if (null == handler.result() || null == handler.result().getRows() || handler.result().getRows().isEmpty()) {
                	logger.error("fetchReportUsersCountWithOddsNoDataFound");
                	promise.complete(new ArrayList<>());
                } else {
                    logger.trace("fetchReportUsersCountWithOddsSuccessful");
                    promise.complete(handler.result().getRows());
                }
            
            }
        });

        return promise.future();
    }
    
    
    public static Future<List<JsonObject>> fetchUsersWithMaximumOdds(SQLConnection sqlConnection) {
        Promise<List<JsonObject>> promise = Promise.promise();
        
        sqlConnection.query("SELECT " + 
        		"    u.id user_id, " + 
        		"    u.name, " + 
        		"    u.lastname, " + 
        		"    u.nikename, " + 
        		"    u.point total_point, " + 
        		"    u.amount total_amount, " + 
        		"    COUNT(o.user_id) odds_count " + 
        		"FROM " + 
        		"    toppuser   u, " + 
        		"    toppodds   o " + 
        		"GROUP BY " + 
        		"    u.id, " + 
        		"    u.name, " + 
        		"    u.lastname, " + 
        		"    u.nikename, " + 
        		"    u.point, " + 
        		"    u.amount, " + 
        		"    o.user_id " + 
        		"HAVING " + 
        		"    u.id = o.user_id " + 
        		"ORDER BY " + 
        		"    u.point DESC", handler -> {
            if (handler.failed()) {
            	logger.error("Unable to get accessQueryResult:", handler.cause());
                promise.fail(new DAOEXCP_Internal(-100, "خطای داخلی. با راهبر سامانه تماس بگیرید."));
            } else {
                if (null == handler.result() || null == handler.result().getRows() || handler.result().getRows().isEmpty()) {
                	logger.error("fetchUsersWithMaximumOddsNoDataFound");
                	promise.complete(new ArrayList<>());
                } else {
                    logger.trace("fetchUsersWithMaximumOddsSuccessful");
                    promise.complete(handler.result().getRows());
                }
            
            }
        });

        return promise.future();
    }
    
    public static Future<List<JsonObject>> fetchReportCompetitionsTotalPoint(SQLConnection sqlConnection, Integer competitionId) {
        Promise<List<JsonObject>> promise = Promise.promise();
        JsonArray params = new JsonArray();
        params.add(competitionId);
        
        sqlConnection.queryWithParams(
        		"SELECT " + 
        		"    o.competition_id, " + 
        		"    COUNT(o.point) total_point " + 
        		"FROM " + 
        		"    toppodds o " + 
        		"GROUP BY " + 
        		"    o.competition_id " + 
        		"HAVING " + 
        		"    o.competition_id=nvl(?,o.competition_id) " + 
        		"ORDER BY " + 
        		"    total_point DESC", params, handler -> {
            if (handler.failed()) {
            	logger.error("Unable to get accessQueryResult:", handler.cause());
                promise.fail(new DAOEXCP_Internal(-100, "خطای داخلی. با راهبر سامانه تماس بگیرید."));
            } else {
                if (null == handler.result() || null == handler.result().getRows() || handler.result().getRows().isEmpty()) {
                	logger.error("fetchReportCompetitionsTotalPointNoDataFound");
                	promise.complete(new ArrayList<>());
                } else {
                    logger.trace("fetchReportCompetitionsTotalPointSuccessful");
                    promise.complete(handler.result().getRows());
                }
            
            }
        });

        return promise.future();
    }
    
    
    public static Future<JsonObject> fetchReport(SQLConnection sqlConnection, Integer competitionId, Integer leagueId, String type) {
        Promise<JsonObject> promise = Promise.promise();
        JsonArray params = new JsonArray();
        params.add(type);
        params.add(competitionId);
        params.add(leagueId);
        
        sqlConnection.queryWithParams("SELECT " + 
        		"    r.id, " + 
        		"    r.competition_id, " + 
        		"    r.league_id, " + 
        		"    r.type, " + 
        		"    r.result, " + 
        		"    to_char(r.creationdate, 'YYYY-MM-DD\"T\"HH24:MI:SS\"Z\"') creation_date " + 
        		"FROM " + 
        		"    toppreport r " + 
        		"WHERE " + 
        		"    r.type = ? " + 
        		"    AND r.competition_id = nvl(?, r.competition_id) " + 
        		"    AND r.league_id = nvl(?, r.league_id) " + 
        		"    AND r.dto IS NULL " + 
        		"ORDER BY " + 
        		"    r.id DESC " + 
        		"FETCH FIRST ROW ONLY", params, handler -> {
            if (handler.failed()) {
            	logger.error("Unable to get accessQueryResult:", handler.cause());
                promise.fail(new DAOEXCP_Internal(-100, "خطای داخلی. با راهبر سامانه تماس بگیرید."));
            } else {
                if (null == handler.result() || null == handler.result().getRows() || handler.result().getRows().isEmpty()) {
                	logger.error("fetchQuestionByIdNoDataFound");
                    promise.fail(new DAOEXCP_Internal(-100, "داده ای یافت نشد"));
                } else {
                    logger.trace("fetchAllQuestionByIdSuccessful");
                    promise.complete(handler.result().getRows().get(0));
                }
            
            }
        });

        return promise.future();
    }
 
}
