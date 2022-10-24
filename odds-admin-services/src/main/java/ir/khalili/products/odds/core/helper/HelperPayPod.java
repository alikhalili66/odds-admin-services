package ir.khalili.products.odds.core.helper;

import static ir.khalili.products.odds.core.EntryPoint.vertx;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;
import ir.khalili.products.odds.core.EntryPoint;
import ir.khalili.products.odds.core.biz.excp.BIZEXCP_PayPod;

public class HelperPayPod {

    private static final Logger logger = LogManager.getLogger(HelperPayPod.class);

    private static final WebClient client = WebClient.create(vertx);

    private static final Integer PORT;
    private static final String HOST;

    static {
        JsonObject joLockin = EntryPoint.joConfig.getJsonObject("paypod");
        HOST = joLockin.getString("host");
        PORT = joLockin.getInteger("port");
    }

    public static Future<Void> confirmTransaction(String username, String transactionId) {

        Promise<Void> promise = Promise.promise();

        JsonObject joInput = new JsonObject();
        joInput.put("userName", username);
        joInput.put("transactionId", transactionId);

        client.post(PORT, HOST, "/megaKipaad/api/app/wallet/transaction/close").putHeader("Content-Type", "application/json").sendJson(joInput, resHandler -> {

            if (resHandler.succeeded()) {
            	JsonObject result = resHandler.result().bodyAsJsonObject();
            	logger.info("PAYPOD_CLOSE_TRANSACTION_WEB_SERVICE_RESULT:" + result);
            	
            	if (!result.getBoolean("hasError").booleanValue()) {
            		promise.complete();
				} else {
					logger.error("ERROR_MESSAGE:" + result.getString("message"));
	                promise.fail(new BIZEXCP_PayPod(-1, "خطا در برقراری ارتباط با سامانه پی پاد"));
				}
            	
            } else {
                logger.error(resHandler.cause());
                promise.fail(new BIZEXCP_PayPod(-1, "خطا در برقراری ارتباط با سامانه پی پاد"));
            }
        });

        return promise.future();
    }

    public static Future<Void> rejectTransaction(String username, String transactionId) {

        Promise<Void> promise = Promise.promise();

        JsonObject joInput = new JsonObject();
        joInput.put("userName", username);
        joInput.put("transactionId", transactionId);

        client.post(PORT, HOST, "/megaKipaad/api/app/wallet/transaction/cancel").putHeader("Content-Type", "application/json").sendJson(joInput, resHandler -> {

            if (resHandler.succeeded()) {
            	JsonObject result = resHandler.result().bodyAsJsonObject();
            	logger.info("PAYPOD_CANCEL_TRANSACTION_WEB_SERVICE_RESULT:" + result);
            	
            	if (!result.getBoolean("hasError").booleanValue()) {
            		promise.complete();
				} else {
					logger.error("ERROR_MESSAGE:" + result.getString("message"));
	                promise.fail(new BIZEXCP_PayPod(-1, "خطا در برقراری ارتباط با سامانه پی پاد"));
				}
            	
            } else {
                logger.error(resHandler.cause());
                promise.fail(new BIZEXCP_PayPod(-1, "خطا در برقراری ارتباط با سامانه پی پاد"));
            }
        });
        return promise.future();
    }
}
