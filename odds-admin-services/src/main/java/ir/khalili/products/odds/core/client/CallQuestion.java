package ir.khalili.products.odds.core.client;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;

/**
 * @author A.KH
 */
public class CallQuestion extends AbstractVerticle{

	private static final int port = 9090;
	private static final String host = "127.0.0.1";
//	private static final String host  ="185.213.167.156";

	public static void main(String[] args) {

		System.out.println("CallQuestion STARTING ......");
		Vertx vertx = Vertx.vertx();
		vertx.deployVerticle(new CallQuestion());
	}

	@Override
	public void start() throws Exception {
		WebClient client = WebClient.create(vertx);
//		questionSave(client);
//		questionUpdate(client);
		questionDelete(client);
//		questionFetchAll(client);
//		questionFetchById(client);
	}

	public void questionSave(WebClient client) {

		JsonObject joInput = new JsonObject();
		joInput.put("leagueId", 2);
		joInput.put("question", "کدام کشور می بازد");
		joInput.put("symbol", "کشور اتیوپی");
		joInput.put("type", "H");
		joInput.put("minPoint", 0);
		
		System.out.println("joInput:" + joInput);

		try {
			client.post(port, host, "/v1/service/odds/question/save")
					
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

	public void questionUpdate(WebClient client) {

		JsonObject joInput = new JsonObject();
		joInput.put("questionId", 4);
		joInput.put("leagueId", 2);
		joInput.put("question", "کدام کشور میتواند");
		joInput.put("symbol", "کشور ایران");
		joInput.put("type", "H");
		joInput.put("minPoint", 40);
		System.out.println("joInput:" + joInput);

		try {
			client.post(port, host, "/v1/service/odds/question/update")
					
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

	public void questionDelete(WebClient client) {

		JsonObject joInput = new JsonObject();
		joInput.put("questionId", 23);
		System.out.println("joInput:" + joInput);

		try {
			client.post(port, host, "/v1/service/odds/question/delete")
					
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

	public void questionFetchAll(WebClient client) {

		JsonObject joInput = new JsonObject();
//		joInput.put("leagueId", 1);
		
		System.out.println("joInput:" + joInput);

		try {
			client.post(port, host, "/v1/service/odds/question/all/fetch")
					
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

	public void questionFetchById(WebClient client) {

		JsonObject joInput = new JsonObject();
		joInput.put("questionId", 1);
		System.out.println("joInput:" + joInput);

		try {
			client.post(port, host, "/v1/service/odds/question/id/fetch")
					
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