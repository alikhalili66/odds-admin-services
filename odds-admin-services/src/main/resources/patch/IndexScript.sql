CREATE INDEX IDX_TEAM_LEAGUE ON TOPPTEAM (LEAGUE_ID,DTO);

CREATE INDEX IDX_TEAMMEMBER_TEAM ON TOPPTEAMMEMBER (TEAM_ID,DTO);

CREATE INDEX IDX_LOCATION_LEAGUE ON TOPPLOCATION (LEAGUE_ID,DTO);

CREATE INDEX IDX_GROUP_LEAGUE ON TOPPGROUP (LEAGUE_ID,DTO);

CREATE INDEX IDX_FOLDER_LEAGUE ON TOPPFOLDER (LEAGUE_ID,DTO);

CREATE INDEX IDX_CONFIG_LEAGUE ON TOPPCONFIG (LEAGUE_ID);
CREATE INDEX IDX_CONFIG_SYMBOL ON TOPPCONFIG (LEAGUE_ID, SYMBOL);


CREATE INDEX IDX_QUESTION_LEAGUE ON TOPPQUESTION (LEAGUE_ID,DTO);


CREATE INDEX IDX_COMPETITION_GROUP ON TOPPCOMPETITION (GROUP_ID,DTO);
CREATE INDEX IDX_COMPETITION_LEAGUE ON TOPPCOMPETITION (LEAGUE_ID,GROUP_ID,DTO);


CREATE INDEX IDX_USER_LEAGUE ON TOPPUSER (LEAGUE_ID);

CREATE INDEX IDX_ODDS_USER1 ON TOPPODDS (ID, QUESTION_ID, GROUP_ID, LEAGUE_ID);
CREATE INDEX IDX_ODDS_QUESTION ON TOPPODDS (USER_ID, COMPETITION_ID, QUESTION_ID);
CREATE INDEX IDX_ODDS_COMPETITION ON TOPPODDS (COMPETITION_ID, QUESTION_ID);
CREATE INDEX IDX_ODDS_ANSWER ON TOPPODDS (QUESTION_ID, COMPETITION_ID, ANSWER);
CREATE INDEX IDX_ODDS_USER2 ON TOPPODDS (QUESTION_ID, COMPETITION_ID, USER_ID);


CREATE INDEX IDX_USERPOINTHISTORY_USER ON TOPPUSERPOINTHISTORY (USER_ID);



CREATE INDEX IDX_LEAGUE_ACTIVE ON TOPPLEAGUE (ID, ACTIVEFROM, ACTIVETO, DTO);

--CREATE INDEX IDX_USER_LEAGUE_UGID ON TOPPUSER (LEAGUE_ID, UGID);

CREATE INDEX IDX_ODDS_LEAGUE ON TOPPODDS (LEAGUE_ID);
CREATE INDEX IDX_ODDS_LEAGUE_USER ON TOPPODDS (LEAGUE_ID, USER_ID);

CREATE INDEX IDX_COMPETITIONREPORT_LEAGUE ON TOPPCOMPETITIONREPORT (LEAGUE_ID, COMPETITION_ID, DTO);

CREATE INDEX IDX_COMPETITION_LEAGUE2 ON TOPPCOMPETITION (LEAGUE_ID);

CREATE INDEX IDX_COMPETITION_ODDS_VALIDATE ON TOPPCOMPETITION (ID, ODDSFROM, ODDSTO);

CREATE INDEX IDX_ODDS_COMPETITION2 ON TOPPODDS (LEAGUE_ID, COMPETITION_ID, QUESTION_ID, USER_ID);
CREATE INDEX IDX_ODDS_COMPETITION3 ON TOPPODDS (LEAGUE_ID, COMPETITION_ID, USER_ID);











