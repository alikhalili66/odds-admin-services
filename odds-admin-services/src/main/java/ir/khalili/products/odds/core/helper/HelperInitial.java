package ir.khalili.products.odds.core.helper;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.sql.SQLConnection;
import ir.khalili.products.odds.core.constants.AppConstants;
import ir.khalili.products.odds.core.utils.Configuration;

public class HelperInitial {

	private static final Logger logger = LogManager.getLogger(HelperInitial.class);
	
	@SuppressWarnings("rawtypes")
	public static Future<Void> initialize(Vertx vertx) {
		
		Promise<Void> promise = Promise.promise();
		
		Future<SQLConnection> futDB = connectToDataBase(vertx);
		
		List<Future> listFutures = new ArrayList<>();
		listFutures.add(futDB);
		
		CompositeFuture.all(listFutures).onComplete(joinHandler01->{
			
			if(joinHandler01.failed()) {
				logger.error("joinHandler01Failed:",joinHandler01.cause());
				promise.fail(joinHandler01.cause());
			}
			
			SQLConnection sqlConnection = futDB.result();
			
			logger.info("GetSqlConnection");
			
			sqlConnection.query("select * from dual", sqlHandler->{
				
				sqlConnection.close();

				if(sqlHandler.failed()) {
					logger.error("Unable to complete sqlHandler: ",sqlHandler.cause());
					promise.fail(sqlHandler.cause());
					return;
				}
				
				logger.info("excuteQueryDone");
				
				promise.complete();
				
			});
		
		});
		
		return promise.future();
	}
	
	@SuppressWarnings("rawtypes")
	public static Future<JsonObject> doHealthCheck(Vertx vertx) {
		
		Promise<JsonObject> promise = Promise.promise();
		
		Future<SQLConnection> futDB = connectToDataBase(vertx);
		
		List<Future> listFutures = new ArrayList<>();
		listFutures.add(futDB);
		
		CompositeFuture.all(listFutures).onComplete(joinHandler01->{
			
			if(joinHandler01.failed()) {
				logger.error("joinHandler01Failed:",joinHandler01.cause());
				promise.fail(joinHandler01.cause());
			}
			
			SQLConnection sqlConnection = futDB.result();
			
			logger.info("GetSqlConnection");
			
			sqlConnection.query("select * from dual", sqlHandler->{
				
				sqlConnection.close();

				if(sqlHandler.failed()) {
					logger.error("Unable to complete sqlHandler: ",sqlHandler.cause());
					promise.fail(sqlHandler.cause());
					return;
				}
				
				logger.info("excuteQueryDone");
				
				promise.complete();
				
			});
		
		});
		
		return promise.future();
	}
	
	private static Future<SQLConnection> connectToDataBase(Vertx vertx) {
		
		Promise<SQLConnection> promise = Promise.promise();
		
		JDBCClient ircJDBC = JDBCClient.createShared(vertx, Configuration.getDataBaseConfig(),AppConstants.APP_DS_ODDS);
		ircJDBC.getConnection(connectionHandler -> {

			if (connectionHandler.failed()) {
				logger.error("getConnectionFailed:",connectionHandler.cause());
				promise.fail(connectionHandler.cause());
			}

			logger.info("GetSqlConnection");
			
			promise.complete(connectionHandler.result());
			
		});
		
		return promise.future();
	}
	
}
