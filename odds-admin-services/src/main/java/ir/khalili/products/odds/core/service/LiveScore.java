package ir.khalili.products.odds.core.service;

import static ir.khalili.products.odds.core.EntryPoint.vertx;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;
import ir.khalili.products.odds.core.EntryPoint;
import ir.khalili.products.odds.core.biz.excp.BIZEXCP_PayPod;

public class LiveScore {

    private static final Logger logger = LogManager.getLogger(LiveScore.class);


    private static HttpClient httpClient = HttpClientBuilder.create()
            .setDefaultRequestConfig(RequestConfig.custom().setConnectTimeout(20 * 1000).build()).build();

    private static final String key;
    private static final String secret;
    private static final String scoreResultUrl;

    static {
        JsonObject joLockin = EntryPoint.joConfig.getJsonObject("liveScore");

        key = joLockin.getString("key");
        secret = joLockin.getString("secret");
        scoreResultUrl = joLockin.getString("scoreResultUrl");
    }

    public static Future<JsonObject> fetchMatchStatistics(String matchId) {

        Promise<JsonObject> promise = Promise.promise();

        vertx.executeBlocking(blockingHandler -> {

            try {
                HttpGet request = new HttpGet(String.format("%s?match_id=%s&key=%s&secret=%s", scoreResultUrl, matchId, key, secret));
                request.setHeader("Content-Type", "application/json");

                HttpResponse response = httpClient.execute(request);
                BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

                StringBuffer sbResult = new StringBuffer();
                String line = "";
                while ((line = rd.readLine()) != null) {
                    sbResult.append(line);
                }

                logger.trace(sbResult.toString());

                JsonObject joResponse = new JsonObject(sbResult.toString());

                if (joResponse.getBoolean("success") && joResponse.getJsonObject("data") != null && !joResponse.getJsonObject("data").isEmpty()) {
                    blockingHandler.complete(joResponse);
                } else {
                    logger.error(joResponse);
                    blockingHandler.fail(new BIZEXCP_PayPod(-110, " ???? ?????????? ?????????? ???????????? ?????????? ???????????? ?????????? ???? ???????? ???????? ?????? !"));
                }

            } catch (Exception e) {
                e.printStackTrace();
                blockingHandler.fail(new BIZEXCP_PayPod(-110, "?????? ???? ???????????????? ?????????? ?????????? ??????????????."));
            }

        }, resultHandler -> {
            if (resultHandler.failed()) {
                promise.fail(resultHandler.cause());
            } else {
                promise.complete((JsonObject) resultHandler.result());
            }
        });

        return promise.future();

    }

}
