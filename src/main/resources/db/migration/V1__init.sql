-- USER
create TABLE IF NOT EXISTS users
(
    id                BIGINT PRIMARY KEY AUTO_INCREMENT,
    nickname          VARCHAR(20) UNIQUE NULL,
    email             VARCHAR(50) UNIQUE NULL,
    password          VARCHAR(100)       NULL,
    introduction      VARCHAR(100)       NULL,
    profile_image_url TEXT               NULL,
    job               VARCHAR(30)        NULL,
    career            VARCHAR(30)        NULL,
    github_url        TEXT               NULL,
    blog_url          TEXT               NULL,
    created_at        TIMESTAMP          NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at        TIMESTAMP          NOT NULL DEFAULT CURRENT_TIMESTAMP ON update CURRENT_TIMESTAMP,
    deleted_at        TIMESTAMP          NULL
);

-- AUTH_PROVIDER
create TABLE IF NOT EXISTS auth_provider
(
    id                       BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id                  BIGINT       NOT NULL,
    provider_type            VARCHAR(50)  NOT NULL,
    provider_id              VARCHAR(100) NULL,
    is_registration_complete TINYINT      NOT NULL DEFAULT 0,
    created_at               TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at               TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users (id)
);

-- PROJECT
create TABLE IF NOT EXISTS project
(
    id              BIGINT PRIMARY KEY AUTO_INCREMENT,
    name            VARCHAR(300)  NOT NULL,
    sub_name        VARCHAR(300)  NULL,
    overview        VARCHAR(1000) NOT NULL,
    thumbnail_url   TEXT          NULL,
    deploy_url      TEXT          NULL,
    github_url      TEXT          NOT NULL,
    like_count      BIGINT        NOT NULL DEFAULT 0,
    view_count      BIGINT        NOT NULL DEFAULT 0,
    start_date      TIMESTAMP     NULL,
    end_date        TIMESTAMP     NULL,
    owner_id        BIGINT        NOT NULL,
    description     TEXT          NOT NULL,
    troubleshooting TEXT          NULL,
    created_at      TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP ON update CURRENT_TIMESTAMP,
    deleted_at      TIMESTAMP     NULL
);

-- PROJECT_MEMBER
create TABLE IF NOT EXISTS project_member
(
    id         BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id    BIGINT      NULL,
    project_id BIGINT      NOT NULL,
    role       VARCHAR(15) NOT NULL,
    nickname   VARCHAR(20) NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users (id),
    FOREIGN KEY (project_id) REFERENCES project (id)
);

-- LIKE
create TABLE IF NOT EXISTS likes
(
    id         BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id    BIGINT    NOT NULL,
    project_id BIGINT    NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON update CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users (id),
    FOREIGN KEY (project_id) REFERENCES project (id)
);

-- SKILL
create TABLE IF NOT EXISTS skill
(
    id             BIGINT PRIMARY KEY AUTO_INCREMENT,
    name           VARCHAR(50) UNIQUE NOT NULL,
    icon_image_url TEXT               NULL
);

-- USER_SKILL
create TABLE IF NOT EXISTS user_skill
(
    id       BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id  BIGINT      NOT NULL,
    skill_id BIGINT      NOT NULL,
    category VARCHAR(50) NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users (id),
    FOREIGN KEY (skill_id) REFERENCES skill (id)
);

-- PROJECT_SKILL
create TABLE IF NOT EXISTS project_skill
(
    id         BIGINT PRIMARY KEY AUTO_INCREMENT,
    project_id BIGINT      NOT NULL,
    skill_id   BIGINT      NOT NULL,
    category   VARCHAR(50) NOT NULL,
    FOREIGN KEY (project_id) REFERENCES project (id),
    FOREIGN KEY (skill_id) REFERENCES skill (id)
);

-- FILE
create TABLE IF NOT EXISTS files
(
    id         BIGINT PRIMARY KEY AUTO_INCREMENT,
    project_id BIGINT      NOT NULL,
    type       VARCHAR(30) NOT NULL,
    url        TEXT        NOT NULL,
    FOREIGN KEY (project_id) REFERENCES project (id)
);
