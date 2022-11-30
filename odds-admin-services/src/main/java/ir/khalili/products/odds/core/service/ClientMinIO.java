package ir.khalili.products.odds.core.service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Base64;
import java.util.Date;

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
        JsonObject joMinIO = EntryPoint.joConfig.getJsonObject("minio");

        String accessKey = joMinIO.getString("accessKey");
        String secretKey = joMinIO.getString("secretKey");
        String endpoint = joMinIO.getString("endpoint");
        
        minioClient = MinioClient.builder().endpoint(endpoint).credentials(accessKey, secretKey).build();
    }
    
	public static Future<String> saveTeamImage(Vertx vertx, String league, String team, String base64Data) {
		
		return saveImage(vertx, league, "team", team, base64Data);
		
	}

	public static Future<String> saveLocationImage(Vertx vertx, String league, String stadium, String base64Data) {

		return saveImage(vertx, league, "stadium", stadium, base64Data);
	}
	
	private static Future<String> saveImage(Vertx vertx, String league, String folder, String fileName, String base64Data) {

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
	
	public static Future<String> saveFile(Vertx vertx, String league, String symbol, String base64Data) {

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
    						.object(league + "/file/" + symbol + "_" + new Date().getTime() + ".txt")
    						.stream(fis, bI.length, -1)
    						.contentType("text/plain")
    						.build());
    			}

    			result = "https://io.paypod.linuxservice.ir/odds/" + league + "/file/" + symbol + "_" + new Date().getTime() + ".txt";
    			
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
	
	public static String saveFile(String league, String symbol, String value) {

		String result = null;
		
		try {
			if (!minioClient.bucketExists(BucketExistsArgs.builder().bucket("odds").build())) {
				minioClient.makeBucket(MakeBucketArgs.builder().bucket("odds").build());
			}

			byte[] bI = value.getBytes();;

			Date date = new Date();
			
			try(InputStream fis = new ByteArrayInputStream(bI)){
				minioClient.putObject(PutObjectArgs
						.builder()
						.bucket("odds")
						.object(league + "/file/" + symbol + "_" + date.getTime() + ".txt")
						.stream(fis, bI.length, -1)
						.contentType("text/plain")
						.build());
			}

			result = "https://io.paypod.linuxservice.ir/odds/" + league + "/file/" + symbol + "_" + date.getTime() + ".txt";
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	public static String saveImage(String league, String symbol, String base64Data) {

		String result = null;
		
		try {
			if (!minioClient.bucketExists(BucketExistsArgs.builder().bucket("odds").build())) {
				minioClient.makeBucket(MakeBucketArgs.builder().bucket("odds").build());
			}

			byte[] bI = Base64.getDecoder().decode(base64Data.getBytes());

			Date date = new Date();
			
			try(InputStream fis = new ByteArrayInputStream(bI)){
				minioClient.putObject(PutObjectArgs
						.builder()
						.bucket("odds")
						.object(league + "/file/" + symbol + "_" + date.getTime() + ".jpg")
						.stream(fis, bI.length, -1)
						.contentType("image/png")
						.build());
			}

			result = "https://io.paypod.linuxservice.ir/odds/" + league + "/file/" + symbol + "_" + date.getTime() + ".jpg";
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
}