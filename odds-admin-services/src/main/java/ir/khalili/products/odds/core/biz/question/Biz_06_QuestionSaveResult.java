package ir.khalili.products.odds.core.biz.question;

import io.vertx.core.AsyncResult;
import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.sql.SQLConnection;
import ir.khalili.products.odds.core.dao.DAO_Competition;
import ir.khalili.products.odds.core.dao.DAO_Config;
import ir.khalili.products.odds.core.dao.DAO_League;
import ir.khalili.products.odds.core.helper.LiveScoreHelper;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * User: Yas-Jalilpour
 * Email: Jalilpourt@gmail.com
 * Date: 11/11/2022
 * Time: 3:32 PM
 */
public class Biz_06_QuestionSaveResult {

    private static final Logger logger = LogManager.getLogger(Biz_06_QuestionSaveResult.class);

    public static void fetch(SQLConnection sqlConnection, JsonObject message, Handler<AsyncResult<JsonObject>> resultHandler) {

        logger.trace("inputMessage:" + message);

        final Integer leagueId = message.getInteger("leagueId");
        final Integer competitionId = message.getInteger("competitionId");

        Future<JsonObject> futLeague = DAO_League.fetchValidLeagueById(sqlConnection, leagueId);
        Future<String> futDate = DAO_Config.fetchSysdate(sqlConnection);

        CompositeFuture.join(futLeague, futDate).onComplete(joinHandler -> {

            if (joinHandler.failed()) {
                logger.error("Unable to complete joinHandler: " + joinHandler.cause());
                resultHandler.handle(Future.failedFuture(joinHandler.cause()));
                return;
            }

            DAO_Competition.fetchCompetitionById(sqlConnection, competitionId).onComplete(competitionResult -> {

                if (competitionResult.failed()) {
                    logger.error("Unable to complete joinHandler: " + competitionResult.cause());
                    resultHandler.handle(Future.failedFuture(competitionResult.cause()));
                    return;
                }
                JsonObject competition = competitionResult.result();

                LiveScoreHelper.liveScore(competition.getString("IDENTIFIER")).onComplete(liveResult -> {

                    if (liveResult.failed()) {
                        logger.error("Unable to complete joinHandler: " + liveResult.cause());
                        resultHandler.handle(Future.failedFuture(liveResult.cause()));
                        return;
                    }

                    JsonObject joUpdOdd = new JsonObject();
                    joUpdOdd.put("leadgueId", leagueId);
                    joUpdOdd.put("competitionId", competition.getInteger("ID"));

                    Map<String, String> liveScoreMap = getLiveScore(liveResult);

                    DAO_Competition.saveQuestionCorrectAnswer(sqlConnection, joUpdOdd, liveScoreMap).onComplete(resultUpd -> {

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
                                        .put("info", new JsonObject())
                                        .put("date", futDate.result())
                        ));
                    });
                });
            });
        });
    }

    private static Map<String, String> getLiveScore(AsyncResult<JsonObject> liveResult) {
        JsonObject liveScore = liveResult.result().getJsonObject("data");
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

