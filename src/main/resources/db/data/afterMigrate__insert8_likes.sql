-- likes 테이블에 랜덤한 좋아요 추가
-- 중복 방지를 위해 (user_id, project_id) 조합이 유일해야 함
-- 총 300개의 랜덤 좋아요 추가

INSERT INTO likes (user_id, project_id)
SELECT DISTINCT
        FLOOR(RAND() * (100 - 13 + 1)) + 13 AS user_id, -- 유저 ID: 13 ~ 100 사이
        FLOOR(RAND() * (50 - 1 + 1)) + 1 AS project_id  -- 프로젝트 ID: 1 ~ 50 사이
FROM
    (SELECT 1 AS n UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5
     UNION SELECT 6 UNION SELECT 7 UNION SELECT 8 UNION SELECT 9 UNION SELECT 10) t1,
    (SELECT 1 AS n UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5
     UNION SELECT 6 UNION SELECT 7 UNION SELECT 8 UNION SELECT 9 UNION SELECT 10) t2
-- 중복된 user_id와 project_id의 조합을 피하기 위해 WHERE 조건 사용
WHERE NOT EXISTS (
    SELECT 1 FROM likes l
    WHERE l.user_id = FLOOR(RAND() * (100 - 13 + 1)) + 13
      AND l.project_id = FLOOR(RAND() * (50 - 1 + 1)) + 1
)
LIMIT 300;  -- 최소 300개의 좋아요 생성
