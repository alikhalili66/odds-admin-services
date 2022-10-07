package ir.khalili.products.odds.core.dao;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.ext.sql.SQLConnection;
import ir.khalili.products.odds.core.excp.dao.DAOEXCP_Internal;

public class DAO_Report {

    private static final Logger logger = LogManager.getLogger(DAO_Report.class);
    
    public static Future<Integer> fetchRegisteredUsersCount(SQLConnection sqlConnection) {
        Promise<Integer> promise = Promise.promise();
        
        sqlConnection.query("select count(*) USER_COUNT from toppuser", handler -> {
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
    
    public static Future<Integer> fetchCompetitorUsersCount(SQLConnection sqlConnection) {
        Promise<Integer> promise = Promise.promise();
        
        sqlConnection.query("select count(distinct user_id) COMPETITOR_COUNT from toppodds", handler -> {
            if (handler.failed()) {
            	logger.error("Unable to get accessQueryResult:", handler.cause());
                promise.fail(new DAOEXCP_Internal(-100, "خطای داخلی. با راهبر سامانه تماس بگیرید."));
            } else {
                if (null == handler.result() || null == handler.result().getRows() || handler.result().getRows().isEmpty()) {
                	promise.complete(0);
                } else {
                    logger.trace("fetchCompetitorUsersCountSuccessful");
                    promise.complete(handler.result().getRows().get(0).getInteger("COMPETITOR_COUNT"));
                }
            
            }
        });

        return promise.future();
    }
    
    public static Future<Long> fetchCompetitorUsersAmount(SQLConnection sqlConnection) {
        Promise<Long> promise = Promise.promise();
        
        sqlConnection.query("select sum(amount) TOTAL_AMOUNT from toppuser", handler -> {
            if (handler.failed()) {
            	logger.error("Unable to get accessQueryResult:", handler.cause());
                promise.fail(new DAOEXCP_Internal(-100, "خطای داخلی. با راهبر سامانه تماس بگیرید."));
            } else {
                if (null == handler.result() || null == handler.result().getRows() || handler.result().getRows().isEmpty()) {
                	promise.complete(0L);
                } else {
                    logger.trace("fetchCompetitorUsersAmountSuccessful");
                    promise.complete(handler.result().getRows().get(0).getLong("TOTAL_AMOUNT"));
                }
            
            }
        });

        return promise.future();
    }
    
    public static Future<Integer> fetchOddsCount(SQLConnection sqlConnection) {
        Promise<Integer> promise = Promise.promise();
        
        sqlConnection.query("select count(*) ODDS_COUNT from toppodds", handler -> {
            if (handler.failed()) {
            	logger.error("Unable to get accessQueryResult:", handler.cause());
                promise.fail(new DAOEXCP_Internal(-100, "خطای داخلی. با راهبر سامانه تماس بگیرید."));
            } else {
                if (null == handler.result() || null == handler.result().getRows() || handler.result().getRows().isEmpty()) {
                	promise.complete(0);
                } else {
                    logger.trace("fetchOddsCountSuccessful");
                    promise.complete(handler.result().getRows().get(0).getInteger("ODDS_COUNT"));
                }
            
            }
        });

        return promise.future();
    }
    

}
