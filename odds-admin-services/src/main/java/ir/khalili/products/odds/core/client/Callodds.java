package ir.khalili.products.odds.core.client;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.ext.web.client.WebClient;

/**
 * @author A.KH
 */
public class Callodds extends AbstractVerticle {

	private static final int port = 6060;
	private static final String host  ="127.0.0.1";
//	private static final String host  ="185.213.167.156";
	
	public static void main(String[] args) {

		System.out.println("CallService STARTING ......");
		Vertx vertx = Vertx.vertx();
		vertx.deployVerticle(new Callodds());
	}

	@Override
	public void start() throws Exception {
		WebClient client = WebClient.create(vertx);
	}

	
}