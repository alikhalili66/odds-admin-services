package ir.khalili.products.odds.core.dao;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
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

public class DAO_Competition {

    private static final Logger logger = LogManager.getLogger(DAO_Competition.class);
    private static final DateFormat formatter = new SimpleDateFormat("YYYYMMDD");
    
    public static Future<Boolean> checkCustomerValidTo(SQLConnection sqlConnection, Long customerId, Long serviceId) {
        Promise<Boolean> promise = Promise.promise();
        return promise.future();
    }
    
    public static Future<Void> save(SQLConnection sqlConnection, JsonObject message) {

		Promise<Void> promise = Promise.promise();
		
		JsonArray params = new JsonArray();
		
		try {
			params.add(message.getInteger("leagueId"));
			params.add(message.getInteger("teamId1"));
			params.add(message.getInteger("teamId2"));
			params.add(message.getInteger("groupId"));
			params.add((Date)formatter.parse(message.getString("activeFrom")));
			params.add((Date)formatter.parse(message.getString("activeTo")));
			params.add((Date)formatter.parse(message.getString("activeTo")));
			params.add((Date)formatter.parse(message.getString("activeTo")));
			
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
				+ "creationDate,"
				+ "createdBy_id)"
				+ "values(soppcompetition.nextval,?,?,?,?,?,?,?,?,sysdate,?)", params, resultHandler->{
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

    public static Future<JsonObject> update(SQLConnection sqlConnection, Long customerId) {
        Promise<JsonObject> promise = Promise.promise();
        JsonArray params = new JsonArray();
        params.add(customerId);
        sqlConnection.queryWithParams("SELECT * FROM tnascustomer WHERE id = ? and dto is null", params, handler -> {
            if (handler.failed()) {
                promise.fail(new DAOEXCP_Internal(-100, "خطای داخلی. با راهبر سامانه تماس بگیرید."));
            } else {

                if (null == handler.result() || null == handler.result().getRows() || handler.result().getRows().isEmpty()) {
                    promise.fail(new DAOEXCP_Internal(-100, "مشتری مورد نظر موجود نمی باشد."));
                } else {
                    logger.trace("updateCustomerLocationInfoSuccessful");
                    promise.complete(handler.result().getRows().get(0));
                }
            
            }
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
    
    public static Future<JsonObject> fetchGroup(SQLConnection sqlConnection, Long customerId) {
        Promise<JsonObject> promise = Promise.promise();
        JsonArray params = new JsonArray();
        params.add(customerId);
        sqlConnection.queryWithParams("SELECT * FROM tnascustomer WHERE id = ? and dto is null", params, handler -> {
            if (handler.failed()) {
                promise.fail(new DAOEXCP_Internal(-100, "خطای داخلی. با راهبر سامانه تماس بگیرید."));
            } else {

                if (null == handler.result() || null == handler.result().getRows() || handler.result().getRows().isEmpty()) {
                    promise.fail(new DAOEXCP_Internal(-100, "مشتری مورد نظر موجود نمی باشد."));
                } else {
                    logger.trace("updateCustomerLocationInfoSuccessful");
                    promise.complete(handler.result().getRows().get(0));
                }
            
            }
        });

        return promise.future();
    }
    
    public static Future<JsonObject> assignQuestion(SQLConnection sqlConnection, Long customerId) {
        Promise<JsonObject> promise = Promise.promise();
        JsonArray params = new JsonArray();
        params.add(customerId);
        sqlConnection.queryWithParams("SELECT * FROM tnascustomer WHERE id = ? and dto is null", params, handler -> {
            if (handler.failed()) {
                promise.fail(new DAOEXCP_Internal(-100, "خطای داخلی. با راهبر سامانه تماس بگیرید."));
            } else {

                if (null == handler.result() || null == handler.result().getRows() || handler.result().getRows().isEmpty()) {
                    promise.fail(new DAOEXCP_Internal(-100, "مشتری مورد نظر موجود نمی باشد."));
                } else {
                    logger.trace("updateCustomerLocationInfoSuccessful");
                    promise.complete(handler.result().getRows().get(0));
                }
            
            }
        });

        return promise.future();
    }
    
    public static Future<JsonObject> unAssignQuestion(SQLConnection sqlConnection, Long customerId) {
        Promise<JsonObject> promise = Promise.promise();
        JsonArray params = new JsonArray();
        params.add(customerId);
        sqlConnection.queryWithParams("SELECT * FROM tnascustomer WHERE id = ? and dto is null", params, handler -> {
            if (handler.failed()) {
                promise.fail(new DAOEXCP_Internal(-100, "خطای داخلی. با راهبر سامانه تماس بگیرید."));
            } else {

                if (null == handler.result() || null == handler.result().getRows() || handler.result().getRows().isEmpty()) {
                    promise.fail(new DAOEXCP_Internal(-100, "مشتری مورد نظر موجود نمی باشد."));
                } else {
                    logger.trace("updateCustomerLocationInfoSuccessful");
                    promise.complete(handler.result().getRows().get(0));
                }
            
            }
        });

        return promise.future();
    }
    
    public static Future<List<JsonObject>> fetchQuestion(SQLConnection sqlConnection, JsonObject message) {
        Promise<List<JsonObject>> promise = Promise.promise();
        JsonArray params = new JsonArray();
        params.add(message.getInteger("competitionId"));
        
        sqlConnection.queryWithParams(""
        		+ "select "
        		+ "cq.QUESTION_ID, "
        		+ "q.question, "
        		+ "cq.RESULT, "
        		+ "q.answers "
        		+ "from "
        		+ "toppquestion q, toppcompetitionquestion cq "
        		+ "where "
        		+ "cq.COMPETITION_ID=? and q.dto is null", params, handler -> {
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
