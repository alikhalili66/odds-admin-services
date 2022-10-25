package ir.khalili.products.odds.core.dao;

import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonArray;
import io.vertx.ext.sql.SQLConnection;
import ir.khalili.products.odds.core.excp.dao.DAOEXCP_Internal;

public class DAO_CompetitionReport {

    private static final Logger logger = LogManager.getLogger(DAO_CompetitionReport.class);
    
    public static Future<Integer> fetchRegisteredUsersCount(SQLConnection sqlConnection, int leagueId) {
        Promise<Integer> promise = Promise.promise();
        
        JsonArray params = new JsonArray();
        params.add(leagueId);
        
        sqlConnection.queryWithParams("select count(*) USER_COUNT from toppuser where league_Id = ?", params, handler -> {
            if (handler.failed()) {
            	logger.error("Unable to get accessQueryResult:", handler.cause());
                promise.fail(new DAOEXCP_Internal(-100, "خطای داخلی. با راهبر سامانه تماس بگیرید."));
            } else {
                if (null == handler.result() || null == handler.result().getRows() || handler.result().getRows().isEmpty()) {
                	promise.complete(0);
                } else {
                    logger.trace("fetchRegisteredUsersCountSuccessful");
                    promise.complete(handler.result().getRows().get(0).getInteger("USER_COUNT"));
                }
            
            }
        });

        return promise.future();
    }
    
    public static Future<Void> saveCompetitionReport(SQLConnection sqlConnection, List<JsonArray> list) {
        Promise<Void> promise = Promise.promise();
        
        String sqlStatement = "insert into tOPPCompetitionReport (id, league_Id, competition_Id, question_Id, question, type, norder, result) "
        		+ "values(sOPPCompetitionReport.nextval, ?, ?, ?, ?, ?, ?, ?)";
        
        sqlConnection.batchWithParams(sqlStatement, list, resultHandler->{
        	
            if (resultHandler.failed()) {
            	logger.error("Unable to get accessQueryResult:", resultHandler.cause());
    				logger.error("Unable to get accessQueryResult:", resultHandler.cause());
    				promise.fail(new DAOEXCP_Internal(-100, "خطای داخلی. با راهبر سامانه تماس بگیرید."));
    				return;
    		}
            promise.complete();
            
        });
        
        return promise.future();
    }
    
    public static Future<Void> deleteCompetitionReport(SQLConnection sqlConnection, int competitionId) {
        Promise<Void> promise = Promise.promise();
        
        JsonArray params = new JsonArray();
        params.add(competitionId);
        
        String sqlStatement = "update tOPPCompetitionReport set dto = sysdate where competition_Id = ? and dto is null";
        
        sqlConnection.updateWithParams(sqlStatement, params, resultHandler->{
        	
            if (resultHandler.failed()) {
            	logger.error("Unable to get accessQueryResult:", resultHandler.cause());
    				logger.error("Unable to get accessQueryResult:", resultHandler.cause());
    				promise.fail(new DAOEXCP_Internal(-100, "خطای داخلی. با راهبر سامانه تماس بگیرید."));
    				return;
    		}
            promise.complete();
            
        });
        
        return promise.future();
    }

}
