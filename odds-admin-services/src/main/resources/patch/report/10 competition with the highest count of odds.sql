گزارش کلی که میخواهیم نمایش دهیم - 10 نفر اول با بیشترین تعداد پیش بینی

SELECT
    u.id user_id,
    u.name,
    u.lastname,
    u.nikename,
    u.point total_point,
    u.amount total_amount,
    COUNT(o.user_id) odds_count
FROM
    toppuser   u,
    toppodds   o
GROUP BY
    u.id,
    u.name,
    u.lastname,
    u.nikename,
    u.point,
    u.amount,
    o.user_id
HAVING
    u.id = o.user_id
ORDER BY
    u.point DESC;
