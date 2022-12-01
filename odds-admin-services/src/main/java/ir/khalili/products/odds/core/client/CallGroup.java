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

	public static final int port = 9090;
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
//		groupSave(client);
//		groupUpdate(client);
//		groupDelete(client);
//		groupFetchAll(client);
//		groupFetchById(client);
//		groupTeamAssign(client);
		groupTeamUnAssign(client);
//		groupTeamFetch(client);
//		groupCompetitionFetch(client);
	}

	public void groupSave(WebClient client) {

		JsonObject joInput = new JsonObject();
		joInput.put("leagueId", 1);
		joInput.put("name", "گروه د");
		joInput.put("activeFrom", "2022/10/01T10:10:10Z");
		joInput.put("activeTo", "2022/10/10T10:10:10Z");
		
		System.out.println("joInput:" + joInput);

		try {
			client.post(port, host, "/v1/service/odds/group/save")
					
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
		joInput.put("groupId", 27);
		joInput.put("leagueId", 1);
		joInput.put("name", "گروه ث");
		joInput.put("activeFrom", "Wed Oct 05 2022 11:00:01 GMT+0330");
		joInput.put("activeTo", "Mon Jan 23 2023 11:00:01 GMT+0330");
		System.out.println("joInput:" + joInput);

		try {
			client.post(port, host, "/v1/service/odds/group/update")
					
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
		joInput.put("groupId", 32);
		System.out.println("joInput:" + joInput);

		try {
			client.post(port, host, "/v1/service/odds/group/delete")
					
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
		joInput.put("leagueId", 1);
		
		System.out.println("joInput:" + joInput);

		try {
			client.post(port, host, "/v1/service/odds/group/all/fetch")
					
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
		joInput.put("groupId", 1);
		System.out.println("joInput:" + joInput);

		try {
			client.post(port, host, "/v1/service/odds/group/id/fetch")
					
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
		joInput.put("groupId", 29);
		joInput.put("teamId", 57);
		
		System.out.println("joInput:" + joInput);

		try {
			client.post(port, host, "/v1/service/odds/group/team/assign")
					
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
		joInput.put("groupId", 29);
		joInput.put("teamId", 57);
		
		System.out.println("joInput:" + joInput);

		try {
			client.post(port, host, "/v1/service/odds/group/team/unassign")
					
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
		joInput.put("groupId", 1);
		System.out.println("joInput:" + joInput);

		try {
			client.post(port, host, "/v1/service/odds/group/team/fetch")
					
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

	public void groupCompetitionFetch(WebClient client) {

		JsonObject joInput = new JsonObject();
		joInput.put("groupId", 1);
		System.out.println("joInput:" + joInput);

		try {
			client.post(port, host, "/v1/service/odds/group/competition/fetch")
					
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