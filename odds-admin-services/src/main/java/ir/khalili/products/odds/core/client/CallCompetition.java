package ir.khalili.products.odds.core.client;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;

/**
 * @author A.KH
 */
public class CallCompetition extends AbstractVerticle {

	private static final int port = 6060;
	private static final String host  ="127.0.0.1";
//	private static final String host  ="185.213.167.156";
	
	public static void main(String[] args) {

		System.out.println("CallAgent STARTING ......");
		Vertx vertx = Vertx.vertx();
		vertx.deployVerticle(new CallCompetition());
	}

	@Override
	public void start() throws Exception {

		WebClient client = WebClient.create(vertx);
//		doSaveAgent(client);
//		doDeleteAgent(client);
//		fetchAllAgent(client);
//		doUpdateAgent(client);
		fetchAgentById(client);
	}

	public void doUpdateAgent(WebClient client) {
		JsonObject joUpdateAgent = new JsonObject();
		joUpdateAgent.put("agentId", 2);
		joUpdateAgent.put("isActive", "N");
		joUpdateAgent.put("name", "test2");
		
		System.out.println("joUpdateAgent:" + joUpdateAgent);
		try {
			client.post(port, host, "/v1/service/nas/agent/update").putHeader("Authorization", CallAuth.token).putHeader("API-KEY", CallAuth.API_KEY).sendJson(joUpdateAgent, ar -> {
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
	
	public void fetchAllAgent(WebClient client) {
		
		try {
			client.post(port, host, "/v1/service/nas/agent/fetch/all").putHeader("Authorization", CallAuth.token).putHeader("API-KEY", CallAuth.API_KEY).send(ar -> {
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
	
	public void fetchAgentById(WebClient client) {
		JsonObject joFetchAgentById = new JsonObject();
		joFetchAgentById.put("agentId", 2);
		
		System.out.println("joFetchAgentById:" + joFetchAgentById);
		try {
			client.post(port, host, "/v1/service/nas/agent/fetch/id").putHeader("Authorization", CallAuth.token).putHeader("API-KEY", CallAuth.API_KEY).sendJson(joFetchAgentById, ar -> {
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
	
	
	public void doDeleteAgent(WebClient client) {
		JsonObject joDelete = new JsonObject();
		joDelete.put("agentId", 2);
		
		System.out.println("doDeleteAgent:" + joDelete);
		try {
			client.post(port, host, "/v1/service/nas/agent/delete").putHeader("Authorization", CallAuth.token).putHeader("API-KEY", CallAuth.API_KEY).sendJson(joDelete, ar -> {
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
	

	
	public void doSaveAgent(WebClient client) {
		JsonObject joSaveAgent = new JsonObject();
		joSaveAgent.put("name", "تاپ");
		joSaveAgent.put("symbol", "APPPPPPP2");
		joSaveAgent.put("isActive", "N");
		joSaveAgent.put("username", "Agent1234");
		joSaveAgent.put("password", "AgentPass");
		
		System.out.println("joSaveAgent:" + joSaveAgent);
		try {
			client.post(port, host, "/v1/service/nas/agent/save").putHeader("Authorization", CallAuth.token).putHeader("API-KEY", CallAuth.API_KEY).sendJson(joSaveAgent, ar -> {
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