package ir.khalili.products.odds.core.client;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;

/**
 * @author A.KH
 */
public class CallLocation extends AbstractVerticle {

	private static final int port = 9090;
	private static final String host  ="127.0.0.1";
//	private static final String host  ="185.213.167.156";
	
	public static void main(String[] args) {

		System.out.println("CallLocation STARTING ......");
		Vertx vertx = Vertx.vertx();
		vertx.deployVerticle(new CallLocation());
	}

	@Override
	public void start() throws Exception {

		WebClient client = WebClient.create(vertx);
//		locationSave(client);
		locationUpdate(client);
//		locationDelete(client);
//		locationFetchAll(client);
//		competitionLocationAssign(client);
	}

	public void locationSave(WebClient client) {
		JsonObject joInput = new JsonObject();
		joInput.put("leagueId", 1);
		joInput.put("name", "Location1");
		joInput.put("description", "desc1");
		joInput.put("image", "iVBORw0KGgoAAAANSUhEUgAAAGQAAABkAQAAAABYmaj5AAAA7ElEQVR42tXUsZHEIAwFUHk2cHZuQDO0QeaWTAN4twK3REYbzNAAyhww1ombvd1NbBHeMQS8CPERAH+MAn9YBWCBzAEGTcR13W8cZaEpoLdpiuA6tIb86JWhHnH1tq7vyk4l53MR3fu0p2pZzbJ8JXiqYtHP6H53uBAH3mKadpg0HRZhRrCZNBHzxnWIadBUbILRbK/KzkXxRhEHNpumMuLXLPOZ4IVoz4flA5LTlTzkO+CkqeU/Sgy65G59q92QptbXLIEZVhXQsblDlxZIy8iPDsmrIn5mdiWui/QCoKr2pq35CUPRf/nBPvUNct67nP2Y9j8AAAAASUVORK5CYII=");
		
		
		System.out.println("joInput:" + joInput);
		try {
			client.post(port, host, "/v1/service/odds/location/save").putHeader("Authorization", CallAuth.token).sendJson(joInput, ar -> {
				try {
					if (ar.succeeded()) {
						JsonObject response = new JsonObject(ar.result().bodyAsString());
						System.out.println(Json.encodePrettily(response));
					} else {
						System.out.println(ar.cause());
					}
				}catch(Exception e) {
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
	
	public void locationUpdate(WebClient client) {
		
		JsonObject joInput = new JsonObject();
		joInput.put("locationId", 11);
		joInput.put("name", "Location2");
		joInput.put("description", "desc2");
		joInput.put("image", "iVBORw0KGgoAAAANSUhEUgAAAGQAAABkAQAAAABYmaj5AAAA7ElEQVR42tXUsZHEIAwFUHk2cHZuQDO0QeaWTAN4twK3REYbzNAAyhww1ombvd1NbBHeMQS8CPERAH+MAn9YBWCBzAEGTcR13W8cZaEpoLdpiuA6tIb86JWhHnH1tq7vyk4l53MR3fu0p2pZzbJ8JXiqYtHP6H53uBAH3mKadpg0HRZhRrCZNBHzxnWIadBUbILRbK/KzkXxRhEHNpumMuLXLPOZ4IVoz4flA5LTlTzkO+CkqeU/Sgy65G59q92QptbXLIEZVhXQsblDlxZIy8iPDsmrIn5mdiWui/QCoKr2pq35CUPRf/nBPvUNct67nP2Y9j8AAAAASUVORK5CYII=");
		
		System.out.println("joInput:" + joInput);
		
		try {
			client.post(port, host, "/v1/service/odds/location/update").putHeader("Authorization", CallAuth.token).sendJson(joInput, ar -> {
				try {
					if (ar.succeeded()) {
						JsonObject response = new JsonObject(ar.result().bodyAsString());
						System.out.println(Json.encodePrettily(response));
					} else {
						System.out.println(ar.cause());
					}
				}catch(Exception e) {
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
	
	public void locationDelete(WebClient client) {
		JsonObject joInput = new JsonObject();
		joInput.put("locationId", 1);
		
		System.out.println("joInput:" + joInput);
		try {
			client.post(port, host, "/v1/service/odds/location/delete").putHeader("Authorization", CallAuth.token).sendJson(joInput, ar -> {
				try {
					if (ar.succeeded()) {
						JsonObject response = new JsonObject(ar.result().bodyAsString());
						System.out.println(Json.encodePrettily(response));
					} else {
						System.out.println(ar.cause());
					}
				}catch(Exception e) {
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
	
	
	public void locationFetchAll(WebClient client) {
		try {
			
			JsonObject joInput = new JsonObject();
			joInput.put("leagueId", 1);
			
			client
			.post(port, host, "/v1/service/odds/location/all/fetch")
			.putHeader("Authorization", CallAuth.token)
			
			.sendJson(joInput, ar -> {
				try {
					if (ar.succeeded()) {
						JsonObject response = new JsonObject(ar.result().bodyAsString());
						System.out.println(Json.encodePrettily(response));
					} else {
						System.out.println(ar.cause());
					}
				}catch(Exception e) {
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
	
	public void competitionLocationAssign(WebClient client) {
		JsonObject joInput = new JsonObject();
		
		joInput.put("competitionId", 1);
		joInput.put("locationId", 1);
		
		System.out.println("joInput:" + joInput);
		try {
			client.post(port, host, "/v1/service/odds/location/competition/assign").putHeader("Authorization", CallAuth.token).sendJson(joInput, ar -> {
				try {
					if (ar.succeeded()) {
						JsonObject response = new JsonObject(ar.result().bodyAsString());
						System.out.println(Json.encodePrettily(response));
					} else {
						System.out.println(ar.cause());
					}
				}catch(Exception e) {
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