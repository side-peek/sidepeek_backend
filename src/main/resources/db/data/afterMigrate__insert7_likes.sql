-- 모든 유저가 모든 프로젝트에 좋아요를 하도록 변경
-- 중복 방지를 위해 (user_id, project_id) 조합이 유일해야 함
-- 총 100 * 50 (5000)개의 좋아요 추가
INSERT INTO likes (user_id, project_id)
SELECT
    u.id AS user_id,
    p.id AS project_id
FROM
    (SELECT DISTINCT id FROM users) u  -- 모든 사용자
        CROSS JOIN
    (SELECT DISTINCT id FROM project WHERE deleted_at IS NULL) p  -- 모든 프로젝트
WHERE
    NOT EXISTS (
        SELECT 1
        FROM likes l
        WHERE l.user_id = u.id
          AND l.project_id = p.id
    );
