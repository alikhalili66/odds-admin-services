package ir.khalili.products.odds.core.helper;

import static ir.khalili.products.odds.core.EntryPoint.vertx;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
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
	
	private static final String refreshToken;
    private static final String rejectURL;
    private static final String confirmURL;
    private static final String checkURL;
    private static final String tokenURL;
    private static String token;
    private static Date date = new Date(new Date().getTime() + 10 * 60 * 1000);
    
    static {
        JsonObject joLockin = EntryPoint.joConfig.getJsonObject("paypod");
        
        refreshToken = joLockin.getString("refreshToken");
        rejectURL = joLockin.getString("reject");
        confirmURL = joLockin.getString("confirm");
        checkURL = joLockin.getString("check");
        tokenURL = joLockin.getString("token");

    }

    private static String getToken() {

        if(null == token ||  new Date().after(date)) {
        	
        }else {
        	return token;
        }
        
		try {

			List<NameValuePair> params = new ArrayList<NameValuePair>(2);
			params.add(new BasicNameValuePair("userName", "09104083004"));
			params.add(new BasicNameValuePair("refreshToken", refreshToken));
			
			HttpPost request = new HttpPost(tokenURL);
			request.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));

			HttpResponse response = httpClient.execute(request);
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

			StringBuffer sbResult = new StringBuffer();
			String line = "";
			while ((line = rd.readLine()) != null) {
				sbResult.append(line);
			}

			logger.trace("tokenOutput:" + sbResult.toString());
			
			JsonObject joResponse = new JsonObject(sbResult.toString());

            if (!joResponse.getBoolean("hasError") && joResponse.getJsonObject("data") != null && !joResponse.getJsonObject("data").isEmpty()) {
            	token = joResponse.getJsonObject("data").getString("access_token");
            } else {
                logger.error(joResponse);
            }
            
		} catch (Exception e) {
			e.printStackTrace();
		}
		
        return token;
    
    }
    
    public static Future<Void> confirmTransaction(String username, String transactionId) {

        Promise<Void> promise = Promise.promise();

        vertx.executeBlocking(blockingHandler -> {
			
    		try {

    			JsonObject joInput = new JsonObject();
    			joInput.put("userName", "09104083004");
    			joInput.put("transactionId", transactionId);
    			joInput.put("purchaseUserName", username);  

    			logger.trace("confirmInput:" + joInput.toString());
    			
    			HttpPost request = new HttpPost(confirmURL);
    			request.setHeader("token", getToken());
    			request.setHeader("Content-Type", "application/json");
    			request.setEntity(new StringEntity(joInput.toString(), "UTF-8"));

    			HttpResponse response = httpClient.execute(request);
    			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

    			StringBuffer sbResult = new StringBuffer();
    			String line = "";
    			while ((line = rd.readLine()) != null) {
    				sbResult.append(line);
    			}

    			logger.trace("confirmOutput:" + sbResult.toString());
    			
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
    			joInput.put("userName", "09104083004");
    			joInput.put("transactionId", transactionId);
    			joInput.put("purchaseUserName", username);  
    			
    			logger.trace("rejectInput:" + joInput);
    			
    			HttpPost request = new HttpPost(rejectURL);
    			request.setHeader("token", getToken());
    			request.setHeader("Content-Type", "application/json");
    			request.setEntity(new StringEntity(joInput.toString(), "UTF-8"));

    			HttpResponse response = httpClient.execute(request);
    			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

    			StringBuffer sbResult = new StringBuffer();
    			String line = "";
    			while ((line = rd.readLine()) != null) {
    				sbResult.append(line);
    			}

    			logger.trace("rejectOutput: " + sbResult.toString());
    			
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
    
    public static Future<String> checkTransaction(String invoiceId, String username) {

        Promise<String> promise = Promise.promise();

        vertx.executeBlocking(blockingHandler -> {
			
    		try {

    			JsonObject joInput = new JsonObject();
    			joInput.put("invoiceId", invoiceId);
    			joInput.put("userName", "09104083004");
    			joInput.put("purchaseUserName", username);  
    			
    			logger.trace("checkInput:" + joInput.toString());
    			
    			HttpPost request = new HttpPost(checkURL);
    			request.setHeader("token", getToken());
    			request.setHeader("Content-Type", "application/json");
    			request.setEntity(new StringEntity(joInput.toString(), "UTF-8"));

    			HttpResponse response = httpClient.execute(request);
    			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

    			StringBuffer sbResult = new StringBuffer();
    			String line = "";
    			while ((line = rd.readLine()) != null) {
    				sbResult.append(line);
    			}

    			logger.trace("checkOutput:" + sbResult.toString());

    			JsonObject joResponse = new JsonObject(sbResult.toString());

                if (	!joResponse.getBoolean("hasError") && 
                		joResponse.getJsonObject("data") != null && 
                		!joResponse.getJsonObject("data").isEmpty() &&
                		joResponse.getJsonObject("data").getString("state", "").equals("SUCCESS")
                		) {
                    
                    blockingHandler.complete(joResponse.getJsonObject("data").toString());
                } else {
                    logger.error(joResponse);
                    String causeMessage = joResponse.getBoolean("hasError") ? joResponse.getString("message") : "تراکنش مورد نظر در وضعیت SUCCESS نمی باشد.";
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
