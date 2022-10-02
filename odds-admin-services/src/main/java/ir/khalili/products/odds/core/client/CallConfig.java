package ir.khalili.products.odds.core.client;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;

/**
 * @author A.KH
 */
public class CallConfig extends AbstractVerticle {

	private static final int port = 9090;
	private static final String host = "127.0.0.1";
//	private static final String host  ="185.213.167.156";

	public static void main(String[] args) {

		System.out.println("CallLeague STARTING ......");
		Vertx vertx = Vertx.vertx();
		vertx.deployVerticle(new CallConfig());
	}

	@Override
	public void start() throws Exception {
		WebClient client = WebClient.create(vertx);
//		leagueUpdate(client);
//		leagueFetchAll(client);
//		leagueFetchById(client);
		leagueFetchBySymbol(client);
	}

	public void leagueUpdate(WebClient client) {

		JsonObject joInput = new JsonObject();
		joInput.put("configId", 1);
		joInput.put("name", "قوانین");
		joInput.put("symbol", "S");
		joInput.put("value", "امتیازهاا از بین نمی روند");
		
		System.out.println("joInput:" + joInput);

		try {
			client.post(port, host, "/v1/service/odds/config/update")
					
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

		try {
			client.post(port, host, "/v1/service/odds/config/all/fetch")
					
					.putHeader("Authorization", CallAuth.token)
					.send(ar -> {
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
		joInput.put("configId", 1);
		System.out.println("joInput:" + joInput);

		try {
			client.post(port, host, "/v1/service/odds/config/id/fetch")
					
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

	public void leagueFetchBySymbol(WebClient client) {

		JsonObject joInput = new JsonObject();
		joInput.put("symbol", "S");
		System.out.println("joInput:" + joInput);

		try {
			client.post(port, host, "/v1/service/odds/config/symbol/fetch")
					
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