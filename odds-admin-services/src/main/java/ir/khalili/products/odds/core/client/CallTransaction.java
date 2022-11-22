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
	private static final String host  ="127.0.0.1";
//	private static final String host  ="185.213.167.156";
	
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
			joInput.put("leagueId", 4);

			joInput.put("date", "2022-11-12T20:30:00Z");
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
			joInput.put("id", 37);
			
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
			joInput.put("applicationCode", "bill_pay_service");
			joInput.put("amount", 534000);
			joInput.put("invoiceId", "180025734");
			joInput.put("description", " پرداخت قبض تلفن ثابت با شناسه قبض : 916285521143 و شناسه پرداخت : 53411329");
			joInput.put("userId", "19b93e68-9e2e-48f3-b4ba-63d2bcdac13d");
			joInput.put("date", "2022/11/12 13:25:46");
	        
			System.out.println(joInput);
	        
			client
			.post(port, host, "/v1/service/odds/transaction/save")
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
}