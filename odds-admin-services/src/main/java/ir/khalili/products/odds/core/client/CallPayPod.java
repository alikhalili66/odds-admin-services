package ir.khalili.products.odds.core.client;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;

/**
 * @author A.KH
 */
public class CallPayPod extends AbstractVerticle {

	private static final int port = 8086;
	private static final String host  ="https://test.kipaad.ir/8086/megaKipaad";
	
	public static void main(String[] args) {

		System.out.println("CallPayPod STARTING ......");
		Vertx vertx = Vertx.vertx();
		vertx.deployVerticle(new CallPayPod());
	}

	@Override
	public void start() throws Exception {

		WebClient client = WebClient.create(vertx);
		transactionCancel(client);
//		transactionClose(client);
	}
	
	public void transactionClose(WebClient client) {
		try {
			
	        JsonObject joInput = new JsonObject();
	        joInput.put("userName", "username");
	        joInput.put("transactionId", "0");
			
			client.post(port, host,"/api/app/wallet/transaction/close").sendJson(joInput, ar -> {
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
	
	public void transactionCancel(WebClient client) {
		try {
			
	        JsonObject joInput = new JsonObject();
	        joInput.put("userName", "username");
	        joInput.put("transactionId", "0");
			
			client.post(port, host, "/megaKipaad/api/app/wallet/transaction/cancel").sendJson(joInput, ar -> {
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