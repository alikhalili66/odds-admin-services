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
//	private static final String host  ="37.32.25.54";
	
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
//		reportFetch(client);
//		reportCalculateCompetition(client);
//		dailyLottery(client);
//		totalUsername(client);
		dailyOdds(client);
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
			joInput.put("groupId", 1); // Optional
			joInput.put("competitionId", 54); // Optional
			joInput.put("questionId", 62); // Optional
			
			System.out.println(joInput);
			
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
	
	public void reportCalculateCompetition(WebClient client) {
		try {
			
			JsonObject joInput = new JsonObject();
			joInput.put("competitionId", 2); // Optional
			
			System.out.println(joInput);
			
			client
			.post(port, host, "/v1/service/odds/report/calculate/competition")
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
	
	public void dailyLottery(WebClient client) {
		try {
			
			JsonObject joInput = new JsonObject();
			joInput.put("leagueId", 1);
			joInput.put("competitionDate", "Tue Nov 22 2022 14:55:00 GMT+0330"); // Optional
			
			System.out.println(joInput);
			
			client
			.post(port, host, "/v1/service/odds/report/daily/lottery")
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
	
	public void totalUsername(WebClient client) {
		try {
			
			JsonObject joInput = new JsonObject();
			joInput.put("leagueId", 1);
			
			System.out.println(joInput);
			
			client
			.post(port, host, "/v1/service/odds/report/total/username")
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
	
	public void dailyOdds(WebClient client) {
		try {
			
			JsonObject joInput = new JsonObject();
			joInput.put("leagueId", 1);
			joInput.put("competitionDate", "Tue Nov 22 2022 14:55:00 GMT+0330"); // Optional
			
			System.out.println(joInput);
			
			client
			.post(port, host, "/v1/service/odds/report/daily/odds")
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