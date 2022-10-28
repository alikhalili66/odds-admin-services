package ir.khalili.products.odds.core.client;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;

/**
 * @author A.KH
 */
public class CallTransaction extends AbstractVerticle {

	private static final int port = 9090;
//	private static final String host  ="127.0.0.1";
	private static final String host  ="185.213.167.156";
	
	public static void main(String[] args) {

		System.out.println("CallTransaction STARTING ......");
		Vertx vertx = Vertx.vertx();
		vertx.deployVerticle(new CallTransaction());
	}

	@Override
	public void start() throws Exception {

		WebClient client = WebClient.create(vertx);
//		transactionFetchAll(client);
//		transactionReject(client);
//		transactionConfirm(client);
		transactionSave(client);
	}

	
	public void transactionReject(WebClient client) {
		try {
			
			JsonObject joInput = new JsonObject();
			joInput.put("id", 1);
			joInput.put("transactionId", "transactionId");
			
			client
			.post(port, host, "/v1/service/odds/transaction/reject")
			.putHeader("Authorization", CallAuth.token)
			
			.sendJson(joInput, ar -> {
				try {
					if (ar.succeeded()) {
						JsonObject response = new JsonObject(ar.result().bodyAsString());
						System.out.println(Json.encodePrettily(response));
					} else {
						System.out.println(ar.cause());
					}
				}catch(Exception e) {
					e.printStackTrace();
				} finally {

					System.exit(0);
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
	}
	
	public void transactionFetchAll(WebClient client) {
		try {
			
			JsonObject joInput = new JsonObject();
//			joInput.put("date", "2022-10-01");
//			joInput.put("userId", 23);
//			joInput.put("status", 'R');
			joInput.put("startIndex", 1);
			joInput.put("endIndex", 10);
			
			client
			.post(port, host, "/v1/service/odds/transaction/all/fetch")
			.putHeader("Authorization", CallAuth.token)
			
			.sendJson(joInput, ar -> {
				try {
					if (ar.succeeded()) {
						JsonObject response = new JsonObject(ar.result().bodyAsString());
						System.out.println(Json.encodePrettily(response));
					} else {
						System.out.println(ar.cause());
					}
				}catch(Exception e) {
					e.printStackTrace();
				} finally {

					System.exit(0);
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
	}
	
	public void transactionConfirm(WebClient client) {
		try {
			
			JsonObject joInput = new JsonObject();
			joInput.put("id", 1);
			
			client
			.post(port, host, "/v1/service/odds/transaction/confirm")
			.putHeader("Authorization", CallAuth.token)
			
			.sendJson(joInput, ar -> {
				try {
					if (ar.succeeded()) {
						JsonObject response = new JsonObject(ar.result().bodyAsString());
						System.out.println(Json.encodePrettily(response));
					} else {
						System.out.println(ar.cause());
					}
				}catch(Exception e) {
					e.printStackTrace();
				} finally {

					System.exit(0);
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
	}
	
	
	public void transactionSave(WebClient client) {
		try {
			
			JsonObject joInput = new JsonObject();
			joInput.put("applicationCode", "applicationCode");
			joInput.put("amount", 1020000);
			joInput.put("invoiceId", "invoiceId");
			joInput.put("description", "description");
			joInput.put("userId", "81b00f55-b884-4b69");
			joInput.put("date", "2022/10/28 12:12:12");
	        
			System.out.println(joInput);
	        
			client
			.post(port, host, "/v1/service/odds/transaction/save")
			.sendJson(joInput, ar -> {
				try {
					if (ar.succeeded()) {
						JsonObject response = new JsonObject(ar.result().bodyAsString());
						System.out.println(Json.encodePrettily(response));
					} else {
						System.out.println(ar.cause());
					}
				}catch(Exception e) {
					e.printStackTrace();
				} finally {

					System.exit(0);
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
	}
}