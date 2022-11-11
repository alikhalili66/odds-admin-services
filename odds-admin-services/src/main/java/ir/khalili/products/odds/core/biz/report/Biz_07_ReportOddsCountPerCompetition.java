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

public class Biz_07_ReportOddsCountPerCompetition {

    private static final Logger logger = LogManager.getLogger(Biz_07_ReportOddsCountPerCompetition.class);

    public static void fetchReportOddsCountPerCompetition(SQLConnection sqlConnection, JsonObject message, Handler<AsyncResult<JsonObject>> resultHandler) {

        logger.trace("inputMessage:" + message);

        Integer competitionId = message.getInteger("competitionId");
        Boolean isLive = message.getBoolean("isLive", null);
        
        if (isLive != null && isLive.booleanValue()) {
			
        	DAO_Report.fetchReportOddsCountPerCompetition(sqlConnection, competitionId).onComplete(result -> {
        		if (result.failed()) {
        			logger.error("Unable to complete result: " + result.cause());
        			resultHandler.handle(Future.failedFuture(result.cause()));
        			return;
        		}
        		
        		JsonObject joReport = new JsonObject();
        		joReport.put("competitionId", competitionId);
        		joReport.putNull("leagueId");
        		joReport.put("type", ReportEnum.ODDS_COUNT_PER_COMPETITION.name());
        		joReport.put("result", result.result().toString());
        		
        		DAO_Report.saveReport(sqlConnection, joReport).onComplete(result0 -> {
        			if (result0.failed()) {
        				logger.error("Unable to complete result0: " + result0.cause());
        				resultHandler.handle(Future.failedFuture(result0.cause()));
        				return;
        			}
        			fetchReport(sqlConnection, competitionId, null, ReportEnum.ODDS_COUNT_PER_COMPETITION.name(), resultHandler);
        		});
        		
        	});
        	
		} else {
			fetchReport(sqlConnection, competitionId, null, ReportEnum.ODDS_COUNT_PER_COMPETITION.name(), resultHandler);
		}
        

    }

    private static void fetchReport(SQLConnection sqlConnection, Integer competitionId, Integer leagueId, String type, Handler<AsyncResult<JsonObject>> resultHandler) {
    	
    	DAO_Report.fetchReport(sqlConnection, competitionId, leagueId, type).onComplete(result0 -> {
            if (result0.failed()) {
            	logger.error("Unable to complete result0: " + result0.cause());
                resultHandler.handle(Future.failedFuture(result0.cause()));
                return;
            }
            
            logger.trace("FETCH_REPORT_ODDS_COUNT_PER_COMPETITION_RESULT : " + result0.result());
            
			resultHandler.handle(Future.succeededFuture(
					new JsonObject()
					.put("resultCode", 1)
					.put("resultMessage", "عملیات با موفقیت انجام شد.")
					.put("info", result0.result())
					));            
            
        });
    	
    }

    
}
