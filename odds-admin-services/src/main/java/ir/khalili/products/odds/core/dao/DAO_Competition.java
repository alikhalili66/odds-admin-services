package ir.khalili.products.odds.core.dao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.sql.SQLConnection;
import ir.khalili.products.odds.core.excp.dao.DAOEXCP_Internal;

public class DAO_Competition {

    private static final Logger logger = LogManager.getLogger(DAO_Competition.class);
    
    public static Future<Void> save(SQLConnection sqlConnection, JsonObject message) {

		Promise<Void> promise = Promise.promise();
		
		JsonArray params = new JsonArray();
		
		params.add(message.getInteger("leagueId"));
		params.add(message.getInteger("teamId1"));
		params.add(message.getInteger("teamId2"));
		params.add(message.getInteger("groupId"));
		params.add(message.getInteger("locationId"));
		params.add(message.getString("activeFrom"));
		params.add(message.getString("activeTo"));
		params.add(message.getString("oddsFrom"));
		params.add(message.getString("oddsTo"));
		params.add(message.getString("competitionDate"));
		params.add(message.getInteger("userId"));
		
		sqlConnection.updateWithParams(""
				+ "insert into toppcompetition("
				+ "ID,"
				+ "LEAGUE_ID,"
				+ "TEAM1_ID,"
				+ "TEAM2_ID,"
				+ "GROUP_ID,"
				+ "LOCATION_ID,"
				+ "ACTIVEFROM,"
				+ "ACTIVETO,"
				+ "ODDSFROM,"
				+ "ODDSTO,"
				+ "COMPETITIONDATE,"
				+ "CREATIONDATE,"
				+ "createdBy_id)"
				+ "values("
				+ "soppcompetition.nextval,"
				+ "?,"
				+ "?,"
				+ "?,"
				+ "?,"
				+ "?,"
				+ "TO_DATE(?, 'Dy Mon DD YYYY HH24:MI:SS'),"
				+ "TO_DATE(?, 'Dy Mon DD YYYY HH24:MI:SS'),"
				+ "TO_DATE(?, 'Dy Mon DD YYYY HH24:MI:SS'),"
				+ "TO_DATE(?, 'Dy Mon DD YYYY HH24:MI:SS'),"
				+ "TO_DATE(?, 'Dy Mon DD YYYY HH24:MI:SS'),"
				+ "sysdate,"
				+ "?)", params, resultHandler->{
			if(resultHandler.failed()) {
				logger.error("Unable to get accessQueryResult:", resultHandler.cause());
				promise.fail(new DAOEXCP_Internal(-100, "???????? ??????????. ???? ?????????? ???????????? ???????? ????????????."));
				return;
			}
			
			logger.trace("SaveCompetitionSuccessful");
			promise.complete();
			
		});
		
		return promise.future();
	}

    public static Future<Void> update(SQLConnection sqlConnection, JsonObject message) {

		Promise<Void> promise = Promise.promise();
		
		JsonArray params = new JsonArray();
		
		params.add(message.getInteger("leagueId"));
		params.add(message.getInteger("teamId1"));
		params.add(message.getInteger("teamId2"));
		params.add(message.getInteger("groupId"));
		params.add(message.getInteger("locationId"));
		params.add(message.getString("activeFrom"));
		params.add(message.getString("activeTo"));
		params.add(message.getString("oddsFrom"));
		params.add(message.getString("oddsTo"));
		params.add(message.getString("competitionDate"));
		params.add(message.getInteger("competitionId"));
		
		sqlConnection.updateWithParams(""
				+ "update toppcompetition c set "
				+ "c.LEAGUE_ID=?,"
				+ "c.TEAM1_ID=?,"
				+ "c.TEAM2_ID=?,"
				+ "c.GROUP_ID=?,"
				+ "c.LOCATION_ID=?,"
				+ "c.ACTIVEFROM=TO_DATE(?, 'Dy Mon DD YYYY HH24:MI:SS'),"
				+ "c.ACTIVETO=TO_DATE(?, 'Dy Mon DD YYYY HH24:MI:SS'),"
				+ "c.ODDSFROM=TO_DATE(?, 'Dy Mon DD YYYY HH24:MI:SS'),"
				+ "c.ODDSTO=TO_DATE(?, 'Dy Mon DD YYYY HH24:MI:SS'),"
				+ "c.COMPETITIONDATE=TO_DATE(?, 'Dy Mon DD YYYY HH24:MI:SS') "
				+ " where c.id=?", params, resultHandler->{
			if(resultHandler.failed()) {
				logger.error("Unable to get accessQueryResult:", resultHandler.cause());
				promise.fail(new DAOEXCP_Internal(-100, "???????? ??????????. ???? ?????????? ???????????? ???????? ????????????."));
				return;
			}
			
			logger.trace("UpdateCompetitionSuccessful");
			promise.complete();
			
		});
		
		return promise.future();
	}
    
    public static Future<Void> delete(SQLConnection sqlConnection, Integer competitionId) {
        Promise<Void> promise = Promise.promise();
        JsonArray params = new JsonArray();
        params.add(competitionId);
        
        sqlConnection.updateWithParams("update toppcompetition set dto=sysdate WHERE id=?", params, handler -> {
			if(handler.failed()) {
				logger.error("Unable to get accessQueryResult:", handler.cause());
				promise.fail(new DAOEXCP_Internal(-100, "???????? ??????????. ???? ?????????? ???????????? ???????? ????????????."));
				return;
			}
			
			logger.trace("deleteCompetitionByIdSuccessful");
			promise.complete();
			
		});
		return promise.future();
    }
    
    public static Future<List<JsonObject>> fetchAll(SQLConnection sqlConnection, JsonObject message) {
        Promise<List<JsonObject>> promise = Promise.promise();
        JsonArray params = new JsonArray();
        params.add(message.getInteger("leagueId"));
        params.add(message.getInteger("groupId"));
        
        sqlConnection.queryWithParams("SELECT "
        		+ "c.id,"
        		+ "c.LEAGUE_ID,"
        		+ "c.TEAM1_ID,"
        		+ "(select name from toppteam t where t.id=c.TEAM1_ID) TEAM1_name,"
        		+ "(select image from toppteam t where t.id=c.TEAM1_ID) TEAM1_image,"
        		+ "c.TEAM2_ID,"
        		+ "(select name from toppteam t where t.id=c.TEAM2_ID) TEAM2_name,"
        		+ "(select image from toppteam t where t.id=c.TEAM2_ID) TEAM2_image,"
        		+ "c.GROUP_ID,"
        		+ "(select g.name from toppgroup g where g.id=c.GROUP_ID) GROUP_name,"
        		+ "c.LOCATION_ID,"
        		+ "(select l.name from tOPPLocation l where l.id=c.LOCATION_ID) LOCATION_name,"
        		+ "c.RESULT,"
        		+ "To_Char(c.ACTIVEFROM, 'Dy Mon DD YYYY HH24:MI:SS')|| ' GMT+0330' ACTIVE_FROM,"
        		+ "To_Char(c.ACTIVETO, 'Dy Mon DD YYYY HH24:MI:SS')|| ' GMT+0330' ACTIVE_TO,"
        		+ "To_Char(c.ODDSFROM, 'Dy Mon DD YYYY HH24:MI:SS')|| ' GMT+0330' ODDS_FROM,"
        		+ "To_Char(c.ODDSTO, 'Dy Mon DD YYYY HH24:MI:SS')|| ' GMT+0330' ODDS_TO,"
        		+ "To_Char(c.COMPETITIONDATE, 'Dy Mon DD YYYY HH24:MI:SS')|| ' GMT+0330' COMPETITION_DATE,"
        		+ "To_Char(c.creationdate, 'Dy Mon DD YYYY HH24:MI:SS')|| ' GMT+0330' creation_date"
        		+ "  FROM toppcompetition c WHERE c.LEAGUE_ID=nvl(?, c.LEAGUE_ID) and c.GROUP_ID=nvl(?, c.GROUP_ID) and c.dto is null", params, handler -> {
            if (handler.failed()) {
            	logger.error("Unable to get accessQueryResult:", handler.cause());
                promise.fail(new DAOEXCP_Internal(-100, "???????? ??????????. ???? ?????????? ???????????? ???????? ????????????."));
            } else {
                if (null == handler.result() || null == handler.result().getRows() || handler.result().getRows().isEmpty()) {
                	logger.error("fetchAllCompetitionNoDataFound");
                	promise.complete(new ArrayList<>());
                } else {
                    logger.trace("fetchAllCompetitionSuccessful");
                    promise.complete(handler.result().getRows());
                }
            
            }
        });

        return promise.future();
    }
    
    public static Future<JsonObject> fetchById(SQLConnection sqlConnection, Integer competitionId) {
        Promise<JsonObject> promise = Promise.promise();
        JsonArray params = new JsonArray();
        params.add(competitionId);
        
        sqlConnection.queryWithParams("SELECT "
        		+ "c.id,"
        		+ "c.LEAGUE_ID,"
        		+ "c.TEAM1_ID,"
        		+ "(select name from toppteam t where t.id=c.TEAM1_ID) TEAM1_name,"
        		+ "c.TEAM2_ID,"
        		+ "(select name from toppteam t where t.id=c.TEAM2_ID) TEAM2_name,"
        		+ "c.GROUP_ID,"
        		+ "c.LOCATION_ID,"
        		+ "c.LEAGUE_ID,"
        		+ "c.RESULT,"
        		+ "c.IDENTIFIER,"
        		+ "To_Char(c.ACTIVEFROM, 'Dy Mon DD YYYY HH24:MI:SS')|| ' GMT+0330' ACTIVE_FROM,"
        		+ "To_Char(c.ACTIVETO, 'Dy Mon DD YYYY HH24:MI:SS')|| ' GMT+0330' ACTIVE_TO,"
        		+ "To_Char(c.ODDSFROM, 'Dy Mon DD YYYY HH24:MI:SS')|| ' GMT+0330' ODDS_FROM,"
        		+ "To_Char(c.ODDSTO, 'Dy Mon DD YYYY HH24:MI:SS')|| ' GMT+0330' ODDS_TO,"
        		+ "To_Char(c.COMPETITIONDATE, 'Dy Mon DD YYYY HH24:MI:SS')|| ' GMT+0330' COMPETITION_DATE,"
        		+ "To_Char(c.creationdate, 'Dy Mon DD YYYY HH24:MI:SS')|| ' GMT+0330' creation_date"
        		+ "  FROM toppcompetition c WHERE c.id=? and c.dto is null", params, handler -> {
            if (handler.failed()) {
            	logger.error("Unable to get accessQueryResult:", handler.cause());
                promise.fail(new DAOEXCP_Internal(-100, "???????? ??????????. ???? ?????????? ???????????? ???????? ????????????."));
            } else {
                if (null == handler.result() || null == handler.result().getRows() || handler.result().getRows().isEmpty()) {
                	logger.error("fetchCompetitionByIdNoDataFound");
                    promise.fail(new DAOEXCP_Internal(-100, "???????? ???? ???????? ??????"));
                } else {
                    logger.trace("fetchAllCompetitionByIdSuccessful");
                    promise.complete(handler.result().getRows().get(0));
                }
            
            }
        });

        return promise.future();
    }
    
    public static Future<Void> assignQuestion(SQLConnection sqlConnection, JsonObject message) {
        Promise<Void> promise = Promise.promise();
        JsonArray params = new JsonArray();
        params.add(message.getInteger("competitionId"));
        params.add(message.getInteger("questionId"));
        params.add(message.getInteger("norder"));
        
		sqlConnection.updateWithParams("insert into toppcompetitionquestion (COMPETITION_ID, QUESTION_ID, NORDER) values(?,?,?)", params, resultHandler->{
			if(resultHandler.failed()) {
				logger.error("Unable to get accessQueryResult:", resultHandler.cause());
				promise.fail(new DAOEXCP_Internal(-100, "???????? ??????????. ???? ?????????? ???????????? ???????? ????????????."));
				return;
			}
			
			logger.trace("assignQuestionSuccessful");
			promise.complete();
			
		});
        return promise.future();
    }
    
    public static Future<Void> unAssignQuestion(SQLConnection sqlConnection, JsonObject message) {
        Promise<Void> promise = Promise.promise();
        JsonArray params = new JsonArray();
        params.add(message.getInteger("competitionId"));
        params.add(message.getInteger("questionId"));
        
        sqlConnection.updateWithParams("delete from toppcompetitionquestion where COMPETITION_ID=? and QUESTION_ID=?", params, handler -> {
			if(handler.failed()) {
				logger.error("Unable to get accessQueryResult:", handler.cause());
				promise.fail(new DAOEXCP_Internal(-100, "???????? ??????????. ???? ?????????? ???????????? ???????? ????????????."));
				return;
			}
			
			logger.trace("unAssignQuestionSuccessful");
			promise.complete();
			
		});
		return promise.future();
    }
    
    public static Future<List<JsonObject>> fetchQuestion(SQLConnection sqlConnection, JsonObject message) {
        Promise<List<JsonObject>> promise = Promise.promise();
        JsonArray params = new JsonArray();
        params.add(message.getInteger("competitionId"));
        
        sqlConnection.queryWithParams(""
        		+ "select "
        		+ "q.id,"
        		+ "q.league_id,"
        		+ "q.question,"
        		+ "q.type,"
        		+ "q.minpoint,"
        		+ "q.symbol, "
        		+ "cq.norder, "
        		+ "cq.result "
        		+ "from "
        		+ "toppquestion q, toppcompetitionquestion cq "
        		+ "where "
        		+ "cq.COMPETITION_ID=? and cq.QUESTION_ID=q.id and q.dto is null "
        		+ "order by cq.norder asc", params, handler -> {
            if (handler.failed()) {
            	logger.error("Unable to get accessQueryResult:", handler.cause());
                promise.fail(new DAOEXCP_Internal(-100, "???????? ??????????. ???? ?????????? ???????????? ???????? ????????????."));
            } else {

                if (null == handler.result() || null == handler.result().getRows() || handler.result().getRows().isEmpty()) {
                	logger.error("fetchQuestionNoDataFound");
                	promise.complete(new ArrayList<>());
                } else {
                    logger.trace("CompetitionfetchQuestionSuccessful");
                    promise.complete(handler.result().getRows());
                }
            
            }
        });

        return promise.future();
    }
    
    public static Future<List<JsonObject>> fetchCompetitionByGroupId(SQLConnection sqlConnection, int groupId) {
        Promise<List<JsonObject>> promise = Promise.promise();
        
        JsonArray params = new JsonArray();
        params.add(groupId);
        //TODO
        sqlConnection.queryWithParams("SELECT "
        		+ "c.id,"
        		+ "c.LEAGUE_ID,"
        		+ "c.TEAM1_ID,"
        		+ "(select name from toppteam t where t.id=c.TEAM1_ID) TEAM1_name,"
        		+ "(select image from toppteam t where t.id=c.TEAM1_ID) TEAM1_image,"
        		+ "c.TEAM2_ID,"
        		+ "(select name from toppteam t where t.id=c.TEAM2_ID) TEAM2_name,"
        		+ "(select image from toppteam t where t.id=c.TEAM2_ID) TEAM2_image,"
        		+ "c.GROUP_ID,"
        		+ "(select g.name from toppgroup g where g.id=c.GROUP_ID) GROUP_name,"
        		+ "c.LOCATION_ID,"
        		+ "(select l.name from tOPPLocation l where l.id=c.LOCATION_ID) LOCATION_name,"
        		+ "c.RESULT,"
        		+ "To_Char(c.ACTIVEFROM, 'Dy Mon DD YYYY HH24:MI:SS')|| ' GMT+0330' ACTIVE_FROM,"
        		+ "To_Char(c.ACTIVETO, 'Dy Mon DD YYYY HH24:MI:SS')|| ' GMT+0330' ACTIVE_TO,"
        		+ "To_Char(c.ODDSFROM, 'Dy Mon DD YYYY HH24:MI:SS')|| ' GMT+0330' ODDS_FROM,"
        		+ "To_Char(c.ODDSTO, 'Dy Mon DD YYYY HH24:MI:SS')|| ' GMT+0330' ODDS_TO,"
        		+ "To_Char(c.COMPETITIONDATE, 'Dy Mon DD YYYY HH24:MI:SS')|| ' GMT+0330' COMPETITION_DATE,"
        		+ "To_Char(c.creationdate, 'Dy Mon DD YYYY HH24:MI:SS')|| ' GMT+0330' creation_date"
        		+ "  FROM toppcompetition c WHERE c.GROUP_ID = ? and c.dto is null", params, handler -> {
            if (handler.failed()) {
            	logger.error("Unable to get accessQueryResult:", handler.cause());
                promise.fail(new DAOEXCP_Internal(-100, "???????? ??????????. ???? ?????????? ???????????? ???????? ????????????."));
            } else {
                if (null == handler.result() || null == handler.result().getRows() || handler.result().getRows().isEmpty()) {
                	logger.error("fetchCompetitionByGroupIdNoDataFound");
                    promise.complete(new ArrayList<>());
                } else {
                    logger.trace("fetchAllCompetitionSuccessful");
                    promise.complete(handler.result().getRows());
                }
            
            }
        });

        return promise.future();
    }
    
    public static Future<Void> resultRegister(SQLConnection sqlConnection, JsonObject message) {

		Promise<Void> promise = Promise.promise();
		
		JsonArray params = new JsonArray();
		
		params.add(message.getString("result"));
		params.add(message.getInteger("competitionId"));
		
		sqlConnection.updateWithParams(""
				+ "update toppcompetition c set "
				+ "c.result=? "
				+ " where c.id=?", params, resultHandler->{
			if(resultHandler.failed()) {
				logger.error("Unable to get accessQueryResult:", resultHandler.cause());
				promise.fail(new DAOEXCP_Internal(-100, "???????? ??????????. ???? ?????????? ???????????? ???????? ????????????."));
				return;
			}
			
			logger.trace("UpdateCompetitionResultSuccessful");
			promise.complete();
			
		});
		
		return promise.future();
	}
    
	public static Future<Void> questionResultRegister(SQLConnection sqlConnection, JsonObject message) {
		
		Promise<Void> promise = Promise.promise();
		
		List<JsonArray> params = new ArrayList<>();
		
		for (Iterator<Object> iterator = message.getJsonArray("results").iterator(); iterator.hasNext();) {
			JsonObject joResult = (JsonObject) iterator.next();
			params.add(new JsonArray().add(joResult.getString("result")).add(message.getInteger("competitionId")).add(joResult.getInteger("questionId")));
		}
		
		sqlConnection.batchWithParams("update toppcompetitionquestion cq set cq.result=? where cq.COMPETITION_ID=? and cq.QUESTION_ID=?", params, resultHandler->{
			if(resultHandler.failed()) {
				logger.error("Unable to get accessQueryResult:", resultHandler.cause());
				promise.fail(new DAOEXCP_Internal(-100, "???????? ??????????. ???? ?????????? ???????????? ???????? ????????????."));
				return;
			}
			logger.trace("QuestionResultRegisterSuccessful");
			promise.complete();
			
		});
		return promise.future();
	}
	
    public static Future<List<JsonObject>> fetchOddsTotalPointByCompetitionId(SQLConnection sqlConnection, Integer competitionId) {
        Promise<List<JsonObject>> promise = Promise.promise();
        JsonArray params = new JsonArray();
        params.add(competitionId);
        
        sqlConnection.queryWithParams("select question_id,SUM(point) total_point from toppodds where competition_id=? group by question_id", params, handler -> {
            if (handler.failed()) {
            	logger.error("Unable to get accessQueryResult:", handler.cause());
                promise.fail(new DAOEXCP_Internal(-100, "???????? ??????????. ???? ?????????? ???????????? ???????? ????????????."));
            } else {
                if (null == handler.result() || null == handler.result().getRows() || handler.result().getRows().isEmpty()) {
                	logger.error("fetchOddsTotalPointByCompetitionIdNoDataFound");
                    promise.fail(new DAOEXCP_Internal(-100, "???????? ???? ???????? ??????"));
                } else {
                    logger.trace("fetchOddsTotalPointSuccessful");
                    promise.complete(handler.result().getRows());
                }
            }
        });
        return promise.future();
    }
    
    public static Future<List<JsonObject>> fetchOddsTotalCorrectPointByCompetitionId(SQLConnection sqlConnection, Integer competitionId) {
        Promise<List<JsonObject>> promise = Promise.promise();
        JsonArray params = new JsonArray();
        params.add(competitionId);
        
        sqlConnection.queryWithParams("select question_id,SUM(point) total_point from toppodds where competition_id=? group by question_id", params, handler -> {
            if (handler.failed()) {
            	logger.error("Unable to get accessQueryResult:", handler.cause());
                promise.fail(new DAOEXCP_Internal(-100, "???????? ??????????. ???? ?????????? ???????????? ???????? ????????????."));
            } else {
                if (null == handler.result() || null == handler.result().getRows() || handler.result().getRows().isEmpty()) {
                	logger.error("fetchOddsTotalCorrectPointByCompetitionIdNoDataFound");
                    promise.fail(new DAOEXCP_Internal(-100, "???????? ???? ???????? ??????"));
                } else {
                    logger.trace("fetchOddsTotalPointSuccessful");
                    promise.complete(handler.result().getRows());
                }
            }
        });
        return promise.future();
    }
    
    public static Future<List<JsonObject>> fetchOddsWinnerUsersByCompetitionIdAndQuestionId(SQLConnection sqlConnection, Integer competitionId, Integer questionId) {
        Promise<List<JsonObject>> promise = Promise.promise();
        JsonArray params = new JsonArray();
        params.add(questionId);
        params.add(competitionId);
        
        sqlConnection.queryWithParams("SELECT" + 
        		"    o.user_id, " + 
        		"    o.POINT " + 
        		" FROM" + 
        		"    toppodds                  o," + 
        		"    toppcompetitionquestion   cq " + 
        		" WHERE 1 = 1 " + 
        		"    AND o.question_id=?" + 
        		"    AND o.competition_id=?" +
        		"    AND o.question_id = cq.question_id" + 
        		"    AND o.competition_id = cq.competition_id" + 
        		"    AND o.answer = cq.result" + 
        		"", params, handler -> {
            if (handler.failed()) {
            	logger.error("Unable to get accessQueryResult:", handler.cause());
                promise.fail(new DAOEXCP_Internal(-100, "???????? ??????????. ???? ?????????? ???????????? ???????? ????????????."));
            } else {
                if (null == handler.result() || null == handler.result().getRows() || handler.result().getRows().isEmpty()) {
                	logger.error("fetchOddsWinnerUsersByCompetitionIdAndQuestionIdNoDataFound");
                	promise.complete(new ArrayList<>());
                } else {
                    logger.trace("fetchOddsWinnerUsersByCompetitionIdAndQuestionIdSuccessful");
                    promise.complete(handler.result().getRows());
                }
            }
        });
        return promise.future();
    }
    
    public static Future<List<JsonObject>> fetchOddsLoserUsersByCompetitionIdAndQuestionId(SQLConnection sqlConnection, Integer competitionId, Integer questionId) {
        Promise<List<JsonObject>> promise = Promise.promise();
        JsonArray params = new JsonArray();
        params.add(questionId);
        params.add(competitionId);
        
        sqlConnection.queryWithParams("SELECT" + 
        		"    o.user_id " + 
        		" FROM " + 
        		"    toppodds                  o," + 
        		"    toppcompetitionquestion   cq " + 
        		" WHERE 1 = 1 " + 
        		"    AND o.question_id=?" + 
        		"    AND o.competition_id=?"+
        		"    AND o.question_id = cq.question_id" + 
        		"    AND o.competition_id = cq.competition_id" + 
        		"    AND o.answer != cq.result" + 
        		" ", params, handler -> {
            if (handler.failed()) {
            	logger.error("Unable to get accessQueryResult:", handler.cause());
                promise.fail(new DAOEXCP_Internal(-100, "???????? ??????????. ???? ?????????? ???????????? ???????? ????????????."));
            } else {
                if (null == handler.result() || null == handler.result().getRows() || handler.result().getRows().isEmpty()) {
                	logger.error("fetchOddsLoserUsersByCompetitionIdAndQuestionIdNoDataFound");
                	promise.complete(new ArrayList<>());
                } else {
                    logger.trace("fetchOddsLoserUsersByCompetitionIdAndQuestionIdSuccessful");
                    promise.complete(handler.result().getRows());
                }
            }
        });
        return promise.future();
    }
    
    
    public static Future<Void> updateRewardPointForCalculation(SQLConnection sqlConnection, int coefficient, Integer questionId, Integer competitionId, List<Integer> winnerUserIdList) {

		Promise<Void> promise = Promise.promise();
		
		List<JsonArray> params = new ArrayList<>();
		
		winnerUserIdList.forEach(id -> {
			params.add(new JsonArray()
					.add(coefficient)
					.add(questionId)
					.add(competitionId)
					.add(id)
					);
		});
		
		
		sqlConnection.batchWithParams(""
				+ "update toppodds o set "
				+ "o.rewardpoint= o.POINT * ? "
				+ " where o.question_id=? and o.competition_id=? and o.user_id=?", params, resultHandler->{
			if(resultHandler.failed()) {
				logger.error("Unable to get accessQueryResult:", resultHandler.cause());
				promise.fail(new DAOEXCP_Internal(-100, "???????? ??????????. ???? ?????????? ???????????? ???????? ????????????."));
				return;
			}
			
			logger.trace("updateRewardPointForCalculationSuccessful");
			promise.complete();
			
		});
		
		return promise.future();
	}
    
    public static Future<Void> updateUserPointForCalculation(SQLConnection sqlConnection, List<JsonObject> winnerUsers, Integer competitionId) {

		Promise<Void> promise = Promise.promise();
		
		List<JsonArray> params = new ArrayList<>();
		winnerUsers.forEach(joUser -> {
			params.add(new JsonArray()
					.add(joUser.getInteger("REWARD_POINT"))
					.add(competitionId)
					.add(joUser.getInteger("REWARD_POINT"))
					.add(joUser.getInteger("USER_ID"))
					);
		});
		
		sqlConnection.batchWithParams(""
				+ ""
				+ " UPDATE TOPPUSER u " 
				+ " SET u.POINT = u.POINT + ? - NVL( (SELECT uph.POINT FROM TOPPUSERPOINTHISTORY uph  WHERE uph.USER_ID = u.id AND uph.COMPETITION_ID = ?  ORDER BY uph.ID DESC  FETCH FIRST ROW ONLY ),0), lastChangePoint= ? " 
				+ " WHERE u.id = ?"
				+ "", params, resultHandler->{
			if(resultHandler.failed()) {
				logger.error("Unable to get accessQueryResult:", resultHandler.cause());
				promise.fail(new DAOEXCP_Internal(-100, "???????? ??????????. ???? ?????????? ???????????? ???????? ????????????."));
				return;
			}
			
			logger.trace("updateUserPointForCalculationSuccessful");
			promise.complete();
			
		});
		
		return promise.future();
	}
    
    public static Future<List<JsonObject>> fetchTotalUserPointOfCompetition(SQLConnection sqlConnection, Integer competitionId) {
        Promise<List<JsonObject>> promise = Promise.promise();
        JsonArray params = new JsonArray();
        params.add(competitionId);
        
        sqlConnection.queryWithParams("select USER_ID,sum(REWARDPOINT) REWARD_POINT from toppodds  where    competition_id=?   group by USER_ID HAVING  sum(REWARDPOINT) > 0", params, handler -> {
            if (handler.failed()) {
            	logger.error("Unable to get accessQueryResult:", handler.cause());
                promise.fail(new DAOEXCP_Internal(-100, "???????? ??????????. ???? ?????????? ???????????? ???????? ????????????."));
            } else {
                if (null == handler.result() || null == handler.result().getRows() || handler.result().getRows().isEmpty()) {
                	logger.error("fetchTotalUserPointOfCompetitionNoDataFound");
                	promise.complete(new ArrayList<>());
                } else {
                    logger.trace("fetchTotalUserPointOfCompetitionSuccessful");
                    promise.complete(handler.result().getRows());
                }
            }
        });
        return promise.future();
    }
    
    public static Future<Void> saveQuestionCorrectAnswer(SQLConnection sqlConnection, int competitionId, int leagueId, Map<String, String> liveScoreMap) {

        Promise<Void> promise = Promise.promise();

        List<JsonArray> params = new ArrayList<>();

        for (Iterator<String> iterator = liveScoreMap.keySet().iterator(); iterator.hasNext(); ) {
            String symbol = iterator.next();
            params.add(new JsonArray().add(liveScoreMap.get(symbol)).add(competitionId).add(symbol).add(leagueId));
        }

        System.out.println(params);
        
        sqlConnection.batchWithParams("" + 
        		" UPDATE toppcompetitionquestion " +
                " SET result = ? " +
                " WHERE competition_id = ? AND question_id = (SELECT q.id FROM toppquestion q WHERE q.symbol = ? and q.league_id = ? AND DTO IS NULL AND ROWNUM = 1) ", params, resultHandler -> {
            if (resultHandler.failed()) {
                logger.error("Unable to get accessQueryResult:", resultHandler.cause());
                promise.fail(new DAOEXCP_Internal(-100, "???????? ??????????. ???? ?????????? ???????????? ???????? ????????????."));
                return;
            }
            logger.trace("saveQuestionCorrectAnswer");
            promise.complete();
        });
       
        return promise.future();
    }
    

    public static Future<Void> saveHistory(SQLConnection sqlConnection, JsonObject joCompetition, String historyType, String historyDescription, Integer historyById) {

		Promise<Void> promise = Promise.promise();
		
		JsonArray params = new JsonArray();
		params.add(joCompetition.getInteger("ID"));
		params.add(joCompetition.getInteger("TEAM1_ID"));
		params.add(joCompetition.getInteger("TEAM2_ID"));
		params.add(joCompetition.getInteger("GROUP_ID"));
		params.add(joCompetition.getInteger("LOCATION_ID"));
		params.add(joCompetition.getString("COMPETITION_DATE").split("GMT")[0]);
		params.add(joCompetition.getString("ACTIVE_FROM").split("GMT")[0]);
		params.add(joCompetition.getString("ACTIVE_TO").split("GMT")[0]);
		params.add(joCompetition.getString("ODDS_FROM").split("GMT")[0]);
		params.add(joCompetition.getString("ODDS_TO").split("GMT")[0]);
		params.add(joCompetition.getString("RESULT"));
		params.add(historyType);
		params.add(historyDescription);
		params.add(historyById);
		
		sqlConnection.updateWithParams(""
				+ "insert into tOPPCompetitionHistory("
				+ "ID,"
				+ "COMPETITION_ID,"
				+ "TEAM1_ID,"
				+ "TEAM2_ID,"
				+ "GROUP_ID,"
				+ "LOCATION_ID,"
				+ "COMPETITIONDATE,"
				+ "ACTIVEFROM,"
				+ "ACTIVETO,"
				+ "ODDSFROM,"
				+ "ODDSTO,"
				+ "RESULT,"
				+ "HISTORYTYPE,"
				+ "HISTORYDESCRIPTION,"
				+ "HISTORYBY_ID,"
				+ "HISTORYDATE)"
				+ "values("
				+ "soppcompetitionhistory.nextval,"
				+ "?,"
				+ "?,"
				+ "?,"
				+ "?,"
				+ "?,"
				+ "TO_DATE(?, 'Dy Mon DD YYYY HH24:MI:SS'),"
				+ "TO_DATE(?, 'Dy Mon DD YYYY HH24:MI:SS'),"
				+ "TO_DATE(?, 'Dy Mon DD YYYY HH24:MI:SS'),"
				+ "TO_DATE(?, 'Dy Mon DD YYYY HH24:MI:SS'),"
				+ "TO_DATE(?, 'Dy Mon DD YYYY HH24:MI:SS'),"
				+ "?,"
				+ "?,"
				+ "?,"
				+ "?,"
				+ "sysdate)", params, resultHandler->{
			if(resultHandler.failed()) {
				logger.error("Unable to get accessQueryResult:", resultHandler.cause());
				promise.fail(new DAOEXCP_Internal(-100, "???????? ??????????. ???? ?????????? ???????????? ???????? ????????????."));
				return;
			}
			
			logger.trace("SaveCompetitionHistorySuccessful");
			promise.complete();
			
		});
		
		return promise.future();
	}


    
}
