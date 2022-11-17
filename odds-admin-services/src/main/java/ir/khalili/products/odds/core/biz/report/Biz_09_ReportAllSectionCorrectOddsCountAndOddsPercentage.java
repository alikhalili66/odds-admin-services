package ir.khalili.products.odds.core.biz.report;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.sql.SQLConnection;
import ir.khalili.products.odds.core.dao.DAO_Report;
import ir.khalili.products.odds.core.enums.ReportEnum;

public class Biz_09_ReportAllSectionCorrectOddsCountAndOddsPercentage {

    private static final Logger logger = LogManager.getLogger(Biz_09_ReportAllSectionCorrectOddsCountAndOddsPercentage.class);

    public static void fetchReportAllSectionCorrectOddsCountAndOddsPercentage(SQLConnection sqlConnection, JsonObject message, Handler<AsyncResult<JsonObject>> resultHandler) {

        logger.trace("inputMessage:" + message);

        Integer leagueId = message.getInteger("leagueId");
        Integer groupId = message.getInteger("groupId", null);
        Integer competitionId = message.getInteger("competitionId", null);
        Integer questionId = message.getInteger("questionId", null);
        
    	DAO_Report.fetchReport(sqlConnection, competitionId, leagueId, groupId, questionId, ReportEnum.REPORT_ALL_SECTION_CORRECT_ODDS_COUNT_AND_ODDS_PERCENTAGE.name(), true).onComplete(result0 -> {
            if (result0.failed()) {
            	logger.error("Unable to complete result0: " + result0.cause());
                resultHandler.handle(Future.failedFuture(result0.cause()));
                return;
            }
            
            if (result0.result() == null) {
				
            	DAO_Report.fetchReportAllSectionCorrectOddsCountAndOddsPercentage(sqlConnection, competitionId, leagueId, groupId, questionId).onComplete(result -> {
            		if (result.failed()) {
            			logger.error("Unable to complete result: " + result.cause());
            			resultHandler.handle(Future.failedFuture(result.cause()));
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
            				resultHandler.handle(Future.failedFuture(result1.cause()));
            				return;
            			}
            			fetchReport(sqlConnection, competitionId, leagueId, groupId, questionId, resultHandler);
            		});
            		
            	});
            	
			} else {
				
				JsonObject joReport = new JsonObject(result0.result().getString("RESULT"));
	            
	            logger.trace("FETCH_REPORT_ALL_SECTION_CORRECT_ODDS_COUNT_AND_ODDS_PERCENTAGE_RESULT : " + result0.result());
	            
				resultHandler.handle(Future.succeededFuture(
						new JsonObject()
						.put("resultCode", 1)
						.put("resultMessage", "عملیات با موفقیت انجام شد.")
						.put("info", joReport)
						));     
			}
    	});

    }

    private static void fetchReport(SQLConnection sqlConnection, Integer competitionId, Integer leagueId, Integer groupId, Integer questionId, Handler<AsyncResult<JsonObject>> resultHandler) {
    	
    	DAO_Report.fetchReport(sqlConnection, competitionId, leagueId, groupId, questionId, ReportEnum.REPORT_ALL_SECTION_CORRECT_ODDS_COUNT_AND_ODDS_PERCENTAGE.name(), false).onComplete(result0 -> {
            if (result0.failed()) {
            	logger.error("Unable to complete result0: " + result0.cause());
                resultHandler.handle(Future.failedFuture(result0.cause()));
                return;
            }
            
            JsonObject joReport = new JsonObject(result0.result().getString("RESULT"));
            
            logger.trace("FETCH_REPORT_ALL_SECTION_CORRECT_ODDS_COUNT_AND_ODDS_PERCENTAGE_RESULT : " + result0.result());
            
			resultHandler.handle(Future.succeededFuture(
					new JsonObject()
					.put("resultCode", 1)
					.put("resultMessage", "عملیات با موفقیت انجام شد.")
					.put("info", joReport)
					));            
            
        });
    	
    }

}
