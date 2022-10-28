package ir.khalili.products.odds.core.biz.team;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.sql.SQLConnection;
import ir.khalili.products.odds.core.dao.DAO_Team;

public class Biz_09_TeamMemberDelete {

    private static final Logger logger = LogManager.getLogger(Biz_09_TeamMemberDelete.class);

    public static void delete(SQLConnection sqlConnection, JsonObject message, Handler<AsyncResult<JsonObject>> resultHandler) {

        logger.trace("inputMessage:" + message);

        final Integer memberId = message.getInteger("memberId");

        DAO_Team.deleteMember(sqlConnection, memberId).onComplete(result -> {
            if (result.failed()) {
            	logger.error("Unable to complete result: " + result.cause());
                resultHandler.handle(Future.failedFuture(result.cause()));
                return;
            }
            
            logger.trace("TEAM_MEMBER_DELETE_SUCCESSFULL.");
            
			resultHandler.handle(Future.succeededFuture(
					new JsonObject()
					.put("resultCode", 1)
					.put("resultMessage", "عملیات با موفقیت انجام شد.")
					));

        });

    }

}
