 X لیست وضعیت سوالات مسابقه برای مسابقه 
 

SELECT
    o.question_id,
    sum(o.point) total_point,
    count(distinct o.user_id) user_count,
    round ((SELECT COUNT(DISTINCT odd.user_id) FROM toppodds odd,toppcompetitionquestion cq WHERE odd.competition_id = o.competition_id AND odd.question_id = o.question_id AND cq.competition_id = odd.competition_id AND cq.question_id = odd.question_id AND odd.answer = cq.result) / count(distinct o.user_id)*100, 2) correct_answer_percentage,
    round ((SELECT COUNT(DISTINCT odd.user_id) FROM toppodds odd,toppcompetitionquestion cq WHERE odd.competition_id = o.competition_id AND odd.question_id = o.question_id AND cq.competition_id = odd.competition_id AND cq.question_id = odd.question_id AND odd.answer != cq.result) / count(distinct o.user_id)*100, 2) incorrect_answer_percentage
FROM
    toppodds o
GROUP BY
    o.question_id, o.competition_id
HAVING
    o.competition_id=?
ORDER BY
    total_point DESC;
