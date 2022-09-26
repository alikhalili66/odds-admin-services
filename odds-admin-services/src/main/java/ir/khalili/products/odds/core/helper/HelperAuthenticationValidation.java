package ir.khalili.products.odds.core.helper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonArray;
import io.vertx.ext.sql.SQLConnection;
import ir.khalili.products.odds.core.excp.dao.DAOEXCP_AuthenticationValidation;
import ir.khalili.products.odds.core.excp.dao.DAOEXCP_Internal;

public class HelperAuthenticationValidation {

    private static final Logger logger = LoggerFactory.getLogger(HelperAuthenticationValidation.class);
    

	public static Future<Void> isActiveServiceForAgent(SQLConnection sqlConnection, Integer agentId, Long serviceId) {
		
		Promise<Void> promise = Promise.promise();
		
		JsonArray params = new JsonArray();
		params.add(serviceId);
		params.add(agentId);
		
		sqlConnection.queryWithParams("select sa.isactive from tnasserviceagent sa where sa.service_id=? and sa.agent_id=?", params, resultHandler->{
			if(resultHandler.failed()) {
				logger.error("Unable to get accessQueryResult:", resultHandler.cause());
				promise.fail(new DAOEXCP_Internal(-100, "خطای داخلی. با راهبر سامانه تماس بگیرید."));
				return;
			}
			
			if(null == resultHandler.result() || null == resultHandler.result().getRows() || resultHandler.result().getRows().isEmpty()) {
				logger.warn("ServiceAgentNotFound");
				promise.fail(new DAOEXCP_AuthenticationValidation(-100, "خدمت مورد نظر برای این عامل غیر فعال است"));
			}else {
				logger.trace("RESULT:"+ resultHandler.result().getRows().get(0));
				if (resultHandler.result().getRows().get(0).getString("ISACTIVE").equals("Y")) {
					promise.complete();
				} else {
					promise.fail(new DAOEXCP_AuthenticationValidation(-100, "خدمت مورد نظر برای این عامل غیر فعال است"));
				}
				
			}
			
		});
		
		return promise.future();
	}

	public static Future<Void> isActiveService(SQLConnection sqlConnection, String symbol) {
		
		Promise<Void> promise = Promise.promise();
		
		JsonArray params = new JsonArray();
		params.add(symbol);
		
		sqlConnection.queryWithParams("select s.isactive from tnasservice s where s.symbol=?", params, resultHandler->{
			if(resultHandler.failed()) {
				logger.error("Unable to get accessQueryResult:", resultHandler.cause());
				promise.fail(new DAOEXCP_Internal(-100, "خطای داخلی. با راهبر سامانه تماس بگیرید."));
				return;
			}
			
			if(null == resultHandler.result() || null == resultHandler.result().getRows() || resultHandler.result().getRows().isEmpty()) {
				logger.warn("ServiceNotFound");
				promise.fail(new DAOEXCP_AuthenticationValidation(-100, "خدمت مورد نظر غیر فعال است"));
			}else {
				logger.trace("RESULT:"+ resultHandler.result().getRows().get(0));
				if (resultHandler.result().getRows().get(0).getString("ISACTIVE").equals("Y")) {
					promise.complete();
				} else {
					promise.fail(new DAOEXCP_AuthenticationValidation(-100, "خدمت مورد نظر غیر فعال است"));	
				}
			}
			
		});
		
		return promise.future();
	}

	public static Future<Void> authenticationValidation(SQLConnection sqlConnection, Integer customerId, Long serviceId, Integer agentId) {
		
		Promise<Void> promise = Promise.promise();
		
		JsonArray params = new JsonArray();
		params.add(serviceId);
		params.add(customerId);
		params.add(agentId);
		
		sqlConnection.queryWithParams("select count(*) cnt from tnascustomerservice cs where cs.service_id =? and cs.customer_id=? and cs.agent_id=? and cs.validto >= sysdate", params, resultHandler->{
			if(resultHandler.failed()) {
				logger.error("Unable to get accessQueryResult:", resultHandler.cause());
				promise.fail(new DAOEXCP_Internal(-100, "خطای داخلی. با راهبر سامانه تماس بگیرید."));
				return;
			}
			
			if(null == resultHandler.result() || null == resultHandler.result().getRows() || resultHandler.result().getRows().isEmpty()) {
				logger.warn("authenticationValidationNotFound");
				promise.fail(new DAOEXCP_AuthenticationValidation(-100, "مشتری فاقد احراز هویت معتبر می باشد"));
			}else {
				logger.trace("RESULT:"+ resultHandler.result().getRows().get(0));
				if (resultHandler.result().getRows().get(0).getInteger("CNT").intValue() == 0) {
					promise.fail(new DAOEXCP_AuthenticationValidation(-100, "مشتری فاقد احراز هویت معتبر می باشد"));	
				} else {
					promise.complete();
				}
			}
			
		});
		
		return promise.future();
	}

}
