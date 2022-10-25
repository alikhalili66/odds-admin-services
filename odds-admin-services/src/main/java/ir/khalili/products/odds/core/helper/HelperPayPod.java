package ir.khalili.products.odds.core.helper;

import static ir.khalili.products.odds.core.EntryPoint.vertx;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;
import ir.khalili.products.odds.core.EntryPoint;
import ir.khalili.products.odds.core.biz.excp.BIZEXCP_PayPod;

public class HelperPayPod {

    private static final Logger logger = LogManager.getLogger(HelperPayPod.class);

    
	private static HttpClient httpClient = HttpClientBuilder.create()
			.setDefaultRequestConfig(RequestConfig.custom().setConnectTimeout(20 * 1000).build()).build();
	
	private static final String token;
    private static final String REJECT;
    private static final String CONFIRM;
    private static final String CHECK;
    
    static {
        JsonObject joLockin = EntryPoint.joConfig.getJsonObject("paypod");
        
        token = joLockin.getString("token");
        REJECT = joLockin.getString("reject");
        CONFIRM = joLockin.getString("confirm");
        CHECK = joLockin.getString("check");
    }

    public static Future<Void> confirmTransaction(String invoiceId, String transactionId) {

        Promise<Void> promise = Promise.promise();

        vertx.executeBlocking(blockingHandler -> {
			
    		try {

    			JsonObject joInput = new JsonObject();
    	        joInput.put("invoiceId", invoiceId);
    	        joInput.put("transactionId", transactionId);
    				  
    			HttpPost request = new HttpPost(CONFIRM);
    			request.setHeader("token", token);
    			request.setHeader("Content-Type", "application/json");
    			request.setEntity(new StringEntity(joInput.toString(), "UTF-8"));

    			HttpResponse response = httpClient.execute(request);
    			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

    			StringBuffer sbResult = new StringBuffer();
    			String line = "";
    			while ((line = rd.readLine()) != null) {
    				sbResult.append(line);
    			}

    			System.out.println(sbResult.toString());

    			JsonObject joResponse = new JsonObject(sbResult.toString());

                if (!joResponse.getBoolean("hasError") && joResponse.getJsonObject("data") != null && !joResponse.getJsonObject("data").isEmpty()) {
                    
                    blockingHandler.complete();
                } else {
                    logger.error(joResponse);
                    String causeMessage = joResponse.getBoolean("hasError") ? joResponse.getString("message") : " در خروجی سرویس دریافت توکن مشکلی به وجود امده است !";
                    blockingHandler.fail(new BIZEXCP_PayPod(-110, causeMessage));
                }
                
    		} catch (Exception e) {
    			e.printStackTrace();
    			promise.fail(new BIZEXCP_PayPod(-110, "خطا در فراخوانی سرویس پی پاد."));
    		}
    		
		}, resultHandler->{
			if(resultHandler.failed()) {
				promise.fail(resultHandler.cause());
			}else{
				promise.complete();
			}
		});
		
        return promise.future();
    
    }

    public static Future<Void> rejectTransaction(String username, String transactionId) {

        Promise<Void> promise = Promise.promise();

        vertx.executeBlocking(blockingHandler -> {
			
    		try {

    			JsonObject joInput = new JsonObject();
    			joInput.put("userName", username);
    			joInput.put("transactionId", transactionId);
    				  
    			System.out.println(joInput);
    			
    			HttpPost request = new HttpPost(REJECT);
    			request.setHeader("token", token);
    			request.setHeader("Content-Type", "application/json");
    			request.setEntity(new StringEntity(joInput.toString(), "UTF-8"));

    			HttpResponse response = httpClient.execute(request);
    			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

    			StringBuffer sbResult = new StringBuffer();
    			String line = "";
    			while ((line = rd.readLine()) != null) {
    				sbResult.append(line);
    			}

    			System.out.println(sbResult.toString());

    			JsonObject joResponse = new JsonObject(sbResult.toString());

                if (!joResponse.getBoolean("hasError") && joResponse.getJsonObject("data") != null && !joResponse.getJsonObject("data").isEmpty()) {
                    
                    blockingHandler.complete();
                } else {
                    logger.error(joResponse);
                    String causeMessage = joResponse.getBoolean("hasError") ? joResponse.getString("message") : " در خروجی سرویس دریافت توکن مشکلی به وجود امده است !";
                    blockingHandler.fail(new BIZEXCP_PayPod(-110, causeMessage));
                }
                
    		} catch (Exception e) {
    			e.printStackTrace();
    			promise.fail(new BIZEXCP_PayPod(-110, "خطا در فراخوانی سرویس پی پاد."));
    		}
    		
		}, resultHandler->{
			if(resultHandler.failed()) {
				promise.fail(resultHandler.cause());
			}else{
				promise.complete();
			}
		});
		
        return promise.future();
    }
    
    public static Future<String> checkTransaction(String invoiceId) {

        Promise<String> promise = Promise.promise();

        vertx.executeBlocking(blockingHandler -> {
			
    		try {

    			JsonObject joInput = new JsonObject();
    			joInput.put("invoiceId", invoiceId);
    				  
    			System.out.println(joInput);
    			
    			HttpPost request = new HttpPost(CHECK);
    			request.setHeader("token", token);
    			request.setHeader("Content-Type", "application/json");
    			request.setEntity(new StringEntity(joInput.toString(), "UTF-8"));

    			HttpResponse response = httpClient.execute(request);
    			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

    			StringBuffer sbResult = new StringBuffer();
    			String line = "";
    			while ((line = rd.readLine()) != null) {
    				sbResult.append(line);
    			}

    			System.out.println(sbResult.toString());

    			JsonObject joResponse = new JsonObject(sbResult.toString());

                if (!joResponse.getBoolean("hasError") && joResponse.getJsonObject("data") != null && !joResponse.getJsonObject("data").isEmpty()) {
                    
                    blockingHandler.complete(joResponse.getJsonObject("data").toString());
                } else {
                    logger.error(joResponse);
                    String causeMessage = joResponse.getBoolean("hasError") ? joResponse.getString("message") : " در خروجی سرویس دریافت توکن مشکلی به وجود امده است !";
                    blockingHandler.fail(new BIZEXCP_PayPod(-110, causeMessage));
                }
                
    		} catch (Exception e) {
    			e.printStackTrace();
    			promise.fail(new BIZEXCP_PayPod(-110, "خطا در فراخوانی سرویس پی پاد."));
    		}
    		
		}, resultHandler->{
			if(resultHandler.failed()) {
				promise.fail(resultHandler.cause());
			}else{
				promise.complete((String) resultHandler.result());
			}
		});
		
        return promise.future();
    }
    
}
