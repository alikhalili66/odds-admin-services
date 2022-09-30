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
import ir.khalili.products.odds.core.utils.CalenderUtil;

public class DAO_Competition {

    private static final Logger logger = LogManager.getLogger(DAO_Competition.class);
    
    public static Future<Void> save(SQLConnection sqlConnection, JsonObject message) {

		Promise<Void> promise = Promise.promise();
		
		JsonArray params = new JsonArray();
		
		try {
			params.add(message.getInteger("leagueId"));
			params.add(message.getInteger("teamId1"));
			params.add(message.getInteger("teamId2"));
			params.add(message.getInteger("groupId"));
			params.add(CalenderUtil.toDate(message.getString("activeFrom")));
			params.add(CalenderUtil.toDate(message.getString("activeTo")));
			params.add(CalenderUtil.toDate(message.getString("oddsFrom")));
			params.add(CalenderUtil.toDate(message.getString("oddsTo")));
			params.add(CalenderUtil.toDate(message.getString("competitionDate")));
			params.add(message.getInteger("userId"));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		sqlConnection.updateWithParams(""
				+ "insert into toppcompetition("
				+ "ID,"
				+ "LEAGUE_ID,"
				+ "TEAM1_ID,"
				+ "TEAM2_ID,"
				+ "GROUP_ID,"
				+ "ACTIVEFROM,"
				+ "ACTIVETO,"
				+ "ODDSFROM,"
				+ "ODDSTO,"
				+ "COMPETITIONDATE,"
				+ "CREATIONDATE,"
				+ "createdBy_id)"
				+ "values(soppcompetition.nextval,?,?,?,?,?,?,?,?,?,sysdate,?)", params, resultHandler->{
			if(resultHandler.failed()) {
				logger.error("Unable to get accessQueryResult:", resultHandler.cause());
				promise.fail(new DAOEXCP_Internal(-100, "خطای داخلی. با راهبر سامانه تماس بگیرید."));
				return;
			}
			
			logger.trace("SaveCompetitionSuccessful");
			promise.complete();
			
		});
		
		return promise.future();
	}

    public static Future<Void> update(SQLConnection sqlConnection, JsonObject message) {

		Promise<Void> promise = Promise.promise();
		
		JsonArray params = new JsonArray();
		
		try {
			params.add(message.getInteger("leagueId"));
			params.add(message.getInteger("teamId1"));
			params.add(message.getInteger("teamId2"));
			params.add(message.getInteger("groupId"));
			params.add(CalenderUtil.toDate(message.getString("activeFrom")));
			params.add(CalenderUtil.toDate(message.getString("activeTo")));
			params.add(CalenderUtil.toDate(message.getString("oddsFrom")));
			params.add(CalenderUtil.toDate(message.getString("oddsTo")));
			params.add(CalenderUtil.toDate(message.getString("competitionDate")));
			params.add(message.getInteger("competitionId"));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		sqlConnection.updateWithParams(""
				+ "update toppcompetition c set "
				+ "c.LEAGUE_ID=?,"
				+ "c.TEAM1_ID=?,"
				+ "c.TEAM2_ID=?,"
				+ "c.GROUP_ID=?,"
				+ "c.ACTIVEFROM=?,"
				+ "c.ACTIVETO=?,"
				+ "c.ODDSFROM=?,"
				+ "c.ODDSTO=?,"
				+ "c.COMPETITIONDATE=? "
				+ " where c.id=?", params, resultHandler->{
			if(resultHandler.failed()) {
				logger.error("Unable to get accessQueryResult:", resultHandler.cause());
				promise.fail(new DAOEXCP_Internal(-100, "خطای داخلی. با راهبر سامانه تماس بگیرید."));
				return;
			}
			
			logger.trace("UpdateCompetitionSuccessful");
			promise.complete();
			
		});
		
		return promise.future();
	}
    
    public static Future<JsonObject> delete(SQLConnection sqlConnection, Integer competitionId) {
        Promise<JsonObject> promise = Promise.promise();
        JsonArray params = new JsonArray();
        params.add(competitionId);
        
        sqlConnection.updateWithParams("update toppcompetition set dto=sysdate WHERE id=?", params, handler -> {
			if(handler.failed()) {
				logger.error("Unable to get accessQueryResult:", handler.cause());
				promise.fail(new DAOEXCP_Internal(-100, "خطای داخلی. با راهبر سامانه تماس بگیرید."));
				return;
			}
			
			logger.trace("deleteCompetitionByIdSuccessful");
			promise.complete();
			
		});
		return promise.future();
    }
    
    public static Future<List<JsonObject>> fetchAll(SQLConnection sqlConnection) {
        Promise<List<JsonObject>> promise = Promise.promise();
        
        sqlConnection.query("SELECT "
        		+ "c.id,"
        		+ "c.LEAGUE_ID,"
        		+ "c.TEAM1_ID,"
        		+ "c.TEAM2_ID,"
        		+ "c.GROUP_ID,"
        		+ "c.RESULT,"
        		+ "To_Char(c.ACTIVEFROM,'yyyy/mm/dd HH:MM:SS','nls_calendar=persian') ACTIVE_FROM,"
        		+ "To_Char(c.ACTIVETO,'yyyy/mm/dd HH:MM:SS','nls_calendar=persian') ACTIVE_TO,"
        		+ "To_Char(c.ODDSFROM,'yyyy/mm/dd HH:MM:SS','nls_calendar=persian') ODDS_FROM,"
        		+ "To_Char(c.ODDSTO,'yyyy/mm/dd HH:MM:SS','nls_calendar=persian') ODDS_TO,"
        		+ "To_Char(c.COMPETITIONDATE,'yyyy/mm/dd HH:MM:SS','nls_calendar=persian') COMPETITION_DATE,"
        		+ "To_Char(c.creationdate,'yyyy/mm/dd HH:MM:SS','nls_calendar=persian') creation_date"
        		+ "  FROM toppcompetition c WHERE c.dto is null", handler -> {
            if (handler.failed()) {
                promise.fail(new DAOEXCP_Internal(-100, "خطای داخلی. با راهبر سامانه تماس بگیرید."));
            } else {
                if (null == handler.result() || null == handler.result().getRows() || handler.result().getRows().isEmpty()) {
                    promise.fail(new DAOEXCP_Internal(-100, "داده ای یافت نشد"));
                } else {
                    logger.trace("fetchAllCompetitionSuccessful");
                    promise.complete(handler.result().getRows());
                }
            
            }
        });

        return promise.future();
    }
    
    public static Future<JsonObject> fetchById(SQLConnection sqlConnection, Integer competitionId) {
        Promise<JsonObject> promise = Promise.promise();
        JsonArray params = new JsonArray();
        params.add(competitionId);
        
        sqlConnection.queryWithParams("SELECT "
        		+ "c.id,"
        		+ "c.LEAGUE_ID,"
        		+ "c.TEAM1_ID,"
        		+ "c.TEAM2_ID,"
        		+ "c.GROUP_ID,"
        		+ "c.RESULT,"
        		+ "To_Char(c.ACTIVEFROM,'yyyy/mm/dd HH:MM:SS','nls_calendar=persian') ACTIVE_FROM,"
        		+ "To_Char(c.ACTIVETO,'yyyy/mm/dd HH:MM:SS','nls_calendar=persian') ACTIVE_TO,"
        		+ "To_Char(c.ODDSFROM,'yyyy/mm/dd HH:MM:SS','nls_calendar=persian') ODDS_FROM,"
        		+ "To_Char(c.ODDSTO,'yyyy/mm/dd HH:MM:SS','nls_calendar=persian') ODDS_TO,"
        		+ "To_Char(c.COMPETITIONDATE,'yyyy/mm/dd HH:MM:SS','nls_calendar=persian') COMPETITION_DATE,"
        		+ "To_Char(c.creationdate,'yyyy/mm/dd HH:MM:SS','nls_calendar=persian') creation_date"
        		+ "  FROM toppcompetition c WHERE c.id=? and c.dto is null", params, handler -> {
            if (handler.failed()) {
                promise.fail(new DAOEXCP_Internal(-100, "خطای داخلی. با راهبر سامانه تماس بگیرید."));
            } else {
                if (null == handler.result() || null == handler.result().getRows() || handler.result().getRows().isEmpty()) {
                    promise.fail(new DAOEXCP_Internal(-100, "داده ای یافت نشد"));
                } else {
                    logger.trace("fetchAllCompetitionByIdSuccessful");
                    promise.complete(handler.result().getRows().get(0));
                }
            
            }
        });

        return promise.future();
    }
    
    public static Future<JsonObject> fetchGroup(SQLConnection sqlConnection, JsonObject message) {
        Promise<JsonObject> promise = Promise.promise();
        JsonArray params = new JsonArray();
        params.add(message.getInteger("competitionId"));
        
        sqlConnection.queryWithParams("select "
        		+ "g.id, "
        		+ "g.name, "
        		+ "To_Char(g.ACTIVEFROM,'yyyy/mm/dd HH:MM:SS','nls_calendar=persian') ACTIVE_FROM,"
        		+ "To_Char(g.ACTIVETO,'yyyy/mm/dd HH:MM:SS','nls_calendar=persian') ACTIVE_TO "
        		+ "from toppgroup g "
        		+ "where g.dto is null and g.id = (select c.group_id from toppcompetition c where c.id=? and c.dto is null)", params, handler -> {
            if (handler.failed()) {
                promise.fail(new DAOEXCP_Internal(-100, "خطای داخلی. با راهبر سامانه تماس بگیرید."));
            } else {

                if (null == handler.result() || null == handler.result().getRows() || handler.result().getRows().isEmpty()) {
                    promise.fail(new DAOEXCP_Internal(-100, "داده ای یافت نشد"));
                } else {
                    logger.trace("CompetitionFetchGroupSuccessful:: RESULT: " + handler.result().getRows().get(0));
                    promise.complete(handler.result().getRows().get(0));
                }
            
            }
        });

        return promise.future();
    }
    
    public static Future<Void> assignQuestion(SQLConnection sqlConnection, JsonObject message) {
        Promise<Void> promise = Promise.promise();
        JsonArray params = new JsonArray();
        params.add(message.getInteger("competitionId"));
        params.add(message.getInteger("questionId"));
        
		sqlConnection.updateWithParams("insert into toppcompetitionquestion (COMPETITION_ID, QUESTION_ID) values(?,?)", params, resultHandler->{
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
        params.add(message.getInteger("competitionId"));
        params.add(message.getInteger("questionId"));
        
        sqlConnection.updateWithParams("delete from toppcompetitionquestion where COMPETITION_ID=? and QUESTION_ID=?", params, handler -> {
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
    
    public static Future<List<JsonObject>> fetchQuestion(SQLConnection sqlConnection, JsonObject message) {
        Promise<List<JsonObject>> promise = Promise.promise();
        JsonArray params = new JsonArray();
        params.add(message.getInteger("competitionId"));
        
        sqlConnection.queryWithParams(""
        		+ "select "
        		+ "q.id,"
        		+ "q.league_id,"
        		+ "q.question,"
        		+ "q.type,"
        		+ "q.minpoint,"
        		+ "q.answers "
        		+ "from "
        		+ "toppquestion q, toppcompetitionquestion cq "
        		+ "where "
        		+ "cq.COMPETITION_ID=? and cq.QUESTION_ID=q.id and q.dto is null", params, handler -> {
            if (handler.failed()) {
                promise.fail(new DAOEXCP_Internal(-100, "خطای داخلی. با راهبر سامانه تماس بگیرید."));
            } else {

                if (null == handler.result() || null == handler.result().getRows() || handler.result().getRows().isEmpty()) {
                    promise.fail(new DAOEXCP_Internal(-100, "داده ای یافت نشد"));
                } else {
                    logger.trace("CompetitionfetchQuestionSuccessful");
                    promise.complete(handler.result().getRows());
                }
            
            }
        });

        return promise.future();
    }
    
}
