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
	
	public static Future<List<JsonObject>> fetchAllAgentConfig(SQLConnection sqlConnection, int agentId) {
		
		Promise<List<JsonObject>> promise = Promise.promise();
		
		JsonArray params = new JsonArray();
		params.add(agentId);
		
		sqlConnection.queryWithParams("select ac.config_Id, c.NAME, c.SYMBOL, ac.VALUE, c.type from tNASAgentConfig ac, tNASConfig c where ac.agent_Id = ? and ac.config_Id=c.id and c.dto is null ", params, resultHandler->{
			if(resultHandler.failed()) {
				logger.error("Unable to get accessQueryResult:", resultHandler.cause());
				promise.fail(new DAOEXCP_Internal(-100, "خطای داخلی. با راهبر سامانه تماس بگیرید."));
				return;
			}
			
			if(null == resultHandler.result() || null == resultHandler.result().getRows() || resultHandler.result().getRows().isEmpty()) {
				logger.warn("ConfigNotFound");
				promise.complete(new ArrayList<>());
			}else {
				logger.trace("RESULT:"+ resultHandler.result().getRows().get(0));
				promise.complete(resultHandler.result().getRows());
			}
			
		});
		
		return promise.future();
	}
	
	public static Future<List<JsonObject>> fetchAllConfig(SQLConnection sqlConnection) {
		
		Promise<List<JsonObject>> promise = Promise.promise();
		
		sqlConnection.query("select c.ID, c.NAME, c.SYMBOL, c.DEFAULTVALUE, c.type from tNASConfig c where c.dto is null ", resultHandler->{
			if(resultHandler.failed()) {
				logger.error("Unable to get accessQueryResult:", resultHandler.cause());
				promise.fail(new DAOEXCP_Internal(-100, "خطای داخلی. با راهبر سامانه تماس بگیرید."));
				return;
			}
			
			if(null == resultHandler.result() || null == resultHandler.result().getRows() || resultHandler.result().getRows().isEmpty()) {
				logger.warn("ConfigNotFound");
				promise.complete(new ArrayList<>());
			}else {
				logger.trace("RESULT:"+ resultHandler.result().getRows().get(0));
				promise.complete(resultHandler.result().getRows());
			}
			
		});
		
		return promise.future();
	}
	
	
	public static Future<JsonObject> fetchAgentConfig(SQLConnection sqlConnection, int agentId, int configId) {
		
		Promise<JsonObject> promise = Promise.promise();
		
		JsonArray params = new JsonArray();
		params.add(agentId);
		params.add(configId);
		
		sqlConnection.queryWithParams("select ID, VALUE from tNASAgentConfig where agent_Id = ? and config_Id = ?", params, resultHandler->{
			if(resultHandler.failed()) {
				logger.error("Unable to get accessQueryResult:", resultHandler.cause());
				promise.fail(new DAOEXCP_Internal(-100, "خطای داخلی. با راهبر سامانه تماس بگیرید."));
				return;
			}
			
			if(null == resultHandler.result() || null == resultHandler.result().getRows() || resultHandler.result().getRows().isEmpty()) {
				logger.warn("ConfigNotFound");
				promise.fail(new DAOEXCP_Internal(-100, "داده ای یافت نشد."));
				return;
			}

			logger.trace("RESULT:"+ resultHandler.result().getRows().get(0));
			promise.complete(resultHandler.result().getRows().get(0));
		
			
		});
		
		return promise.future();
	}
	
	public static Future<JsonObject> fetchConfigById(SQLConnection sqlConnection, int configId) {
		
		Promise<JsonObject> promise = Promise.promise();
		
		JsonArray params = new JsonArray();
		params.add(configId);
		
		sqlConnection.queryWithParams("select ID, PARENT_ID, NAME, SYMBOL, DEFAULTVALUE from tNASConfig where id = ? and dto is null", params, resultHandler->{
			if(resultHandler.failed()) {
				logger.error("Unable to get accessQueryResult:", resultHandler.cause());
				promise.fail(new DAOEXCP_Internal(-100, "خطای داخلی. با راهبر سامانه تماس بگیرید."));
				return;
			}
			
			if(null == resultHandler.result() || null == resultHandler.result().getRows() || resultHandler.result().getRows().isEmpty()) {
				logger.warn("ConfigNotFound");
				promise.fail(new DAOEXCP_Internal(-100, "داده ای یافت نشد."));
				return;
			}

			logger.trace("RESULT:"+ resultHandler.result().getRows().get(0));
			promise.complete(resultHandler.result().getRows().get(0));
		
			
		});
		
		return promise.future();
	}
	
	
	public static Future<Void> doUpdateAgentConfig(SQLConnection sqlConnection, int agentId, int configId, String value) {

		Promise<Void> promise = Promise.promise();
		
		JsonArray params = new JsonArray();
		params.add(value);
		params.add(agentId);
		params.add(configId);
		
		sqlConnection.updateWithParams("update tNASAgentConfig set value=? where agent_Id = ? and config_Id = ?", params, resultHandler->{
			if(resultHandler.failed()) {
				logger.error("Unable to get accessQueryResult:", resultHandler.cause());
				promise.fail(new DAOEXCP_Internal(-100, "خطای داخلی. با راهبر سامانه تماس بگیرید."));
				return;
			}
			
			logger.trace("UpdateAgentConfigSuccessful");
			promise.complete();
			
		});
		
		return promise.future();
	}
	
	
	public static Future<Void> doSaveAgentConfigHistory(SQLConnection sqlConnection, int id, String value, int userId) {

		Promise<Void> promise = Promise.promise();
		
		JsonArray params = new JsonArray();
		params.add(id);
		params.add(value);
		params.add(userId);
		params.add("E");
		params.add("");
		
		sqlConnection.updateWithParams(""
				+ "insert into tNASAgentConfigHistory ("
				+ "id,"
				+ "agentConfig_Id,"
				+ "value,"
				+ "historyBy_ID,"
				+ "HistoryType,"
				+ "historyDescription,"
				+ "historyDate)"
				+ "values(sNASAgentConfigHistory.nextval,?,?,?,?,?,sysdate) ", params, resultHandler->{
			if(resultHandler.failed()) {
				logger.error("Unable to get accessQueryResult:", resultHandler.cause());
				promise.fail(new DAOEXCP_Internal(-100, "خطای داخلی. با راهبر سامانه تماس بگیرید."));
				return;
			}
			
			logger.trace("doSaveAgentConfigHistorySuccessful");
			promise.complete();
			
		});
		
		return promise.future();
	}
	
	public static Future<Void> doSaveConfigHistory(
			SQLConnection sqlConnection,
			int configId,
			String defaultValue,
			int userId) {

		Promise<Void> promise = Promise.promise();
		
		JsonArray params = new JsonArray();
		params.add(configId);
		params.add(defaultValue);
		params.add(userId);
		params.add("E");
		params.add("");
		
		sqlConnection.updateWithParams(""
				+ "insert into tNASConfigHistory ("
				+ "id,"
				+ "config_Id,"
				+ "defaultValue,"
				+ "historyBy_ID,"
				+ "HistoryType,"
				+ "historyDescription,"
				+ "historyDate)"
				+ "values(snasconfighistory.nextval,?,?,?,?,?,sysdate) ", params, resultHandler->{
			if(resultHandler.failed()) {
				logger.error("Unable to get accessQueryResult:", resultHandler.cause());
				promise.fail(new DAOEXCP_Internal(-100, "خطای داخلی. با راهبر سامانه تماس بگیرید."));
				return;
			}
			
			logger.trace("SaveConfigHistorySuccessful");
			promise.complete();
			
		});
		
		return promise.future();
	}

	public static Future<Void> doUpdateConfig(SQLConnection sqlConnection, int configId, String defaultValue) {

		Promise<Void> promise = Promise.promise();
		
		JsonArray params = new JsonArray();
		params.add(defaultValue);
		params.add(configId);
		
		sqlConnection.updateWithParams("update tNASConfig set defaultValue=? where id = ?", params, resultHandler->{
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
	
	
	public static Future<Void> doSaveAgentConfig(SQLConnection sqlConnection, int agentId, List<JsonObject> configList) {

		Promise<Void> promise = Promise.promise();
		
		List<JsonArray> params = new ArrayList<>();
		configList.forEach(config -> {
			params.add(new JsonArray().add(agentId).add(config.getInteger("ID")).add(config.getString("DEFAULTVALUE")));
		});
		
		sqlConnection.batchWithParams(""
				+ "insert into tNASAgentConfig("
				+ "id,"
				+ "agent_Id,"
				+ "config_Id,"
				+ "value)"
				+ "values(snasagentconfig.nextval,?,?,?) ", params, resultHandler->{
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
