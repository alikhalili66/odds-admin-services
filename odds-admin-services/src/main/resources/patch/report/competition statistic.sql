آمار بر اساس یک مسابقه


SELECT
    count(distinct o.user_id) user_count,
    (SELECT COUNT(DISTINCT odd.user_id) FROM toppodds odd,toppcompetitionquestion cq WHERE odd.competition_id = o.competition_id AND cq.competition_id = odd.competition_id AND odd.answer = cq.result) correct_answer_count,
    round ((SELECT COUNT(DISTINCT odd.user_id) FROM toppodds odd,toppcompetitionquestion cq WHERE odd.competition_id = o.competition_id AND cq.competition_id = odd.competition_id AND odd.answer = cq.result) / count(distinct o.user_id)*100, 2) correct_answer_percentage,
    sum(o.point) total_point
FROM
    toppodds o
GROUP BY
    o.competition_id
HAVING
    o.competition_id=?;
