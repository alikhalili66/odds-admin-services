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
		
		reportUsersWithMaximumOdds(client);
//		reportOddsCountPerCompetition(client);
//		reportUsersCountWithOdds(client);
//		reportQuestionStatisticPerCompetition(client);
//		reportUsersWithMaximumPoint(client);
//		reportCompetitionWithMaximumOdds(client);
//		reportCompetitionsTotalPoint(client);
		
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

	
	public void reportUsersWithMaximumOdds(WebClient client) {
		try {
			
			JsonObject joInput = new JsonObject();
			joInput.put("isLive", Boolean.TRUE);
			
			client
			.post(port, host, "/v1/service/odds/report/odds/users/maximum")
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
	
	public void reportOddsCountPerCompetition(WebClient client) {
		try {
			
			JsonObject joInput = new JsonObject();
			joInput.put("isLive", Boolean.TRUE);
			joInput.put("competitionId", 31);
			
			client
			.post(port, host, "/v1/service/odds/report/odds/competition/count")
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
	
	public void reportUsersCountWithOdds(WebClient client) {
		try {
			
			JsonObject joInput = new JsonObject();
			joInput.put("isLive", Boolean.TRUE);
			
			client
			.post(port, host, "/v1/service/odds/report/odds/users/odds/count")
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
	
	public void reportQuestionStatisticPerCompetition(WebClient client) {
		try {
			
			JsonObject joInput = new JsonObject();
			joInput.put("isLive", Boolean.TRUE);
			joInput.put("competitionId", 31);
			
			client
			.post(port, host, "/v1/service/odds/report/odds/question/statistic")
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
	
	public void reportUsersWithMaximumPoint(WebClient client) {
		try {
			
			JsonObject joInput = new JsonObject();
			joInput.put("isLive", Boolean.TRUE);
			
			client
			.post(port, host, "/v1/service/odds/report/odds/users/point/maximum")
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
	
	public void reportCompetitionWithMaximumOdds(WebClient client) {
		try {
			
			JsonObject joInput = new JsonObject();
			joInput.put("isLive", Boolean.TRUE);
			
			client
			.post(port, host, "/v1/service/odds/report/odds/competition/odds/maximum")
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
	
	public void reportCompetitionsTotalPoint(WebClient client) {
		try {
			
			JsonObject joInput = new JsonObject();
			joInput.put("isLive", Boolean.TRUE);
			joInput.put("competitionId", 31);
			
			client
			.post(port, host, "/v1/service/odds/report/odds/competition/point")
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