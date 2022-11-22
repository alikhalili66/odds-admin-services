package ir.khalili.products.odds.core.biz.report;

import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import io.vertx.core.AsyncResult;
import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.sql.SQLConnection;
import ir.khalili.products.odds.core.dao.DAO_Report;

public class Biz_11_ReportDailyLottery {

    private static final Logger logger = LogManager.getLogger(Biz_11_ReportDailyLottery.class);

    public static void fetchReport(SQLConnection sqlConnection, JsonObject message, Handler<AsyncResult<JsonObject>> resultHandler) {

        logger.trace("inputMessage:" + message);

        Integer leagueId = message.getInteger("leagueId");
        String competitionDate = message.getString("competitionDate");
        
        Future<List<JsonObject>>  futLotteryCompetition = DAO_Report.fetchReportTodayLotteryCompetition(sqlConnection, leagueId, competitionDate);
        Future<List<JsonObject>>  futLotteryQuestion1 = DAO_Report.fetchReportTodayLotteryQuestion1(sqlConnection, leagueId, competitionDate);
        
        CompositeFuture.join(futLotteryCompetition, futLotteryQuestion1).onComplete(joinHandler01->{
        	
        	 if (joinHandler01.failed()) {
             	logger.error("Unable to complete joinHandler01: " + joinHandler01.cause());
             	resultHandler.handle(Future.failedFuture(joinHandler01.cause()));
                 return;
             }
             
 			resultHandler.handle(Future.succeededFuture(
					new JsonObject()
					.put("resultCode", 1)
					.put("resultMessage", "عملیات با موفقیت انجام شد.")
					.put("info", new JsonObject()
							.put("competition", futLotteryCompetition.result())
							.put("question", futLotteryQuestion1.result())
							)
					));
        	 
        });
        
    }

}
