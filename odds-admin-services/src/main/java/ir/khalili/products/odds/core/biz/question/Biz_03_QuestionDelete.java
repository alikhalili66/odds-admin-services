package ir.khalili.products.odds.core.biz.question;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.sql.SQLConnection;
import ir.khalili.products.odds.core.dao.DAO_Question;

public class Biz_03_QuestionDelete {

    private static final Logger logger = LogManager.getLogger(Biz_03_QuestionDelete.class);

    public static void delete(SQLConnection sqlConnection, JsonObject message, Handler<AsyncResult<JsonObject>> resultHandler) {

        logger.trace("inputMessage:" + message);

        final Integer questionId = message.getInteger("questionId");

        DAO_Question.delete(sqlConnection, questionId).onComplete(result -> {
            if (result.failed()) {
            	logger.error("Unable to complete result: " + result.cause());
                resultHandler.handle(Future.failedFuture(result.cause()));
                return;
            }
            
            logger.trace("QUESTION_DELETE_SUCCESSFULL.");
            
			resultHandler.handle(Future.succeededFuture(
					new JsonObject()
					.put("resultCode", 1)
					.put("resultMessage", "عملیات با موفقیت انجام شد.")
					));

        });

    }

}
