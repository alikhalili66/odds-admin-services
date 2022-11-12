package ir.khalili.products.odds.core.service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Base64;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import ir.khalili.products.odds.core.EntryPoint;

public class ClientMinIO {

	public static MinioClient minioClient;
	
    static {
        JsonObject joLockin = EntryPoint.joConfig.getJsonObject("minio");

        String accessKey = joLockin.getString("accessKey");
        String secretKey = joLockin.getString("secretKey");
        String endpoint = joLockin.getString("endpoint");
        
        minioClient = MinioClient.builder().endpoint(endpoint).credentials(accessKey, secretKey).build();
    }
    
	public static Future<String> saveTeamImage(Vertx vertx, String league, String team, String base64Data) {
		
		return saveImage(vertx, league, "team", team, base64Data);
		
	}

	public static Future<String> saveLocationImage(Vertx vertx, String league, String stadium, String base64Data) {

		return saveImage(vertx, league, "stadium", stadium, base64Data);
	}
	
	public static Future<String> saveImage(Vertx vertx, String league, String folder, String fileName, String base64Data) {

		Promise<String> promise = Promise.promise();
		
        vertx.executeBlocking(blockingHandler -> {
        	
    		String result = null;
    		
    		try {
    			if (!minioClient.bucketExists(BucketExistsArgs.builder().bucket("odds").build())) {
    				minioClient.makeBucket(MakeBucketArgs.builder().bucket("odds").build());
    			}

    			byte[] bI = Base64.getDecoder().decode(base64Data.getBytes());

    			try(InputStream fis = new ByteArrayInputStream(bI)){
    				minioClient.putObject(PutObjectArgs
    						.builder()
    						.bucket("odds")
    						.object(league + "/" + folder + "/" + fileName + ".jpg")
    						.stream(fis, bI.length, -1)
    						.contentType("image/png")
    						.build());
    			}

    			result = "https://io.paypod.linuxservice.ir/odds/" + league + "/" + folder + "/" + fileName + ".jpg";
    			
    		} catch (Exception e) {
    			e.printStackTrace();
    		}
    		
    		blockingHandler.complete(result);
            
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