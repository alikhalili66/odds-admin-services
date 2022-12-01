package ir.khalili.products.odds.core.biz.team;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import io.vertx.core.AsyncResult;
import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.sql.SQLConnection;
import ir.khalili.products.odds.core.dao.DAO_Team;
import ir.khalili.products.odds.core.enums.HistoryEnum;

public class Biz_09_TeamMemberDelete {

    private static final Logger logger = LogManager.getLogger(Biz_09_TeamMemberDelete.class);

    public static void delete(SQLConnection sqlConnection, JsonObject message, Handler<AsyncResult<JsonObject>> resultHandler) {

        logger.trace("inputMessage:" + message);

        final Integer memberId = message.getInteger("memberId");

        DAO_Team.fetchTeamMemberById(sqlConnection, memberId).onComplete(handler0 -> {
            if (handler0.failed()) {
            	logger.error("Unable to complete handler0: " + handler0.cause());
                resultHandler.handle(Future.failedFuture(handler0.cause()));
                return;
            }
            
            JsonObject joTeamMember = handler0.result();
            
            Future<Void> futDeleteTeamMember = DAO_Team.deleteMember(sqlConnection, memberId);
            Future<Void> futSaveTeamMemberHistory = DAO_Team.saveTeamMemberHistory(sqlConnection,joTeamMember,HistoryEnum.DELETE.getSymbol()," ", message.getInteger("userId"));
            
            CompositeFuture.all(futDeleteTeamMember, futSaveTeamMemberHistory).onComplete(handler -> {
            	if (handler.failed()) {
            		logger.error("Unable to complete handler: " + handler.cause());
            		resultHandler.handle(Future.failedFuture(handler.cause()));
            		return;
            	}
            	logger.trace("TEAM_MEMBER_DELETE_SUCCESSFULL.");
            	
            	resultHandler.handle(Future.succeededFuture(
            			new JsonObject()
            			.put("resultCode", 1)
            			.put("resultMessage", "عملیات با موفقیت انجام شد.")
            			));
            	
            });
        });
    }

}
