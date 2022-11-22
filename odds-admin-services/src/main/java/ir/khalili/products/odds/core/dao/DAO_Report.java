package ir.khalili.products.odds.core.dao;

import java.util.ArrayList;
import java.util.Date;
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
		params.add(message.getInteger("groupId"));
		params.add(message.getInteger("questionId"));
		params.add(message.getString("type"));
		params.add(message.getString("result"));
		
		sqlConnection.updateWithParams(""
				+ "INSERT INTO TOPPREPORT("
				+ "ID,"
				+ "COMPETITION_ID,"
				+ "LEAGUE_ID,"
				+ "GROUP_ID,"
				+ "QUESTION_ID,"
				+ "TYPE,"
				+ "RESULT,"
				+ "CREATIONDATE)"
				+ "values("
				+ "soppreport.nextval,"
				+ "?,"
				+ "?,"
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
    
    public static Future<List<JsonObject>> fetchReportLeagueUsersWithMaximumPoint(SQLConnection sqlConnection, Integer leagueId) {
        Promise<List<JsonObject>> promise = Promise.promise();
        
        JsonArray params = new JsonArray();
        params.add(leagueId);
        
        sqlConnection.queryWithParams("SELECT " + 
        		"    * " + 
        		"FROM " + 
        		"    ( " + 
        		"        SELECT " + 
        		"            u.id      user_id, " + 
        		"            u.name, " + 
        		"            u.lastname, " + 
        		"            u.nikename, " + 
        		"            u.username, " + 
        		"            u.point   total_point " + 
        		"        FROM " + 
        		"            toppuser u " + 
        		"        GROUP BY " + 
        		"            u.id, " + 
        		"            u.league_id, " + 
        		"            u.name, " + 
        		"            u.lastname, " + 
        		"            u.nikename, " + 
        		"            u.username, " + 
        		"            u.point " + 
        		"        HAVING " + 
        		"            u.league_id=? " + 
        		"        ORDER BY " + 
        		"            u.point DESC " + 
        		"    ) " + 
        		"WHERE " + 
        		"    ROWNUM < 31", params, handler -> {
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
    
    
    public static Future<JsonObject> fetchReport(SQLConnection sqlConnection, Integer competitionId, Integer leagueId, Integer groupId, Integer questionId, String type, boolean checkUpTo48HoursAgo) {
        Promise<JsonObject> promise = Promise.promise();
        JsonArray params = new JsonArray();
        params.add(type);
        
        StringBuilder query = new StringBuilder();
        query.append( "SELECT " + 
        		"    r.id, " + 
        		"    r.league_id, " +
        		"    r.group_id, " + 
        		"    r.competition_id, " + 
        		"    r.question_id, " + 
        		"    r.type, " + 
        		"    r.result, " + 
        		"    To_Char(r.creationdate, 'Dy Mon DD YYYY HH24:MI:SS')|| ' GMT+0330' as creation_date " + 
        		"FROM " + 
        		"    TOPPREPORT r " + 
        		"WHERE " + 
        		"    r.type = ? ");
        
        if (checkUpTo48HoursAgo) {
        	query.append(" AND r.creationdate > sysdate - 1");
		}
        
        if (competitionId != null) {
        	params.add(competitionId);
        	query.append("    AND r.competition_id = nvl(?, r.competition_id) ");
		} else {
			query.append("    AND r.competition_id is null ");
		}
        
        if (leagueId != null) {
        	params.add(leagueId);
        	query.append("    AND r.league_id = ? ");
		}
        
        if (groupId != null) {
        	params.add(groupId);
        	query.append("    AND r.group_id = nvl(?, r.group_id) ");
		} else {
			query.append("    AND r.group_id is null ");
		}
        
        if (questionId != null) {
        	params.add(questionId);
        	query.append("    AND r.question_id = nvl(?, r.question_id) ");
		} else {
			query.append("    AND r.question_id is null ");
		}
        
        query.append("    AND r.dto IS NULL " + 
        		"ORDER BY " + 
        		"    r.id DESC " + 
        		"FETCH FIRST ROW ONLY");
        
        sqlConnection.queryWithParams(query.toString(), params, handler -> {
            if (handler.failed()) {
            	logger.error("Unable to get accessQueryResult:", handler.cause());
                promise.fail(new DAOEXCP_Internal(-100, "خطای داخلی. با راهبر سامانه تماس بگیرید."));
            } else {
                if (null == handler.result() || null == handler.result().getRows() || handler.result().getRows().isEmpty()) {
                	logger.error("fetchReportNoDataFound");
                	promise.complete(null);
                } else {
                    logger.trace("fetchReportSuccessful");
                    promise.complete(handler.result().getRows().get(0));
                }
            
            }
        });

        return promise.future();
    }
 
    public static Future<JsonObject> fetchReportLeagueBlockedAmount(SQLConnection sqlConnection, Integer leagueId) {
        Promise<JsonObject> promise = Promise.promise();
        JsonArray params = new JsonArray();
        params.add(leagueId);
        
        sqlConnection.queryWithParams("select sum(amount) BLOCKED_AMOUNT from toppuser where league_id=?", params, handler -> {
            if (handler.failed()) {
            	logger.error("Unable to get accessQueryResult:", handler.cause());
                promise.fail(new DAOEXCP_Internal(-100, "خطای داخلی. با راهبر سامانه تماس بگیرید."));
            } else {
                if (null == handler.result() || null == handler.result().getRows() || handler.result().getRows().isEmpty()) {
                	logger.error("fetchReportLeagueBlockedAmountNoDataFound");
                	promise.complete(new JsonObject().put("BLOCKED_AMOUNT", 0L));
                } else {
                    logger.trace("fetchReportLeagueBlockedAmountSuccessful");
                    promise.complete(handler.result().getRows().get(0).getString("BLOCKED_AMOUNT") == null ? new JsonObject().put("BLOCKED_AMOUNT", 0L) : handler.result().getRows().get(0));
                }
            
            }
        });

        return promise.future();
    }
    
    public static Future<JsonObject> fetchReportLeagueTransactionAmount(SQLConnection sqlConnection, Integer leagueId) {
        Promise<JsonObject> promise = Promise.promise();
        JsonArray params = new JsonArray();
        params.add(leagueId);
        
        sqlConnection.queryWithParams("select sum(amount) TRANSACTION_AMOUNT from topptransaction where league_id=? and APPLICATIONCODE in ('operator_charge_service', 'bill_pay_service')", params, handler -> {
            if (handler.failed()) {
            	logger.error("Unable to get accessQueryResult:", handler.cause());
                promise.fail(new DAOEXCP_Internal(-100, "خطای داخلی. با راهبر سامانه تماس بگیرید."));
            } else {
                if (null == handler.result() || null == handler.result().getRows() || handler.result().getRows().isEmpty()) {
                	logger.error("fetchReportLeagueTransactionAmountNoDataFound");
                	promise.complete(new JsonObject().put("TRANSACTION_AMOUNT", 0L));
                } else {
                    logger.trace("fetchReportLeagueTransactionAmountSuccessful");
                    promise.complete(handler.result().getRows().get(0).getString("TRANSACTION_AMOUNT") == null ? new JsonObject().put("TRANSACTION_AMOUNT", 0L) : handler.result().getRows().get(0));
                }
            
            }
        });

        return promise.future();
    }
 
    
    public static Future<JsonObject> fetchReportAllSectionOddsCountParticipantCountTotalPoint(SQLConnection sqlConnection, Integer competitionId, Integer leagueId, Integer groupId, Integer questionId) {
        Promise<JsonObject> promise = Promise.promise();
        JsonArray params = new JsonArray();
        params.add(leagueId);
        
        StringBuilder query = new StringBuilder();
        StringBuilder having = new StringBuilder();
        having.append("having o.league_id =? ");
        
        query.append("SELECT " + 
        		"    count(*) odds_count, " + 
        		"    count(distinct o.user_id) participant_count, " + 
        		"    sum(o.point) total_point " + 
        		"FROM " + 
        		"    toppodds o " + 
        		"GROUP BY " + 
        		"    o.league_id ");
        
        if (groupId != null) {
        	params.add(groupId);
        	query.append(", o.group_id ");
        	having.append("and o.group_id=? ");
		}
        
        if (competitionId != null) {
        	params.add(competitionId);
        	query.append(", o.competition_id ");
        	having.append("and o.competition_id=? ");
		}
        
        if (questionId != null) {
        	params.add(questionId);
        	query.append(", o.question_id ");
        	having.append("and o.question_id=? ");
		}
        
        query.append(having);
        
        sqlConnection.queryWithParams(query.toString(), params, handler -> {
            if (handler.failed()) {
            	logger.error("Unable to get accessQueryResult:", handler.cause());
                promise.fail(new DAOEXCP_Internal(-100, "خطای داخلی. با راهبر سامانه تماس بگیرید."));
            } else {
                if (null == handler.result() || null == handler.result().getRows() || handler.result().getRows().isEmpty()) {
                	logger.error("fetchReportAllSectionOddsCountParticipantCountTotalPointNoDataFound");
                	promise.complete(new JsonObject().put("ODDS_COUNT", 0).put("PARTICIPANT_COUNT", 0).put("TOTAL_POINT", 0));
                } else {
                    logger.trace("fetchReportAllSectionOddsCountParticipantCountTotalPointSuccessful");
                    promise.complete(handler.result().getRows().get(0));
                }
            
            }
        });

        return promise.future();
    }
 
    public static Future<JsonObject> fetchReportAllSectionCorrectOddsCountAndOddsPercentage(SQLConnection sqlConnection, Integer competitionId, Integer leagueId, Integer groupId, Integer questionId) {
        Promise<JsonObject> promise = Promise.promise();
        JsonArray params = new JsonArray();
        params.add(leagueId);
        params.add(groupId);
        params.add(competitionId);
        params.add(questionId);
        params.add(leagueId);
        params.add(groupId);
        params.add(competitionId);
        params.add(questionId);
        
        StringBuilder query = new StringBuilder();
        
        query.append(" SELECT  ");
        query.append(" t1.correct_answer_count, ");
        query.append(" (case when t2.total_answer_count <> 0 then ROUND(t1.correct_answer_count /t2.total_answer_count* 100, 2) else 0 end) correct_answer_percentage ");
        query.append(" FROM  ");
        
        query.append(" (SELECT COUNT(*) correct_answer_count ");
        query.append(" FROM toppodds o, toppcompetitionquestion cq ");
        query.append(" WHERE o.league_id     = ? ");
        query.append(" AND o.group_id = nvl(?,o.group_id) ");
        query.append(" AND o.competition_id = nvl(?,o.competition_id) ");
        query.append(" AND o.question_id = nvl(?,o.question_id) ");
        query.append(" AND cq.competition_id = o.competition_id ");
        query.append(" AND cq.question_id    = o.question_id ");
        query.append(" AND o.answer          = cq.result ");
        query.append(" ) t1, ");
        
        	
        query.append(" (SELECT COUNT(*) total_answer_count ");
        query.append(" FROM toppodds o, toppcompetitionquestion cq ");
        query.append(" WHERE o.league_id     = ? ");
        query.append(" AND o.group_id = nvl(?,o.group_id) ");
        query.append(" AND o.competition_id = nvl(?,o.competition_id) ");
        query.append(" AND o.question_id = nvl(?,o.question_id) ");
        query.append(" AND cq.competition_id = o.competition_id ");
        query.append(" AND cq.question_id    = o.question_id ");
        query.append(" ) t2 ");
        
    	logger.trace(query.toString());
        
        sqlConnection.queryWithParams(query.toString(), params, handler -> {
            if (handler.failed()) {
            	logger.error("Unable to get accessQueryResult:", handler.cause());
                promise.fail(new DAOEXCP_Internal(-100, "خطای داخلی. با راهبر سامانه تماس بگیرید."));
            } else {
                if (null == handler.result() || null == handler.result().getRows() || handler.result().getRows().isEmpty()) {
                	logger.error("fetchReportAllSectionCorrectOddsCountAndOddsPercentageNoDataFound");
                	promise.complete(null);
                } else {
                    logger.trace("fetchReportAllSectionCorrectOddsCountAndOddsPercentageSuccessful");
                    promise.complete(handler.result().getRows().get(0));
                }
            
            }
        });

        return promise.future();
    }
 
    public static Future<List<JsonObject>> fetchReportThreeSectionUsersWithMaximumPoint(SQLConnection sqlConnection, Integer competitionId, Integer leagueId, Integer groupId, Integer questionId) {
        Promise<List<JsonObject>> promise = Promise.promise();
        
        JsonArray params = new JsonArray();
        params.add(leagueId);
        params.add(groupId);
        params.add(competitionId);
        params.add(questionId);
        
        StringBuilder query = new StringBuilder();
        
        query.append("SELECT u.id user_id,u.name,u.lastname,u.nikename,u.username,total_point FROM ( " +
        		" SELECT  u.id , nvl(SUM(o.rewardpoint),0) total_point " +
        		" FROM toppodds o, toppuser u " +
        		" where o.league_id = ? AND o.group_id =? AND o.competition_id=nvl(?,o.competition_id)  AND o.question_id=nvl(?,o.question_id) and o.USER_ID = u.ID " +
        		" GROUP BY u.id " +
        		" ORDER BY total_point DESC) t, toppuser u  WHERE u.id = t.id and ROWNUM < 31 ")  ;
        
        sqlConnection.queryWithParams(query.toString(), params, handler -> {
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
    
    public static Future<List<JsonObject>> fetchReportTodayLotteryCompetition(SQLConnection sqlConnection, int leagueId, String competitionDate) {
        Promise<List<JsonObject>> promise = Promise.promise();
        
        long timer01 = new Date().getTime();
        
        JsonArray params = new JsonArray();
        params.add(leagueId);
        params.add(competitionDate);
        
        String query = ""
        		+ " select USERNAME from "
        		+ " (SELECT  DISTINCT USER_ID,COMPETITION_ID FROM TOPPODDS where COMPETITION_ID in (select c.id from TOPPCOMPETITION c where c.LEAGUE_ID = ? and trunc(c.COMPETITIONDATE) = trunc(TO_DATE(?, 'Dy Mon DD YYYY')) and dto is null)) t, toppuser u "
        		+ " where t.USER_ID=u.id " ;
        
        sqlConnection.queryWithParams(query, params, handler -> {
            if (handler.failed()) {
            	logger.error("Unable to get accessQueryResult:", handler.cause());
                promise.fail(new DAOEXCP_Internal(-100, "خطای داخلی. با راهبر سامانه تماس بگیرید."));
            } else {
                if (null == handler.result() || null == handler.result().getRows() || handler.result().getRows().isEmpty()) {
                	logger.error("fetchReportDailyLotteryCompetitionNoDataFound");
                	promise.complete(new ArrayList<>());
                } else {
                    logger.trace("fetchReportDailyLotteryCompetitionSuccessful");
                    promise.complete(handler.result().getRows());
                }
            
                logger.info("LotteryCompetition_timer01 : " + (new Date().getTime() - timer01));
                
            }
        });

        return promise.future();
    }
    
    public static Future<List<JsonObject>> fetchReportTodayLotteryQuestion1(SQLConnection sqlConnection, int leagueId, String competitionDate) {
        Promise<List<JsonObject>> promise = Promise.promise();
        
        long timer01 = new Date().getTime();
        
        JsonArray params = new JsonArray();
        params.add(leagueId);
        params.add(competitionDate);
        
        String query = ""
        		+ " select USERNAME from "
        		+ " (SELECT  DISTINCT USER_ID,COMPETITION_ID FROM TOPPODDS where COMPETITION_ID in (select c.id from TOPPCOMPETITION c where c.LEAGUE_ID = ? and trunc(c.COMPETITIONDATE) = trunc(TO_DATE(?, 'Dy Mon DD YYYY')) and dto is null ) and QUESTION_ID = 28 and TOPPODDS.CORRECTANSWER =TOPPODDS.ANSWER) t, toppuser u "
        		+ " where t.USER_ID=u.id ";
        
        sqlConnection.queryWithParams(query, params, handler -> {
            if (handler.failed()) {
            	logger.error("Unable to get accessQueryResult:", handler.cause());
                promise.fail(new DAOEXCP_Internal(-100, "خطای داخلی. با راهبر سامانه تماس بگیرید."));
            } else {
                if (null == handler.result() || null == handler.result().getRows() || handler.result().getRows().isEmpty()) {
                	logger.error("fetchReportDailyLotteryCompetitionNoDataFound");
                	promise.complete(new ArrayList<>());
                } else {
                    logger.trace("fetchReportDailyLotteryCompetitionSuccessful");
                    promise.complete(handler.result().getRows());
                }
            
                logger.info("LotteryQuestion1_timer01 : " + (new Date().getTime() - timer01));
                
            }
        });

        return promise.future();
    }
    
}
