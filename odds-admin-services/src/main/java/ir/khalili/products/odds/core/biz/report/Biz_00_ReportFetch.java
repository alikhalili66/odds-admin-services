package ir.khalili.products.odds.core.biz.report;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import io.vertx.core.AsyncResult;
import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.sql.SQLConnection;

public class Biz_00_ReportFetch {

    private static final Logger logger = LogManager.getLogger(Biz_00_ReportFetch.class);

    public static void fetchReport(SQLConnection sqlConnection, JsonObject message, Handler<AsyncResult<JsonObject>> resultHandler) {

        logger.trace("inputMessage:" + message);

        Future<JsonObject> futBiz06 = Biz_06_ReportLeagueUsersWithMaximumPoint.fetchReport(sqlConnection, message);
        Future<JsonObject> futBiz07 = Biz_07_ReportLeagueBlockedAmount.fetchReport(sqlConnection, message);
        Future<JsonObject> futBiz08 = Biz_08_ReportAllSectionOddsCountParticipantCountTotalPoint.fetchReport(sqlConnection, message);
        Future<JsonObject> futBiz09 = Biz_09_ReportAllSectionCorrectOddsCountAndOddsPercentage.fetchReport(sqlConnection, message);
        Future<JsonObject> futBiz10 = Biz_10_ReportLeagueTransactionAmount.fetchReport(sqlConnection, message);
        
        CompositeFuture.join(futBiz06, futBiz07, futBiz08, futBiz09, futBiz10).onComplete(joinHandler01->{
        	
        	 if (joinHandler01.failed()) {
             	logger.error("Unable to complete joinHandler01: " + joinHandler01.cause());
                 resultHandler.handle(Future.failedFuture(joinHandler01.cause()));
                 return;
             }
        	 
        	 JsonObject joInfo = new JsonObject();
        	 joInfo.mergeIn(futBiz06.result());
        	 joInfo.mergeIn(futBiz07.result());
        	 joInfo.mergeIn(futBiz08.result());
        	 joInfo.mergeIn(futBiz09.result());
        	 joInfo.mergeIn(futBiz10.result());
        	 
          	logger.trace("joInfo" + joInfo);
          	
        	 resultHandler.handle(Future.succeededFuture(
 					new JsonObject()
 					.put("resultCode", 1)
 					.put("resultMessage", "عملیات با موفقیت انجام شد.")
 					.put("info", joInfo)
 					));
        	 
        });
        
    }

}
