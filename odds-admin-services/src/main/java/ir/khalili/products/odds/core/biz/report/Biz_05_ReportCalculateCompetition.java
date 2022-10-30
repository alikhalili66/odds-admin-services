package ir.khalili.products.odds.core.biz.report;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import io.vertx.core.AsyncResult;
import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.sql.SQLConnection;
import ir.khalili.products.odds.core.dao.DAO_Competition;
import ir.khalili.products.odds.core.dao.DAO_CompetitionReport;
import ir.khalili.products.odds.core.dao.DAO_Odds;

public class Biz_05_ReportCalculateCompetition {

    private static final Logger logger = LogManager.getLogger(Biz_05_ReportCalculateCompetition.class);

    public static void fetchOddsCount(SQLConnection sqlConnection, JsonObject message, Handler<AsyncResult<JsonObject>> resultHandler) {

    	logger.trace("inputMessage:" + message);

        final Integer competitionId = message.getInteger("competitionId");
        
        Future<JsonObject> futCompetition = DAO_Competition.fetchById(sqlConnection, competitionId);
        Future<List<JsonObject>> futQuestion = DAO_Competition.fetchQuestion(sqlConnection, message);
        Future<List<JsonObject>> futOdds = DAO_Odds.fetchOddsReport(sqlConnection, competitionId);
        Future<List<JsonObject>> futOddsCount = DAO_Odds.fetchOddsCount(sqlConnection, competitionId);
        Future<Void> futDelete = DAO_CompetitionReport.deleteCompetitionReport(sqlConnection, competitionId);
        
        CompositeFuture.join(futCompetition, futQuestion, futOdds, futOddsCount, futDelete).onComplete(joinHandler01->{
        	
            if (joinHandler01.failed()) {
            	logger.error("Unable to complete joinHandler01: " + joinHandler01.cause());
                resultHandler.handle(Future.failedFuture(joinHandler01.cause()));
                return;
            }
            
            logger.trace("competition: " + futCompetition.result());
            
            List<JsonArray> list = new ArrayList<>();
            
            for (JsonObject joQuestion : futQuestion.result()) {
            	
            	int totalCount = 1;
            	for (JsonObject joOdds : futOddsCount.result()) {
            		if(joOdds.getInteger("QUESTION_ID").intValue() == joQuestion.getInteger("ID").intValue()) {
            			totalCount = joOdds.getInteger("COUNT");
            			break;
            		}
            	}
            	
            	JsonObject joResult = new JsonObject();
            	
            	for (JsonObject joOdds : futOdds.result()) {
					if(joOdds.getInteger("QUESTION_ID").intValue() == joQuestion.getInteger("ID").intValue()) {
						if(joResult.size() < 4) {
							
//							System.out.println("QID: " + joOdds.getInteger("QUESTION_ID").intValue() + ", COUNT: " + joOdds.getInteger("COUNT") + ", totalCount:" + totalCount + ", Result:" + joOdds.getInteger("COUNT") * 100 / totalCount);
							
							joResult.put(joOdds.getString("ANSWER"), joOdds.getInteger("COUNT") * 100 / totalCount);
						}
					}
				}
            	
            	list.add(new JsonArray()
            			.add(futCompetition.result().getInteger("LEAGUE_ID"))
            			.add(competitionId)		
            			.add(joQuestion.getInteger("ID"))	
            			.add(joQuestion.getString("QUESTION"))	
            			.add(joQuestion.getString("TYPE"))	
            			.add(joQuestion.getInteger("norder"))	
            			.add(joResult.toString())
            					);
			}
            
            DAO_CompetitionReport.saveCompetitionReport(sqlConnection, list).onComplete(joinHandler02->{
            	
            	 if (joinHandler02.failed()) {
            		 logger.error("Unable to complete joinHandler02: " + joinHandler02.cause());
                     resultHandler.handle(Future.failedFuture(joinHandler02.cause()));
                     return;
                 }
            	 
            	 resultHandler.handle(Future.succeededFuture(
            			 new JsonObject()
            			 .put("resultCode", 1)
            			 .put("resultMessage", "عملیات با موفقیت انجام شد.")
            			 ));
            });
        
        });

    }

}
