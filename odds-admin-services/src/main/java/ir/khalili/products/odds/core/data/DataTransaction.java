package ir.khalili.products.odds.core.data;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.sql.SQLConnection;
import io.vertx.ext.web.client.WebClient;
import ir.khalili.products.odds.core.EntryPoint;
import ir.khalili.products.odds.core.constants.AppConstants;
import ir.khalili.products.odds.core.helper.HelperInitial;
import ir.khalili.products.odds.core.utils.Configuration;

public class DataTransaction extends AbstractVerticle {

    public static Vertx vertx;
    private static Logger logger = LogManager.getLogger(DataTransaction.class);
    
    public static void main(String[] args) {

        logger.info("STARTING ......");

        try (InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream("conf/config.json")) {

            StringBuilder textBuilder = new StringBuilder();
            try (Reader reader = new BufferedReader(
                    new InputStreamReader(in, Charset.forName(StandardCharsets.UTF_8.name())))) {
                int c = 0;
                while ((c = reader.read()) != -1) {
                    textBuilder.append((char) c);
                }
            }

            String jsonString = textBuilder.toString();
            logger.info("jsonString:" + jsonString);

            EntryPoint.joConfig = new JsonObject(jsonString);

        } catch (Exception e) {
            logger.error("configNotValid:", e);
            System.exit(0);
        }

        DeploymentOptions deploymentOptions = new DeploymentOptions();
        deploymentOptions.setInstances(1);

        vertx = Vertx.vertx();
        vertx.deployVerticle(DataTransaction.class.getName(), deploymentOptions, resHandler -> {

            if (resHandler.failed()) {
                logger.error("deployVerticleFailed:", resHandler.cause());
                vertx.close();
                System.exit(0);
            }

            HelperInitial.initialize(vertx).onComplete(initHandler -> {

                if (initHandler.failed()) {
                    logger.error("HelperInitial.initialize.Failed:", initHandler.cause());
                    vertx.close();
                    System.exit(0);
                }


        		
        		JDBCClient ircJDBC = JDBCClient.createShared(vertx, Configuration.getDataBaseConfig(),AppConstants.APP_DS_ODDS);
        		ircJDBC.getConnection(connectionHandler -> {

        			if (connectionHandler.failed()) {
        				logger.error("getConnectionFailed:",connectionHandler.cause());
//        				promise.fail(connectionHandler.cause());
        			}

        			logger.info("GetSqlConnection");
        			
        			SQLConnection sqlConnection = connectionHandler.result();
        			
        			sqlConnection.query("SELECT * FROM tTempReport20221121 where USERID is not null and C_CODE_1 = 'SUCCESS' and C_CODE in ('operator_charge_service','bill_pay_service')", sqlHandler->{
        				
        				sqlConnection.close();

        				if(sqlHandler.failed()) {
        					logger.error("Unable to complete sqlHandler: ",sqlHandler.cause());
//        					promise.fail(sqlHandler.cause());
        					return;
        				}
        				
        				WebClient client = WebClient.create(vertx);
        				
        				for (int i = 0; i < sqlHandler.result().getRows().size(); i++) {
//        				for (int i = 0; i < 1; i++) {
        					JsonObject jo = sqlHandler.result().getRows().get(i);
							

        					try {
        						
        						JsonObject joInput = new JsonObject();
        						joInput.put("applicationCode", jo.getString("C_CODE"));
        						joInput.put("amount", Long.parseLong(jo.getString("C_AMOUNT")));
        						joInput.put("invoiceId", jo.getString("C_INVOICE_NUMBER"));
        						joInput.put("description", jo.getString("C_DESCRIPTION"));
        						joInput.put("userId", jo.getString("USERID"));
        						joInput.put("date", jo.getString("C_DATE_TIME"));
        						
        						
        						
        						System.out.println(joInput);
        				        
        						client
        						.post(9090, "37.32.25.54", "/v1/service/odds/transaction/save")
        						.sendJson(joInput, ar -> {
        							try {
        								if (ar.succeeded()) {
        									System.out.println(ar.result().bodyAsString());
        								} else {
        									System.out.println(ar.cause());
        								}
        							}catch(Exception e) {
        								e.printStackTrace();
        							} finally {

        							}
        						});
        					} catch (Exception e) {
        						e.printStackTrace();
        					}
        				
        					
						}
        				
        				
        				logger.info("excuteQueryDone");
        				
//        				promise.complete();
        				
        			});
        			
        		});
        		
            });
        });

    }


}
