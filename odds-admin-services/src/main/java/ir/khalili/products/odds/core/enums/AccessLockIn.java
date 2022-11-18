package ir.khalili.products.odds.core.enums;

public enum AccessLockIn {
	
	// COMPETITION
	ODDS_COMPETITION_SAVE						,	
	ODDS_COMPETITION_UPDATE						,
	ODDS_COMPETITION_DELETE						,
	ODDS_COMPETITION_FETCH_ALL					,
	ODDS_COMPETITION_FETCH_BY_ID					,
	ODDS_COMPETITION_QUESTION_ASSIGN				,
	ODDS_COMPETITION_QUESTION_UNASSIGN			,
	ODDS_COMPETITION_QUESTION_FETCH				,
	ODDS_COMPETITION_RESULT_REGISTER				,
	ODDS_COMPETITION_QUESTION_RESULT_REGISTER	,
	ODDS_COMPETITION_POINT_CALCULATION			,
	
	// CONFIG
	ODDS_CONFIG_UPDATE							,
	ODDS_CONFIG_FETCH_ALL						,
	ODDS_CONFIG_FETCH_BY_ID						,
	ODDS_CONFIG_FETCH_BY_SYMBOL					,
	
	// FOLDER
	ODDS_FOLDER_SAVE								,	
	ODDS_FOLDER_UPDATE							,
	ODDS_FOLDER_DELETE							,
	ODDS_FOLDER_FETCH_ALL						,
	ODDS_FOLDER_FETCH_BY_ID						,
	ODDS_FOLDER_QUESTION_ASSIGN					,
	ODDS_FOLDER_QUESTION_UNASSIGN				,
	ODDS_FOLDER_QUESTION_FETCH					,
	
	// GROUP
	ODDS_GROUP_SAVE								,	
	ODDS_GROUP_UPDATE							,
	ODDS_GROUP_DELETE							,
	ODDS_GROUP_FETCH_ALL							,
	ODDS_GROUP_FETCH_BY_ID						,
	ODDS_GROUP_TEAM_ASSIGN						,
	ODDS_GROUP_TEAM_UNASSIGN						,
	ODDS_GROUP_TEAM_FETCH						,	
	ODDS_GROUP_COMPETITION_FETCH					,
	
	// LEAGUE
	ODDS_LEAGUE_SAVE								,	
	ODDS_LEAGUE_UPDATE							,
	ODDS_LEAGUE_DELETE							,
	ODDS_LEAGUE_FETCH_ALL						,
	ODDS_LEAGUE_FETCH_BY_ID						,
	
	// LOCATION
	ODDS_LOCATION_SAVE							,	
	ODDS_LOCATION_UPDATE							,
	ODDS_LOCATION_DELETE							,
	ODDS_LOCATION_FETCH_ALL						,
	
	// QUESTION
	ODDS_QUESTION_SAVE							,	
	ODDS_QUESTION_UPDATE							,
	ODDS_QUESTION_DELETE							,
	ODDS_QUESTION_FETCH_ALL						,
	ODDS_QUESTION_FETCH_BY_ID					,
	
	// REPORT
	ODDS_REPORT_REGISTERED_USERS_COUNT			,
	ODDS_REPORT_COMPETITOR_USERS_COUNT			,
	ODDS_REPORT_COMPETITOR_USERS_AMOUNT			,
	ODDS_REPORT_ODDS_COUNT						,
	ODDS_REPORT_CALCULATE_COMPETITION			,
	ODDS_REPORT_LEAGUE_USERS_WITH_MAXIMUM_POINT	,
	ODDS_REPORT_LEAGUE_BLOCKED_AMOUNT			,
	ODDS_REPORT_ALL_SECTION_ODDS_COUNT_PARTICIPANT_COUNT_TOTAL_POINT,
	ODDS_REPORT_ALL_SECTION_CORRECT_ODDS_COUNT_AND_ODDS_PERCENTAGE,
	ODDS_REPORT_THREE_SECTION_USERS_WITH_MAXIMUM_POINT,
	ODDS_REPORT_LEAGUE_TRANSACTION_AMOUNT		,
	
	// TEAM
	ODDS_TEAM_SAVE								,	
	ODDS_TEAM_UPDATE							,
	ODDS_TEAM_DELETE							,
	ODDS_TEAM_FETCH_ALL							,
	ODDS_TEAM_FETCH_BY_ID						,
	ODDS_TEAM_IMAGE_UPDATE						,
	ODDS_TEAM_MEMBER_SAVE						,
	ODDS_TEAM_MEMBER_UPDATE						,
	ODDS_TEAM_MEMBER_DELETE						,
	ODDS_TEAM_MEMBER_FETCH_ALL					,
		
		
	// TRANSACTION
	ODDS_TRANSACTION_FETCH_ALL					,
	ODDS_TRANSACTION_REJECT						,
	ODDS_TRANSACTION_CONFIRM						,
	ODDS_TRANSACTION_SAVE						,
		
	// USER
	ODDS_USER_FETCH_ALL							,
	ODDS_USER_FETCH_BY_ID						,
	ODDS_USER_FETCH_ODDS,
	ODDS_USER_FETCH_QUESTION_ANSWER				,
	ODDS_USER_FETCH_POINT_HISTORY				,
	;
	

}
