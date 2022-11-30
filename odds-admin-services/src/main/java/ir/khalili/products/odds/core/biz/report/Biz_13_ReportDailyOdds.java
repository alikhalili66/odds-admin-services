package ir.khalili.products.odds.core.biz.report;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import io.vertx.core.AsyncResult;
import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.sql.SQLConnection;
import ir.khalili.products.odds.core.dao.DAO_Report;

public class Biz_13_ReportDailyOdds {

    private static final Logger logger = LogManager.getLogger(Biz_13_ReportDailyOdds.class);

    public static void fetchReport(SQLConnection sqlConnection, JsonObject message, Handler<AsyncResult<JsonObject>> resultHandler) {

        logger.trace("inputMessage:" + message);

        Integer leagueId = message.getInteger("leagueId");
        String competitionDate = message.getString("competitionDate");
        
        Future<JsonObject>  futRegisterUser = DAO_Report.fetchReportRegisterUserCount1(sqlConnection, leagueId, competitionDate);
        Future<JsonObject>  futOdds = DAO_Report.fetchReportOddsCount2(sqlConnection, leagueId, competitionDate);
        Future<JsonObject>  futBill = DAO_Report.fetchReportBillCount3(sqlConnection, leagueId, competitionDate);
        Future<JsonObject>  futUnique = DAO_Report.fetchReportOddsUniqueUserCount4(sqlConnection, leagueId, competitionDate);
        Future<JsonObject>  futInvest = DAO_Report.fetchReportInvestCount5(sqlConnection, leagueId, competitionDate);
        
        CompositeFuture.join(futRegisterUser, futOdds, futBill, futUnique, futInvest).onComplete(joinHandler01->{
        	
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
							.put("RegisterUser", futRegisterUser.result())
							.put("Odds", futOdds.result())
							.put("Bill", futBill.result())
							.put("Unique", futUnique.result())
							.put("Invest", futInvest.result())
							)
					));
        	 
        });
        
    }

}
