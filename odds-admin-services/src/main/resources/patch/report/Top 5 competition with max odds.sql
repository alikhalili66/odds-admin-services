گزارش 5 تا بازی که بیشترین پیش بینی را داشته است


SELECT 

    competition_id, 
    cnt,
    (select name from toppteam t where t.id=team1_id) TEAM1_name,
    (select name from toppteam t where t.id=team2_id) TEAM2_name,
    (select g.name from toppgroup g where g.id=group_id) GROUP_name

FROM
    (
        SELECT
            o.competition_id,
            o.team1_id,
            o.team2_id,
            o.group_id,
            COUNT(*) cnt
        FROM
            toppodds o
        GROUP BY
            o.competition_id, o.team1_id, o.team2_id, o.group_id
        ORDER BY
            cnt DESC
    )
WHERE ROWNUM < 6;