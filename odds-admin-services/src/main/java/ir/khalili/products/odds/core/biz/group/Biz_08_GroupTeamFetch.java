package ir.khalili.products.odds.core.biz.group;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.sql.SQLConnection;
import ir.khalili.products.odds.core.dao.DAO_Group;

public class Biz_08_GroupTeamFetch {

    private static final Logger logger = LogManager.getLogger(Biz_08_GroupTeamFetch.class);

    public static void groupTeamFetch(Vertx vertx, SQLConnection sqlConnection, JsonObject message, Handler<AsyncResult<JsonObject>> resultHandler) {

        logger.trace("inputMessage:" + message);

        DAO_Group.fetchTeam(sqlConnection, message).onComplete(result -> {
            if (result.failed()) {
            	logger.error("Unable to complete result: " + result);
                resultHandler.handle(Future.failedFuture(result.cause()));
                return;
            }
            
//            HelperImage.getImage(vertx, result.result()).onComplete(result0 -> {
//                if (result0.failed()) {
//                	logger.error("Unable to complete result0: " + result0);
//                    resultHandler.handle(Future.failedFuture(result0.cause()));
//                    return;
//                }
                logger.trace("GROUP_TEAM_LIST : " + result.result());
                
                resultHandler.handle(Future.succeededFuture(
                		new JsonObject()
                		.put("resultCode", 1)
                		.put("resultMessage", "عملیات با موفقیت انجام شد.")
                		.put("info", result.result())
                		));
//            });

        });

    }

}
