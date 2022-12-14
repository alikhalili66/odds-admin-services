package ir.khalili.products.odds.core.biz.report;

import java.util.Date;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.sql.SQLConnection;
import ir.khalili.products.odds.core.dao.DAO_Report;
import ir.khalili.products.odds.core.enums.ReportEnum;

public class Biz_09_ReportAllSectionCorrectOddsCountAndOddsPercentage {

    private static final Logger logger = LogManager.getLogger(Biz_09_ReportAllSectionCorrectOddsCountAndOddsPercentage.class);

    public static Future<JsonObject> fetchReport(SQLConnection sqlConnection, JsonObject message) {

    	Promise<JsonObject> promise = Promise.promise();
    	
        logger.trace("inputMessage:" + message);

        Integer leagueId = message.getInteger("leagueId");
        Integer groupId = message.getInteger("groupId", null);
        Integer competitionId = message.getInteger("competitionId", null);
        Integer questionId = message.getInteger("questionId", null);
        
        long timer01 = new Date().getTime();
        
    	DAO_Report.fetchReport(sqlConnection, competitionId, leagueId, groupId, questionId, ReportEnum.REPORT_ALL_SECTION_CORRECT_ODDS_COUNT_AND_ODDS_PERCENTAGE.name(), true).onComplete(result0 -> {
            if (result0.failed()) {
            	logger.error("Unable to complete result0: " + result0.cause());
                promise.fail(result0.cause());
                return;
            }
            
            if (result0.result() == null) {
				
            	DAO_Report.fetchReportAllSectionCorrectOddsCountAndOddsPercentage(sqlConnection, competitionId, leagueId, groupId, questionId).onComplete(result -> {
            		if (result.failed()) {
            			logger.error("Unable to complete result: " + result.cause());
            			promise.fail(result.cause());
            			return;
            		}
            		
            		JsonObject joReport = new JsonObject();
            		joReport.put("competitionId", competitionId);
            		joReport.put("leagueId", leagueId);
            		joReport.put("groupId", groupId);
            		joReport.put("questionId", questionId);
            		joReport.put("type", ReportEnum.REPORT_ALL_SECTION_CORRECT_ODDS_COUNT_AND_ODDS_PERCENTAGE.name());
            		joReport.put("result", result.result().toString());
            		
            		DAO_Report.saveReport(sqlConnection, joReport).onComplete(result1 -> {
            			if (result1.failed()) {
            				logger.error("Unable to complete result1: " + result1.cause());
            				promise.fail(result1.cause());
            				return;
            			}
            	    	
            			fetchReport(sqlConnection, message).onComplete(handler->{
            				if (handler.failed()) {
                				logger.error("Unable to complete handler: " + handler.cause());
                				promise.fail(handler.cause());
                				return;
                			}
            				promise.complete(handler.result());
            				
            				logger.trace("OddsCountAndOddsPercentage_timer01 : " + (new Date().getTime() - timer01));
            				
            			});
            	    	
            		});
            		
            	});
            	
			} else {
				
				JsonObject joReport = new JsonObject(result0.result().getString("RESULT"));
	            
	            logger.trace("FETCH_REPORT_ALL_SECTION_CORRECT_ODDS_COUNT_AND_ODDS_PERCENTAGE_RESULT : " + result0.result());
	            
	            promise.complete(joReport);    
	            
	            logger.trace("OddsCountAndOddsPercentage_timer01 : " + (new Date().getTime() - timer01));
			}
    	});
    	
    	return promise.future();

    }

}
