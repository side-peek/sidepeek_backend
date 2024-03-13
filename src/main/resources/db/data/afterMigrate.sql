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
                    start_date, end_date, owner_id, description, troubleshooting)
values (1, '사이드픽👀', '요즘 사이드 플젝 뭐함? 사이드픽 👀', '데브코스 5기 육개짱팀의 좌충우돌 우당탕탕 프로젝트 개발 일대기',
        'https://thumbnail-images.sidepeek.com/1.png', 'https://github.com/side-peek', 20, 7,
        '2024-01-29 00:00:00', '2024-03-25 00:00:00', 1, '# SidePeek 기능 Markdown', '');
insert into project(id, name, sub_name, overview, thumbnail_url, github_url, deploy_url, view_count,
                    like_count,
                    start_date, end_date, owner_id, description, troubleshooting)
values (2, '스타일드', '스타일 No.1',
        'Styled는 자신의 ootd를 공유하며 소통하고자하는 사람들의 니즈를 충족하고자 기획된, OOTD만을 위한 패션 특화 소셜 네트워크 서비스 입니다.',
        'https://thumbnail-images.styled.com/1.png', 'https://github.com/sstyled',
        'https://styled.netlify.app/', 30, 20,
        '2024-01-29 00:00:00', '2024-03-25 00:00:00', 1, '# Styled 기능 Markdown', '');
insert into project(id, name, sub_name, overview, thumbnail_url, github_url, deploy_url, view_count,
                    like_count,
                    start_date, end_date, owner_id, description, troubleshooting)
values (3, '프로젝트3', '프로젝트3 더미데이터', '프로젝트3 더미데이터입니다.',
        'https://thumbnail-images.project.com/3.png', 'https://github.com/project',
        'https://project3.netlify.app/', 3, 3,
        '2024-01-29 00:00:00', '2024-03-25 00:00:00', 1, '# Project 3 기능', '');
insert into project(id, name, sub_name, overview, thumbnail_url, github_url, deploy_url, view_count,
                    like_count,
                    start_date, end_date, owner_id, description, troubleshooting)
values (4, '프로젝트4', '프로젝트4 더미데이터', '프로젝트4 더미데이터입니다.',
        'https://thumbnail-images.project.com/4.png', 'https://github.com/project',
        'https://project4.netlify.app/', 4, 4,
        '2024-01-29 00:00:00', '2024-03-25 00:00:00', 1, '# Project 4 기능', '');
insert into project(id, name, sub_name, overview, thumbnail_url, github_url, view_count, like_count,
                    start_date, end_date, owner_id, description, troubleshooting)
values (5, '프로젝트5', '프로젝트5 더미데이터', '프로젝트5 더미데이터입니다.',
        'https://thumbnail-images.project.com/5.png', 'https://github.com/project', 5, 5,
        '2024-01-29 00:00:00', '2024-03-25 00:00:00', 1, '# Project 5 기능', '');
insert into project(id, name, sub_name, overview, thumbnail_url, github_url, view_count, like_count,
                    start_date, end_date, owner_id, description, troubleshooting)
values (6, '프로젝트6', '프로젝트6 더미데이터', '프로젝트6 더미데이터입니다.',
        'https://thumbnail-images.project.com/6.png', 'https://github.com/project', 6, 6,
        '2024-01-29 00:00:00', '2024-03-25 00:00:00', 1, '# Project 6 기능', '');
insert into project(id, name, sub_name, overview, thumbnail_url, github_url, view_count, like_count,
                    start_date, end_date, owner_id, description, troubleshooting)
values (7, '프로젝트7', '프로젝트7 더미데이터', '프로젝트7 더미데이터입니다.',
        'https://thumbnail-images.project.com/7.png', 'https://github.com/project', 7, 7,
        '2024-01-29 00:00:00', '2024-03-25 00:00:00', 1, '# Project 7 기능', '');
insert into project(id, name, sub_name, overview, thumbnail_url, github_url, view_count, like_count,
                    start_date, end_date, owner_id, description, troubleshooting)
values (8, '프로젝트8', '프로젝트8 더미데이터', '프로젝트8 더미데이터입니다.',
        'https://thumbnail-images.project.com/8.png', 'https://github.com/project', 8, 8,
        '2024-01-29 00:00:00', '2024-03-25 00:00:00', 1, '# Project 8 기능', '');
insert into project(id, name, sub_name, overview, thumbnail_url, github_url, view_count, like_count,
                    start_date, end_date, owner_id, description, troubleshooting)
values (9, '프로젝트9', '프로젝트9 더미데이터', '프로젝트9 더미데이터입니다.',
        'https://thumbnail-images.project.com/9.png', 'https://github.com/project', 9, 9,
        '2024-01-29 00:00:00', '2024-03-25 00:00:00', 1, '# Project 9 기능', '');
insert into project(id, name, sub_name, overview, thumbnail_url, github_url, view_count, like_count,
                    start_date, end_date, owner_id, description, troubleshooting)
values (10, '프로젝트10', '프로젝트10 더미데이터', '프로젝트10 더미데이터입니다.',
        'https://thumbnail-images.project.com/10.png', 'https://github.com/project', 10, 10,
        '2024-01-29 00:00:00', '2024-03-25 00:00:00', 1, '# Project 10 기능', '');
insert into project(id, name, sub_name, overview, thumbnail_url, github_url, view_count, like_count,
                    start_date, end_date, owner_id, description, troubleshooting)
values (11, '프로젝트11', '프로젝트11 더미데이터', '프로젝트11 더미데이터입니다.',
        'https://thumbnail-images.project.com/11.png', 'https://github.com/project', 11, 11,
        '2024-01-29 00:00:00', '2024-03-25 00:00:00', 1, '# Project 11 기능', '');
insert into project(id, name, sub_name, overview, thumbnail_url, github_url, view_count, like_count,
                    start_date, end_date, owner_id, description, troubleshooting)
values (12, '프로젝트12', '프로젝트12 더미데이터', '프로젝트12 더미데이터입니다.',
        'https://thumbnail-images.project.com/12.png', 'https://github.com/project', 12, 12,
        '2024-01-29 00:00:00', '2024-03-25 00:00:00', 1, '# Project 12 기능', '');
insert into project(id, name, sub_name, overview, thumbnail_url, github_url, view_count, like_count,
                    start_date, end_date, owner_id, description, troubleshooting)
values (13, '프로젝트13', '프로젝트13 더미데이터', '프로젝트13 더미데이터입니다.',
        'https://thumbnail-images.project.com/13.png', 'https://github.com/project', 13, 13,
        '2024-01-29 00:00:00', '2024-03-25 00:00:00', 1, '# Project 13 기능', '');
insert into project(id, name, sub_name, overview, thumbnail_url, github_url, view_count, like_count,
                    start_date, end_date, owner_id, description, troubleshooting)
values (14, '프로젝트14', '프로젝트14 더미데이터', '프로젝트14 더미데이터입니다.',
        'https://thumbnail-images.project.com/14.png', 'https://github.com/project', 14, 14,
        '2024-01-29 00:00:00', '2024-03-25 00:00:00', 1, '# Project 14 기능', '');
insert into project(id, name, sub_name, overview, thumbnail_url, github_url, view_count, like_count,
                    start_date, end_date, owner_id, description, troubleshooting)
values (15, '프로젝트15', '프로젝트15 더미데이터', '프로젝트15 더미데이터입니다.',
        'https://thumbnail-images.project.com/15.png', 'https://github.com/project', 15, 15,
        '2024-01-29 00:00:00', '2024-03-25 00:00:00', 1, '# Project 15 기능', '');
insert into project(id, name, sub_name, overview, thumbnail_url, github_url, view_count, like_count,
                    start_date, end_date, owner_id, description, troubleshooting)
values (16, '프로젝트16', '프로젝트16 더미데이터', '프로젝트16 더미데이터입니다.',
        'https://thumbnail-images.project.com/16.png', 'https://github.com/project', 16, 16,
        '2024-01-29 00:00:00', '2024-03-25 00:00:00', 1, '# Project 16 기능', '');
insert into project(id, name, sub_name, overview, thumbnail_url, github_url, view_count, like_count,
                    start_date, end_date, owner_id, description, troubleshooting)
values (17, '프로젝트17', '프로젝트17 더미데이터', '프로젝트17 더미데이터입니다.',
        'https://thumbnail-images.project.com/17.png', 'https://github.com/project', 17, 17,
        '2024-01-29 00:00:00', '2024-03-25 00:00:00', 1, '# Project 17 기능', '');
insert into project(id, name, sub_name, overview, thumbnail_url, github_url, view_count, like_count,
                    start_date, end_date, owner_id, description, troubleshooting)
values (18, '프로젝트18', '프로젝트18 더미데이터', '프로젝트18 더미데이터입니다.',
        'https://thumbnail-images.project.com/18.png', 'https://github.com/project', 18, 18,
        '2024-01-29 00:00:00', '2024-03-25 00:00:00', 1, '# Project 18 기능', '');
insert into project(id, name, sub_name, overview, thumbnail_url, github_url, view_count, like_count,
                    start_date, end_date, owner_id, description, troubleshooting)
values (19, '프로젝트19', '프로젝트19 더미데이터', '프로젝트19 더미데이터입니다.',
        'https://thumbnail-images.project.com/19.png', 'https://github.com/project', 19, 19,
        '2024-01-29 00:00:00', '2024-03-25 00:00:00', 1, '# Project 19 기능', '');
insert into project(id, name, sub_name, overview, thumbnail_url, github_url, view_count, like_count,
                    start_date, end_date, owner_id, description, troubleshooting)
values (20, '프로젝트20', '프로젝트20 더미데이터', '프로젝트20 더미데이터입니다.',
        'https://thumbnail-images.project.com/20.png', 'https://github.com/project', 20, 20,
        '2024-01-29 00:00:00', '2024-03-25 00:00:00', 1, '# Project 20 기능', '');
insert into project(id, name, sub_name, overview, thumbnail_url, github_url, view_count, like_count,
                    start_date, end_date, owner_id, description, troubleshooting)
values (21, '프로젝트21', '프로젝트21 더미데이터', '프로젝트21 더미데이터입니다.',
        'https://thumbnail-images.project.com/21.png', 'https://github.com/project', 21, 21,
        '2024-01-29 00:00:00', '2024-03-25 00:00:00', 1, '# Project 21 기능', '');
insert into project(id, name, sub_name, overview, thumbnail_url, github_url, view_count, like_count,
                    start_date, end_date, owner_id, description, troubleshooting)
values (22, '프로젝트22', '프로젝트22 더미데이터', '프로젝트22 더미데이터입니다.',
        'https://thumbnail-images.project.com/12.png', 'https://github.com/project', 22, 22,
        '2024-01-29 00:00:00', '2024-03-25 00:00:00', 1, '# Project 22 기능', '');
insert into project(id, name, sub_name, overview, thumbnail_url, github_url, view_count, like_count,
                    start_date, end_date, owner_id, description, troubleshooting)
values (23, '프로젝트23', '프로젝트23 더미데이터', '프로젝트23 더미데이터입니다.',
        'https://thumbnail-images.project.com/23.png', 'https://github.com/project', 23, 23,
        '2024-01-29 00:00:00', '2024-03-25 00:00:00', 1, '# Project 23 기능', '');
insert into project(id, name, sub_name, overview, thumbnail_url, github_url, view_count, like_count,
                    start_date, end_date, owner_id, description, troubleshooting)
values (24, '프로젝트24', '프로젝트24 더미데이터', '프로젝트24 더미데이터입니다.',
        'https://thumbnail-images.project.com/24.png', 'https://github.com/project', 24, 24,
        '2024-01-29 00:00:00', '2024-03-25 00:00:00', 1, '# Project 24 기능', '');
insert into project(id, name, sub_name, overview, thumbnail_url, github_url, view_count, like_count,
                    start_date, end_date, owner_id, description, troubleshooting)
values (25, '프로젝트25', '프로젝트25 더미데이터', '프로젝트25 더미데이터입니다.',
        'https://thumbnail-images.project.com/25.png', 'https://github.com/project', 25, 25,
        '2024-01-29 00:00:00', '2024-03-25 00:00:00', 1, '# Project 25 기능', '');
insert into project(id, name, sub_name, overview, thumbnail_url, github_url, view_count, like_count,
                    start_date, end_date, owner_id, description, troubleshooting)
values (26, '프로젝트26', '프로젝트26 더미데이터', '프로젝트26 더미데이터입니다.',
        'https://thumbnail-images.project.com/26.png', 'https://github.com/project', 26, 26,
        '2024-01-29 00:00:00', '2024-03-25 00:00:00', 1, '# Project 26 기능', '');
insert into project(id, name, sub_name, overview, thumbnail_url, github_url, view_count, like_count,
                    start_date, end_date, owner_id, description, troubleshooting)
values (27, '프로젝트27', '프로젝트27 더미데이터', '프로젝트27 더미데이터입니다.',
        'https://thumbnail-images.project.com/27.png', 'https://github.com/project', 27, 27,
        '2024-01-29 00:00:00', '2024-03-25 00:00:00', 1, '# Project 27 기능', '');
insert into project(id, name, sub_name, overview, thumbnail_url, github_url, view_count, like_count,
                    start_date, end_date, owner_id, description, troubleshooting)
values (28, '프로젝트28', '프로젝트28 더미데이터', '프로젝트28 더미데이터입니다.',
        'https://thumbnail-images.project.com/28.png', 'https://github.com/project', 28, 28,
        '2024-01-29 00:00:00', '2024-03-25 00:00:00', 1, '# Project 28 기능', '');
insert into project(id, name, sub_name, overview, thumbnail_url, github_url, view_count, like_count,
                    start_date, end_date, owner_id, description, troubleshooting)
values (29, '프로젝트29', '프로젝트29 더미데이터', '프로젝트29 더미데이터입니다.',
        'https://thumbnail-images.project.com/29.png', 'https://github.com/project', 29, 29,
        '2024-01-29 00:00:00', '2024-03-25 00:00:00', 1, '# Project 29 기능', '');
insert into project(id, name, sub_name, overview, thumbnail_url, github_url, view_count, like_count,
                    start_date, end_date, owner_id, description, troubleshooting)
values (30, '프로젝트30', '프로젝트30 더미데이터', '프로젝트30 더미데이터입니다.',
        'https://thumbnail-images.project.com/30.png', 'https://github.com/project', 30, 30,
        '2024-01-29 00:00:00', '2024-03-25 00:00:00', 1, '# Project 30 기능', '');


-- PROJECT_MEMBER
insert into project_member(id, project_id, user_id, role, nickname)
values (1, 1, 1, 'PO', '의진');
insert into project_member(id, project_id, user_id, role, nickname)
values (2, 1, 2, '프론트', '동건');
insert into project_member(id, project_id, role, nickname)
values (3, 1, '오락부짱', '민호');
insert into project_member(id, project_id, user_id, role, nickname)
values (4, 2, 1, 'BE', '의진');
insert into project_member(id, project_id, user_id, role, nickname)
values (5, 3, 1, '디자인', '의진');
insert into project_member(id, project_id, user_id, role, nickname)
values (6, 4, 1, 'BE', '지니');
insert into project_member(id, project_id, user_id, role, nickname)
values (7, 5, 1, 'BE', 'uijin');
insert into project_member(id, project_id, user_id, role, nickname)
values (8, 6, 1, 'PO', '의진');
insert into project_member(id, project_id, user_id, role, nickname)
values (9, 7, 1, '디자인', '의진');
insert into project_member(id, project_id, user_id, role, nickname)
values (10, 8, 1, 'BE', '의진');
insert into project_member(id, project_id, user_id, role, nickname)
values (11, 9, 1, 'BE', '의진');
insert into project_member(id, project_id, user_id, role, nickname)
values (12, 10, 1, 'BE', '의진');
insert into project_member(id, project_id, user_id, role, nickname)
values (13, 11, 1, 'BE', '의진');
insert into project_member(id, project_id, user_id, role, nickname)
values (14, 12, 1, 'BE & FE', '의진');
insert into project_member(id, project_id, user_id, role, nickname)
values (15, 13, 1, 'BE', '의진');
insert into project_member(id, project_id, user_id, role, nickname)
values (16, 14, 1, 'BE', '의진');
insert into project_member(id, project_id, user_id, role, nickname)
values (17, 15, 1, 'BE', '의진');
insert into project_member(id, project_id, user_id, role, nickname)
values (18, 16, 1, 'BE', '의진');
insert into project_member(id, project_id, user_id, role, nickname)
values (19, 17, 1, 'BE', '의진');
insert into project_member(id, project_id, user_id, role, nickname)
values (20, 18, 1, 'BE', '의진');
insert into project_member(id, project_id, user_id, role, nickname)
values (21, 19, 1, 'BE', '의진');
insert into project_member(id, project_id, user_id, role, nickname)
values (22, 20, 1, 'BE', '의진');
insert into project_member(id, project_id, user_id, role, nickname)
values (23, 21, 1, 'BE', '의진');
insert into project_member(id, project_id, user_id, role, nickname)
values (24, 22, 1, 'BE', '의진');
insert into project_member(id, project_id, user_id, role, nickname)
values (25, 23, 1, 'BE', '의진');
insert into project_member(id, project_id, user_id, role, nickname)
values (26, 24, 1, 'BE', '의진');
insert into project_member(id, project_id, user_id, role, nickname)
values (27, 25, 1, 'BE', '의진');
insert into project_member(id, project_id, user_id, role, nickname)
values (28, 26, 1, 'BE', '의진');
insert into project_member(id, project_id, user_id, role, nickname)
values (29, 27, 1, 'BE', '의진');
insert into project_member(id, project_id, user_id, role, nickname)
values (30, 28, 1, 'BE', '의진');
insert into project_member(id, project_id, user_id, role, nickname)
values (31, 29, 1, 'BE', '의진');
insert into project_member(id, project_id, user_id, role, nickname)
values (32, 30, 1, 'BE', '의진');

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
values (6, 'Github Actions',
        'https://sidepeek-bucket.s3.ap-northeast-2.amazonaws.com/skill/github-actions.png');
insert into skill(id, name, icon_image_url)
values (7, 'Github',
        'https://sidepeek-bucket.s3.ap-northeast-2.amazonaws.com/skill/github-icon.png');
insert into skill(id, name, icon_image_url)
values (8, 'Java', 'https://sidepeek-bucket.s3.ap-northeast-2.amazonaws.com/skill/java.png');
insert into skill(id, name, icon_image_url)
values (9, 'Javascript',
        'https://sidepeek-bucket.s3.ap-northeast-2.amazonaws.com/skill/javascript.png');
insert into skill(id, name, icon_image_url)
values (10, 'Kotlin',
        'https://sidepeek-bucket.s3.ap-northeast-2.amazonaws.com/skill/kotlin-icon.png');
insert into skill(id, name, icon_image_url)
values (11, 'Notion',
        'https://sidepeek-bucket.s3.ap-northeast-2.amazonaws.com/skill/notion-icon.png');
insert into skill(id, name, icon_image_url)
values (12, 'PostgreSQL',
        'https://sidepeek-bucket.s3.ap-northeast-2.amazonaws.com/skill/postgresql.png');
insert into skill(id, name, icon_image_url)
values (13, 'React Query',
        'https://sidepeek-bucket.s3.ap-northeast-2.amazonaws.com/skill/react-query-icon.png');
insert into skill(id, name, icon_image_url)
values (14, 'React', 'https://sidepeek-bucket.s3.ap-northeast-2.amazonaws.com/skill/react.png');
insert into skill(id, name, icon_image_url)
values (15, 'Slack',
        'https://sidepeek-bucket.s3.ap-northeast-2.amazonaws.com/skill/slack-icon.png');
insert into skill(id, name, icon_image_url)
values (16, 'Spring',
        'https://sidepeek-bucket.s3.ap-northeast-2.amazonaws.com/skill/spring-icon.png');
insert into skill(id, name, icon_image_url)
values (17, 'Swagger', 'https://sidepeek-bucket.s3.ap-northeast-2.amazonaws.com/skill/swagger.png');
insert into skill(id, name, icon_image_url)
values (18, 'Thymeleaf',
        'https://sidepeek-bucket.s3.ap-northeast-2.amazonaws.com/skill/thymeleaf-icon.png');
insert into skill(id, name, icon_image_url)
values (19, 'Tomcat', 'https://sidepeek-bucket.s3.ap-northeast-2.amazonaws.com/skill/tomcat.png');
insert into skill(id, name, icon_image_url)
values (20, 'Typescript',
        'https://sidepeek-bucket.s3.ap-northeast-2.amazonaws.com/skill/typescript-icon.png');
insert into skill(id, name, icon_image_url)
values (21, 'Vercel',
        'https://sidepeek-bucket.s3.ap-northeast-2.amazonaws.com/skill/vercel-icon.png');

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
