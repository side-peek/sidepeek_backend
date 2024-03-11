-- CLEAR
SET foreign_key_checks = 0;
truncate table users;
truncate table project;
truncate table project_member;
truncate table files;
truncate table skill;
truncate table project_skill;
truncate table auth_provider;
truncate table likes;
truncate table comments;
SET foreign_key_checks = 1;

-- USER
insert into users(id, nickname, email, password, profile_image_url)
values (1, '의진', 'uijin@naver.com',
        '$2a$12$Wk6AU7Z419AVDcyRLDKSHOEO1oAmdirbidsrqcW8S620SQXcOfEI.',
        'https://user-images.githubusercontent.com/uijin.png');
insert into users(id, nickname, email, password, profile_image_url)
values (2, '동건', 'donggun@naver.com',
        '$2a$12$Wk6AU7Z419AVDcyRLDKSHOEO1oAmdirbidsrqcW8S620SQXcOfEI.',
        'https://user-images.githubusercontent.com/donggun.png');
insert into users(id, nickname, email, password, profile_image_url)
values (3, '세희', 'hailey@naver.com',
        '$2a$12$Wk6AU7Z419AVDcyRLDKSHOEO1oAmdirbidsrqcW8S620SQXcOfEI.',
        'https://user-images.githubusercontent.com/hailey.png');
insert into users(id, nickname, email, password, profile_image_url)
values (4, '훈오', 'whoknow@naver.com',
        '$2a$12$Wk6AU7Z419AVDcyRLDKSHOEO1oAmdirbidsrqcW8S620SQXcOfEI.',
        'https://user-images.githubusercontent.com/whoknow.png');

-- PROJECT
insert into project(id, name, sub_name, overview, thumbnail_url, github_url, view_count, like_count,
                    start_date, end_date, owner_id, description)
values (1, '사이드픽👀', '요즘 사이드 플젝 뭐함? 사이드픽 👀', '데브코스 5기 육개짱팀의 좌충우돌 우당탕탕 프로젝트 개발 일대기',
        'https://thumbnail-images.sidepeek.com/1.png', 'https://github.com/side-peek', 20, 7,
        '2024-01-29 00:00:00', '2024-03-25 00:00:00', 1, '# SidePeek 기능 Markdown');
insert into project(id, name, sub_name, overview, thumbnail_url, github_url, deploy_url, view_count,
                    like_count,
                    start_date, end_date, owner_id, description)
values (2, '스타일드', '스타일 No.1',
        'Styled는 자신의 ootd를 공유하며 소통하고자하는 사람들의 니즈를 충족하고자 기획된, OOTD만을 위한 패션 특화 소셜 네트워크 서비스 입니다.',
        'https://thumbnail-images.styled.com/1.png', 'https://github.com/sstyled',
        'https://styled.netlify.app/', 30, 20,
        '2024-01-29 00:00:00', '2024-03-25 00:00:00', 1, '# Styled 기능 Markdown');

-- PROJECT_MEMBER
insert into project_member(id, project_id, user_id, role, nickname)
values (1, 1, 1, 'PO', '의진');
insert into project_member(id, project_id, user_id, role, nickname)
values (2, 1, 2, '프론트', '동건');
insert into project_member(id, project_id, role, nickname)
values (3, 1, '오락부짱', '민호');

-- FILE
insert into files(id, project_id, type, url)
values (1, 1, 'OVERVIEW_IMAGE', 'https://project-images.sidepeek.com/1.png');
insert into files(id, project_id, type, url)
values (2, 1, 'OVERVIEW_IMAGE', 'https://project-images.sidepeek.com/2.png');

-- SKILL
insert into skill(id, name, icon_image_url)
values (1, 'AWS EC2', 'https://sidepeek-bucket.s3.ap-northeast-2.amazonaws.com/skill/aws-ec2.png');
insert into skill(id, name, icon_image_url)
values (2, 'AWS RDS', 'https://sidepeek-bucket.s3.ap-northeast-2.amazonaws.com/skill/aws-rds.png');
insert into skill(id, name, icon_image_url)
values (3, 'AWS S3', 'https://sidepeek-bucket.s3.ap-northeast-2.amazonaws.com/skill/aws-s3.png');
insert into skill(id, name, icon_image_url)
values (4, 'Figma', 'https://sidepeek-bucket.s3.ap-northeast-2.amazonaws.com/skill/figma.png');
insert into skill(id, name, icon_image_url)
values (5, 'Git', 'https://sidepeek-bucket.s3.ap-northeast-2.amazonaws.com/skill/git-icon.png');
insert into skill(id, name, icon_image_url)
values (6, 'Github Actions', 'https://sidepeek-bucket.s3.ap-northeast-2.amazonaws.com/skill/github-actions.png');
insert into skill(id, name, icon_image_url)
values (7, 'Github', 'https://sidepeek-bucket.s3.ap-northeast-2.amazonaws.com/skill/github-icon.png');
insert into skill(id, name, icon_image_url)
values (8, 'Java', 'https://sidepeek-bucket.s3.ap-northeast-2.amazonaws.com/skill/java.png');
insert into skill(id, name, icon_image_url)
values (9, 'Javascript', 'https://sidepeek-bucket.s3.ap-northeast-2.amazonaws.com/skill/javascript.png');
insert into skill(id, name, icon_image_url)
values (10, 'Kotlin', 'https://sidepeek-bucket.s3.ap-northeast-2.amazonaws.com/skill/kotlin-icon.png');
insert into skill(id, name, icon_image_url)
values (11, 'Notion', 'https://sidepeek-bucket.s3.ap-northeast-2.amazonaws.com/skill/notion-icon.png');
insert into skill(id, name, icon_image_url)
values (12, 'PostgreSQL', 'https://sidepeek-bucket.s3.ap-northeast-2.amazonaws.com/skill/postgresql.png');
insert into skill(id, name, icon_image_url)
values (13, 'React Query', 'https://sidepeek-bucket.s3.ap-northeast-2.amazonaws.com/skill/react-query-icon.png');
insert into skill(id, name, icon_image_url)
values (14, 'React', 'https://sidepeek-bucket.s3.ap-northeast-2.amazonaws.com/skill/react.png');
insert into skill(id, name, icon_image_url)
values (15, 'Slack', 'https://sidepeek-bucket.s3.ap-northeast-2.amazonaws.com/skill/slack-icon.png');
insert into skill(id, name, icon_image_url)
values (16, 'Spring', 'https://sidepeek-bucket.s3.ap-northeast-2.amazonaws.com/skill/spring-icon.png');
insert into skill(id, name, icon_image_url)
values (17, 'Swagger', 'https://sidepeek-bucket.s3.ap-northeast-2.amazonaws.com/skill/swagger.png');
insert into skill(id, name, icon_image_url)
values (18, 'Thymeleaf', 'https://sidepeek-bucket.s3.ap-northeast-2.amazonaws.com/skill/thymeleaf-icon.png');
insert into skill(id, name, icon_image_url)
values (19, 'Tomcat', 'https://sidepeek-bucket.s3.ap-northeast-2.amazonaws.com/skill/tomcat.png');
insert into skill(id, name, icon_image_url)
values (20, 'Typescript', 'https://sidepeek-bucket.s3.ap-northeast-2.amazonaws.com/skill/typescript-icon.png');
insert into skill(id, name, icon_image_url)
values (21, 'Vercel', 'https://sidepeek-bucket.s3.ap-northeast-2.amazonaws.com/skill/vercel-icon.png');

-- PROJECT_SKILL
insert into project_skill(id, project_id, skill_id, category)
values (21, 1, 1, '프론트');
insert into project_skill(id, project_id, skill_id, category)
values (22, 1, 2, '백');
insert into project_skill(id, project_id, skill_id, category)
values (23, 1, 3, '협업툴');
insert into project_skill(id, project_id, skill_id, category)
values (24, 1, 4, '프론트');

-- LIKE
insert into likes(id, user_id, project_id)
values (1, 2, 2);
insert into likes(id, user_id, project_id)
values (2, 3, 1);
insert into likes(id, user_id, project_id)
values (3, 3, 2);

-- COMMENT
insert into comments(id, project_id, user_id, parent_id, is_anonymous, content)
values (1, 1, 3, null, 0, '우와 이 프로젝트 대박인데요?');
insert into comments(id, project_id, user_id, parent_id, is_anonymous, content)
values (2, 1, 4, null, 1, 'LGTM ✨💖');
insert into comments(id, project_id, user_id, parent_id, is_anonymous, content)
values (3, 1, 1, 1, 0, '좋게 봐주셔서 감사합니다!');
