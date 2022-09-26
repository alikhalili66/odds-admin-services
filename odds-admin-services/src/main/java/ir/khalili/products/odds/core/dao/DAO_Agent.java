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
import ir.khalili.products.odds.core.excp.dao.DAOEXCP_NoDataFound;

public class DAO_Agent {

	private static final Logger logger = LogManager.getLogger(DAO_Agent.class);

	public static Future<JsonObject> fetchAgentById(SQLConnection sqlConnection, int agentId) {
		
		Promise<JsonObject> promise = Promise.promise();
		
		JsonArray params = new JsonArray();
		params.add(agentId);
		
		sqlConnection.queryWithParams("select c.ID, c.NAME, c.SYMBOL, c.ISACTIVE from tNASAgent c where c.id = ? and c.dto is null", params, resultHandler->{
			if(resultHandler.failed()) {
				logger.error("Unable to get accessQueryResult:", resultHandler.cause());
				promise.fail(new DAOEXCP_Internal(-100, "خطای داخلی. با راهبر سامانه تماس بگیرید."));
				return;
			}
			
			if(null == resultHandler.result() || null == resultHandler.result().getRows() || resultHandler.result().getRows().isEmpty()) {
				logger.warn("AgentNotFound");
				promise.complete(new JsonObject());
			}else {
				logger.trace("RESULT:"+ resultHandler.result().getRows().get(0));
				promise.complete(resultHandler.result().getRows().get(0));
			}
			
		});
		
		return promise.future();
	}
	
	
	public static Future<List<JsonObject>> fetchAllAgent(SQLConnection sqlConnection) {
		
		Promise<List<JsonObject>> promise = Promise.promise();
		
		sqlConnection.query("select c.ID, c.NAME, c.SYMBOL, c.ISACTIVE from tNASAgent c where c.dto is null order by creationDate desc", resultHandler->{
			if(resultHandler.failed()) {
				logger.error("Unable to get accessQueryResult:", resultHandler.cause());
				promise.fail(new DAOEXCP_Internal(-100, "خطای داخلی. با راهبر سامانه تماس بگیرید."));
				return;
			}
			
			if(null == resultHandler.result() || null == resultHandler.result().getRows() || resultHandler.result().getRows().isEmpty()) {
				logger.warn("AgentNotFound");
				promise.complete(new ArrayList<>());
			}else {
				logger.trace("RESULT:"+ resultHandler.result().getRows().get(0));
				promise.complete(resultHandler.result().getRows());
			}
			
		});
		
		return promise.future();
	}
	
	
	public static Future<JsonObject> fetchAgentByUserId(SQLConnection sqlConnection, int userId) {
		
		Promise<JsonObject> promise = Promise.promise();
		
		JsonArray params = new JsonArray();
		params.add(userId);
		
		sqlConnection.queryWithParams("select id, NAME, SYMBOL, isActive from tNASAgent where user_Id = ? and dto is null ", params, resultHandler->{
			if(resultHandler.failed()) {
				logger.error("Unable to get accessQueryResult:", resultHandler.cause());
				promise.fail(new DAOEXCP_Internal(-100, "خطای داخلی. با راهبر سامانه تماس بگیرید."));
				return;
			}
			
			if(null == resultHandler.result() || null == resultHandler.result().getRows() || resultHandler.result().getRows().isEmpty()) {
				logger.error("AgentNotFound:userId:" + userId);
				promise.fail(new DAOEXCP_NoDataFound(-100, "عاملی یافت نشد."));
			}else {
				logger.trace("RESULT:"+ resultHandler.result().getRows().get(0));
				promise.complete(resultHandler.result().getRows().get(0));
			}
			
		});
		
		return promise.future();
	}
	
	public static Future<Void> doSaveAgent(
			SQLConnection sqlConnection, 
			int id,
			int lockinUserId, 
			String name,
			String symbol,
			String isActive,
			int userId) {

		Promise<Void> promise = Promise.promise();
		
		JsonArray params = new JsonArray();
		params.add(id);
		params.add(name);
		params.add(symbol);
		params.add(isActive);
		params.add(lockinUserId);
		params.add(userId);
		
		sqlConnection.updateWithParams(""
				+ "insert into tNASAgent("
				+ "id,"
				+ "name,"
				+ "symbol,"
				+ "isActive,"
				+ "user_id,"
				+ "creationDate,"
				+ "createdBy_id)"
				+ "values(?,?,?,?,?,sysdate,?)", params, resultHandler->{
			if(resultHandler.failed()) {
				logger.error("Unable to get accessQueryResult:", resultHandler.cause());
				promise.fail(new DAOEXCP_Internal(-100, "خطای داخلی. با راهبر سامانه تماس بگیرید."));
				return;
			}
			
			logger.trace("SaveAgentSuccessful");
			promise.complete();
			
		});
		
		return promise.future();
	}

	public static Future<Integer> getNextAgentSequence(SQLConnection sqlConnection) {

		Promise<Integer> promise = Promise.promise();
		
		sqlConnection.query("select snasagent.nextval from dual", resultHandler->{
			if(resultHandler.failed()) {
				logger.error("Unable to get accessQueryResult:", resultHandler.cause());
				promise.fail(new DAOEXCP_Internal(-100, "خطای داخلی. با راهبر سامانه تماس بگیرید."));
				return;
			}
			
			logger.trace("getNextAgentSequenceSuccessful");
			promise.complete(resultHandler.result().getRows().get(0).getInteger("NEXTVAL"));
			
		});
		
		return promise.future();
	}

	public static Future<Void> updateAgentById(SQLConnection sqlConnection, Integer agentId, String isActive, String name) {

		Promise<Void> promise = Promise.promise();
		JsonArray params = new JsonArray();
		params.add(isActive);
		params.add(name);
		params.add(agentId);
		
		sqlConnection.updateWithParams("update tnasagent set isActive=?, name=? where id=?", params, resultHandler->{
			if(resultHandler.failed()) {
				logger.error("Unable to get accessQueryResult:", resultHandler.cause());
				promise.fail(new DAOEXCP_Internal(-100, "خطای داخلی. با راهبر سامانه تماس بگیرید."));
				return;
			}
			
			logger.trace("updateAgentByIsActiveSuccessful");
			promise.complete();
			
		});
		
		return promise.future();
	}
	
	
	public static Future<Void> deleteAgentById(SQLConnection sqlConnection, Integer agentId) {

		Promise<Void> promise = Promise.promise();
		JsonArray params = new JsonArray();
		params.add(agentId);
		
		sqlConnection.updateWithParams("update tnasagent set dto=sysdate where id=?", params, resultHandler->{
			if(resultHandler.failed()) {
				logger.error("Unable to get accessQueryResult:", resultHandler.cause());
				promise.fail(new DAOEXCP_Internal(-100, "خطای داخلی. با راهبر سامانه تماس بگیرید."));
				return;
			}
			
			logger.trace("deleteAgentByIdSuccessful");
			promise.complete();
			
		});
		
		return promise.future();
	}
	
	public static Future<Void> doSaveAgentHistory(
			SQLConnection sqlConnection, 
			int agentId, 
			String name,
			String symbol, 
			String isActive,
			String historyType,
			int userId) {

		Promise<Void> promise = Promise.promise();
		
		JsonArray params = new JsonArray();
		params.add(agentId);
		params.add(name);
		params.add(symbol);
		params.add(isActive);
		params.add(userId);
		params.add(historyType);
		params.add("");
		
		sqlConnection.updateWithParams(""
				+ "insert into tNASAgentHistory ("
				+ "id,"
				+ "agent_Id,"
				+ "name,"
				+ "symbol,"
				+ "isActive,"
				+ "historyBy_ID,"
				+ "HistoryType,"
				+ "historyDescription,"
				+ "historyDate)"
				+ "values(sNASAgentHistory.nextval,?,?,?,?,?,?,?,sysdate) ", params, resultHandler->{
			if(resultHandler.failed()) {
				logger.error("Unable to get accessQueryResult:", resultHandler.cause());
				promise.fail(new DAOEXCP_Internal(-100, "خطای داخلی. با راهبر سامانه تماس بگیرید."));
				return;
			}
			
			logger.trace("doSaveAgentHistorySuccessful");
			promise.complete();
			
		});
		
		return promise.future();
	}

}
