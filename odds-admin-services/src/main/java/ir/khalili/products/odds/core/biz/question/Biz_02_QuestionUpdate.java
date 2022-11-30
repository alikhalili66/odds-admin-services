package ir.khalili.products.odds.core.biz.question;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import io.vertx.core.AsyncResult;
import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.sql.SQLConnection;
import ir.khalili.products.odds.core.dao.DAO_Question;
import ir.khalili.products.odds.core.enums.HistoryEnum;

public class Biz_02_QuestionUpdate {

    private static final Logger logger = LogManager.getLogger(Biz_02_QuestionUpdate.class);

    public static void update(SQLConnection sqlConnection, JsonObject message, Handler<AsyncResult<JsonObject>> resultHandler) {
    	
        logger.trace("inputMessage:" + message);
        
        DAO_Question.fetchById(sqlConnection, message.getInteger("questionId")).onComplete(handler0 -> {
            if (handler0.failed()) {
            	logger.error("Unable to complete handler0: " + handler0.cause());
                resultHandler.handle(Future.failedFuture(handler0.cause()));
                return;
            }
            
            JsonObject joQuestion = handler0.result();
            
            Future<Void> futUpdateQuestion = DAO_Question.update(sqlConnection, message);
            Future<Void> futSaveQuestionHistory = DAO_Question.saveHistory(sqlConnection,joQuestion,HistoryEnum.UPDATE.getSymbol()," ", message.getInteger("userId"));
            
            CompositeFuture.all(futUpdateQuestion, futSaveQuestionHistory).onComplete(handler -> {
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
