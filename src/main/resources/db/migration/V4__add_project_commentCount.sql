alter table project
add COLUMN comment_count BIGINT NOT NULL DEFAULT 0
after view_count;
