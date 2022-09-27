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

	private static final int port = 6060;
	private static final String host = "127.0.0.1";
//	private static final String host  ="185.213.167.156";

	public static void main(String[] args) {

		System.out.println("CallQuestion STARTING ......");
		Vertx vertx = Vertx.vertx();
		vertx.deployVerticle(new CallLeague());
	}

	@Override
	public void start() throws Exception {
		WebClient client = WebClient.create(vertx);
		questionSave(client);
		questionUpdate(client);
		questionDelete(client);
		questionFetchById(client);
		questionFetchAll(client);
	}

	public void questionSave(WebClient client) {

		JsonObject joInput = new JsonObject();
		System.out.println("joInput:" + joInput);

		try {
			client.post(port, host, "/v1/service/odds/question/save")
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

	public void questionUpdate(WebClient client) {

		JsonObject joInput = new JsonObject();
		System.out.println("joInput:" + joInput);

		try {
			client.post(port, host, "/v1/service/odds/question/update")
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

	public void questionDelete(WebClient client) {

		JsonObject joInput = new JsonObject();
		System.out.println("joInput:" + joInput);

		try {
			client.post(port, host, "/v1/service/odds/question/delete")
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

	public void questionFetchAll(WebClient client) {

		JsonObject joInput = new JsonObject();
		System.out.println("joInput:" + joInput);

		try {
			client.post(port, host, "/v1/service/odds/question/fetch/all")
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

	public void questionFetchById(WebClient client) {

		JsonObject joInput = new JsonObject();
		System.out.println("joInput:" + joInput);

		try {
			client.post(port, host, "/v1/service/odds/question/fetch/id")
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