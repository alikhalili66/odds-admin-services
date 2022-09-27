package ir.khalili.products.odds.core.client;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;

/**
 * @author A.KH
 */
public class CallGroup extends AbstractVerticle {

	public static final int port = 6060;
	public static final String host  ="127.0.0.1";
//	public static final String host  ="185.213.167.156";
	
	public static void main(String[] args) {

		System.out.println("CallGroup STARTING ......");
		Vertx vertx = Vertx.vertx();
		vertx.deployVerticle(new CallGroup());
	}


	@Override
	public void start() throws Exception {
		WebClient client = WebClient.create(vertx);
		groupSave(client);
		groupUpdate(client);
		groupDelete(client);
		groupFetchById(client);
		groupFetchAll(client);
		groupTeamAssign(client);
		groupTeamUnAssign(client);
		groupTeamFetch(client);
		
	}

	public void groupSave(WebClient client) {

		JsonObject joInput = new JsonObject();
		System.out.println("joInput:" + joInput);

		try {
			client.post(port, host, "/v1/service/odds/group/save")
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

	public void groupUpdate(WebClient client) {

		JsonObject joInput = new JsonObject();
		System.out.println("joInput:" + joInput);

		try {
			client.post(port, host, "/v1/service/odds/group/update")
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

	public void groupDelete(WebClient client) {

		JsonObject joInput = new JsonObject();
		System.out.println("joInput:" + joInput);

		try {
			client.post(port, host, "/v1/service/odds/group/delete")
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

	public void groupFetchAll(WebClient client) {

		JsonObject joInput = new JsonObject();
		System.out.println("joInput:" + joInput);

		try {
			client.post(port, host, "/v1/service/odds/group/fetch/all")
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

	public void groupFetchById(WebClient client) {

		JsonObject joInput = new JsonObject();
		System.out.println("joInput:" + joInput);

		try {
			client.post(port, host, "/v1/service/odds/group/fetch/id")
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

	public void groupTeamAssign(WebClient client) {

		JsonObject joInput = new JsonObject();
		System.out.println("joInput:" + joInput);

		try {
			client.post(port, host, "/v1/service/odds/group/assign/team")
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

	public void groupTeamUnAssign(WebClient client) {

		JsonObject joInput = new JsonObject();
		System.out.println("joInput:" + joInput);

		try {
			client.post(port, host, "/v1/service/odds/group/unassign/team")
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

	public void groupTeamFetch(WebClient client) {

		JsonObject joInput = new JsonObject();
		System.out.println("joInput:" + joInput);

		try {
			client.post(port, host, "/v1/service/odds/group/fetch/team")
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