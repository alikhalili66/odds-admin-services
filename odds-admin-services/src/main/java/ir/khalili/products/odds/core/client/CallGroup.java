package ir.khalili.products.odds.core.client;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;

/**
 * @author A.KH
 */
public class CallGroup extends AbstractVerticle {

	public static final int port = 6060;
	public static final String host  ="127.0.0.1";
//	public static final String host  ="185.213.167.156";
	
	public static void main(String[] args) {

		System.out.println("CallOperator STARTING ......");
		Vertx vertx = Vertx.vertx();
		vertx.deployVerticle(new CallGroup());
	}

	@Override
	public void start() throws Exception {

		WebClient client = WebClient.create(vertx);
//		doSaveOperator(client);
//		doUpdateOperator(client);
//		operatorFetchByAgentId(client);
		operatorSearch(client);
//		doDeleteOperator(client);
	}

	public void doUpdateOperator(WebClient client) {
		JsonObject joUpdateAgent = new JsonObject();
		joUpdateAgent.put("operatorId", 4);
		joUpdateAgent.put("isActive", "N");
		joUpdateAgent.put("name", "Amir");
		joUpdateAgent.put("lastname", "Norozi");
		
		System.out.println("joUpdateAgent:" + joUpdateAgent);
		try {
			client.post(port, host, "/v1/service/nas/operator/update").putHeader("Authorization", CallAuth.token).putHeader("API-KEY", CallAuth.API_KEY).sendJson(joUpdateAgent, ar -> {
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
	
	public void operatorSearch(WebClient client) {
		
		JsonObject joInput = new JsonObject();
		joInput.put("agentId", 25); // send agentId is optional.
		joInput.put("startIndex", 0);
		joInput.put("endIndex", 10);
		
		System.out.println("joInput:" + joInput);
		
		try {
			client.post(port, host, "/v1/service/nas/operator/search").putHeader("Authorization", CallAuth.token).putHeader("API-KEY", CallAuth.API_KEY).sendJson(joInput, ar -> {
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

	public void operatorFetchByAgentId(WebClient client) {
		
		JsonObject joInput = new JsonObject();
		joInput.put("agentId", 2);
		
		System.out.println("joInput:" + joInput);
		
		try {
			client.post(port, host, "/v1/service/nas/operator/agent/fetch/all").putHeader("Authorization", CallAuth.token).putHeader("API-KEY", CallAuth.API_KEY).sendJson(joInput, ar -> {
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
	
	
	public void doDeleteOperator(WebClient client) {
		JsonObject joDelete = new JsonObject();
		joDelete.put("operatorId", 3);
		
		System.out.println("doDeleteOperator:" + joDelete);
		try {
			client.post(port, host, "/v1/service/nas/operator/delete").putHeader("Authorization", CallAuth.token).putHeader("API-KEY", CallAuth.API_KEY).sendJson(joDelete, ar -> {
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
	

	
	public void doSaveOperator(WebClient client) {
		JsonObject joSaveOperator = new JsonObject();
		
		joSaveOperator.put("username", "operator4");
		joSaveOperator.put("password", "1234abcd");
		joSaveOperator.put("name", "امیر");
		joSaveOperator.put("lastname", "نوروزی");
		joSaveOperator.put("agentId", 25);
		joSaveOperator.put("isActive", "Y");
		
		System.out.println("joSaveOperator:" + joSaveOperator);
		try {
			client.post(port, host, "/v1/service/nas/operator/save").putHeader("Authorization", CallAuth.token).putHeader("API-KEY", CallAuth.API_KEY).sendJson(joSaveOperator, ar -> {
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