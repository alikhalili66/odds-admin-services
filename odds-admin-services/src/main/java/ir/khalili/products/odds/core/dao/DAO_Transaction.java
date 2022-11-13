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

public class DAO_Transaction {

    private static final Logger logger = LogManager.getLogger(DAO_Transaction.class);
    
    public static Future<Integer> saveTransaction(SQLConnection sqlConnection, int point, int amount, int userId, String applicationCode, String invoiceId, String description, String date) {

        Promise<Integer> promise = Promise.promise();

        sqlConnection.query("SELECT SOPPTRANSACTION.NEXTVAL ID FROM DUAL ", seqtHandler -> {
            if (seqtHandler.failed()) {
                logger.error("Unable to get accessQueryResult:", seqtHandler.cause());
                promise.fail(new DAOEXCP_Internal(-100, "خطای داخلی. با راهبر سامانه تماس بگیرید."));
            } else {
                if (null == seqtHandler.result() || null == seqtHandler.result().getRows() || seqtHandler.result().getRows().isEmpty()) {
                    promise.fail(new DAOEXCP_Internal(-100, "اطلاعات مورد نظر موجود نمی باشد."));
                } else {
                    logger.trace("fetchTransactionID");
                    
                    JsonArray params = new JsonArray();
                    params.add(seqtHandler.result().getRows().get(0).getInteger("ID"));
                    params.add(point);
                    params.add(amount);
                    params.add("P");
                    params.add("C");
                    params.add(userId);
                    params.add(applicationCode);
                    params.add(invoiceId);
                    params.add(description);
                    params.add(date);
                    
                    System.out.println(params);

                    sqlConnection.updateWithParams("INSERT INTO TOPPTRANSACTION (" +
                            "    ID," +
                            "    POINT," +
                            "    AMOUNT," +
                            "    TYPE," +
                            "    STATUS," +
                            "    USER_ID," +
                            "    APPLICATIONCODE," +
                            "    INVOICEID," +
                            "    DESCRIPTION," +
                            "    CREATIONDATE" +
                            ") VALUES (?,?,?,?,?,?,?,?,?,TO_Date(?,'YYYY/MM/DD HH24:MI:SS'))", params, resultHandler -> {
                        if (resultHandler.failed()) {
                            logger.error("Unable to get accessQueryResult:", resultHandler.cause());
                            promise.fail(new DAOEXCP_Internal(-100, "خطای داخلی. با راهبر سامانه تماس بگیرید."));
                            return;
                        }
                        logger.trace("saveUserHistoryPointByAppTransaction");
                        promise.complete(seqtHandler.result().getRows().get(0).getInteger("ID"));

                    });
                }
            }
        });
        
        return promise.future();
    
    }

	public static Future<JsonObject> fetchTransactionById(SQLConnection sqlConnection, Integer id) {
		
		Promise<JsonObject> promise = Promise.promise();
		
		JsonArray params = new JsonArray();
		params.add(id);
		
		sqlConnection.queryWithParams("select "+
				"t.ID," + 
				"t.POINT," + 
				"t.AMOUNT," + 
				"t.TYPE," +
				"t.STATUS," +
				"t.USER_ID," +
				"t.APPLICATIONCODE," +
				"t.INVOICEID," +
				"t.DESCRIPTION," +
				"t.TRANSACTIONID," +
				"t.USERNAME," +
				"to_char(t.creationDate,'YYYY-MM-DD HH24:MI:SS.mm') as CREATIONDATE " +
				" FROM topptransaction t where t.id=?", params, resultHandler->{
			if(resultHandler.failed()) {
				logger.error("Unable to get accessQueryResult:", resultHandler.cause());
				promise.fail(new DAOEXCP_Internal(-100, "خطای داخلی. با راهبر سامانه تماس بگیرید."));
				return;
			}
			
			if(null == resultHandler.result() || null == resultHandler.result().getRows() || resultHandler.result().getRows().isEmpty()) {
				logger.error("TransactionFetchByTransactionIdNotFound");
				promise.fail(new DAOEXCP_Internal(-100, "داده ای یافت نشد."));
			}else {
				logger.trace("TransactionFetchByTransactionIdRESULT:"+ resultHandler.result().getRows().get(0));
				promise.complete(resultHandler.result().getRows().get(0));
			}
			
		});
		
		return promise.future();
	}

    
	public static Future<Integer> fetchCountAllTransaction(SQLConnection sqlConnection, JsonObject message) {
		
		Promise<Integer> promise = Promise.promise();
		
		JsonArray params = new JsonArray();
		params.add(message.getInteger("leagueId"));
		params.add(null == message.getString("date") ? null : message.getString("date").split("T")[0]);
		params.add(message.getString("username"));
		params.add(message.getString("status"));
		
		sqlConnection.queryWithParams("select count(*) CNT " +
				" FROM topptransaction t " +
				" where 1=1 "
				+ " and LEAGUE_ID = ? "
				+ " and trunc(CREATIONDATE) = nvl(TO_DATE(?, 'YYYY-MM-DD'),trunc(CREATIONDATE)) "
				+ " and t.username=nvl(?,t.username) "
				+ " and t.status=nvl(?,t.status) " +
				" ", params, resultHandler->{
			if(resultHandler.failed()) {
				logger.error("Unable to get accessQueryResult:", resultHandler.cause());
				promise.fail(new DAOEXCP_Internal(-100, "خطای داخلی. با راهبر سامانه تماس بگیرید."));
				return;
			}
			
			if(null == resultHandler.result() || null == resultHandler.result().getRows() || resultHandler.result().getRows().isEmpty()) {
				logger.error("TransactionCountNotFound");
				promise.complete(0);
			}else {
				logger.trace("FetchCountAllTransactionRESULT:"+ resultHandler.result().getRows().get(0).getInteger("CNT"));
				promise.complete(resultHandler.result().getRows().get(0).getInteger("CNT"));
			}
			
		});
		
		return promise.future();
	}

	
	public static Future<List<JsonObject>> fetchAllTransaction(SQLConnection sqlConnection, JsonObject message) {
		
		Promise<List<JsonObject>> promise = Promise.promise();
		
		JsonArray params = new JsonArray();
		params.add(message.getInteger("leagueId"));
		params.add(null == message.getString("date") ? null : message.getString("date").split("T")[0]);
		params.add(message.getString("username"));
		params.add(message.getString("status"));
		params.add(message.getInteger("startIndex"));
		params.add(message.getInteger("endIndex"));
		
		sqlConnection.queryWithParams("SELECT * FROM (select "+
				"t.ID," + 
				"t.TRANSACTIONID," + 
				"t.POINT," + 
				"t.AMOUNT," + 
				"t.TYPE," +
				"t.STATUS," +
				"t.APPLICATIONCODE," +
				"t.INVOICEID," +
				"t.DESCRIPTION," +
				"t.USERNAME," +
				"to_char(t.creationDate,'YYYY-MM-DD HH24:MI:SS.mm')   as CREATIONDATE," +
				" row_number() over (ORDER BY t.id desc) line_number" +
				" FROM topptransaction t " +
				" where 1=1 "
				+ " and LEAGUE_ID = ? "
				+ " and trunc(CREATIONDATE) = nvl(TO_DATE(?, 'YYYY-MM-DD'),trunc(CREATIONDATE)) "
				+ " and t.username=nvl(?,t.username) "
				+ " and t.status=nvl(?,t.status) " +
				"  ) WHERE line_number BETWEEN ? AND ?", params, resultHandler->{
			if(resultHandler.failed()) {
				logger.error("Unable to get accessQueryResult:", resultHandler.cause());
				promise.fail(new DAOEXCP_Internal(-100, "خطای داخلی. با راهبر سامانه تماس بگیرید."));
				return;
			}
			
			if(null == resultHandler.result() || null == resultHandler.result().getRows() || resultHandler.result().getRows().isEmpty()) {
				logger.error("TransactionNotFound");
				promise.complete(new ArrayList<>());
			}else {
				logger.trace("TransactionRESULT:"+ resultHandler.result().getRows().get(0));
				promise.complete(resultHandler.result().getRows());
			}
			
		});
		
		return promise.future();
	}

    public static Future<Void> updateTransactionStatus(SQLConnection sqlConnection, Integer id, String status) {
        Promise<Void> promise = Promise.promise();
        JsonArray params = new JsonArray();
        params.add(status);
        params.add(id);
        
        sqlConnection.updateWithParams("update topptransaction set status=? WHERE id=?", params, handler -> {
			if(handler.failed()) {
				logger.error("Unable to get accessQueryResult:", handler.cause());
				promise.fail(new DAOEXCP_Internal(-100, "خطای داخلی. با راهبر سامانه تماس بگیرید."));
				return;
			}
			
			logger.trace("doUpdateTransactionStatusSuccessful");
			promise.complete();
			
		});
		return promise.future();
    }
    
    public static Future<Void> updateTransactionStatus(SQLConnection sqlConnection, Integer id, String status, String transactionId) {
        Promise<Void> promise = Promise.promise();
        JsonArray params = new JsonArray();
        params.add(status);
        params.add(transactionId);
        params.add(id);
        
        sqlConnection.updateWithParams("update topptransaction set status=?, transactionId=? WHERE id=?", params, handler -> {
			if(handler.failed()) {
				logger.error("Unable to get accessQueryResult:", handler.cause());
				promise.fail(new DAOEXCP_Internal(-100, "خطای داخلی. با راهبر سامانه تماس بگیرید."));
				return;
			}
			
			logger.trace("updateTransactionStatus");
			promise.complete();
			
		});
		return promise.future();
    }
    
}
