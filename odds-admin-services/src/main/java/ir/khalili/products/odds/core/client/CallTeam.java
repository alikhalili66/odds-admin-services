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
//		teamImageUpdate(client);
//		teamMemberSave(client);
//		teamMemberUpdate(client);
		teamMemberDelete(client);
//		teamMemberFetchById(client);
	}

	public void teamSave(WebClient client) {

		JsonObject joInput = new JsonObject();
		joInput.put("leagueId", 1);
		joInput.put("name", "تست");
		joInput.put("symbol", "test");
		joInput.put("image", "iVBORw0KGgoAAAANSUhEUgAAAGQAAABkAQAAAABYmaj5AAAA7ElEQVR42tXUsZHEIAwFUHk2cHZuQDO0QeaWTAN4twK3REYbzNAAyhww1ombvd1NbBHeMQS8CPERAH+MAn9YBWCBzAEGTcR13W8cZaEpoLdpiuA6tIb86JWhHnH1tq7vyk4l53MR3fu0p2pZzbJ8JXiqYtHP6H53uBAH3mKadpg0HRZhRrCZNBHzxnWIadBUbILRbK/KzkXxRhEHNpumMuLXLPOZ4IVoz4flA5LTlTzkO+CkqeU/Sgy65G59q92QptbXLIEZVhXQsblDlxZIy8iPDsmrIn5mdiWui/QCoKr2pq35CUPRf/nBPvUNct67nP2Y9j8AAAAASUVORK5CYII=");
		
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
		joInput.put("teamId", 57);
		joInput.put("leagueId", 1);
		joInput.put("name", "تست");
		joInput.put("symbol", "test");

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
		joInput.put("leagueId", 1);
		
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
		joInput.put("teamId", 57);
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
		joInput.put("teamId", 57);
		joInput.put("image", "iVBORw0KGgoAAAANSUhEUgAAAGQAAABkAQAAAABYmaj5AAAA7ElEQVR42tXUsZHEIAwFUHk2cHZuQDO0QeaWTAN4twK3REYbzNAAyhww1ombvd1NbBHeMQS8CPERAH+MAn9YBWCBzAEGTcR13W8cZaEpoLdpiuA6tIb86JWhHnH1tq7vyk4l53MR3fu0p2pZzbJ8JXiqYtHP6H53uBAH3mKadpg0HRZhRrCZNBHzxnWIadBUbILRbK/KzkXxRhEHNpumMuLXLPOZ4IVoz4flA5LTlTzkO+CkqeU/Sgy65G59q92QptbXLIEZVhXQsblDlxZIy8iPDsmrIn5mdiWui/QCoKr2pq35CUPRf/nBPvUNct67nP2Y9j8AAAAASUVORK5CYII=");

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
	
	public void teamMemberSave(WebClient client) {

		JsonObject joInput = new JsonObject();
		joInput.put("teamId", 1);
		joInput.put("name", "علی کریمی");
		joInput.put("count", 10);
		joInput.put("position", "FORWARD");
		
		System.out.println("joInput:" + joInput);

		try {
			client.post(port, host, "/v1/service/odds/team/member/save")
					
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

	public void teamMemberUpdate(WebClient client) {

		JsonObject joInput = new JsonObject();
		joInput.put("memberId", 1);
		joInput.put("teamId", 1);
		joInput.put("name", "کریم باقری");
		joInput.put("count", 10);
		joInput.put("position", "FORWARD");

		System.out.println("joInput:" + joInput);

		try {
			client.post(port, host, "/v1/service/odds/team/member/update")
					
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

	public void teamMemberDelete(WebClient client) {

		JsonObject joInput = new JsonObject();
		joInput.put("memberId", 96);
		System.out.println("joInput:" + joInput);

		try {
			client.post(port, host, "/v1/service/odds/team/member/delete")
					
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

	public void teamMemberFetchById(WebClient client) {

		JsonObject joInput = new JsonObject();
		joInput.put("teamId", 21);
		System.out.println("joInput:" + joInput);

		try {
			client.post(port, host, "/v1/service/odds/team/member/all/fetch")
					
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