package ir.khalili.products.odds.core.client;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;

/**
 * @author A.KH
 */
public class CallReport extends AbstractVerticle {

	private static final int port = 9090;
	private static final String host  ="127.0.0.1";
//	private static final String host  ="185.213.167.156";
	
	public static void main(String[] args) {

		System.out.println("CallReport STARTING ......");
		Vertx vertx = Vertx.vertx();
		vertx.deployVerticle(new CallReport());
	}

	@Override
	public void start() throws Exception {

		WebClient client = WebClient.create(vertx);
//		reportCompetitorUsersAmount(client);
//		reportCompetitorUsersCount(client);
//		reportOddsCount(client);
//		reportRegisteredUsersCount(client);
		reportFetch(client);
	}

	public void reportRegisteredUsersCount(WebClient client) {
		try {
			
			JsonObject joInput = new JsonObject();
			joInput.put("leagueId", 1);
			
			client
			.post(port, host, "/v1/service/odds/report/registered/users/count")
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
	
	public void reportCompetitorUsersCount(WebClient client) {
		try {
			
			JsonObject joInput = new JsonObject();
			joInput.put("leagueId", 1);
			
			client
			.post(port, host, "/v1/service/odds/report/competitor/users/count")
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
	
	public void reportCompetitorUsersAmount(WebClient client) {
		try {
			
			JsonObject joInput = new JsonObject();
			joInput.put("leagueId", 1);
			
			client
			.post(port, host, "/v1/service/odds/report/competitor/users/amount")
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
	
	public void reportOddsCount(WebClient client) {
		try {
			
			JsonObject joInput = new JsonObject();
			joInput.put("leagueId", 1);
			
			client
			.post(port, host, "/v1/service/odds/report/odds/count")
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

	public void reportFetch(WebClient client) {
		try {
			
			JsonObject joInput = new JsonObject();
			joInput.put("leagueId", 1);
			joInput.put("competitionId", 31); // Optional
			joInput.put("groupId", 28); // Optional
			joInput.put("questionId", 1); // Optional
			
			client
			.post(port, host, "/v1/service/odds/report/fetch")
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