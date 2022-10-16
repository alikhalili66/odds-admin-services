package ir.khalili.products.odds.core.biz.config;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Date;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.sql.SQLConnection;
import ir.khalili.products.odds.core.biz.excp.BIZEXCP_Invalid;
import ir.khalili.products.odds.core.dao.DAO_Config;

public class Biz_01_ConfigUpdate {

    private static final Logger logger = LogManager.getLogger(Biz_01_ConfigUpdate.class);

    public static void update(SQLConnection sqlConnection, JsonObject message, Handler<AsyncResult<JsonObject>> resultHandler) {

        logger.trace("inputMessage:" + message);

		final Integer configId = message.getInteger("configId");
		
		DAO_Config.fetchById(sqlConnection, configId).onComplete(result -> {
			if (result.failed()) {
				resultHandler.handle(Future.failedFuture(result.cause()));
				return;
			}

			logger.trace("CONFIG_FETCH_BY_ID_RESULT : " + result.result());

			if(result.result().getString("TYPE").equals("File")) {
            	
                String fileName = "/app/odds/config/" + result.result().getInteger("LEAGUE_ID") + "/" + result.result().getString("SYMBOL")+ "_" + new Date().getTime() + ".txt";

            	try {
                	try(Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName), "UTF-8"))) {
                	    out.write(message.getString("value"));
                	} 
                	message.put("value", fileName);
                } catch (Exception e) {
                    e.printStackTrace();
                    resultHandler.handle(Future.failedFuture(new BIZEXCP_Invalid("خطای در تبدیل فایل. با راهبر سامانه تماس بگیرید.")));
                    return;
                }
            	
			}
			
	        DAO_Config.update(sqlConnection, configId, message.getString("value")).onComplete(handler -> {
	            if (handler.failed()) {
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
