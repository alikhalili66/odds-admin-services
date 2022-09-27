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

		System.out.println("CallCompetition STARTING ......");
		Vertx vertx = Vertx.vertx();
		vertx.deployVerticle(new CallCompetition());
	}

	@Override
	public void start() throws Exception {

		WebClient client = WebClient.create(vertx);
		competitionSave(client);
		competitionUpdate(client);
		competitionDelete(client);
		competitionFetchAll(client);
		competitionFetchById(client);
		competitionGroupFetch(client);
		competitionQuestionAssign(client);
		competitionQuestionUnAssign(client);
		competitionQuestionFetch(client);
	}

	public void competitionSave(WebClient client) {
		JsonObject joInput = new JsonObject();
		
		System.out.println("joInput:" + joInput);
		try {
			client.post(port, host, "/v1/service/odds/competition/save").putHeader("Authorization", CallAuth.token).putHeader("API-KEY", CallAuth.API_KEY).sendJson(joInput, ar -> {
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
		
		System.out.println("joInput:" + joInput);
		
		try {
			client.post(port, host, "/v1/service/odds/competition/update").putHeader("Authorization", CallAuth.token).putHeader("API-KEY", CallAuth.API_KEY).sendJson(joInput, ar -> {
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
		
		System.out.println("joInput:" + joInput);
		try {
			client.post(port, host, "/v1/service/odds/competition/delete").putHeader("Authorization", CallAuth.token).putHeader("API-KEY", CallAuth.API_KEY).sendJson(joInput, ar -> {
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
		
		System.out.println("joInput:" + joInput);
		try {
			client.post(port, host, "/v1/service/odds/competition/fetch/all").putHeader("Authorization", CallAuth.token).putHeader("API-KEY", CallAuth.API_KEY).sendJson(joInput, ar -> {
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
		
		System.out.println("joInput:" + joInput);
		try {
			client.post(port, host, "/v1/service/odds/competition/fetch/id").putHeader("Authorization", CallAuth.token).putHeader("API-KEY", CallAuth.API_KEY).sendJson(joInput, ar -> {
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
		
		System.out.println("joInput:" + joInput);
		try {
			client.post(port, host, "/v1/service/odds/competition/fetch/group").putHeader("Authorization", CallAuth.token).putHeader("API-KEY", CallAuth.API_KEY).sendJson(joInput, ar -> {
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
		
		System.out.println("joInput:" + joInput);
		try {
			client.post(port, host, "/v1/service/odds/competition/assign/question").putHeader("Authorization", CallAuth.token).putHeader("API-KEY", CallAuth.API_KEY).sendJson(joInput, ar -> {
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
		
		System.out.println("joInput:" + joInput);
		try {
			client.post(port, host, "/v1/service/odds/competition/unassign/question").putHeader("Authorization", CallAuth.token).putHeader("API-KEY", CallAuth.API_KEY).sendJson(joInput, ar -> {
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
		
		System.out.println("joInput:" + joInput);
		try {
			client.post(port, host, "/v1/service/odds/competition/fetch/question").putHeader("Authorization", CallAuth.token).putHeader("API-KEY", CallAuth.API_KEY).sendJson(joInput, ar -> {
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