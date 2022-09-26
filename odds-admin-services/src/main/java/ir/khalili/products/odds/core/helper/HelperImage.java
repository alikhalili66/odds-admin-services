package ir.khalili.products.odds.core.helper;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Base64;

import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;

public class HelperImage {

	public static Future<String> getImage(Vertx vertx, String imagePath) {
		
		Promise<String> promise = Promise.promise();
		
		vertx.executeBlocking(blockingHandler -> {
			
			String image = "";
			try {
				if(null != imagePath) {
					File file = new File(imagePath);
					try(FileInputStream fileInputStreamReader = new FileInputStream(file)) {
						
						byte[] bytes = new byte[(int)file.length()];
						fileInputStreamReader.read(bytes);
						
						image = new String(Base64.getEncoder().encodeToString(bytes));
						
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			blockingHandler.complete(image);
			
		}, resultHandler->{
			if(resultHandler.failed()) {
				promise.fail(resultHandler.cause());
			}else{
				promise.complete((String) resultHandler.result());
			}
		});
		
		return promise.future();
		
	}
	
}
