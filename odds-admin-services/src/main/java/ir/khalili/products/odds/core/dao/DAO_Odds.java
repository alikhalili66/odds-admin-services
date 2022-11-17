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

public class DAO_Odds {

	private static final Logger logger = LogManager.getLogger(DAO_Odds.class);
	
    public static Future<List<JsonObject>> fetchOddsReport(SQLConnection sqlConnection, int competitionId) {
        Promise<List<JsonObject>> promise = Promise.promise();
        
        JsonArray params = new JsonArray();
        params.add(competitionId);

        sqlConnection.queryWithParams("SELECT QUESTION_ID, ANSWER, count(1) as count FROM tOPPOdds where competition_Id = ? group by QUESTION_ID, ANSWER  order by QUESTION_ID", params, handler -> {
            if (handler.failed()) {
            	logger.error("Unable to get accessQueryResult:", handler.cause());
                promise.fail(new DAOEXCP_Internal(-100, "خطای داخلی. با راهبر سامانه تماس بگیرید."));
            } else {
                if (null == handler.result() || null == handler.result().getRows() || handler.result().getRows().isEmpty()) {
                	logger.error("fetchOddsReportNoDataFound");
                    promise.complete(new ArrayList<>());
                } else {
                    logger.trace("fetchAllCompetitionSuccessful");
                    promise.complete(handler.result().getRows());
                }
            
            }
        });

        return promise.future();
    }
    
    public static Future<List<JsonObject>> fetchOddsCount(SQLConnection sqlConnection, int competitionId) {
        Promise<List<JsonObject>> promise = Promise.promise();
        
        JsonArray params = new JsonArray();
        params.add(competitionId);

        sqlConnection.queryWithParams("SELECT QUESTION_ID, count(1) as count FROM tOPPOdds where competition_Id = ? group by QUESTION_ID  order by QUESTION_ID", params, handler -> {
            if (handler.failed()) {
            	logger.error("Unable to get accessQueryResult:", handler.cause());
                promise.fail(new DAOEXCP_Internal(-100, "خطای داخلی. با راهبر سامانه تماس بگیرید."));
            } else {
                if (null == handler.result() || null == handler.result().getRows() || handler.result().getRows().isEmpty()) {
                	logger.error("fetchOddsCountNoDataFound");
                    promise.complete(new ArrayList<>());
                } else {
                    logger.trace("fetchAllCompetitionSuccessful");
                    promise.complete(handler.result().getRows());
                }
            
            }
        });

        return promise.future();
    }
    
    public static Future<Void> updateCorrectAnswers(SQLConnection sqlConnection, Integer competitionId) {

		Promise<Void> promise = Promise.promise();
		
		JsonArray params = new JsonArray();
        params.add(competitionId);
		
		sqlConnection.updateWithParams(""
				+ "update toppodds o set o.CORRECTANSWER  = (SELECT cq.RESULT FROM TOPPCOMPETITIONQUESTION cq where cq.COMPETITION_ID = o.COMPETITION_ID and o.QUESTION_ID = cq.QUESTION_ID) where o.COMPETITION_ID = ?"
				+ "", params, resultHandler->{
			if(resultHandler.failed()) {
				logger.error("Unable to get accessQueryResult:", resultHandler.cause());
				promise.fail(new DAOEXCP_Internal(-100, "خطای داخلی. با راهبر سامانه تماس بگیرید."));
				return;
			}
			
			logger.trace("updateCorrectAnswersSuccessful");
			promise.complete();
			
		});
		
		return promise.future();
	}
    
}
