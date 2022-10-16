package ir.khalili.products.odds.core.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.sql.SQLConnection;
import ir.khalili.products.odds.core.excp.dao.DAOEXCP_Internal;

public class DAO_Config {

    private static final Logger logger = LogManager.getLogger(DAO_Config.class);
    
    public static Future<Void> update(SQLConnection sqlConnection, int configId, String value) {

		Promise<Void> promise = Promise.promise();
		
		JsonArray params = new JsonArray();
		
		params.add(value);
		params.add(configId);
		
		sqlConnection.updateWithParams(""
				+ "update toppconfig c set "
				+ "c.VALUE=? "
				+ " where c.id=? ", params, resultHandler->{
			if(resultHandler.failed()) {
				logger.error("Unable to get accessQueryResult:", resultHandler.cause());
				promise.fail(new DAOEXCP_Internal(-100, "خطای داخلی. با راهبر سامانه تماس بگیرید."));
				return;
			}
			
			logger.trace("UpdateConfigSuccessful");
			promise.complete();
			
		});
		
		return promise.future();
	}
    
    public static Future<List<JsonObject>> fetchAll(SQLConnection sqlConnection, int leagueId) {
        Promise<List<JsonObject>> promise = Promise.promise();
        
        JsonArray params = new JsonArray();
        params.add(leagueId);
        
        sqlConnection.queryWithParams("SELECT "
        		+ "c.id,"
        		+ "c.NAME,"
        		+ "c.SYMBOL,"
        		+ "c.type,"
        		+ "c.value "
        		+ "  FROM toppconfig c where league_Id = ?", params, handler -> {
            if (handler.failed()) {
            	logger.error("Unable to get accessQueryResult:", handler.cause());
                promise.fail(new DAOEXCP_Internal(-100, "خطای داخلی. با راهبر سامانه تماس بگیرید."));
            } else {
                if (null == handler.result() || null == handler.result().getRows() || handler.result().getRows().isEmpty()) {
                	promise.complete(new ArrayList<>());
                } else {
                    logger.trace("fetchAllConfigSuccessful");
                    promise.complete(handler.result().getRows());
                }
            
            }
        });

        return promise.future();
    }
        
    public static Future<JsonObject> fetchById(SQLConnection sqlConnection, Integer configId) {
        Promise<JsonObject> promise = Promise.promise();
        JsonArray params = new JsonArray();
        params.add(configId);
        
        sqlConnection.queryWithParams("SELECT "
        		+ "c.id,"
        		+ "c.NAME,"
        		+ "c.SYMBOL,"
        		+ "c.type,"
        		+ "c.LEAGUE_ID,"
        		+ "c.value "
        		+ "  FROM toppconfig c WHERE c.id=?", params, handler -> {
            if (handler.failed()) {
            	logger.error("Unable to get accessQueryResult:", handler.cause());
                promise.fail(new DAOEXCP_Internal(-100, "خطای داخلی. با راهبر سامانه تماس بگیرید."));
            } else {
                if (null == handler.result() || null == handler.result().getRows() || handler.result().getRows().isEmpty()) {
                    promise.fail(new DAOEXCP_Internal(-100, "داده ای یافت نشد"));
                } else {
                    logger.trace("fetchAllConfigByIdSuccessful");
                    promise.complete(handler.result().getRows().get(0));
                }
            
            }
        });

        return promise.future();
    }
      
    public static Future<JsonObject> fetchBySymbol(SQLConnection sqlConnection, String symbol) {
        Promise<JsonObject> promise = Promise.promise();
        JsonArray params = new JsonArray();
        params.add(symbol);
        
        sqlConnection.queryWithParams("SELECT "
        		+ "c.id,"
        		+ "c.NAME,"
        		+ "c.SYMBOL,"
        		+ "c.type,"
        		+ "c.value "
        		+ "  FROM toppconfig c WHERE c.SYMBOL=?", params, handler -> {
            if (handler.failed()) {
            	logger.error("Unable to get accessQueryResult:", handler.cause());
                promise.fail(new DAOEXCP_Internal(-100, "خطای داخلی. با راهبر سامانه تماس بگیرید."));
            } else {
                if (null == handler.result() || null == handler.result().getRows() || handler.result().getRows().isEmpty()) {
                    promise.fail(new DAOEXCP_Internal(-100, "داده ای یافت نشد"));
                } else {
                    logger.trace("fetchAllConfigBySymbolSuccessful");
                    promise.complete(handler.result().getRows().get(0));
                }
            
            }
        });

        return promise.future();
    }
     
	public static Future<Void> doSaveConfigLeague(SQLConnection sqlConnection, int leagueId) {

		Promise<Void> promise = Promise.promise();
		
		List<JsonArray> params = new ArrayList<>();
		params.add(new JsonArray().add("قوانین و مقررات").add("TERMS_CONDITIONS").add("/app/odds/config/S_1665312994399.txt").add("File").add(leagueId));
		params.add(new JsonArray().add("امتیاز هدیه اولیه").add("PRIMARY_GIFT_POINTS").add("100").add("Number").add(leagueId));
		params.add(new JsonArray().add("حداقل امتیاز هر سوال").add("MINIMUM_SCORE_QUESTION").add("10").add("Number").add(leagueId));
		
		sqlConnection.batchWithParams(
				"insert into toppconfig (ID,NAME,SYMBOL,VALUE,TYPE,LEAGUE_ID) values(soppconfig.nextval,?,?,?,?,?)" 
				, params, resultHandler->{
			if(resultHandler.failed()) {
				logger.error("Unable to get accessQueryResult:", resultHandler.cause());
				promise.fail(new DAOEXCP_Internal(-100, "خطای داخلی. با راهبر سامانه تماس بگیرید."));
				return;
			}
			
			logger.trace("doSaveAgentConfigSuccessful");
			promise.complete();
			
		});
		
		return promise.future();
	}
}
