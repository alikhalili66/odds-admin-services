package ir.khalili.products.odds.core.helper;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;
import java.util.Iterator;
import java.util.List;

import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import ir.khalili.products.odds.core.EntryPoint;

public class HelperImage {

	@SuppressWarnings("unchecked")
	public static Future<List<JsonObject>> getImage(Vertx vertx, List<JsonObject> competitionList) {
		
		Promise<List<JsonObject>> promise = Promise.promise();
		
		vertx.executeBlocking(blockingHandler -> {
			
			for (Iterator<JsonObject> iterator = competitionList.iterator(); iterator.hasNext();) {
				JsonObject jsonObject = (JsonObject) iterator.next();

				try {
					
					if (jsonObject.containsKey("TEAM1_IMAGE")) { // Convert image to base64 for 2 Team
						
						if(null != jsonObject.getString("TEAM1_IMAGE")) {
							File file = new File(jsonObject.getString("TEAM1_IMAGE"));
							try(FileInputStream fileInputStreamReader = new FileInputStream(file)) {
								
								byte[] bytes = new byte[(int)file.length()];
								fileInputStreamReader.read(bytes);
								
								jsonObject.put("TEAM1_IMAGE", new String(Base64.getEncoder().encodeToString(bytes)));
								
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
						
						if(null != jsonObject.getString("TEAM2_IMAGE")) {
							File file = new File(jsonObject.getString("TEAM2_IMAGE"));
							try(FileInputStream fileInputStreamReader = new FileInputStream(file)) {
								
								byte[] bytes = new byte[(int)file.length()];
								fileInputStreamReader.read(bytes);
								
								jsonObject.put("TEAM2_IMAGE", new String(Base64.getEncoder().encodeToString(bytes)));
								
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
						
					} else {// Convert image to base64 for 1 Team
						
						if(null != jsonObject.getString("IMAGE")) {
							File file = new File(jsonObject.getString("IMAGE"));
							try(FileInputStream fileInputStreamReader = new FileInputStream(file)) {
								
								byte[] bytes = new byte[(int)file.length()];
								fileInputStreamReader.read(bytes);
								
								jsonObject.put("IMAGE", new String(Base64.getEncoder().encodeToString(bytes)));
								
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			blockingHandler.complete(competitionList);
			
		}, resultHandler->{
			if(resultHandler.failed()) {
				promise.fail(resultHandler.cause());
			}else{
				promise.complete((List<JsonObject>) resultHandler.result());
			}
		});
		
		return promise.future();
	}

	
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

    public static Future<String> saveImage(Vertx vertx, String base64, String teamName) {
        JsonObject fileManager = EntryPoint.joConfig.getJsonObject("fileManager");
        Promise<String> promise = Promise.promise();
        
        vertx.executeBlocking(blockingHandler -> {
        	
            StringBuilder resultPath = new StringBuilder(fileManager.getString("root"));
            String filePath = null;
            if (base64 != null) {
                try {
                    filePath = resultPath + "/" + teamName + "/";
                    if (!new File(filePath).exists()) {
                        Files.createDirectories(new File(filePath).toPath());
                    }
                    filePath += teamName + ".jpg";
                    Files.write(new File(filePath).toPath(), Base64.getDecoder().decode(base64));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            
            blockingHandler.complete(filePath);
            
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
