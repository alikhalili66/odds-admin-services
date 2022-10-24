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
				"(select username from toppuser where id=t.USER_ID) as USERNAME, " +
				"to_char(t.creationDate,'YYYY-MM-DD HH24:MI:SS.mm') as CREATIONDATE " +
				" FROM topptransaction t where t.id=?", params, resultHandler->{
			if(resultHandler.failed()) {
				logger.error("Unable to get accessQueryResult:", resultHandler.cause());
				promise.fail(new DAOEXCP_Internal(-100, "خطای داخلی. با راهبر سامانه تماس بگیرید."));
				return;
			}
			
			if(null == resultHandler.result() || null == resultHandler.result().getRows() || resultHandler.result().getRows().isEmpty()) {
				logger.warn("TransactionFetchByTransactionIdNotFound");
				promise.complete(new JsonObject());
			}else {
				logger.trace("TransactionFetchByTransactionIdRESULT:"+ resultHandler.result().getRows().get(0));
				promise.complete(resultHandler.result().getRows().get(0));
			}
			
		});
		
		return promise.future();
	}

    
	public static Future<List<JsonObject>> fetchAllTransaction(SQLConnection sqlConnection, JsonObject message) {
		
		Promise<List<JsonObject>> promise = Promise.promise();
		
		JsonArray params = new JsonArray();
		params.add(message.getInteger("transactionId"));
		params.add(message.getInteger("userId"));
		params.add(message.getString("status"));
		params.add(message.getInteger("startIndex"));
		params.add(message.getInteger("endIndex"));
		
		sqlConnection.queryWithParams("SELECT * FROM (select "+
				"t.ID," + 
				"t.POINT," + 
				"t.AMOUNT," + 
				"t.TYPE," +
				"t.STATUS," +
				"t.USER_ID," +
				"t.APPLICATIONCODE," +
				"t.INVOICEID," +
				"t.DESCRIPTION," +
				"to_char(t.creationDate,'YYYY-MM-DD HH24:MI:SS.mm')   as CREATIONDATE,"
				+ " row_number() over (ORDER BY t.id desc) line_number" 
				+ " FROM topptransaction t where t.id=nvl(?,t.id) and t.USER_ID=nvl(?,t.USER_ID) and t.status=nvl(?,t.status) " 
				+ "  ) WHERE line_number BETWEEN ? AND ?", params, resultHandler->{
			if(resultHandler.failed()) {
				logger.error("Unable to get accessQueryResult:", resultHandler.cause());
				promise.fail(new DAOEXCP_Internal(-100, "خطای داخلی. با راهبر سامانه تماس بگیرید."));
				return;
			}
			
			if(null == resultHandler.result() || null == resultHandler.result().getRows() || resultHandler.result().getRows().isEmpty()) {
				logger.warn("TransactionNotFound");
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
    
    

}
