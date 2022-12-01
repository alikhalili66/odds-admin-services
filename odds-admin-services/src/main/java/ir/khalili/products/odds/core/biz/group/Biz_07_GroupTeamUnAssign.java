package ir.khalili.products.odds.core.biz.group;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import io.vertx.core.AsyncResult;
import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.sql.SQLConnection;
import ir.khalili.products.odds.core.dao.DAO_Group;
import ir.khalili.products.odds.core.enums.HistoryEnum;

public class Biz_07_GroupTeamUnAssign {

    private static final Logger logger = LogManager.getLogger(Biz_07_GroupTeamUnAssign.class);

    public static void groupTeamUnAssign(SQLConnection sqlConnection, JsonObject message, Handler<AsyncResult<JsonObject>> resultHandler) {

        logger.trace("inputMessage:" + message);
        
        DAO_Group.fetchById(sqlConnection, message.getInteger("groupId")).onComplete(handler0 -> {
            if (handler0.failed()) {
            	logger.error("Unable to complete handler0: " + handler0.cause());
                resultHandler.handle(Future.failedFuture(handler0.cause()));
                return;
            }
            
            JsonObject joGroup = handler0.result();
            
            Future<Void> futUnAssignQuestion = DAO_Group.unAssignQuestion(sqlConnection, message);
			Future<Void> futSaveGroupHistory = DAO_Group.saveHistory(
					sqlConnection,
					joGroup,
					HistoryEnum.UNASSIGN.getSymbol(),
					new JsonObject().put("groupId", message.getInteger("groupId")).put("teamId", message.getInteger("teamId")).toString(),
					message.getInteger("userId"));
            
            CompositeFuture.all(futUnAssignQuestion, futSaveGroupHistory).onComplete(handler -> {
            	if (handler.failed()) {
            		logger.error("Unable to complete handler: " + handler.cause());
            		resultHandler.handle(Future.failedFuture(handler.cause()));
            		return;
            	}
            	
            	resultHandler.handle(Future.succeededFuture(
            			new JsonObject()
            			.put("resultCode", 1)
            			.put("resultMessage", "عملیات با موفقیت انجام شد.")
            			));
            	
            });
        });
    }

}
