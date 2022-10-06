package ir.khalili.products.odds.core.client;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;

/**
 * @author A.KH
 */
public class CallAuth extends AbstractVerticle {

	private static final int port = 9090;
	private static final String host  ="127.0.0.1";
	
	public static String token = "Bearer " + "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOjY4MSwiZXhwIjoxNjY1MTQzMDUzLCJpcCI6IjE0MS4xMDEuNzYuNjUiLCJwcm9qZWN0SWQiOjMyMSwidHlwZSI6IlMiLCJpc0NoYW5nZVBhc3MiOmZhbHNlLCJpYXQiOjE2NjUwNTY2NTN9.TsjhT0PuAuBpPJ5LiD8eLZKJf0HXeOLw1Dh3Sjfqmb4";

	public static void main(String[] args) {

		System.out.println("CallAuth STARTING ......");
		Vertx vertx = Vertx.vertx();
		vertx.deployVerticle(new CallAuth());
	}

	@Override
	public void start() throws Exception {

		WebClient client = WebClient.create(vertx);
		
		System.out.println("token:"+token);
		
		JsonObject joAgent= new JsonObject();
		joAgent.put("username", "khalili");
		joAgent.put("password", "11111111");
		
		JsonObject joCustomer= new JsonObject();
		joCustomer.put("username", "khalili");
		joCustomer.put("password", "11111111");
		
		JsonObject joLogin= new JsonObject();
		joLogin.put("username", "khalili");
		joLogin.put("password", "11111111");
		
		JsonObject joOTP= new JsonObject();
		joOTP.put("cellphone", 9124083504l);
		joOTP.put("code", 118303);
		
		System.out.println("joCheckAccess:" + joOTP);
		try {
//			client.post(port, host, "/v1/service/odds/auth/agent").sendJson(joAgent, ar -> {
//			client.post(port, host, "/v1/service/odds/auth/customer").sendJson(joCustomer, ar -> {
			client.post(port, host, "/v1/service/odds/auth/login").sendJson(joLogin, ar -> {
//			client.post(port, host, "/v1/service/odds/auth/otp").sendJson(joOTP, ar -> {
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