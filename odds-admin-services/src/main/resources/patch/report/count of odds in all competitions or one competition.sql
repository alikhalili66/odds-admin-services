گزارشات مدیریتی : تعداد پیش بینی های انجام شده برای تمام مسابقات یا یک مسابقه خاص


SELECT
    o.competition_id,
    COUNT(DISTINCT o.id) cnt
FROM
    toppodds o
GROUP BY
    o.competition_id
HAVING
    o.competition_id=nvl(?,o.competition_id)
ORDER BY
    cnt DESC;
    