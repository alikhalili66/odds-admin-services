package ir.khalili.products.odds.core.client;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;

/**
 * @author A.KH
 */
public class CallTeam extends AbstractVerticle {

	private static final int port = 6060;
	private static final String host  ="127.0.0.1";
//	private static final String host  ="185.213.167.156";
	
	public static void main(String[] args) {

		System.out.println("CallTeam STARTING ......");
		Vertx vertx = Vertx.vertx();
		vertx.deployVerticle(new CallTeam());
	}

	@Override
	public void start() throws Exception {
		WebClient client = WebClient.create(vertx);
		teamSave(client);
//		teamUpdate(client);
//		teamDelete(client);
//		teamFetchAll(client);
//		teamFetchById(client);
	}

	public void teamSave(WebClient client) {

		JsonObject joInput = new JsonObject();
		joInput.put("leagueId", 2);
		joInput.put("name", "اکوادور");
		joInput.put("symbol", "E");
		joInput.put("image", "path");
		
		System.out.println("joInput:" + joInput);

		try {
			client.post(port, host, "/v1/service/odds/team/save")
					.putHeader("API-KEY", CallAuth.API_KEY)
					.putHeader("Authorization", CallAuth.token)
					.sendJson(joInput, ar -> {
						try {
							if (ar.succeeded()) {
								JsonObject response = new JsonObject(ar.result().bodyAsString());
								System.out.println(Json.encodePrettily(response));
							} else {
								System.out.println(ar.cause());
							}
						} catch (Exception e) {
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

	public void teamUpdate(WebClient client) {

		JsonObject joInput = new JsonObject();
		joInput.put("teamId", 3);
		joInput.put("leagueId", 2);
		joInput.put("name", "اکوادور");
		joInput.put("symbol", "اکواد");
		joInput.put("image", "path");

		System.out.println("joInput:" + joInput);

		try {
			client.post(port, host, "/v1/service/odds/team/update")
					.putHeader("API-KEY", CallAuth.API_KEY)
					.putHeader("Authorization", CallAuth.token)
					.sendJson(joInput, ar -> {
						try {
							if (ar.succeeded()) {
								JsonObject response = new JsonObject(ar.result().bodyAsString());
								System.out.println(Json.encodePrettily(response));
							} else {
								System.out.println(ar.cause());
							}
						} catch (Exception e) {
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

	public void teamDelete(WebClient client) {

		JsonObject joInput = new JsonObject();
		joInput.put("teamId", 3);
		System.out.println("joInput:" + joInput);

		try {
			client.post(port, host, "/v1/service/odds/team/delete")
					.putHeader("API-KEY", CallAuth.API_KEY)
					.putHeader("Authorization", CallAuth.token)
					.sendJson(joInput, ar -> {
						try {
							if (ar.succeeded()) {
								JsonObject response = new JsonObject(ar.result().bodyAsString());
								System.out.println(Json.encodePrettily(response));
							} else {
								System.out.println(ar.cause());
							}
						} catch (Exception e) {
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

	public void teamFetchAll(WebClient client) {

		JsonObject joInput = new JsonObject();
		System.out.println("joInput:" + joInput);

		try {
			client.post(port, host, "/v1/service/odds/team/fetch/all")
					.putHeader("API-KEY", CallAuth.API_KEY)
					.putHeader("Authorization", CallAuth.token)
					.sendJson(joInput, ar -> {
						try {
							if (ar.succeeded()) {
								JsonObject response = new JsonObject(ar.result().bodyAsString());
								System.out.println(Json.encodePrettily(response));
							} else {
								System.out.println(ar.cause());
							}
						} catch (Exception e) {
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

	public void teamFetchById(WebClient client) {

		JsonObject joInput = new JsonObject();
		joInput.put("teamId", 2);
		System.out.println("joInput:" + joInput);

		try {
			client.post(port, host, "/v1/service/odds/team/fetch/id")
					.putHeader("API-KEY", CallAuth.API_KEY)
					.putHeader("Authorization", CallAuth.token)
					
					.sendJson(joInput, ar -> {
						try {
							if (ar.succeeded()) {
								JsonObject response = new JsonObject(ar.result().bodyAsString());
								System.out.println(Json.encodePrettily(response));
							} else {
								System.out.println(ar.cause());
							}
						} catch (Exception e) {
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