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

	private static final int port = 9090;
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
//		teamSave(client);
//		teamUpdate(client);
//		teamDelete(client);
//		teamFetchAll(client);
//		teamFetchById(client);
		teamImageUpdate(client);
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

		System.out.println("joInput:" + joInput);

		try {
			client.post(port, host, "/v1/service/odds/team/update")
					
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
			client.post(port, host, "/v1/service/odds/team/all/fetch")
					
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
			client.post(port, host, "/v1/service/odds/team/id/fetch")
					
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

	public void teamImageUpdate(WebClient client) {

		JsonObject joInput = new JsonObject();
		joInput.put("teamId", 3);
		joInput.put("image", "path2");

		System.out.println("joInput:" + joInput);

		try {
			client.post(port, host, "/v1/service/odds/team/image/update")
					
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