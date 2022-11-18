package ir.khalili.products.odds.core;

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
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CorsHandler;
import io.vertx.ext.web.handler.ResponseTimeHandler;
import io.vertx.ext.web.handler.StaticHandler;
import ir.khalili.products.odds.core.helper.HelperInitial;
import ir.khalili.products.odds.core.routemanager.auth.RtMgr_01_AuthLogin;
import ir.khalili.products.odds.core.routemanager.auth.RtMgr_02_AuthOTP;
import ir.khalili.products.odds.core.routemanager.competition.RtMgr_01_CompetitionSave;
import ir.khalili.products.odds.core.routemanager.competition.RtMgr_02_CompetitionUpdate;
import ir.khalili.products.odds.core.routemanager.competition.RtMgr_03_CompetitionDelete;
import ir.khalili.products.odds.core.routemanager.competition.RtMgr_04_CompetitionFetchAll;
import ir.khalili.products.odds.core.routemanager.competition.RtMgr_05_CompetitionFetchById;
import ir.khalili.products.odds.core.routemanager.competition.RtMgr_07_CompetitionQuestionAssign;
import ir.khalili.products.odds.core.routemanager.competition.RtMgr_08_CompetitionQuestionUnAssign;
import ir.khalili.products.odds.core.routemanager.competition.RtMgr_09_CompetitionQuestionFetch;
import ir.khalili.products.odds.core.routemanager.competition.RtMgr_10_CompetitionResultRegister;
import ir.khalili.products.odds.core.routemanager.competition.RtMgr_11_CompetitionQuestionResultRegister;
import ir.khalili.products.odds.core.routemanager.competition.RtMgr_12_CompetitionPointCalculation;
import ir.khalili.products.odds.core.routemanager.config.RtMgr_01_ConfigUpdate;
import ir.khalili.products.odds.core.routemanager.config.RtMgr_02_ConfigFetchAll;
import ir.khalili.products.odds.core.routemanager.config.RtMgr_03_ConfigFetchById;
import ir.khalili.products.odds.core.routemanager.config.RtMgr_04_ConfigFetchBySymbol;
import ir.khalili.products.odds.core.routemanager.folder.RtMgr_01_FolderSave;
import ir.khalili.products.odds.core.routemanager.folder.RtMgr_02_FolderUpdate;
import ir.khalili.products.odds.core.routemanager.folder.RtMgr_03_FolderDelete;
import ir.khalili.products.odds.core.routemanager.folder.RtMgr_04_FolderFetchAll;
import ir.khalili.products.odds.core.routemanager.folder.RtMgr_05_FolderFetchById;
import ir.khalili.products.odds.core.routemanager.folder.RtMgr_06_FolderQuestionAssign;
import ir.khalili.products.odds.core.routemanager.folder.RtMgr_07_FolderQuestionUnAssign;
import ir.khalili.products.odds.core.routemanager.folder.RtMgr_08_FolderQuestionFetch;
import ir.khalili.products.odds.core.routemanager.group.RtMgr_01_GroupSave;
import ir.khalili.products.odds.core.routemanager.group.RtMgr_02_GroupUpdate;
import ir.khalili.products.odds.core.routemanager.group.RtMgr_03_GroupDelete;
import ir.khalili.products.odds.core.routemanager.group.RtMgr_04_GroupFetchAll;
import ir.khalili.products.odds.core.routemanager.group.RtMgr_05_GroupFetchById;
import ir.khalili.products.odds.core.routemanager.group.RtMgr_06_GroupTeamAssign;
import ir.khalili.products.odds.core.routemanager.group.RtMgr_07_GroupTeamUnAssign;
import ir.khalili.products.odds.core.routemanager.group.RtMgr_08_GroupTeamFetch;
import ir.khalili.products.odds.core.routemanager.group.RtMgr_09_GroupCompetitionFetch;
import ir.khalili.products.odds.core.routemanager.league.RtMgr_01_LeagueSave;
import ir.khalili.products.odds.core.routemanager.league.RtMgr_02_LeagueUpdate;
import ir.khalili.products.odds.core.routemanager.league.RtMgr_03_LeagueDelete;
import ir.khalili.products.odds.core.routemanager.league.RtMgr_04_LeagueFetchAll;
import ir.khalili.products.odds.core.routemanager.league.RtMgr_05_LeagueFetchById;
import ir.khalili.products.odds.core.routemanager.location.RtMgr_01_LocationSave;
import ir.khalili.products.odds.core.routemanager.location.RtMgr_02_LocationUpdate;
import ir.khalili.products.odds.core.routemanager.location.RtMgr_03_LocationDelete;
import ir.khalili.products.odds.core.routemanager.location.RtMgr_04_LocationFetchAll;
import ir.khalili.products.odds.core.routemanager.question.RtMgr_01_QuestionSave;
import ir.khalili.products.odds.core.routemanager.question.RtMgr_02_QuestionUpdate;
import ir.khalili.products.odds.core.routemanager.question.RtMgr_03_QuestionDelete;
import ir.khalili.products.odds.core.routemanager.question.RtMgr_04_QuestionFetchAll;
import ir.khalili.products.odds.core.routemanager.question.RtMgr_05_QuestionFetchById;
import ir.khalili.products.odds.core.routemanager.report.RtMgr_01_ReportRegisteredUsersCount;
import ir.khalili.products.odds.core.routemanager.report.RtMgr_02_ReportCompetitorUsersCount;
import ir.khalili.products.odds.core.routemanager.report.RtMgr_03_ReportCompetitorUsersAmount;
import ir.khalili.products.odds.core.routemanager.report.RtMgr_04_ReportOddsCount;
import ir.khalili.products.odds.core.routemanager.report.RtMgr_06_ReportLeagueUsersWithMaximumPoint;
import ir.khalili.products.odds.core.routemanager.report.RtMgr_07_ReportLeagueBlockedAmount;
import ir.khalili.products.odds.core.routemanager.report.RtMgr_08_ReportAllSectionOddsCountParticipantCountTotalPoint;
import ir.khalili.products.odds.core.routemanager.report.RtMgr_09_ReportAllSectionCorrectOddsCountAndOddsPercentage;
import ir.khalili.products.odds.core.routemanager.report.RtMgr_11_ReportLeagueTransactionAmount;
import ir.khalili.products.odds.core.routemanager.team.RtMgr_01_TeamSave;
import ir.khalili.products.odds.core.routemanager.team.RtMgr_02_TeamUpdate;
import ir.khalili.products.odds.core.routemanager.team.RtMgr_03_TeamDelete;
import ir.khalili.products.odds.core.routemanager.team.RtMgr_04_TeamFetchAll;
import ir.khalili.products.odds.core.routemanager.team.RtMgr_05_TeamFetchById;
import ir.khalili.products.odds.core.routemanager.team.RtMgr_06_TeamImageUpdate;
import ir.khalili.products.odds.core.routemanager.team.RtMgr_07_TeamMemberSave;
import ir.khalili.products.odds.core.routemanager.team.RtMgr_08_TeamMemberUpdate;
import ir.khalili.products.odds.core.routemanager.team.RtMgr_09_TeamMemberDelete;
import ir.khalili.products.odds.core.routemanager.team.RtMgr_10_TeamMemberFetchAll;
import ir.khalili.products.odds.core.routemanager.transaction.RtMgr_01_TransactionFetchAll;
import ir.khalili.products.odds.core.routemanager.transaction.RtMgr_02_TransactionReject;
import ir.khalili.products.odds.core.routemanager.transaction.RtMgr_03_TransactionConfirm;
import ir.khalili.products.odds.core.routemanager.transaction.RtMgr_04_TransactionSave;
import ir.khalili.products.odds.core.routemanager.user.RtMgr_01_UserFetchAll;
import ir.khalili.products.odds.core.routemanager.user.RtMgr_02_UserFetchById;
import ir.khalili.products.odds.core.routemanager.user.RtMgr_03_UserFetchOdds;
import ir.khalili.products.odds.core.routemanager.user.RtMgr_04_UserFetchQuestionAnswer;
import ir.khalili.products.odds.core.routemanager.user.RtMgr_05_UserFetchPointHistory;
import ir.khalili.products.odds.core.verticle.auth.VRTCL_01_AuthLogin;
import ir.khalili.products.odds.core.verticle.auth.VRTCL_02_AuthOTP;
import ir.khalili.products.odds.core.verticle.competition.VRTCL_01_CompetitionSave;
import ir.khalili.products.odds.core.verticle.competition.VRTCL_02_CompetitionUpdate;
import ir.khalili.products.odds.core.verticle.competition.VRTCL_03_CompetitionDelete;
import ir.khalili.products.odds.core.verticle.competition.VRTCL_04_CompetitionFetchAll;
import ir.khalili.products.odds.core.verticle.competition.VRTCL_05_CompetitionFetchById;
import ir.khalili.products.odds.core.verticle.competition.VRTCL_07_CompetitionQuestionAssign;
import ir.khalili.products.odds.core.verticle.competition.VRTCL_08_CompetitionQuestionUnAssign;
import ir.khalili.products.odds.core.verticle.competition.VRTCL_09_CompetitionQuestionFetch;
import ir.khalili.products.odds.core.verticle.competition.VRTCL_10_CompetitionResultRegister;
import ir.khalili.products.odds.core.verticle.competition.VRTCL_11_CompetitionQuestionResultRegister;
import ir.khalili.products.odds.core.verticle.competition.VRTCL_12_CompetitionPointCalculation;
import ir.khalili.products.odds.core.verticle.config.VRTCL_01_ConfigUpdate;
import ir.khalili.products.odds.core.verticle.config.VRTCL_02_ConfigFetchAll;
import ir.khalili.products.odds.core.verticle.config.VRTCL_03_ConfigFetchById;
import ir.khalili.products.odds.core.verticle.config.VRTCL_04_ConfigFetchBySymbol;
import ir.khalili.products.odds.core.verticle.folder.VRTCL_01_FolderSave;
import ir.khalili.products.odds.core.verticle.folder.VRTCL_02_FolderUpdate;
import ir.khalili.products.odds.core.verticle.folder.VRTCL_03_FolderDelete;
import ir.khalili.products.odds.core.verticle.folder.VRTCL_04_FolderFetchAll;
import ir.khalili.products.odds.core.verticle.folder.VRTCL_05_FolderFetchById;
import ir.khalili.products.odds.core.verticle.folder.VRTCL_06_FolderQuestionAssign;
import ir.khalili.products.odds.core.verticle.folder.VRTCL_07_FolderQuestionUnAssign;
import ir.khalili.products.odds.core.verticle.folder.VRTCL_08_FolderQuestionFetch;
import ir.khalili.products.odds.core.verticle.group.VRTCL_01_GroupSave;
import ir.khalili.products.odds.core.verticle.group.VRTCL_02_GroupUpdate;
import ir.khalili.products.odds.core.verticle.group.VRTCL_03_GroupDelete;
import ir.khalili.products.odds.core.verticle.group.VRTCL_04_GroupFetchAll;
import ir.khalili.products.odds.core.verticle.group.VRTCL_05_GroupFetchById;
import ir.khalili.products.odds.core.verticle.group.VRTCL_06_GroupTeamAssign;
import ir.khalili.products.odds.core.verticle.group.VRTCL_07_GroupTeamUnAssign;
import ir.khalili.products.odds.core.verticle.group.VRTCL_08_GroupTeamFetch;
import ir.khalili.products.odds.core.verticle.group.VRTCL_09_GroupCompetitionFetch;
import ir.khalili.products.odds.core.verticle.league.VRTCL_01_LeagueSave;
import ir.khalili.products.odds.core.verticle.league.VRTCL_02_LeagueUpdate;
import ir.khalili.products.odds.core.verticle.league.VRTCL_03_LeagueDelete;
import ir.khalili.products.odds.core.verticle.league.VRTCL_04_LeagueFetchAll;
import ir.khalili.products.odds.core.verticle.league.VRTCL_05_LeagueFetchById;
import ir.khalili.products.odds.core.verticle.location.VRTCL_01_LocationSave;
import ir.khalili.products.odds.core.verticle.location.VRTCL_02_LocationUpdate;
import ir.khalili.products.odds.core.verticle.location.VRTCL_03_LocationDelete;
import ir.khalili.products.odds.core.verticle.location.VRTCL_04_LocationFetchAll;
import ir.khalili.products.odds.core.verticle.question.VRTCL_01_QuestionSave;
import ir.khalili.products.odds.core.verticle.question.VRTCL_02_QuestionUpdate;
import ir.khalili.products.odds.core.verticle.question.VRTCL_03_QuestionDelete;
import ir.khalili.products.odds.core.verticle.question.VRTCL_04_QuestionFetchAll;
import ir.khalili.products.odds.core.verticle.question.VRTCL_05_QuestionFetchById;
import ir.khalili.products.odds.core.verticle.report.VRTCL_01_ReportRegisteredUsersCount;
import ir.khalili.products.odds.core.verticle.report.VRTCL_02_ReportCompetitorUsersCount;
import ir.khalili.products.odds.core.verticle.report.VRTCL_03_ReportCompetitorUsersAmount;
import ir.khalili.products.odds.core.verticle.report.VRTCL_04_ReportOddsCount;
import ir.khalili.products.odds.core.verticle.report.VRTCL_05_ReportCalculateCompetition;
import ir.khalili.products.odds.core.verticle.report.VRTCL_06_ReportLeagueUsersWithMaximumPoint;
import ir.khalili.products.odds.core.verticle.report.VRTCL_07_ReportLeagueBlockedAmount;
import ir.khalili.products.odds.core.verticle.report.VRTCL_08_ReportAllSectionOddsCountParticipantCountTotalPoint;
import ir.khalili.products.odds.core.verticle.report.VRTCL_09_ReportAllSectionCorrectOddsCountAndOddsPercentage;
import ir.khalili.products.odds.core.verticle.report.VRTCL_11_ReportLeagueTransactionAmount;
import ir.khalili.products.odds.core.verticle.team.VRTCL_01_TeamSave;
import ir.khalili.products.odds.core.verticle.team.VRTCL_02_TeamUpdate;
import ir.khalili.products.odds.core.verticle.team.VRTCL_03_TeamDelete;
import ir.khalili.products.odds.core.verticle.team.VRTCL_04_TeamFetchAll;
import ir.khalili.products.odds.core.verticle.team.VRTCL_05_TeamFetchById;
import ir.khalili.products.odds.core.verticle.team.VRTCL_06_TeamImageUpdate;
import ir.khalili.products.odds.core.verticle.team.VRTCL_07_TeamMemberSave;
import ir.khalili.products.odds.core.verticle.team.VRTCL_08_TeamMemberUpdate;
import ir.khalili.products.odds.core.verticle.team.VRTCL_09_TeamMemberDelete;
import ir.khalili.products.odds.core.verticle.team.VRTCL_10_TeamMemberFetchAll;
import ir.khalili.products.odds.core.verticle.transaction.VRTCL_01_TransactionFetchAll;
import ir.khalili.products.odds.core.verticle.transaction.VRTCL_02_TransactionReject;
import ir.khalili.products.odds.core.verticle.transaction.VRTCL_03_TransactionConfirm;
import ir.khalili.products.odds.core.verticle.transaction.VRTCL_04_TransactionSave;
import ir.khalili.products.odds.core.verticle.user.VRTCL_01_UserFetchAll;
import ir.khalili.products.odds.core.verticle.user.VRTCL_02_UserFetchById;
import ir.khalili.products.odds.core.verticle.user.VRTCL_03_UserFetchOdds;
import ir.khalili.products.odds.core.verticle.user.VRTCL_04_UserFetchQuestionAnswer;
import ir.khalili.products.odds.core.verticle.user.VRTCL_05_UserFetchPointHistory;

public class EntryPoint extends AbstractVerticle {

    public static Vertx vertx;
    public static JsonObject joConfig;
    private static Logger logger = LogManager.getLogger(EntryPoint.class);
    private static Router router;
    private static int port;

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

            joConfig = new JsonObject(jsonString);

        } catch (Exception e) {
            logger.error("configNotValid:", e);
            System.exit(0);
        }

        try {
            port = joConfig.getInteger("port");
        } catch (Exception e) {
            logger.error("invalidConfig:", e);
            System.exit(0);
        }

        DeploymentOptions deploymentOptions = new DeploymentOptions();
        deploymentOptions.setInstances(1);

        vertx = Vertx.vertx();
        vertx.deployVerticle(EntryPoint.class.getName(), deploymentOptions, resHandler -> {

            if (resHandler.failed()) {
                logger.error("deployVerticleFailed:", resHandler.cause());
                vertx.close();
                System.exit(0);
            }

            HelperInitial.initialize(vertx).onComplete(handler -> {

                if (handler.failed()) {
                    logger.error("HelperInitial.initialize.Failed:", handler.cause());
                    vertx.close();
                    System.exit(0);
                }

                deployVerticle();

            });
        });

    }

    private static void deployVerticle() {

		//AUTH
		vertx.deployVerticle(VRTCL_01_AuthLogin.class.getName());
		vertx.deployVerticle(VRTCL_02_AuthOTP.class.getName());


        //COMPETITION
    	vertx.deployVerticle(VRTCL_01_CompetitionSave.class.getName());
    	vertx.deployVerticle(VRTCL_02_CompetitionUpdate.class.getName());
    	vertx.deployVerticle(VRTCL_03_CompetitionDelete.class.getName());
    	vertx.deployVerticle(VRTCL_04_CompetitionFetchAll.class.getName());
    	vertx.deployVerticle(VRTCL_05_CompetitionFetchById.class.getName());
    	vertx.deployVerticle(VRTCL_07_CompetitionQuestionAssign.class.getName());
    	vertx.deployVerticle(VRTCL_08_CompetitionQuestionUnAssign.class.getName());
    	vertx.deployVerticle(VRTCL_09_CompetitionQuestionFetch.class.getName());
    	vertx.deployVerticle(VRTCL_10_CompetitionResultRegister.class.getName());
    	vertx.deployVerticle(VRTCL_11_CompetitionQuestionResultRegister.class.getName());
    	vertx.deployVerticle(VRTCL_12_CompetitionPointCalculation.class.getName());
    	
    	//CONFIG
    	vertx.deployVerticle(VRTCL_01_ConfigUpdate.class.getName());
    	vertx.deployVerticle(VRTCL_02_ConfigFetchAll.class.getName());
    	vertx.deployVerticle(VRTCL_03_ConfigFetchById.class.getName());
    	vertx.deployVerticle(VRTCL_04_ConfigFetchBySymbol.class.getName());
    	
		//FOLDER
    	vertx.deployVerticle(VRTCL_01_FolderSave.class.getName());
    	vertx.deployVerticle(VRTCL_02_FolderUpdate.class.getName());
    	vertx.deployVerticle(VRTCL_03_FolderDelete.class.getName());
    	vertx.deployVerticle(VRTCL_04_FolderFetchAll.class.getName());
    	vertx.deployVerticle(VRTCL_05_FolderFetchById.class.getName());
    	vertx.deployVerticle(VRTCL_06_FolderQuestionAssign.class.getName());
    	vertx.deployVerticle(VRTCL_07_FolderQuestionUnAssign.class.getName());
    	vertx.deployVerticle(VRTCL_08_FolderQuestionFetch.class.getName());

    	
		//GROUP
    	vertx.deployVerticle(VRTCL_01_GroupSave.class.getName());
    	vertx.deployVerticle(VRTCL_02_GroupUpdate.class.getName());
    	vertx.deployVerticle(VRTCL_03_GroupDelete.class.getName());
    	vertx.deployVerticle(VRTCL_04_GroupFetchAll.class.getName());
    	vertx.deployVerticle(VRTCL_05_GroupFetchById.class.getName());
    	vertx.deployVerticle(VRTCL_06_GroupTeamAssign.class.getName());
    	vertx.deployVerticle(VRTCL_07_GroupTeamUnAssign.class.getName());
    	vertx.deployVerticle(VRTCL_08_GroupTeamFetch.class.getName());
    	vertx.deployVerticle(VRTCL_09_GroupCompetitionFetch.class.getName());
    	
		//LEAGUE
    	vertx.deployVerticle(VRTCL_01_LeagueSave.class.getName());
    	vertx.deployVerticle(VRTCL_02_LeagueUpdate.class.getName());
    	vertx.deployVerticle(VRTCL_03_LeagueDelete.class.getName());
    	vertx.deployVerticle(VRTCL_04_LeagueFetchAll.class.getName());
    	vertx.deployVerticle(VRTCL_05_LeagueFetchById.class.getName());

        //LOCATION
    	vertx.deployVerticle(VRTCL_01_LocationSave.class.getName());
    	vertx.deployVerticle(VRTCL_02_LocationUpdate.class.getName());
    	vertx.deployVerticle(VRTCL_03_LocationDelete.class.getName());
    	vertx.deployVerticle(VRTCL_04_LocationFetchAll.class.getName());
    	
    	//ODDS
    	
    	
    	
    	//QUESTION
    	vertx.deployVerticle(VRTCL_01_QuestionSave.class.getName());
    	vertx.deployVerticle(VRTCL_02_QuestionUpdate.class.getName());
    	vertx.deployVerticle(VRTCL_03_QuestionDelete.class.getName());
    	vertx.deployVerticle(VRTCL_04_QuestionFetchAll.class.getName());
    	vertx.deployVerticle(VRTCL_05_QuestionFetchById.class.getName());
    	
    	
    	//REPORT
    	vertx.deployVerticle(VRTCL_01_ReportRegisteredUsersCount.class.getName());
    	vertx.deployVerticle(VRTCL_02_ReportCompetitorUsersCount.class.getName());
    	vertx.deployVerticle(VRTCL_03_ReportCompetitorUsersAmount.class.getName());
    	vertx.deployVerticle(VRTCL_04_ReportOddsCount.class.getName());
    	vertx.deployVerticle(VRTCL_05_ReportCalculateCompetition.class.getName());
    	vertx.deployVerticle(VRTCL_06_ReportLeagueUsersWithMaximumPoint.class.getName());
    	vertx.deployVerticle(VRTCL_07_ReportLeagueBlockedAmount.class.getName());
    	vertx.deployVerticle(VRTCL_08_ReportAllSectionOddsCountParticipantCountTotalPoint.class.getName());
    	vertx.deployVerticle(VRTCL_09_ReportAllSectionCorrectOddsCountAndOddsPercentage.class.getName());
    	vertx.deployVerticle(VRTCL_11_ReportLeagueTransactionAmount.class.getName());
    	
    	//TEAM
    	vertx.deployVerticle(VRTCL_01_TeamSave.class.getName());
    	vertx.deployVerticle(VRTCL_02_TeamUpdate.class.getName());
    	vertx.deployVerticle(VRTCL_03_TeamDelete.class.getName());
    	vertx.deployVerticle(VRTCL_04_TeamFetchAll.class.getName());
    	vertx.deployVerticle(VRTCL_05_TeamFetchById.class.getName());
    	vertx.deployVerticle(VRTCL_06_TeamImageUpdate.class.getName());
    	vertx.deployVerticle(VRTCL_07_TeamMemberSave.class.getName());
    	vertx.deployVerticle(VRTCL_08_TeamMemberUpdate.class.getName());
    	vertx.deployVerticle(VRTCL_09_TeamMemberDelete.class.getName());
    	vertx.deployVerticle(VRTCL_10_TeamMemberFetchAll.class.getName());
    	
    	
		//USER
    	vertx.deployVerticle(VRTCL_01_UserFetchAll.class.getName());
    	vertx.deployVerticle(VRTCL_02_UserFetchById.class.getName());
    	vertx.deployVerticle(VRTCL_03_UserFetchOdds.class.getName());
    	vertx.deployVerticle(VRTCL_04_UserFetchQuestionAnswer.class.getName());
    	vertx.deployVerticle(VRTCL_05_UserFetchPointHistory.class.getName());

		//TRANSACTION
    	vertx.deployVerticle(VRTCL_01_TransactionFetchAll.class.getName());
    	vertx.deployVerticle(VRTCL_02_TransactionReject.class.getName());
    	vertx.deployVerticle(VRTCL_03_TransactionConfirm.class.getName());
    	vertx.deployVerticle(VRTCL_04_TransactionSave.class.getName());
    	
    }

    @Override
    public void start(Promise<Void> startPromise) throws Exception {

        /*********************************************************/
        /*******************CorsHandler***************************/
        /*********************************************************/

        CorsHandler corsHandler = CorsHandler.create();
        corsHandler.allowedMethod(HttpMethod.POST);
        corsHandler.allowedMethod(HttpMethod.PUT);
        corsHandler.allowedMethod(HttpMethod.GET);
        corsHandler.allowedMethod(HttpMethod.DELETE);
        corsHandler.allowedHeader("Authorization");
        corsHandler.allowedHeader("Content-Type");
        corsHandler.allowedHeader("Access-Control-Request-Method");
        corsHandler.allowedHeader("Access-Control-Allow-Credentials");
        corsHandler.allowedHeader("Access-Control-Allow-Origin");
        corsHandler.allowedHeader("Access-Control-Allow-Headers");
        
        /*********************************************************/
        /*******************Router********************************/
        /*********************************************************/
        router = Router.router(vertx);
        router.route().handler(BodyHandler.create());
        router.route().handler(StaticHandler.create());
        router.route().handler(corsHandler);
        router.route().handler(ResponseTimeHandler.create());

        /*********************************************************/
        /*******************RouteManager**************************/
        /*********************************************************/


        //AUTH
		router.post		("/v1/service/odds/auth/login")									.handler(RtMgr_01_AuthLogin 								:: handler);
		router.post		("/v1/service/odds/auth/otp")									.handler(RtMgr_02_AuthOTP									:: handler);
		

        //COMPETITION
        router.post		("/v1/service/odds/competition/save")							.handler(RtMgr_01_CompetitionSave							:: handler);
        router.post		("/v1/service/odds/competition/update")							.handler(RtMgr_02_CompetitionUpdate							:: handler);
        router.post		("/v1/service/odds/competition/delete")							.handler(RtMgr_03_CompetitionDelete							:: handler);
        router.post		("/v1/service/odds/competition/all/fetch")						.handler(RtMgr_04_CompetitionFetchAll						:: handler);
        router.post		("/v1/service/odds/competition/id/fetch")						.handler(RtMgr_05_CompetitionFetchById						:: handler);
        router.post		("/v1/service/odds/competition/question/assign")				.handler(RtMgr_07_CompetitionQuestionAssign					:: handler);
        router.post		("/v1/service/odds/competition/question/unassign")				.handler(RtMgr_08_CompetitionQuestionUnAssign				:: handler);
        router.post		("/v1/service/odds/competition/question/fetch")					.handler(RtMgr_09_CompetitionQuestionFetch					:: handler);
        router.post		("/v1/service/odds/competition/result/register")				.handler(RtMgr_10_CompetitionResultRegister					:: handler);
        router.post		("/v1/service/odds/competition/question/result/register")		.handler(RtMgr_11_CompetitionQuestionResultRegister			:: handler);
        router.post		("/v1/service/odds/competition/point/calculation")				.handler(RtMgr_12_CompetitionPointCalculation				:: handler);
        
    	//CONFIG
        router.post		("/v1/service/odds/config/update")								.handler(RtMgr_01_ConfigUpdate								:: handler);
        router.post		("/v1/service/odds/config/all/fetch")							.handler(RtMgr_02_ConfigFetchAll							:: handler);
        router.post		("/v1/service/odds/config/id/fetch")							.handler(RtMgr_03_ConfigFetchById							:: handler);
        router.post		("/v1/service/odds/config/symbol/fetch")						.handler(RtMgr_04_ConfigFetchBySymbol						:: handler);
        
		//FOLDER
        router.post		("/v1/service/odds/folder/save")								.handler(RtMgr_01_FolderSave								:: handler);
        router.post		("/v1/service/odds/folder/update")								.handler(RtMgr_02_FolderUpdate								:: handler);
        router.post		("/v1/service/odds/folder/delete")								.handler(RtMgr_03_FolderDelete								:: handler);
        router.post		("/v1/service/odds/folder/all/fetch")							.handler(RtMgr_04_FolderFetchAll							:: handler);
        router.post		("/v1/service/odds/folder/id/fetch")							.handler(RtMgr_05_FolderFetchById							:: handler);
        router.post		("/v1/service/odds/folder/question/assign")						.handler(RtMgr_06_FolderQuestionAssign						:: handler);
        router.post		("/v1/service/odds/folder/question/unaasign")					.handler(RtMgr_07_FolderQuestionUnAssign					:: handler);
        router.post		("/v1/service/odds/folder/question/fetch")						.handler(RtMgr_08_FolderQuestionFetch						:: handler);
    	
		//GROUP
        router.post		("/v1/service/odds/group/save")									.handler(RtMgr_01_GroupSave									:: handler);
        router.post		("/v1/service/odds/group/update")								.handler(RtMgr_02_GroupUpdate								:: handler);
        router.post		("/v1/service/odds/group/delete")								.handler(RtMgr_03_GroupDelete								:: handler);
        router.post		("/v1/service/odds/group/all/fetch")							.handler(RtMgr_04_GroupFetchAll								:: handler);
        router.post		("/v1/service/odds/group/id/fetch")								.handler(RtMgr_05_GroupFetchById							:: handler);
        router.post		("/v1/service/odds/group/team/assign")							.handler(RtMgr_06_GroupTeamAssign							:: handler);
        router.post		("/v1/service/odds/group/team/unassign")						.handler(RtMgr_07_GroupTeamUnAssign							:: handler);
        router.post		("/v1/service/odds/group/team/fetch")							.handler(RtMgr_08_GroupTeamFetch							:: handler);
        router.post		("/v1/service/odds/group/competition/fetch")					.handler(RtMgr_09_GroupCompetitionFetch						:: handler);

		//LEAGUE
        router.post		("/v1/service/odds/league/save")								.handler(RtMgr_01_LeagueSave								:: handler);
        router.post		("/v1/service/odds/league/update")								.handler(RtMgr_02_LeagueUpdate								:: handler);
        router.post		("/v1/service/odds/league/delete")								.handler(RtMgr_03_LeagueDelete								:: handler);
        router.post		("/v1/service/odds/league/all/fetch")							.handler(RtMgr_04_LeagueFetchAll							:: handler);
        router.post		("/v1/service/odds/league/id/fetch")							.handler(RtMgr_05_LeagueFetchById							:: handler);
       
        //LOCATION
        router.post		("/v1/service/odds/location/save")								.handler(RtMgr_01_LocationSave								:: handler);
        router.post		("/v1/service/odds/location/update")							.handler(RtMgr_02_LocationUpdate							:: handler);
        router.post		("/v1/service/odds/location/delete")							.handler(RtMgr_03_LocationDelete							:: handler);
        router.post		("/v1/service/odds/location/all/fetch")							.handler(RtMgr_04_LocationFetchAll							:: handler);
        
    	//ODDS
    	
    	
    	
    	//QUESTION
        router.post		("/v1/service/odds/question/save")								.handler(RtMgr_01_QuestionSave								:: handler);
        router.post		("/v1/service/odds/question/update")							.handler(RtMgr_02_QuestionUpdate							:: handler);
        router.post		("/v1/service/odds/question/delete")							.handler(RtMgr_03_QuestionDelete							:: handler);
        router.post		("/v1/service/odds/question/all/fetch")							.handler(RtMgr_04_QuestionFetchAll							:: handler);
        router.post		("/v1/service/odds/question/id/fetch")							.handler(RtMgr_05_QuestionFetchById							:: handler);

        //REPORT
        router.post		("/v1/service/odds/report/registered/users/count")				.handler(RtMgr_01_ReportRegisteredUsersCount				:: handler);
        router.post		("/v1/service/odds/report/competitor/users/count")				.handler(RtMgr_02_ReportCompetitorUsersCount				:: handler);
        router.post		("/v1/service/odds/report/competitor/users/amount")				.handler(RtMgr_03_ReportCompetitorUsersAmount				:: handler);
        router.post		("/v1/service/odds/report/odds/count")							.handler(RtMgr_04_ReportOddsCount							:: handler);
        router.post		("/v1/service/odds/report/league/odds/users/point/maximum")		.handler(RtMgr_06_ReportLeagueUsersWithMaximumPoint			:: handler);
        router.post		("/v1/service/odds/report/league/blocked/amount")				.handler(RtMgr_07_ReportLeagueBlockedAmount					:: handler);
        router.post		("/v1/service/odds/report/odds/participant/point/count")		.handler(RtMgr_08_ReportAllSectionOddsCountParticipantCountTotalPoint:: handler);
        router.post		("/v1/service/odds/report/odds/correct/percentage/count")		.handler(RtMgr_09_ReportAllSectionCorrectOddsCountAndOddsPercentage:: handler);
        router.post		("/v1/service/odds/report/league/transaction/amount")			.handler(RtMgr_11_ReportLeagueTransactionAmount				:: handler);
        
    	//TEAM
        router.post		("/v1/service/odds/team/save")									.handler(RtMgr_01_TeamSave									:: handler);
        router.post		("/v1/service/odds/team/update")								.handler(RtMgr_02_TeamUpdate								:: handler);
        router.post		("/v1/service/odds/team/delete")								.handler(RtMgr_03_TeamDelete								:: handler);
        router.post		("/v1/service/odds/team/all/fetch")								.handler(RtMgr_04_TeamFetchAll								:: handler);
        router.post		("/v1/service/odds/team/id/fetch")								.handler(RtMgr_05_TeamFetchById								:: handler);
        router.post		("/v1/service/odds/team/image/update")							.handler(RtMgr_06_TeamImageUpdate 							:: handler);
        router.post		("/v1/service/odds/team/member/save")							.handler(RtMgr_07_TeamMemberSave							:: handler);
        router.post		("/v1/service/odds/team/member/update")							.handler(RtMgr_08_TeamMemberUpdate							:: handler);
        router.post		("/v1/service/odds/team/member/delete")							.handler(RtMgr_09_TeamMemberDelete							:: handler);
        router.post		("/v1/service/odds/team/member/all/fetch")						.handler(RtMgr_10_TeamMemberFetchAll						:: handler);
        
		//USER
        router.post		("/v1/service/odds/user/all/fetch")								.handler(RtMgr_01_UserFetchAll								:: handler);
        router.post		("/v1/service/odds/user/id/fetch")								.handler(RtMgr_02_UserFetchById								:: handler);
        router.post		("/v1/service/odds/user/fetch/odds")							.handler(RtMgr_03_UserFetchOdds								:: handler);
        router.post		("/v1/service/odds/user/fetch/question")						.handler(RtMgr_04_UserFetchQuestionAnswer					:: handler);
        router.post		("/v1/service/odds/user/fetch/history")							.handler(RtMgr_05_UserFetchPointHistory						:: handler);

		//TRANSACTION
        router.post		("/v1/service/odds/transaction/all/fetch")						.handler(RtMgr_01_TransactionFetchAll						:: handler);
        router.post		("/v1/service/odds/transaction/reject")							.handler(RtMgr_02_TransactionReject							:: handler);
        router.post		("/v1/service/odds/transaction/confirm")						.handler(RtMgr_03_TransactionConfirm						:: handler);
        router.post		("/v1/service/odds/transaction/save")							.handler(RtMgr_04_TransactionSave							:: handler);

        
        vertx.createHttpServer().requestHandler(router).listen(port);

        /*********************************************************/
        /*********************************************************/

        startPromise.complete();
    }

}
