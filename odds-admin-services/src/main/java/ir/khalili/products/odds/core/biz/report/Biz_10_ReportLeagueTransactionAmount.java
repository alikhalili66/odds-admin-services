package ir.khalili.products.odds.core.biz.report;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.sql.SQLConnection;
import ir.khalili.products.odds.core.dao.DAO_Report;
import ir.khalili.products.odds.core.enums.ReportEnum;

public class Biz_10_ReportLeagueTransactionAmount {

    private static final Logger logger = LogManager.getLogger(Biz_10_ReportLeagueTransactionAmount.class);

    public static Future<JsonObject> fetchReport(SQLConnection sqlConnection, JsonObject message) {

    	Promise<JsonObject> promise = Promise.promise();
    	
        logger.trace("inputMessage:" + message);

        Integer leagueId = message.getInteger("leagueId");
        
    	DAO_Report.fetchReport(sqlConnection, null, leagueId, null, null, ReportEnum.REPORT_LEAGUE_TRANSACTION_AMOUNT.name(), true).onComplete(result0 -> {
            if (result0.failed()) {
            	logger.error("Unable to complete result0: " + result0.cause());
                promise.fail(result0.cause());
                return;
            }
            
            if (result0.result() == null) {
				
            	DAO_Report.fetchReportLeagueTransactionAmount(sqlConnection, leagueId).onComplete(result -> {
            		if (result.failed()) {
            			logger.error("Unable to complete result: " + result.cause());
            			promise.fail(result.cause());
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
            			});
            	    	
            		});
            		
            	});
            	
			} else {
				
	            JsonObject joReport = new JsonObject(result0.result().getString("RESULT"));
	            
	            logger.trace("FETCH_REPORT_LEAGUE_TRANSACTION_AMOUNT_RESULT : " + result0.result());
	            
	            promise.complete(joReport);     
			}
    	});
    	
    	return promise.future();

    }

}
