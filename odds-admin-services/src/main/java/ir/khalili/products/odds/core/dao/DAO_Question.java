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

public class DAO_Question {

    private static final Logger logger = LogManager.getLogger(DAO_Question.class);
    
    public static Future<Void> save(SQLConnection sqlConnection, JsonObject message) {

		Promise<Void> promise = Promise.promise();
		
		JsonArray params = new JsonArray();
		params.add(message.getInteger("leagueId"));
		params.add(message.getString("question"));
		params.add(message.getString("answers"));
		params.add(message.getString("type"));
		params.add(message.getInteger("minPoint"));
		params.add(message.getInteger("userId"));
		
		sqlConnection.updateWithParams(""
				+ "insert into toppquestion("
				+ "id,"
				+ "league_id,"
				+ "question,"
				+ "answers,"
				+ "type,"
				+ "minpoint,"
				+ "creationDate,"
				+ "createdBy_id)"
				+ "values(soppquestion.nextval,?,?,?,?,?,sysdate,?)", params, resultHandler->{
			if(resultHandler.failed()) {
				logger.error("Unable to get accessQueryResult:", resultHandler.cause());
				promise.fail(new DAOEXCP_Internal(-100, "خطای داخلی. با راهبر سامانه تماس بگیرید."));
				return;
			}
			
			logger.trace("SaveQuestionSuccessful");
			promise.complete();
			
		});
		
		return promise.future();
	}
    public static Future<Void> update(SQLConnection sqlConnection, JsonObject message) {

		Promise<Void> promise = Promise.promise();
		
		JsonArray params = new JsonArray();
		params.add(message.getInteger("leagueId"));
		params.add(message.getString("question"));
		params.add(message.getString("answers"));
		params.add(message.getString("type"));
		params.add(message.getInteger("minPoint"));
		params.add(message.getInteger("questionId"));
		
		sqlConnection.updateWithParams(""
				+ "update toppquestion q set "
				+ "q.league_id=?,"
				+ "q.question=?,"
				+ "q.answers=?,"
				+ "q.type=?,"
				+ "q.minpoint=? "
				+ " where q.id=?", params, resultHandler->{
			if(resultHandler.failed()) {
				logger.error("Unable to get accessQueryResult:", resultHandler.cause());
				promise.fail(new DAOEXCP_Internal(-100, "خطای داخلی. با راهبر سامانه تماس بگیرید."));
				return;
			}
			
			logger.trace("UpdateQuestionSuccessful");
			promise.complete();
			
		});
		
		return promise.future();
	}
    
    public static Future<JsonObject> delete(SQLConnection sqlConnection, Integer questionId) {
        Promise<JsonObject> promise = Promise.promise();
        JsonArray params = new JsonArray();
        params.add(questionId);
        
        sqlConnection.updateWithParams("update toppquestion set dto=sysdate WHERE id=?", params, handler -> {
			if(handler.failed()) {
				logger.error("Unable to get accessQueryResult:", handler.cause());
				promise.fail(new DAOEXCP_Internal(-100, "خطای داخلی. با راهبر سامانه تماس بگیرید."));
				return;
			}
			
			logger.trace("deleteQuestionByIdSuccessful");
			promise.complete();
			
		});
		return promise.future();
    }
    
    public static Future<List<JsonObject>> fetchAll(SQLConnection sqlConnection) {
        Promise<List<JsonObject>> promise = Promise.promise();
        
        sqlConnection.query("SELECT "
        		+ "q.id,"
        		+ "q.LEAGUE_ID,"
        		+ "q.QUESTION,"
        		+ "q.ANSWERS,"
        		+ "q.TYPE,"
        		+ "q.MINPOINT,"
        		+ "To_Char(q.creationdate,'yyyy/mm/dd HH:MM:SS','nls_calendar=persian') creation_date"
        		+ "  FROM toppquestion q WHERE q.dto is null", handler -> {
            if (handler.failed()) {
                promise.fail(new DAOEXCP_Internal(-100, "خطای داخلی. با راهبر سامانه تماس بگیرید."));
            } else {
                if (null == handler.result() || null == handler.result().getRows() || handler.result().getRows().isEmpty()) {
                    promise.fail(new DAOEXCP_Internal(-100, "داده ای یافت نشد"));
                } else {
                    logger.trace("fetchAllQuestionSuccessful");
                    promise.complete(handler.result().getRows());
                }
            
            }
        });

        return promise.future();
    }
        
    public static Future<JsonObject> fetchById(SQLConnection sqlConnection, Integer questionId) {
        Promise<JsonObject> promise = Promise.promise();
        JsonArray params = new JsonArray();
        params.add(questionId);
        
        sqlConnection.queryWithParams("SELECT "
        		+ "q.id,"
        		+ "q.LEAGUE_ID,"
        		+ "q.QUESTION,"
        		+ "q.ANSWERS,"
        		+ "q.TYPE,"
        		+ "q.MINPOINT,"
        		+ "To_Char(q.creationdate,'yyyy/mm/dd HH:MM:SS','nls_calendar=persian') creation_date"
        		+ "  FROM toppquestion q WHERE q.id=? and q.dto is null", params, handler -> {
            if (handler.failed()) {
                promise.fail(new DAOEXCP_Internal(-100, "خطای داخلی. با راهبر سامانه تماس بگیرید."));
            } else {
                if (null == handler.result() || null == handler.result().getRows() || handler.result().getRows().isEmpty()) {
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
