package ir.khalili.products.odds.core.biz.transaction;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import io.vertx.core.AsyncResult;
import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.sql.SQLConnection;
import ir.khalili.products.odds.core.dao.DAO_Competition;
import ir.khalili.products.odds.core.dao.DAO_Config;
import ir.khalili.products.odds.core.dao.DAO_League;
import ir.khalili.products.odds.core.dao.DAO_Transaction;
import ir.khalili.products.odds.core.dao.DAO_User;

public class Biz_04_TransactionSave {

    private static final Logger logger = LogManager.getLogger(Biz_04_TransactionSave.class);

    public static void confirm(Vertx vertx, SQLConnection sqlConnection, JsonObject message, Handler<AsyncResult<JsonObject>> resultHandler) {

        logger.trace("inputMessage:" + message);

        final String applicationCode = message.getString("applicationCode");
        final Integer amount = message.getInteger("amount");
        final String invoiceId = message.getString("invoiceId");
        final String description = message.getString("description");
        final String UGID = message.getString("userId");
        final String date = message.getString("date");
        
        DAO_Config.fetchActiveLeague(sqlConnection).onComplete(leagueHandler->{
        	
        	if (leagueHandler.failed()) {
                logger.error("Unable to complete leagueHandler: " + leagueHandler.cause());
                resultHandler.handle(Future.failedFuture(leagueHandler.cause()));
                return;
            }

        	final Integer leagueId = leagueHandler.result();
        	
        	Future<JsonObject> futPoint = DAO_Config.fetchBySymbol(sqlConnection, leagueId, "POINTS_PER_TRANSACTION");
        	Future<JsonObject> futAmount = DAO_Config.fetchBySymbol(sqlConnection, leagueId, "AMOUNT_PER_TRANSACTION");
        	Future<JsonObject> futLeague = DAO_League.fetchValidLeagueById(sqlConnection, leagueId);
        	Future<JsonObject> futUser = DAO_User.fetchUserByUGID(sqlConnection, leagueId, UGID);
        	
        	CompositeFuture.join(futPoint, futAmount, futLeague, futUser).onComplete(joinHandler01 -> {
        		
        		if (joinHandler01.failed()) {
        			logger.error("Unable to complete joinHandler01: " + joinHandler01.cause());
        			resultHandler.handle(Future.failedFuture(joinHandler01.cause()));
        			return;
        		}
        		
        		int minAmount = Integer.parseInt(futAmount.result().getString("VALUE"));
        		int point = (amount/ minAmount) * Integer.parseInt(futPoint.result().getString("VALUE"));
        		
        		Future<Integer> futTransaction = DAO_Transaction.saveTransaction(sqlConnection, point, amount, futUser.result().getInteger("ID"), applicationCode, invoiceId, description, date);
        		
        		String historyType = "A";
        		
        		Future<Void> futSaveUserPointHistory = DAO_User.saveUserPointHistory(sqlConnection, new JsonObject().put("ID", futUser.result().getInteger("ID")).put("AMOUNT", amount).put("POINT", point), historyType);
        		Future<Void> futUpdateUserPointAndAmount = DAO_Competition.updateUserPointAndAmount(sqlConnection, point, 0l, futUser.result().getInteger("ID"));
        		
        		CompositeFuture.all(futTransaction , futSaveUserPointHistory, futUpdateUserPointAndAmount).onComplete(joinHandler04 -> {
        			
        			if (joinHandler04.failed()) {
        				resultHandler.handle(Future.failedFuture(joinHandler04.cause()));
        				return;
        			}
        			logger.trace("TRANSACTION_CONFIRM_DONE");
        			resultHandler.handle(Future.succeededFuture(
        					new JsonObject()
        					.put("resultCode", 1)
        					.put("resultMessage", "عملیات با موفقیت انجام شد.")
        					));
        		});
        		
        	});
        });
        
    }

}
