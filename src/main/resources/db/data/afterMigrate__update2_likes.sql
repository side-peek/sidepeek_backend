UPDATE likes
SET created_at = DATE_SUB(NOW(), INTERVAL 7 DAY);
