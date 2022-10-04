package ir.khalili.products.odds.core.dao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.sql.SQLConnection;
import ir.khalili.products.odds.core.excp.dao.DAOEXCP_Internal;

public class DAO_Competition {

    private static final Logger logger = LogManager.getLogger(DAO_Competition.class);
    
    public static Future<Void> save(SQLConnection sqlConnection, JsonObject message) {

		Promise<Void> promise = Promise.promise();
		
		JsonArray params = new JsonArray();
		
		params.add(message.getInteger("leagueId"));
		params.add(message.getInteger("teamId1"));
		params.add(message.getInteger("teamId2"));
		params.add(message.getInteger("groupId"));
		params.add(message.getString("activeFrom"));
		params.add(message.getString("activeTo"));
		params.add(message.getString("oddsFrom"));
		params.add(message.getString("oddsTo"));
		params.add(message.getString("competitionDate"));
		params.add(message.getInteger("userId"));
		
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
				+ "values("
				+ "soppcompetition.nextval,"
				+ "?,"
				+ "?,"
				+ "?,"
				+ "?,"
				+ "TO_DATE(?,'YYYY-MM-DD\"T\"HH24:MI:SS\"Z\"'),"
				+ "TO_DATE(?,'YYYY-MM-DD\"T\"HH24:MI:SS\"Z\"'),"
				+ "TO_DATE(?,'YYYY-MM-DD\"T\"HH24:MI:SS\"Z\"'),"
				+ "TO_DATE(?,'YYYY-MM-DD\"T\"HH24:MI:SS\"Z\"'),"
				+ "TO_DATE(?,'YYYY-MM-DD\"T\"HH24:MI:SS\"Z\"'),"
				+ "sysdate,"
				+ "?)", params, resultHandler->{
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
		
		params.add(message.getInteger("leagueId"));
		params.add(message.getInteger("teamId1"));
		params.add(message.getInteger("teamId2"));
		params.add(message.getInteger("groupId"));
		params.add(message.getString("activeFrom"));
		params.add(message.getString("activeTo"));
		params.add(message.getString("oddsFrom"));
		params.add(message.getString("oddsTo"));
		params.add(message.getString("competitionDate"));
		params.add(message.getInteger("competitionId"));
		
		sqlConnection.updateWithParams(""
				+ "update toppcompetition c set "
				+ "c.LEAGUE_ID=?,"
				+ "c.TEAM1_ID=?,"
				+ "c.TEAM2_ID=?,"
				+ "c.GROUP_ID=?,"
				+ "c.ACTIVEFROM=TO_DATE(?,'YYYY-MM-DD\"T\"HH24:MI:SS\"Z\"'),"
				+ "c.ACTIVETO=TO_DATE(?,'YYYY-MM-DD\"T\"HH24:MI:SS\"Z\"'),"
				+ "c.ODDSFROM=TO_DATE(?,'YYYY-MM-DD\"T\"HH24:MI:SS\"Z\"'),"
				+ "c.ODDSTO=TO_DATE(?,'YYYY-MM-DD\"T\"HH24:MI:SS\"Z\"'),"
				+ "c.COMPETITIONDATE=TO_DATE(?,'YYYY-MM-DD\"T\"HH24:MI:SS\"Z\"') "
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
        		+ "(select name from toppteam t where t.id=c.TEAM1_ID) TEAM1_name,"
        		+ "(select image from toppteam t where t.id=c.TEAM1_ID) TEAM1_image,"
        		+ "c.TEAM2_ID,"
        		+ "(select name from toppteam t where t.id=c.TEAM2_ID) TEAM2_name,"
        		+ "(select image from toppteam t where t.id=c.TEAM2_ID) TEAM2_image,"
        		+ "c.GROUP_ID,"
        		+ "c.RESULT,"
        		+ "To_Char(c.ACTIVEFROM,'YYYY-MM-DD\"T\"HH24:MI:SS\"Z\"') ACTIVE_FROM,"
        		+ "To_Char(c.ACTIVETO,'YYYY-MM-DD\"T\"HH24:MI:SS\"Z\"') ACTIVE_TO,"
        		+ "To_Char(c.ODDSFROM,'YYYY-MM-DD\"T\"HH24:MI:SS\"Z\"') ODDS_FROM,"
        		+ "To_Char(c.ODDSTO,'YYYY-MM-DD\"T\"HH24:MI:SS\"Z\"') ODDS_TO,"
        		+ "To_Char(c.COMPETITIONDATE,'YYYY-MM-DD\"T\"HH24:MI:SS\"Z\"') COMPETITION_DATE,"
        		+ "To_Char(c.creationdate,'YYYY-MM-DD\"T\"HH24:MI:SS\"Z\"') creation_date"
        		+ "  FROM toppcompetition c WHERE c.dto is null", handler -> {
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
        		+ "To_Char(c.ACTIVEFROM,'YYYY-MM-DD\"T\"HH24:MI:SS\"Z\"') ACTIVE_FROM,"
        		+ "To_Char(c.ACTIVETO,'YYYY-MM-DD\"T\"HH24:MI:SS\"Z\"') ACTIVE_TO,"
        		+ "To_Char(c.ODDSFROM,'YYYY-MM-DD\"T\"HH24:MI:SS\"Z\"') ODDS_FROM,"
        		+ "To_Char(c.ODDSTO,'YYYY-MM-DD\"T\"HH24:MI:SS\"Z\"') ODDS_TO,"
        		+ "To_Char(c.COMPETITIONDATE,'YYYY-MM-DD\"T\"HH24:MI:SS\"Z\"') COMPETITION_DATE,"
        		+ "To_Char(c.creationdate,'YYYY-MM-DD\"T\"HH24:MI:SS\"Z\"') creation_date"
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
    
    public static Future<Void> assignQuestion(SQLConnection sqlConnection, JsonObject message) {
        Promise<Void> promise = Promise.promise();
        JsonArray params = new JsonArray();
        params.add(message.getInteger("competitionId"));
        params.add(message.getInteger("questionId"));
        params.add(message.getInteger("norder"));
        
		sqlConnection.updateWithParams("insert into toppcompetitionquestion (COMPETITION_ID, QUESTION_ID, NORDER) values(?,?,?)", params, resultHandler->{
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
        		+ "q.answers, "
        		+ "cq.norder, "
        		+ "cq.result "
        		+ "from "
        		+ "toppquestion q, toppcompetitionquestion cq "
        		+ "where "
        		+ "cq.COMPETITION_ID=? and cq.QUESTION_ID=q.id and q.dto is null", params, handler -> {
            if (handler.failed()) {
                promise.fail(new DAOEXCP_Internal(-100, "خطای داخلی. با راهبر سامانه تماس بگیرید."));
            } else {

                if (null == handler.result() || null == handler.result().getRows() || handler.result().getRows().isEmpty()) {
                	promise.complete(new ArrayList<>());
                } else {
                    logger.trace("CompetitionfetchQuestionSuccessful");
                    promise.complete(handler.result().getRows());
                }
            
            }
        });

        return promise.future();
    }
    
    public static Future<List<JsonObject>> fetchCompetitionByGroupId(SQLConnection sqlConnection, int groupId) {
        Promise<List<JsonObject>> promise = Promise.promise();
        
        JsonArray params = new JsonArray();
        params.add(groupId);
        //TODO
        sqlConnection.queryWithParams("SELECT "
        		+ "c.id,"
        		+ "c.LEAGUE_ID,"
        		+ "c.TEAM1_ID,"
        		+ "(select name from toppteam t where t.id=c.TEAM1_ID) TEAM1_name,"
        		+ "(select image from toppteam t where t.id=c.TEAM1_ID) TEAM1_image,"
        		+ "c.TEAM2_ID,"
        		+ "(select name from toppteam t where t.id=c.TEAM2_ID) TEAM2_name,"
        		+ "(select image from toppteam t where t.id=c.TEAM2_ID) TEAM2_image,"
        		+ "c.GROUP_ID,"
        		+ "c.RESULT,"
        		+ "To_Char(c.ACTIVEFROM,'YYYY-MM-DD\"T\"HH24:MI:SS\"Z\"') ACTIVE_FROM,"
        		+ "To_Char(c.ACTIVETO,'YYYY-MM-DD\"T\"HH24:MI:SS\"Z\"') ACTIVE_TO,"
        		+ "To_Char(c.ODDSFROM,'YYYY-MM-DD\"T\"HH24:MI:SS\"Z\"') ODDS_FROM,"
        		+ "To_Char(c.ODDSTO,'YYYY-MM-DD\"T\"HH24:MI:SS\"Z\"') ODDS_TO,"
        		+ "To_Char(c.COMPETITIONDATE,'YYYY-MM-DD\"T\"HH24:MI:SS\"Z\"') COMPETITION_DATE,"
        		+ "To_Char(c.creationdate,'YYYY-MM-DD\"T\"HH24:MI:SS\"Z\"') creation_date"
        		+ "  FROM toppcompetition c WHERE c.GROUP_ID = ? and c.dto is null", params, handler -> {
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
    
    public static Future<Void> resultRegister(SQLConnection sqlConnection, JsonObject message) {

		Promise<Void> promise = Promise.promise();
		
		JsonArray params = new JsonArray();
		
		params.add(message.getString("result"));
		params.add(message.getInteger("competitionId"));
		
		sqlConnection.updateWithParams(""
				+ "update toppcompetition c set "
				+ "c.result=? "
				+ " where c.id=?", params, resultHandler->{
			if(resultHandler.failed()) {
				logger.error("Unable to get accessQueryResult:", resultHandler.cause());
				promise.fail(new DAOEXCP_Internal(-100, "خطای داخلی. با راهبر سامانه تماس بگیرید."));
				return;
			}
			
			logger.trace("UpdateCompetitionResultSuccessful");
			promise.complete();
			
		});
		
		return promise.future();
	}
    
	public static Future<Void> questionResultRegister(SQLConnection sqlConnection, JsonObject message) {
		
		Promise<Void> promise = Promise.promise();
		
		List<JsonArray> params = new ArrayList<>();
		
		for (Iterator<Object> iterator = message.getJsonArray("results").iterator(); iterator.hasNext();) {
			JsonObject joResult = (JsonObject) iterator.next();
			params.add(new JsonArray().add(joResult.getString("result")).add(message.getInteger("competitionId")).add(joResult.getInteger("questionId")));
		}
		
		sqlConnection.batchWithParams("update toppcompetitionquestion cq set cq.result=? where cq.COMPETITION_ID=? and cq.QUESTION_ID=?", params, resultHandler->{
			if(resultHandler.failed()) {
				logger.error("Unable to get accessQueryResult:", resultHandler.cause());
				promise.fail(new DAOEXCP_Internal(-100, "خطای داخلی. با راهبر سامانه تماس بگیرید."));
				return;
			}
			logger.trace("QuestionResultRegisterSuccessful");
			promise.complete();
			
		});
		return promise.future();
	}
	
    
}
