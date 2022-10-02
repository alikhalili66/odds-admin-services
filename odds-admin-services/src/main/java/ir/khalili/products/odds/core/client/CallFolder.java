package ir.khalili.products.odds.core.client;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;

/**
 * @author A.KH
 */
public class CallFolder extends AbstractVerticle {

	private static final int port = 9090;
	private static final String host = "127.0.0.1";
//	private static final String host  ="185.213.167.156";

	public static void main(String[] args) {

		System.out.println("CallFolder STARTING ......");
		Vertx vertx = Vertx.vertx();
		vertx.deployVerticle(new CallFolder());
	}

	@Override
	public void start() throws Exception {
		WebClient client = WebClient.create(vertx);
		folderSave(client);
//		folderUpdate(client);
//		folderDelete(client);
//		folderFetchAll(client);
//		folderFetchById(client);
//		folderQuestionAssign(client);
//		folderQuestionUnAssign(client);
//		folderQuestionFetch(client);
	}

	public void folderSave(WebClient client) {

		JsonObject joInput = new JsonObject();
		joInput.put("parentId", null);
		joInput.put("leagueId",1);
		joInput.put("name","پوشه سوم");
		
		System.out.println("joInput:" + joInput);

		try {
			client.post(port, host, "/v1/service/odds/folder/save")
					
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

	public void folderUpdate(WebClient client) {

		JsonObject joInput = new JsonObject();
		joInput.put("folderId", 3);
		joInput.put("parentId", null);
		joInput.put("leagueId",2);
		joInput.put("name","پوشه چهارم");
		System.out.println("joInput:" + joInput);

		try {
			client.post(port, host, "/v1/service/odds/folder/update")
					
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

	public void folderDelete(WebClient client) {

		JsonObject joInput = new JsonObject();
		joInput.put("folderId", 3);
		System.out.println("joInput:" + joInput);

		try {
			client.post(port, host, "/v1/service/odds/folder/delete")
					
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

	public void folderFetchAll(WebClient client) {

		JsonObject joInput = new JsonObject();
		System.out.println("joInput:" + joInput);

		try {
			client.post(port, host, "/v1/service/odds/folder/all/fetch")
					
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

	public void folderFetchById(WebClient client) {

		JsonObject joInput = new JsonObject();
		joInput.put("folderId", 3);
		System.out.println("joInput:" + joInput);

		try {
			client.post(port, host, "/v1/service/odds/folder/id/fetch")
					
					.putHeader("Authorization", CallAuth.token)
					.putHeader("agentSession", "agentSession")
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

	public void folderQuestionAssign(WebClient client) {

		JsonObject joInput = new JsonObject();
		joInput.put("folderId", 3);
		joInput.put("questionId", 1);
		
		System.out.println("joInput:" + joInput);

		try {
			client.post(port, host, "/v1/service/odds/folder/assign/question")
					
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

	public void folderQuestionUnAssign(WebClient client) {

		JsonObject joInput = new JsonObject();
		joInput.put("folderId", 3);
		joInput.put("questionId", 1);
		System.out.println("joInput:" + joInput);

		try {
			client.post(port, host, "/v1/service/odds/folder/unaasign/question")
					
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

	public void folderQuestionFetch(WebClient client) {

		JsonObject joInput = new JsonObject();
		joInput.put("folderId", 3);
		System.out.println("joInput:" + joInput);

		try {
			client.post(port, host, "/v1/service/odds/folder/fetch/question")
					
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