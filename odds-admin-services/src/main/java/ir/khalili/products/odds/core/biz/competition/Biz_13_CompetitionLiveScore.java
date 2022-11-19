package ir.khalili.products.odds.core.biz.competition;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.sql.SQLConnection;
import ir.khalili.products.odds.core.biz.excp.BIZEXCP_Transaction;
import ir.khalili.products.odds.core.dao.DAO_Competition;
import ir.khalili.products.odds.core.service.LiveScore;

public class Biz_13_CompetitionLiveScore {

    private static final Logger logger = LogManager.getLogger(Biz_13_CompetitionLiveScore.class);

    public static void fetch(SQLConnection sqlConnection, JsonObject message, Handler<AsyncResult<JsonObject>> resultHandler) {

        logger.trace("inputMessage:" + message);

        final Integer competitionId = message.getInteger("competitionId");

        DAO_Competition.fetchById(sqlConnection, competitionId).onComplete(competitionResult -> {

            if (competitionResult.failed()) {
                logger.error("Unable to complete joinHandler: " + competitionResult.cause());
                resultHandler.handle(Future.failedFuture(competitionResult.cause()));
                return;
            }
            JsonObject competition = competitionResult.result();
            
            logger.trace("competition:" + competition);

            int leagueId = competition.getInteger("LEAGUE_ID");
            Integer IDENTIFIER = competition.getInteger("IDENTIFIER");

            if(null == IDENTIFIER) {
            	logger.error("INALID_IDENTIFIER");
           	 	resultHandler.handle(Future.failedFuture(new BIZEXCP_Transaction(-100, "شناسه مسابقه برای api صحیح نمی باشد.")));
           	 	return;
           }
            
            LiveScore.fetchMatchStatistics(String.valueOf(IDENTIFIER)).onComplete(liveResult -> {

                if (liveResult.failed()) {
                    logger.error("Unable to complete joinHandler: " + liveResult.cause());
                    resultHandler.handle(Future.failedFuture(liveResult.cause()));
                    return;
                }

                Map<String, String> liveScoreMap = getLiveScore(liveResult.result().getJsonObject("data"));

                logger.trace("liveScoreMap:" + liveScoreMap);

                
                DAO_Competition.saveQuestionCorrectAnswer(sqlConnection, competitionId, leagueId, liveScoreMap).onComplete(resultUpd -> {

                    if (resultUpd.failed()) {
                        logger.error("Unable to complete joinHandler: " + resultUpd.cause());
                        resultHandler.handle(Future.failedFuture(resultUpd.cause()));
                        return;
                    }

                    logger.trace("record update");

                    resultHandler.handle(Future.succeededFuture(
                            new JsonObject()
                                    .put("resultCode", 1)
                                    .put("resultMessage", "عملیات با موفقیت انجام شد.")
                    ));
                });
            });
        });
    
    }

    private static Map<String, String> getLiveScore(JsonObject liveScore) {

    	Map<String, String> liveScoreMap = new HashMap<>();
        liveScoreMap.put("yellow_cards", liveScore.getString("yellow_cards"));
        liveScoreMap.put("red_cards", liveScore.getString("red_cards"));
        liveScoreMap.put("substitutions", liveScore.getString("substitutions"));
        liveScoreMap.put("possesion", liveScore.getString("possesion"));
        liveScoreMap.put("free_kicks", liveScore.getString("free_kicks"));
        liveScoreMap.put("goal_kicks", liveScore.getString("goal_kicks"));
        liveScoreMap.put("throw_ins", liveScore.getString("throw_ins"));
        liveScoreMap.put("offsides", liveScore.getString("offsides"));
        liveScoreMap.put("corners", liveScore.getString("corners"));
        liveScoreMap.put("shots_on_target", liveScore.getString("shots_on_target"));
        liveScoreMap.put("attempts_on_goal", liveScore.getString("attempts_on_goal"));
        liveScoreMap.put("saves", liveScore.getString("saves"));
        liveScoreMap.put("fauls", liveScore.getString("fauls"));
        liveScoreMap.put("treatments", liveScore.getString("treatments"));
        liveScoreMap.put("penalties", liveScore.getString("penalties"));
        liveScoreMap.put("shots_blocked", liveScore.getString("shots_blocked"));
        liveScoreMap.put("dangerous_attacks", liveScore.getString("dangerous_attacks"));
        liveScoreMap.put("attacks", liveScore.getString("attacks"));
        return liveScoreMap;
    }
}
