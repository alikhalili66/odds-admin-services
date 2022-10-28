package ir.khalili.products.odds.core.biz.folder;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.sql.SQLConnection;
import ir.khalili.products.odds.core.dao.DAO_Folder;

public class Biz_05_FolderFetchById {

	private static final Logger logger = LogManager.getLogger(Biz_05_FolderFetchById.class);

	public static void fetchById(SQLConnection sqlConnection, JsonObject message, Handler<AsyncResult<JsonObject>> resultHandler) {
		logger.trace("inputMessage:" + message);

		final Integer folderId = message.getInteger("folderId");

		DAO_Folder.fetchById(sqlConnection, folderId).onComplete(result -> {
			if (result.failed()) {
				logger.error("Unable to complete result: " + result.cause());
				resultHandler.handle(Future.failedFuture(result.cause()));
				return;
			}

			logger.trace("FOLDER_FETCH_BY_ID_RESULT : " + result.result());

			resultHandler.handle(Future.succeededFuture(
					new JsonObject()
					.put("resultCode", 1)
					.put("resultMessage", "عملیات با موفقیت انجام شد.")
					.put("info", result.result())));

		});
	}

}
