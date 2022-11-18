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

public class Biz_11_ReportLeagueTransactionAmount {

    private static final Logger logger = LogManager.getLogger(Biz_11_ReportLeagueTransactionAmount.class);

    public static void fetchReportLeagueTransactionAmount(SQLConnection sqlConnection, JsonObject message, Handler<AsyncResult<JsonObject>> resultHandler) {

        logger.trace("inputMessage:" + message);

        Integer leagueId = message.getInteger("leagueId");
        
    	DAO_Report.fetchReport(sqlConnection, null, leagueId, null, null, ReportEnum.REPORT_LEAGUE_TRANSACTION_AMOUNT.name(), true).onComplete(result0 -> {
            if (result0.failed()) {
            	logger.error("Unable to complete result0: " + result0.cause());
                resultHandler.handle(Future.failedFuture(result0.cause()));
                return;
            }
            
            if (result0.result() == null) {
				
            	DAO_Report.fetchReportLeagueTransactionAmount(sqlConnection, leagueId).onComplete(result -> {
            		if (result.failed()) {
            			logger.error("Unable to complete result: " + result.cause());
            			resultHandler.handle(Future.failedFuture(result.cause()));
            			return;
            		}
            		
            		JsonObject joReport = new JsonObject();
            		joReport.putNull("competitionId");
            		joReport.put("leagueId", leagueId);
            		joReport.putNull("groupId");
            		joReport.putNull("questionId");
            		joReport.put("type", ReportEnum.REPORT_LEAGUE_TRANSACTION_AMOUNT.name());
            		joReport.put("result", result.result().toString());
            		
            		DAO_Report.saveReport(sqlConnection, joReport).onComplete(result1 -> {
            			if (result1.failed()) {
            				logger.error("Unable to complete result1: " + result1.cause());
            				resultHandler.handle(Future.failedFuture(result1.cause()));
            				return;
            			}
            			fetchReport(sqlConnection, null, leagueId, resultHandler);
            		});
            		
            	});
            	
			} else {
				
	            JsonObject joReport = new JsonObject(result0.result().getString("RESULT"));
	            
	            logger.trace("FETCH_REPORT_LEAGUE_TRANSACTION_AMOUNT_RESULT : " + result0.result());
	            
				resultHandler.handle(Future.succeededFuture(
						new JsonObject()
						.put("resultCode", 1)
						.put("resultMessage", "عملیات با موفقیت انجام شد.")
						.put("info", joReport)
						));     
			}
    	});

    }

    private static void fetchReport(SQLConnection sqlConnection, Integer competitionId, Integer leagueId, Handler<AsyncResult<JsonObject>> resultHandler) {
    	
    	DAO_Report.fetchReport(sqlConnection, null, leagueId, null, null, ReportEnum.REPORT_LEAGUE_TRANSACTION_AMOUNT.name(), false).onComplete(result0 -> {
            if (result0.failed()) {
            	logger.error("Unable to complete result0: " + result0.cause());
                resultHandler.handle(Future.failedFuture(result0.cause()));
                return;
            }
            
            JsonObject joReport = new JsonObject(result0.result().getString("RESULT"));
            
            logger.trace("FETCH_REPORT_LEAGUE_TRANSACTION_AMOUNT_RESULT : " + result0.result());
            
			resultHandler.handle(Future.succeededFuture(
					new JsonObject()
					.put("resultCode", 1)
					.put("resultMessage", "عملیات با موفقیت انجام شد.")
					.put("info", joReport)
					));            
            
        });
    	
    }

}
