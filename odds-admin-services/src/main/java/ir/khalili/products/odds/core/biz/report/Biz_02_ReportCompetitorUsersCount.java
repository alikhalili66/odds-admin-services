package ir.khalili.products.odds.core.biz.report;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.sql.SQLConnection;
import ir.khalili.products.odds.core.dao.DAO_Report;

public class Biz_02_ReportCompetitorUsersCount {

    private static final Logger logger = LogManager.getLogger(Biz_02_ReportCompetitorUsersCount.class);

    public static void fetchCompetitorUsersCount(SQLConnection sqlConnection, JsonObject message, Handler<AsyncResult<JsonObject>> resultHandler) {

        logger.trace("inputMessage:" + message);

        final int leagueId = message.getInteger("leagueId");
        
        DAO_Report.fetchCompetitorUsersCount(sqlConnection, leagueId).onComplete(result -> {
            if (result.failed()) {
                resultHandler.handle(Future.failedFuture(result.cause()));
                return;
            }
            
            logger.trace("COMPETITOR_USERS_COUNT_RESULT : " + result.result());
            
			resultHandler.handle(Future.succeededFuture(
					new JsonObject()
					.put("resultCode", 1)
					.put("resultMessage", "عملیات با موفقیت انجام شد.")
					.put("info", new JsonObject().put("COMPETITOR_COUNT", result.result()))
					));

        });

    }

}
