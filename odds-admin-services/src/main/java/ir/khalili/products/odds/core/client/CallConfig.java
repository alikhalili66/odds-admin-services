package ir.khalili.products.odds.core.client;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;

/**
 * @author A.KH
 */
public class CallConfig extends AbstractVerticle {

	private static final int port = 6060;
	private static final String host  ="127.0.0.1";
//	private static final String host  ="185.213.167.156";
	
	public static void main(String[] args) {

		System.out.println("CallConfig STARTING ......");
		Vertx vertx = Vertx.vertx();
		vertx.deployVerticle(new CallConfig());
	}

	@Override
	public void start() throws Exception {

		WebClient client = WebClient.create(vertx);
//		callFetchAllConfig(client);
		callFetchAllConfigByAgentId(client);
//		callConfigUpdate(client);
//		callAgentConfigUpdate(client);
	}

	public void callFetchAllConfigByAgentId(WebClient client) {
		
		JsonObject joInput = new JsonObject();
		joInput.put("agentId", 2);
		
		System.out.println("joInput:" + joInput);
		
		try {
			client.post(port, host, "/v1/service/nas/config/agent/fetch/all").putHeader("Authorization", CallAuth.token).putHeader("API-KEY", CallAuth.API_KEY).sendJson(joInput, ar -> {
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
		
	
	public void callFetchAllConfig(WebClient client) {
		try {
			client.post(port, host, "/v1/service/nas/config/fetch/all").putHeader("Authorization", CallAuth.token).putHeader("API-KEY", CallAuth.API_KEY).send(ar -> {
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
	
	public void callConfigUpdate(WebClient client) {
		
		JsonObject joInput = new JsonObject();
		joInput.put("configId", 13);
		joInput.put("defaultValue", "3");
		
		System.out.println("joInput:" + joInput);
		
		try {
			client.post(port, host, "/v1/service/nas/config/update").putHeader("Authorization", CallAuth.token).putHeader("API-KEY", CallAuth.API_KEY).sendJson(joInput, ar -> {
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
	
	public void callAgentConfigUpdate(WebClient client) {
		
		JsonObject joInput = new JsonObject();
		joInput.put("configId", 13);
		joInput.put("agentId", 2);
		joInput.put("value", "10");
		
		System.out.println("joInput:" + joInput);
		
		try {
			client.post(port, host, "/v1/service/nas/config/agent/update").putHeader("Authorization", CallAuth.token).putHeader("API-KEY", CallAuth.API_KEY).sendJson(joInput, ar -> {
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