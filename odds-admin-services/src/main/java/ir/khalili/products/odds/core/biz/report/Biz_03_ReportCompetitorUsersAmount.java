package ir.khalili.products.odds.core.biz.report;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.sql.SQLConnection;
import ir.khalili.products.odds.core.dao.DAO_Report;

public class Biz_03_ReportCompetitorUsersAmount {

    private static final Logger logger = LogManager.getLogger(Biz_03_ReportCompetitorUsersAmount.class);

    public static void fetchCompetitorUsersAmount(SQLConnection sqlConnection, JsonObject message, Handler<AsyncResult<JsonObject>> resultHandler) {

        logger.trace("inputMessage:" + message);

        DAO_Report.fetchCompetitorUsersAmount(sqlConnection).onComplete(result -> {
            if (result.failed()) {
                resultHandler.handle(Future.failedFuture(result.cause()));
                return;
            }
            
            logger.trace("FETCH_COMPETITOR_USERS_AMOUNT_RESULT : " + result.result());
            
			resultHandler.handle(Future.succeededFuture(
					new JsonObject()
					.put("resultCode", 1)
					.put("resultMessage", "عملیات با موفقیت انجام شد.")
					.put("info", new JsonObject().put("TOTAL_AMOUNT", result.result()))
					));

        });

    }

}
