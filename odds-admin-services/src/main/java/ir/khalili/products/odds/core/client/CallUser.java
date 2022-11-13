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

	private static final int port = 9090;
	private static final String host  ="127.0.0.1";
//	private static final String host  ="185.213.167.156";
	
	public static void main(String[] args) {

		System.out.println("CallUser STARTING ......");
		Vertx vertx = Vertx.vertx();
		vertx.deployVerticle(new CallUser());
	}

	@Override
	public void start() throws Exception {
		WebClient client = WebClient.create(vertx);
//		userFetchAll(client);
//		userFetchById(client);
		userFetchOdds(client);
//		userFetchQuestionAnswer(client);
//		userFetchPointHistory(client);
	}


	public void userFetchAll(WebClient client) {
		JsonObject joInput = new JsonObject();
		joInput.put("leagueId", 1); 
		joInput.put("username", "09124083504"); // optional
//		joInput.put("cellphone", 9124083504L); // optional
		joInput.put("startIndex", 1);
		joInput.put("endIndex", 10);
		
		try {
			client.post(port, host, "/v1/service/odds/user/all/fetch")
			
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

	public void userFetchById(WebClient client) {
		JsonObject joInput = new JsonObject();
		joInput.put("id", 23);
		try {
			client.post(port, host, "/v1/service/odds/user/id/fetch")
			
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

	public void userFetchOdds(WebClient client) {
		JsonObject joInput = new JsonObject();
		joInput.put("id", 1);
		joInput.put("startIndex", 1);
		joInput.put("endIndex", 10);
		
		try {
			client.post(port, host, "/v1/service/odds/user/fetch/odds")
			
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

	public void userFetchQuestionAnswer(WebClient client) {
		JsonObject joInput = new JsonObject();
		joInput.put("userId", 1);
		joInput.put("competitionId", 1);
		
		try {
			client.post(port, host, "/v1/service/odds/user/fetch/question")
			
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

	
	public void userFetchPointHistory(WebClient client) {
		JsonObject joInput = new JsonObject();
		joInput.put("userId", 1);
		joInput.put("startIndex", 1);
		joInput.put("endIndex", 10);
		
		try {
			client.post(port, host, "/v1/service/odds/user/fetch/history")
			
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