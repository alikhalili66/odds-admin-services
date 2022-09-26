package ir.khalili.products.odds.core.client;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;
import ir.khalili.products.odds.core.enums.ServiceType;

/**
 * @author A.KH
 */
public class Callodds extends AbstractVerticle {

	private static final int port = 6060;
	private static final String host  ="127.0.0.1";
//	private static final String host  ="185.213.167.156";
	
	public static void main(String[] args) {

		System.out.println("CallService STARTING ......");
		Vertx vertx = Vertx.vertx();
		vertx.deployVerticle(new Callodds());
	}

	@Override
	public void start() throws Exception {

		WebClient client = WebClient.create(vertx);
//		doUpdateService(client);
//		fetchAllService(client);
//		doUpdateServiceAgent(client);
		fetchAllServiceByAgentId(client);
	}

	public void doUpdateServiceAgent(WebClient client) {
		
		JsonObject joInput = new JsonObject();
		joInput.put("serviceId", ServiceType.CHANGE_CUSTOMER_PHONE.getServiceId());
		joInput.put("agentId", 2);
		joInput.put("isActive", "Y");
		
		System.out.println("joInput:" + joInput);
		
		try {
			client.post(port, host, "/v1/service/nas/service/agent/update").putHeader("API-KEY", CallAuth.API_KEY).putHeader("Authorization", CallAuth.token).sendJson(joInput, ar -> {
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
	
	
	public void doUpdateService(WebClient client) {
		
		JsonObject joInput = new JsonObject();
		joInput.put("serviceId", 3);
		joInput.put("isActive", "N");
		
		System.out.println("joInput:" + joInput);
		
		try {
			client.post(port, host, "/v1/service/nas/service/update").putHeader("API-KEY", CallAuth.API_KEY).putHeader("Authorization", CallAuth.token).sendJson(joInput, ar -> {
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
	
	public void fetchAllService(WebClient client) {
		try {
			client.post(port, host, "/v1/service/nas/service/fetch/all").putHeader("API-KEY", CallAuth.API_KEY).putHeader("Authorization", CallAuth.token).send( ar -> {
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

	public void fetchAllServiceByAgentId(WebClient client) {
		
		JsonObject joInput = new JsonObject();
		joInput.put("agentId", 2);
		
		System.out.println("joInput:" + joInput);
		
		try {
			client.post(port, host, "/v1/service/nas/service/agent/fetch/all").putHeader("API-KEY", CallAuth.API_KEY).putHeader("Authorization", CallAuth.token).sendJson(joInput, ar -> {
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