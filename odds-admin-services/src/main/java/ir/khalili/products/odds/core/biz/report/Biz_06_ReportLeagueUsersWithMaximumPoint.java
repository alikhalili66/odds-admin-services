package ir.khalili.products.odds.core.biz.report;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.sql.SQLConnection;
import ir.khalili.products.odds.core.dao.DAO_Report;
import ir.khalili.products.odds.core.enums.ReportEnum;

public class Biz_06_ReportLeagueUsersWithMaximumPoint {

    private static final Logger logger = LogManager.getLogger(Biz_06_ReportLeagueUsersWithMaximumPoint.class);

    public static void fetchReport(SQLConnection sqlConnection, JsonObject message, Handler<AsyncResult<JsonObject>> resultHandler) {

        logger.trace("inputMessage:" + message);

        Integer groupId = message.getInteger("groupId");
        
        if(null == groupId) {
        	fetchReportUsersWithMaximumPoint(sqlConnection, message, resultHandler);
        }else {
        	fetchReportThreeSectionUsersWithMaximumPoint(sqlConnection, message, resultHandler);
        }
        
    }
    
    private static void fetchReportUsersWithMaximumPoint(SQLConnection sqlConnection, JsonObject message, Handler<AsyncResult<JsonObject>> resultHandler) {

        logger.trace("inputMessage:" + message);

        Integer leagueId = message.getInteger("leagueId");
        
    	DAO_Report.fetchReport(sqlConnection, null, leagueId, null, null, ReportEnum.REPORT_LEAGUE_USERS_WITH_MAXIMUM_POINT.name(), true).onComplete(result0 -> {
            if (result0.failed()) {
            	logger.error("Unable to complete result0: " + result0.cause());
                resultHandler.handle(Future.failedFuture(result0.cause()));
                return;
            }
            
            if (result0.result() == null) {
				
            	DAO_Report.fetchReportLeagueUsersWithMaximumPoint(sqlConnection, leagueId).onComplete(result -> {
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
            		joReport.put("type", ReportEnum.REPORT_LEAGUE_USERS_WITH_MAXIMUM_POINT.name());
            		joReport.put("result", result.result().toString());
            		
            		DAO_Report.saveReport(sqlConnection, joReport).onComplete(result1 -> {
            			if (result1.failed()) {
            				logger.error("Unable to complete result1: " + result1.cause());
            				resultHandler.handle(Future.failedFuture(result1.cause()));
            				return;
            			}
            	    	
            			fetchReport(sqlConnection, message, resultHandler);
            			
            		});
            		
            	});
            	
			} else {
				
	            JsonObject joReport = result0.result();
	            JsonArray jaResult = new JsonArray(joReport.getString("RESULT"));
	            joReport.put("RESULT", jaResult);
	            
	            logger.trace("FETCH_REPORT_USERS_WITH_MAXIMUM_POINT_RESULT : " + result0.result());
	            
				resultHandler.handle(Future.succeededFuture(
						new JsonObject()
						.put("resultCode", 1)
						.put("resultMessage", "عملیات با موفقیت انجام شد.")
						.put("info", joReport)
						));     
			}
    	});

    }

    private static void fetchReportThreeSectionUsersWithMaximumPoint(SQLConnection sqlConnection, JsonObject message, Handler<AsyncResult<JsonObject>> resultHandler) {

        logger.trace("inputMessage:" + message);

        Integer leagueId = message.getInteger("leagueId");
        Integer groupId = message.getInteger("groupId");
        Integer competitionId = message.getInteger("competitionId", null);
        Integer questionId = message.getInteger("questionId", null);
        
    	DAO_Report.fetchReport(sqlConnection, competitionId, leagueId, groupId, questionId, ReportEnum.REPORT_THREE_SECTION_USERS_WITH_MAXIMUM_POINT.name(), true).onComplete(result0 -> {
            if (result0.failed()) {
            	logger.error("Unable to complete result0: " + result0.cause());
                resultHandler.handle(Future.failedFuture(result0.cause()));
                return;
            }
            
            if (result0.result() == null) {
				
            	DAO_Report.fetchReportThreeSectionUsersWithMaximumPoint(sqlConnection, competitionId, leagueId, groupId, questionId).onComplete(result -> {
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
            		joReport.put("type", ReportEnum.REPORT_THREE_SECTION_USERS_WITH_MAXIMUM_POINT.name());
            		joReport.put("result", result.result().toString());
            		
            		DAO_Report.saveReport(sqlConnection, joReport).onComplete(result1 -> {
            			if (result1.failed()) {
            				logger.error("Unable to complete result1: " + result1.cause());
            				resultHandler.handle(Future.failedFuture(result1.cause()));
            				return;
            			}

            			fetchReport(sqlConnection, message, resultHandler);
            	    	
            		});
            		
            	});
            	
			} else {
				
	            JsonObject joReport = result0.result();
	            JsonArray jaResult = new JsonArray(joReport.getString("RESULT"));
	            joReport.put("RESULT", jaResult);
	            
	            logger.trace("FETCH_REPORT_THREE_SECTION_USERS_WITH_MAXIMUM_POINT_RESULT : " + result0.result());
	            
				resultHandler.handle(Future.succeededFuture(
						new JsonObject()
						.put("resultCode", 1)
						.put("resultMessage", "عملیات با موفقیت انجام شد.")
						.put("info", joReport)
						));     
			}
    	});

    }
    
}
