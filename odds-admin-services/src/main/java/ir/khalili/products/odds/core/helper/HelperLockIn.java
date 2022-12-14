package ir.khalili.products.odds.core.helper;

import static ir.khalili.products.odds.core.EntryPoint.vertx;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;
import ir.khalili.products.odds.core.EntryPoint;
import ir.khalili.products.odds.core.biz.excp.BIZEXCP_Group;
import ir.khalili.products.odds.core.enums.AccessLockIn;
import ir.khalili.products.odds.core.excp.validation.EXCP_RtMgr_Validation;

public class HelperLockIn {

    private static final Logger logger = LogManager.getLogger(HelperLockIn.class);

    private static final WebClient client = WebClient.create(vertx);

    private static final String HOST;
    private static final Integer PORT;
    private static final String API;

    static {
        JsonObject joLockin = EntryPoint.joConfig.getJsonObject("lockin");

        HOST = joLockin.getString("host");
        PORT = joLockin.getInteger("port");
        API = joLockin.getString("APIKEY");
    }

    public static Future<JsonObject> doLogin(String username, String password) {

        Promise<JsonObject> promise = Promise.promise();

        JsonObject joLoginUser = new JsonObject();
        joLoginUser.put("username", username);
        joLoginUser.put("password", password);

        client.post(PORT, HOST, "/v1/service/odds/user/login").putHeader("API-KEY", API).sendJson(joLoginUser, resHandler -> {

            if (resHandler.succeeded()) {
                logger.info(resHandler.result().bodyAsJsonObject());
                JsonObject result = resHandler.result().bodyAsJsonObject();
                int resultCode = result.getInteger("resultCode");
                if (resultCode == 1) {
                    promise.complete(result.getJsonObject("info"));
                } else {
                    logger.error(resHandler.result().bodyAsJsonObject());
                    promise.fail(new BIZEXCP_Group(-1, resHandler.result().bodyAsJsonObject().getString("resultMessage")));
                }
            } else {
                logger.error(resHandler.cause());
                promise.fail(new BIZEXCP_Group(-1, "?????? ???? ?????????????? ???????????? ???? ???????????? ?????????? ????????"));
            }

        });

        return promise.future();
    }

    public static Future<JsonObject> checkOTP(long cellphone, int code) {

        Promise<JsonObject> promise = Promise.promise();

        JsonObject sendOTP = new JsonObject();
        sendOTP.put("cellphone", cellphone);
        sendOTP.put("code", code);

        client.post(PORT, HOST, "/v1/service/odds/user/check/otp").putHeader("API-KEY", API).sendJson(sendOTP, resHandler -> {

            if (resHandler.succeeded()) {
                logger.info(resHandler.result().bodyAsJsonObject());
                JsonObject result = resHandler.result().bodyAsJsonObject();
                int resultCode = result.getInteger("resultCode");
                if (resultCode == 1) {
                    promise.complete(result.getJsonObject("info"));
                } else {
                    logger.error(resHandler.result().bodyAsJsonObject());
                    promise.fail(new BIZEXCP_Group(-1, resHandler.result().bodyAsJsonObject().getString("resultMessage")));
                }
            } else {
                logger.error(resHandler.cause());
                promise.fail(resHandler.cause());
            }

        });

        return promise.future();

    }

    public static Future<JsonObject> checkToken(String token) {

        logger.trace(token);

        Promise<JsonObject> promise = Promise.promise();

        client
                .post(PORT, HOST, "/v1/service/odds/user/check/token")
                .putHeader("API-KEY", API)
                .putHeader("Authorization", token)
                .send(resHandler -> {

                    if (resHandler.succeeded()) {
                        logger.info(resHandler.result().bodyAsJsonObject());
                        JsonObject result = resHandler.result().bodyAsJsonObject();
                        int resultCode = result.getInteger("resultCode");
                        if (resultCode == 1) {
                            promise.complete(result.getJsonObject("info"));
                        } else {
                            logger.error(resHandler.result().bodyAsJsonObject());
                            promise.fail(new EXCP_RtMgr_Validation(-1, resHandler.result().bodyAsJsonObject().getString("resultMessage")));
                        }
                    } else {
                        logger.error(resHandler.cause());
                        promise.fail(resHandler.cause());
                    }

                });

        return promise.future();

    }

    public static Future<Void> checkAcccess(String token, AccessLockIn access) {

        logger.trace(token);


        Promise<Void> promise = Promise.promise();

        String url = String.format("/v1/service/odds/user/check/access?access=%s", access.name());

        client
                .get(PORT, HOST, url)
                .putHeader("API-KEY", API)
                .putHeader("Authorization", token)
                .send()
                .onComplete(getHandler -> {

                    if (getHandler.failed()) {
                        logger.error("Unable to complete handle: " + getHandler.cause());
                        promise.fail(new EXCP_RtMgr_Validation(-1, "???????????? ???? ?????? ???????????? ???????? ?????? ?????????? ???????? ??????."));
                        return;
                    }

                    JsonObject getResult = getHandler.result().bodyAsJsonObject();
                    logger.info(getResult);

                    if (getResult.getInteger("resultCode") == 1) {
                        if (getResult.getBoolean("info")) {
                            promise.complete();
                        } else {
                            promise.fail(new EXCP_RtMgr_Validation(-1, "???????????? ???? ?????? ???????????? ???????? ?????? ?????????? ???????? ??????."));
                        }
                    } else if (getResult.getInteger("resultCode") == -3) {
                        promise.fail(new EXCP_RtMgr_Validation(-1, "???????? ?????????? ?????? ????????."));
                    } else {
                        promise.fail(new EXCP_RtMgr_Validation(-1, "???????????? ???? ?????? ???????????? ???????? ?????? ?????????? ???????? ??????."));
                    }
                });

        return promise.future();
    }

}
