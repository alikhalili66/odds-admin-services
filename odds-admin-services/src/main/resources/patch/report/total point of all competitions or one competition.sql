گزارشات مدیریتی: مجموع امتیاز برای تمام مسابقات یا یک مسابقه خاص


SELECT
    o.competition_id,
    COUNT(o.point) total_point
FROM
    toppodds o
GROUP BY
    o.competition_id
HAVING
    o.competition_id=nvl(?,o.competition_id)
ORDER BY
    total_point DESC;
	