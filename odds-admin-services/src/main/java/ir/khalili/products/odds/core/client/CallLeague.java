package ir.khalili.products.odds.core.client;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;

/**
 * @author A.KH
 */
public class CallLeague extends AbstractVerticle {

	private static final int port = 9090;
	private static final String host = "127.0.0.1";
//	private static final String host  ="185.213.167.156";

	public static void main(String[] args) {

		System.out.println("CallLeague STARTING ......");
		Vertx vertx = Vertx.vertx();
		vertx.deployVerticle(new CallLeague());
	}

	@Override
	public void start() throws Exception {
		WebClient client = WebClient.create(vertx);
//		leagueSave(client);
//		leagueUpdate(client);
		leagueDelete(client);
//		leagueFetchAll(client);
//		leagueFetchById(client);
	}

	public void leagueSave(WebClient client) {

		JsonObject joInput = new JsonObject();
		joInput.put("name", "Z");
		joInput.put("symbol", "Z");
		joInput.put("image", "path");
		joInput.put("activeFrom", "2022-09-30T10:10:10Z");
		joInput.put("activeTo", "2022-09-30T10:10:10Z");
		joInput.put("oddsFrom", "2022-09-30T10:10:10Z");
		joInput.put("oddsTo", "2022-09-30T10:10:10Z");
		
		System.out.println("joInput:" + joInput);

		try {
			client.post(port, host, "/v1/service/odds/league/save")
					
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

	public void leagueUpdate(WebClient client) {

		JsonObject joInput = new JsonObject();
		joInput.put("leagueId", 1);
		joInput.put("name", "لیگ برترر");
		joInput.put("symbol", "Bartar");
		joInput.put("image", "path");
		joInput.put("activeFrom", "Wed Oct 05 2022 11:00:01 GMT+0330");
		joInput.put("activeTo", "Mon Jan 23 2023 11:00:01 GMT+0330");
		joInput.put("oddsFrom", "Mon Oct 10 2022 00:00:00 GMT+0330");
		joInput.put("oddsTo", "Mon Oct 10 2022 00:00:00 GMT+0330");
		
		System.out.println("joInput:" + joInput);

		try {
			client.post(port, host, "/v1/service/odds/league/update")
					
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

	public void leagueDelete(WebClient client) {

		JsonObject joInput = new JsonObject();
		joInput.put("leagueId", 15);
		System.out.println("joInput:" + joInput);

		try {
			client.post(port, host, "/v1/service/odds/league/delete")
					
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

	public void leagueFetchAll(WebClient client) {

		JsonObject joInput = new JsonObject();
		System.out.println("joInput:" + joInput);

		try {
			client.post(port, host, "/v1/service/odds/league/all/fetch")
					
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

	public void leagueFetchById(WebClient client) {

		JsonObject joInput = new JsonObject();
		joInput.put("leagueId", 1);
		System.out.println("joInput:" + joInput);

		try {
			client.post(port, host, "/v1/service/odds/league/id/fetch")
					
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