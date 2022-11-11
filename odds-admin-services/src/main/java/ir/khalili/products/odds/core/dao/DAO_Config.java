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
        		+ "  FROM toppconfig c where league_Id = ? or league_Id is null", params, handler -> {
            if (handler.failed()) {
            	logger.error("Unable to get accessQueryResult:", handler.cause());
                promise.fail(new DAOEXCP_Internal(-100, "خطای داخلی. با راهبر سامانه تماس بگیرید."));
            } else {
                if (null == handler.result() || null == handler.result().getRows() || handler.result().getRows().isEmpty()) {
                	logger.error("fetchAllConfigNoDataFound");
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
                	logger.error("fetchConfigByIdNoDataFound");
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
                	logger.error("fetchBySymbolNoDataFound");
                    promise.fail(new DAOEXCP_Internal(-100, "داده ای یافت نشد"));
                } else {
                    logger.trace("fetchAllConfigBySymbolSuccessful");
                    promise.complete(handler.result().getRows().get(0));
                }
            
            }
        });

        return promise.future();
    }
     
    public static Future<JsonObject> fetchBySymbol(SQLConnection sqlConnection, int leagueId, String symbol) {
        Promise<JsonObject> promise = Promise.promise();
        JsonArray params = new JsonArray();
        params.add(leagueId);
        params.add(symbol);
        
        sqlConnection.queryWithParams("SELECT "
        		+ "c.id,"
        		+ "c.NAME,"
        		+ "c.SYMBOL,"
        		+ "c.type,"
        		+ "c.value "
        		+ "  FROM toppconfig c WHERE c.league_Id = ? and c.SYMBOL=?", params, handler -> {
            if (handler.failed()) {
            	logger.error("Unable to get accessQueryResult:", handler.cause());
                promise.fail(new DAOEXCP_Internal(-100, "خطای داخلی. با راهبر سامانه تماس بگیرید."));
            } else {
                if (null == handler.result() || null == handler.result().getRows() || handler.result().getRows().isEmpty()) {
                    promise.fail(new DAOEXCP_Internal(-100, "کانفیگ مورد نظر یافت نشد"));
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
		params.add(new JsonArray().add("مبلغ بسته برنزی").add("AMOUNT_BRONZE_PACKAGE").add("200000").add("Number").add(leagueId));
		params.add(new JsonArray().add("مبلغ بسته نقره ای").add("AMOUNT_SILVER_PACKAGE").add("500000").add("Number").add(leagueId));
		params.add(new JsonArray().add("مبلغ بسته طلایی").add("AMOUNT_GOLDEN_PACKAGE").add("1000000").add("Number").add(leagueId));
		params.add(new JsonArray().add("امتیاز بسته برنزی").add("POINT_BRONZE_PACKAGE").add("10").add("Number").add(leagueId));
		params.add(new JsonArray().add("امتیاز بسته نقره ای").add("POINT_SILVER_PACKAGE").add("50").add("Number").add(leagueId));
		params.add(new JsonArray().add("امتیاز بسته طلایی").add("POINT_GOLDEN_PACKAGE").add("100").add("Number").add(leagueId));
		params.add(new JsonArray().add("شناسه لیگ فعال در پنل کاربری").add("ACTIVE_LEAGUE_ID").add("1").add("Number").add(leagueId));
		params.add(new JsonArray().add("محدودیت خرید روزانه").add("LIMITATION_BUY_PACKAGE").add("5000000").add("Number").add(leagueId));
		params.add(new JsonArray().add("ضریب حداقل").add("MINIMUM_COEFFICIENT").add("2").add("Number").add(leagueId));
		params.add(new JsonArray().add("امتیاز هر تراکنش").add("POINTS_PER_TRANSACTION").add("10").add("Number").add(leagueId));
		params.add(new JsonArray().add("مبلغ هر تراکنش").add("AMOUNT_PER_TRANSACTION").add("50000").add("Number").add(leagueId));
		params.add(new JsonArray().add("شناسه بسته برنزی").add("PRODUCT_BRONZE_PACKAGE").add("1").add("Number").add(leagueId));
		params.add(new JsonArray().add("شناسه بسته نقره ای").add("PRODUCT_SILVER_PACKAGE").add("1").add("Number").add(leagueId));
		params.add(new JsonArray().add("شناسه بسته طلایی").add("PRODUCT_GOLDEN_PACKAGE").add("1").add("Number").add(leagueId));
		params.add(new JsonArray().add("محدودیت خرید روزانه").add("LIMITATION_BUY_PACKAGE").add("5000000").add("Number").add(leagueId));
//		params.add(new JsonArray().add("ضریب حداقل").add("MINIMUM_COEFFICIENT").add("2").add("Number").add(leagueId));
		params.add(new JsonArray().add("امتیاز هر تراکنش").add("POINTS_PER_TRANSACTION").add("10").add("Number").add(leagueId));
		params.add(new JsonArray().add("مبلغ هر تراکنش").add("AMOUNT_PER_TRANSACTION").add("50000").add("Number").add(leagueId));
		params.add(new JsonArray().add("شناسه گروه پیشفرض").add("DEFAULT_GROUP_ID").add("1").add("Number").add(leagueId));

		
		sqlConnection.batchWithParams("insert into toppconfig (ID,NAME,SYMBOL,VALUE,TYPE,LEAGUE_ID) values(soppconfig.nextval,?,?,?,?,?)" , params, resultHandler->{
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
	
    public static Future<Integer> fetchActiveLeague(SQLConnection sqlConnection) {
        Promise<Integer> promise = Promise.promise();
        JsonArray params = new JsonArray();
        
        sqlConnection.queryWithParams("SELECT "
        		+ "c.id,"
        		+ "c.NAME,"
        		+ "c.SYMBOL,"
        		+ "c.type,"
        		+ "c.value "
        		+ "  FROM toppconfig c WHERE c.league_Id is null and c.SYMBOL='ACTIVE_LEAGUE_ID'", params, handler -> {
            if (handler.failed()) {
            	logger.error("Unable to get accessQueryResult:", handler.cause());
                promise.fail(new DAOEXCP_Internal(-100, "خطای داخلی. با راهبر سامانه تماس بگیرید."));
            } else {
                if (null == handler.result() || null == handler.result().getRows() || handler.result().getRows().isEmpty()) {
                    promise.fail(new DAOEXCP_Internal(-100, "داده ای یافت نشد"));
                } else {
                    logger.trace("fetchAllConfigBySymbolSuccessful");
                    promise.complete(Integer.parseInt(handler.result().getRows().get(0).getString("VALUE")));
                }
            
            }
        });

        return promise.future();
    }

	public static Future<String> fetchSysdate(SQLConnection sqlConnection) {
		Promise<String> promise = Promise.promise();

		sqlConnection.query("SELECT To_Char(sysdate,'YYYY-MM-DD\"T\"HH24:MI:SS\"Z\"') X FROM DUAL", handler -> {
			if (handler.failed()) {
				logger.error("Unable to get accessQueryResult:", handler.cause());
				promise.fail(new DAOEXCP_Internal(-100, "خطای داخلی. با راهبر سامانه تماس بگیرید."));
			} else {
				logger.trace("fetchAllLeagueByIdSuccessful");
				promise.complete(handler.result().getRows().get(0).getString("X"));
			}
		});

		return promise.future();
	}


}
