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
		configUpdate(client);
//		configFetchAll(client);
//		configFetchById(client);
//		configFetchBySymbol(client);
	}

	public void configUpdate(WebClient client) {

		JsonObject joInput = new JsonObject();
		joInput.put("configId", 139);
		joInput.put("value", "iVBORw0KGgoAAAANSUhEUgAAACoAAAArCAYAAAAOnxr+AAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAAJcEhZcwAAEnQAABJ0Ad5mH3gAAADSSURBVFhH7dcxDsIwEERR5/4ngXA/SBVwgbSsv7xYkwARW7zO8vwmgZRyua3eNPeV83VfJ1DmZfUozsLLX9Qzgp8JpZBIhvZQSCRDeygkss/rqZ4RZGijnhFkaKOe2difh+7g0KFfl6Ee/CyPmECGKjK0US9WvPvCl9H4iAz1aHxEhno0PiJDPRof8bFQVYZuYX78lXvKUI8ekAjFWTikopAIxVk4pKKQCMVZOKSikAjFWTikopAIxVk4pKKQCMVZOKSikAGH+RTJ0Ea9WNCGLusdZ4yVSRll4+4AAAAASUVORK5CYII=");
		
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

	public void configFetchAll(WebClient client) {

		try {
			
			JsonObject joInput = new JsonObject();
			joInput.put("leagueId", 1);
			
			client.post(port, host, "/v1/service/odds/config/all/fetch")
					
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

	public void configFetchById(WebClient client) {

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

	public void configFetchBySymbol(WebClient client) {

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