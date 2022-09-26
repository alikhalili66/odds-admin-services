package ir.khalili.products.odds.core.client;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;

/**
 * @author A.KH
 */
public class CallUser extends AbstractVerticle {

	private static final int port = 6060;
	private static final String host  ="127.0.0.1";
//	private static final String host  ="185.213.167.156";
	
	public static void main(String[] args) {

		System.out.println("CallTask STARTING ......");
		Vertx vertx = Vertx.vertx();
		vertx.deployVerticle(new CallUser());
	}

	@Override
	public void start() throws Exception {
		WebClient client = WebClient.create(vertx);
		fetchCustomerTaskFetchAll(client);
//		fetchAllTaskByOperatorId(client);
	}


	public void fetchCustomerTaskFetchAll(WebClient client) {
		JsonObject joInput = new JsonObject();
		
		try {
			client.post(port, host, "/v1/service/nas/task/customer/fetch")
			.putHeader("API-KEY", CallAuth.API_KEY)
			.putHeader("Authorization", CallAuth.token)
			.putHeader("customerSession", "customerSession")
			.putHeader("agentSession", "agentSession")
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