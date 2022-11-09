لیست کاربران که بیشترین امتیاز را گرفته اند تا 30 تا

SELECT
    *
FROM
    (
        SELECT
            u.id      user_id,
            u.name,
            u.lastname,
            u.nikename,
            u.point   total_point
        FROM
            toppuser u
        GROUP BY
            u.id,
            u.name,
            u.lastname,
            u.nikename,
            u.point
        ORDER BY
            u.point DESC
    )
WHERE
    ROWNUM < 31;
    