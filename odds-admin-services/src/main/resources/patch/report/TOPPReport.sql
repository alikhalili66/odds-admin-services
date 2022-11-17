CREATE TABLE toppreport (
    id               NUMBER(38) NOT NULL,
    league_id        NUMBER(38),
    group_id         NUMBER(38),
    competition_id   NUMBER(38),
    question_id      NUMBER(38),
    type             VARCHAR2(100 CHAR),
    result           VARCHAR2(2000 CHAR) NOT NULL,
    creationdate     DATE,
    dto              DATE,
    CONSTRAINT pk_report PRIMARY KEY ( id )
);

CREATE SEQUENCE  SOPPREPORT  MINVALUE 1 MAXVALUE 9999999999999999999999999999 INCREMENT BY 1 START WITH 1 NOCACHE  ORDER  NOCYCLE  NOKEEP  NOSCALE  GLOBAL ;
