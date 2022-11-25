package ir.khalili.products.odds.core.client;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;

/**
 * @author A.KH
 */
public class CallCompetition extends AbstractVerticle {

	private static final int port = 9090;
	private static final String host  ="127.0.0.1";
//	private static final String host  ="185.213.167.156";
	
	public static void main(String[] args) {

		System.out.println("CallCompetition STARTING ......");
		Vertx vertx = Vertx.vertx();
		vertx.deployVerticle(new CallCompetition());
	}

	@Override
	public void start() throws Exception {

		WebClient client = WebClient.create(vertx);
//		competitionSave(client);
		competitionUpdate(client);
//		competitionDelete(client);
//		competitionFetchAll(client);
//		competitionFetchById(client);
//		competitionGroupFetch(client);
//		competitionQuestionAssign(client);
//		competitionQuestionUnAssign(client);
//		competitionQuestionFetch(client);
//		competitionResultRegister(client);
//		questionResultRegister(client);
//		competitionPointCalculation(client);
//		competitionLiveScore(client);
	}

	public void competitionSave(WebClient client) {
		JsonObject joInput = new JsonObject();
		
		joInput.put("leagueId", 1);
		joInput.put("teamId1",1);
		joInput.put("teamId2",2);
		joInput.put("groupId",1);
		joInput.put("locationId",1);
		joInput.put("activeFrom", "Sun Nov 14 2021 14:55:00 GMT+0330");
		joInput.put("activeTo", "Sun Nov 14 2021 14:55:00 GMT+0330");
		joInput.put("oddsFrom", "Sun Nov 14 2021 14:55:00 GMT+0330");
		joInput.put("oddsTo", "Sun Nov 14 2021 14:55:00 GMT+0330");
		joInput.put("competitionDate", "Sun Nov 14 2021 14:55:00 GMT+0330");
		
		System.out.println("joInput:" + joInput);
		try {
			client.post(port, host, "/v1/service/odds/competition/save").putHeader("Authorization", CallAuth.token).sendJson(joInput, ar -> {
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
	
	public void competitionUpdate(WebClient client) {
		
		JsonObject joInput = new JsonObject();
		joInput.put("competitionId", 1);
		joInput.put("leagueId", 1);
		joInput.put("teamId1",1);
		joInput.put("teamId2",2);
		joInput.put("groupId",1);
		joInput.put("locationId",1);
		joInput.put("activeFrom", "Wed Oct 05 2022 11:00:01 GMT+0330");
		joInput.put("activeTo", "Mon Jan 23 2023 11:00:01 GMT+0330");
		joInput.put("oddsFrom", "Mon Oct 10 2022 00:00:00 GMT+0330");
		joInput.put("oddsTo", "Mon Oct 10 2022 00:00:00 GMT+0330");
		joInput.put("competitionDate", "Fri Sep 30 2022 20:45:41 GMT+0330");
		
		System.out.println("joInput:" + joInput);
		
		try {
			client.post(port, host, "/v1/service/odds/competition/update").putHeader("Authorization", CallAuth.token).sendJson(joInput, ar -> {
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
	
	public void competitionDelete(WebClient client) {
		JsonObject joInput = new JsonObject();
		joInput.put("competitionId", 1);
		
		System.out.println("joInput:" + joInput);
		try {
			client.post(port, host, "/v1/service/odds/competition/delete").putHeader("Authorization", CallAuth.token).sendJson(joInput, ar -> {
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
	
	
	public void competitionFetchAll(WebClient client) {
		JsonObject joInput = new JsonObject();
		joInput.put("leagueId", 1);
//		joInput.put("groupId", 1);
		
		System.out.println("joInput:" + joInput);
		
		try {
			client
			.post(port, host, "/v1/service/odds/competition/all/fetch")
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
	

	
	public void competitionFetchById(WebClient client) {
		JsonObject joInput = new JsonObject();
		joInput.put("competitionId", 1);
		
		System.out.println("joInput:" + joInput);
		try {
			client.post(port, host, "/v1/service/odds/competition/id/fetch").putHeader("Authorization", CallAuth.token).sendJson(joInput, ar -> {
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

	public void competitionGroupFetch(WebClient client) {
		JsonObject joInput = new JsonObject();
		joInput.put("competitionId", 1);
		
		System.out.println("joInput:" + joInput);
		try {
			client.post(port, host, "/v1/service/odds/competition/group/fetch").putHeader("Authorization", CallAuth.token).sendJson(joInput, ar -> {
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

	public void competitionQuestionAssign(WebClient client) {
		JsonObject joInput = new JsonObject();
		
		joInput.put("competitionId", 1);
		joInput.put("questionId", 1);
		joInput.put("norder", 1);
		
		System.out.println("joInput:" + joInput);
		try {
			client.post(port, host, "/v1/service/odds/competition/assign/question").putHeader("Authorization", CallAuth.token).sendJson(joInput, ar -> {
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

	public void competitionQuestionUnAssign(WebClient client) {
		JsonObject joInput = new JsonObject();
		joInput.put("competitionId", 1);
		joInput.put("questionId", 1);
		
		System.out.println("joInput:" + joInput);
		try {
			client.post(port, host, "/v1/service/odds/competition/unassign/question").putHeader("Authorization", CallAuth.token).sendJson(joInput, ar -> {
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

	public void competitionQuestionFetch(WebClient client) {
		JsonObject joInput = new JsonObject();
		joInput.put("competitionId", 15);

		System.out.println("joInput:" + joInput);
		try {
			client.post(port, host, "/v1/service/odds/competition/question/fetch").putHeader("Authorization", CallAuth.token).sendJson(joInput, ar -> {
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

	public void competitionResultRegister(WebClient client) {
		
		JsonObject joInput = new JsonObject();
		joInput.put("competitionId", 10);
		joInput.put("result", "OK");
		
		System.out.println("joInput:" + joInput);
		
		try {
			client.post(port, host, "/v1/service/odds/competition/result/register").putHeader("Authorization", CallAuth.token).sendJson(joInput, ar -> {
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
	
	public void questionResultRegister(WebClient client) {
		
		JsonArray jaResults = new JsonArray();
		jaResults.add(new JsonObject().put("questionId", 1).put("result", "گزینه اول"));
		jaResults.add(new JsonObject().put("questionId", 2).put("result", "گزینه دوم"));
		
		JsonObject joInput = new JsonObject();
		joInput.put("competitionId", 1);
		joInput.put("results", jaResults);		
		
		System.out.println("joInput:" + joInput);
		
		try {
			client.post(port, host, "/v1/service/odds/competition/question/result/register").putHeader("Authorization", CallAuth.token).sendJson(joInput, ar -> {
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
	
	public void competitionPointCalculation(WebClient client) {
		JsonObject joInput = new JsonObject();
		joInput.put("competitionId", 2);
		
		System.out.println("joInput:" + joInput);
		try {
			client.post(port, host, "/v1/service/odds/competition/point/calculation").putHeader("Authorization", CallAuth.token).sendJson(joInput, ar -> {
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
	
	public void competitionLiveScore(WebClient client) {
		JsonObject joInput = new JsonObject();
		joInput.put("competitionId", 23);
		
		System.out.println("joInput:" + joInput);
		try {
			client.post(port, host, "/v1/service/odds/competition/livescore").putHeader("Authorization", CallAuth.token).sendJson(joInput, ar -> {
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