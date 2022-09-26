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

	private static final int port = 6060;
	private static final String host = "127.0.0.1";
//	private static final String host  ="185.213.167.156";

	public static void main(String[] args) {

		System.out.println("CallOtp STARTING ......");
		Vertx vertx = Vertx.vertx();
		vertx.deployVerticle(new CallLeague());
	}

	@Override
	public void start() throws Exception {
		WebClient client = WebClient.create(vertx);
		customerPostcodeUpdate(client);
		customerMailUpdate(client);
		customerCellphoneUpdate(client);
		customerPhoneUpdate(client);
		customerPostcodeInquery(client);
	}

	public void customerPhoneUpdate(WebClient client) {

		JsonObject joInput = new JsonObject();
		joInput.put("phone", 2188553432L);
		joInput.put("code", 123456);
		System.out.println("joInput:" + joInput);

		try {
			client.post(port, host, "/v1/service/nas/customer/update/phone")
					.putHeader("API-KEY", CallAuth.API_KEY)
					.putHeader("Authorization", CallAuth.token)
					.putHeader("customerSession", "customerSession")
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

	public void customerCellphoneUpdate(WebClient client) {

		JsonObject joInput = new JsonObject();
		joInput.put("cellphone", 9359308163L);
		System.out.println("joInput:" + joInput);

		try {
			client.post(port, host, "/v1/service/nas/customer/update/cellphone")
					.putHeader("API-KEY", CallAuth.API_KEY)
					.putHeader("Authorization", CallAuth.token)
					.putHeader("customerSession", "customerSession")
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

	public void customerMailUpdate(WebClient client) {

		JsonObject joInput = new JsonObject();
		joInput.put("email", "amir.mail@gmail.com");
		joInput.put("code", 123456);
		System.out.println("joInput:" + joInput);

		try {
			client.post(port, host, "/v1/service/nas/customer/update/mail").putHeader("API-KEY", CallAuth.API_KEY)
					.putHeader("Authorization", CallAuth.token)
					.putHeader("customerSession", "customerSession")
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

	public void customerPostcodeUpdate(WebClient client) {

		JsonObject joInput = new JsonObject();
		joInput.put("postcode", 3158633519L);
		System.out.println("joInput:" + joInput);

		try {
			client.post(port, host, "/v1/service/nas/customer/update/postcode")
					.putHeader("API-KEY", CallAuth.API_KEY)
					.putHeader("Authorization", CallAuth.token)
					.putHeader("customerSession", "customerSession")
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

	public void customerPostcodeInquery(WebClient client) {

		JsonObject joInput = new JsonObject();
		joInput.put("postcode", 3158633519L);
//		joInput.put("username", "caspUser"); // optional
//		joInput.put("password", "casp00123"); // optional
		System.out.println("joInput:" + joInput);

		try {
			client.post(port, host, "/v1/service/nas/customer/inquery/postcode")
					.putHeader("API-KEY", CallAuth.API_KEY)
					.putHeader("Authorization", CallAuth.token)
					.putHeader("customerSession", "customerSession")
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

	
}