
> [!NOTE]
> 사이드픽 Backend 레포지토리입니다!

![image](https://github.com/side-peek/sidepeek_backend/assets/85275893/264774cd-f9e7-4351-9032-c7bea5d8b704)

[사이드픽(Side Peek)👀](https://www.sidepeek.site/)은 다른 개발자들의 사이드 프로젝트에서 인사이트를 얻고 싶은 개발자들을 위한 사이드 프로젝트 공유 플랫폼입니다.

## 구성원

> [!IMPORTANT]
> 총 3명의 구성원으로 2024년 2월 1일부터 백엔드 개발을 진행하고 있습니다.

<table>
<tr align="center">
<td>
<img src="https://github.com/Sehee-Lee-01.png?size=100">
</td>
<td>
<img src="https://github.com/uijin-j.png?size=100">
</td>
<td>
<img src="https://github.com/yenzip.png?size=100">
</td>
</tr>
<tr align="center">
<td><B>이세희</B></td>
<td><B>정의진</B></td>
<td><B>엄예림</B></td>
</tr>
<tr align="center">
<td><a href="https://github.com/Sehee-Lee-01">@Sehee-Lee-01</a></td>
<td><a href="https://github.com/uijin-j">@uijin-j</a></td>
<td><a href="https://github.com/yenzip">@yenzip</a></td>
</tr>
</table>

## 팀 문화

> [!TIP]
> 팀 문화를 기반으로 팀원 간의 신뢰를 쌓으며 생산성을 높여왔습니다.

1. 문제가 발생하면 해결방안 기록하고 공유하자🚨

- 팀 내에서 새로 배운 내용, 트러블 슈팅을 노션에 기록해서 공유하고 있어요.

    <img src="https://github.com/side-peek/sidepeek_backend/assets/85275893/cbb37b88-0455-449e-a238-5cec076a531e" width="50%">

3. 혼자 고민해보고 안되면 빠르게 공유하자🫠
- 어떤 문제가 발생도 함께 해결하는 분위기를 만들었어요.

## 개발 컨벤션

1. **Github Flow** 전략 도입 및 **Github Action**을 통한 **CI/CD**
    - 기능 개발, 버그 픽스 등 소스코드 변경이 필요할 때 **새로운 브랜치**(ex. `feat#1-xxxx`) 생성 후 개발 환경(`dev`) 등의 Backbone 브랜치에 Merge
    - **CI** 테스트 통과 후, **최소 1명**에게 Approve 받고 Merge 하기(@yenzip CI 설정)
    - 일주일에 한 번씩 배포 환경(`main`) 브랜치에 `dev` 브랜치 Merge 하기(@uijin-j CD 설정)
2. 코드 스타일
    - [**CheckStyle**](https://checkstyle.sourceforge.io/) 도입하여 build 실행마다 코드 컨벤션 확인 및 관리(@uijin-j 설정)
    - 구글 자바 컨벤션 도입(Indent, Javadoc 등 팀 상황에 맞게 일부 수정)
    - 그 외 커밋 컨벤션, 애너테이션 순서, 생성자, 메서드 네임 등 **팀 방향성**에 맞게 설정
3. 테스트
    - [**JaCoCo**](https://www.jacoco.org/jacoco/) 도입으로 build 실행마다 테스트 커버리지 확인 및 관리(@Sehee-Lee-01 설정)
    - **Service 레이어** 중심으로 테스트 커버리지 **최소 90% 이상** 되도록 작성

## 기술 스택

- **Language:** JAVA 17
- **Server:** Spring Boot 3.2.2, Spring Security
- **ORM:** Spring Data JPA, QueryDSL
- **API Docs:** Swagger
- **DB:** Flyway, MySQL, Redis
- **Infra:** Docker, GiHub Action, Amazon EC2, S3, RDS, IAM, CodeDeploy
- **Test:** JUnit5, Mockito, Data Faker
- **Etc:** Slack API Client, Sentry

## 핵심 기능

> [!CAUTION]
> 내용 정리 필요

### 게시글
- 게시글
    - 생성
        - 프로젝트 상세 정보(주제, 요약, 링크, 기능 설명 등)를 저장할 수 있습니다.
        - 기술스택 데이터를 검색해서 등록합니다.
        - 회원 닉네임을 검색하여 프로젝트 멤버로 등록합니다. 비회원 멤버는 임의로 이름을 추가합니다.
    - 수정
        - 프로젝트 작성자와 멤버만 수정할 수 있습니다.
        - 프로젝트 상세 정보(주제, 요약, 링크, 기능 설명 등)를 수정할 수 있습니다.
        - 기술스택 데이터를 검색해서 등록합니다.
        - 회원 닉네임을 검색하여 프로젝트 멤버로 등록합니다. 비회원 멤버는 임의로 이름을 추가합니다.
    - 삭제
      - 프로젝트 작성자만 삭제할 수 있습니다.
    - 단건 조회
      - 프로젝트에 대한 상세정보, 댓글, 조회수 등을 확인할 수 있습니다.
      - 프로젝트를 조회하면 조회수가 올라갑니다.
    - 목록 조회 및 검색
        - 필터로 원하는 조건의 프로젝트를 검색할 수 있습니다.
    - 좋아요
      - 회원은 하나의 프로젝트에 좋아요를 한 번 누를 수 있습니다.
- 댓글
    - 생성
      - 댓글에 대댓글을 생성할 수 있습니다. 
      - 익명 댓글로 설정할 수 있습니다.
    - 수정
      - 작성자만이 수정할 수 있습니다.
    - 삭제
      - 작성자만이 삭제할 수 있습니다. 

### 회원(사용자)
- 회원가입
  - 이메일로 회원가입을 할 수 있습니다.
- 로그인
    - 이메일 로그인을 할 수 있습니다.
    - 소셜 로그인(깃허브)을 할 수 있습니다.
- 프로필 수정
    - 회원의 상세정보(프로필 이미지, 소개글, 직군, 경력, 기술 스택 등)를 수정할 수 있습니다.
- 프로필 조회
    - 회원 관련 상세 정보프로필 이미지, 소개글, 직군, 경력, 기술 스택 등)를 조회할 수 있습니다.
    - 회원 관련 프로젝트 정보를 조회할 수 있습니다.
        - 회원이 작성한 프로젝트, 멤버로 참여한 프로젝트, 댓글 단 프로젝트를 확인할 수 있습니다.

## 아키텍쳐

> [!CAUTION]
> 내용 정리 필요

- **역할 분리, 가독성**에 초점을 두고 개발 진행
    - 외부 의존성 추가시 Config Class 정의
        - Config를 통해 추후 외부 의존성을 선택적으로 불러올 수 있는 환경을 구성
    - 도메인별 패키지 구성
        - 복잡한 서비스 구조를 단순화하고 가독성을 높이고자 도메인별 패키지 분리
    - 클래스 안의 역할을 최소화하고자 Validator, Constant, ErrorMessage 클래스 구현 및 적용 ⇒ 덕분에 코드 수정이 더욱 간편해짐
        - ex) 기획에서 닉네임 최대 길이 변경 → 닉네임 최대길이를 정의하는 상수 값만 바꾸면 전역으로 수정된다.(비즈니스 로직 구현 메서드, Swagger, 테스트 코드 등)
- 인프라
    - AWS DEV, PROD 환경 분리
        - PROD 환경은 서브넷 구성하여 RDS 외부 접근 제한
    - CodeDeploy 설정으로 CD 구현

## 문제 해결트러블슈팅

> [!CAUTION]
> 각자 대표적인 트러블 슈팅 하나씩 작성해주세용!

## ERD

<img src="https://github.com/side-peek/sidepeek_backend/assets/85275893/d384b7a3-c941-4baf-83b8-64de5a2876ab" width="50%">

## API 목록

### 인증/인가 API

| Method | PATH                     | 설명                       | 담당       |
|--------|--------------------------|--------------------------|----------|
| POST   | `/auth/reissue`          | Access/Refresh Token 재발급 | [@uijin-j](https://github.com/uijin-j) |
| POST   | `/auth/me`               | Access Token 검증          | [@uijin-j](https://github.com/uijin-j) |
| POST   | `/auth/login`            | 이메일 로그인(기본 로그인)          | [@uijin-j](https://github.com/uijin-j) |
| POST   | `/auth/login/{provider}` | 소셜 로그인                   | [@uijin-j](https://github.com/uijin-j) |

### 회원 API

| Method | PATH                    | 설명                       | 담당            |
|--------|-------------------------|--------------------------|---------------|
| GET    | `/users/{id}`           | 회원 프로필 상세 정보 조회          | [@Sehee-Lee-01](https://github.com/Sehee-Lee-01) |
| GET    | `/users/{id}/projects`  | 회원 관련 프로젝트 조회(참여/좋아요/댓글) | [@uijin-j](https://github.com/uijin-j)      |
| GET    | `/users/nickname`       | 회원 닉네임 검색                | [@Sehee-Lee-01](https://github.com/Sehee-Lee-01) |
| POST   | `/users`                | 회원 가입                    | [@uijin-j](https://github.com/uijin-j)      |
| POST   | `/users/nickname/check` | 닉네임 중복 확인                | [@uijin-j](https://github.com/uijin-j)      |
| POST   | `/users/email/check`    | 이메일 중복 확인                | [@uijin-j](https://github.com/uijin-j)      |
| PUT    | `/users/{id}`           | 회원 프로필 상세 정보 수정          | [@Sehee-Lee-01](https://github.com/Sehee-Lee-01) |
| PUT    | `/users/{id}/password`  | 비밀번호 수정                  | [@uijin-j](https://github.com/uijin-j)     |

### 프로젝트(게시글) API

| Method | PATH               | 설명                             | 담당            |
|--------|--------------------|--------------------------------|---------------|
| GET    | `/projects`        | 프로젝트 전체 조회(검색)                 | [@yenzip](https://github.com/yenzip)       |
| GET    | `/projects/{id}`   | 프로젝트 상세 조회(조회수, 좋아요 수, 댓글/대댓글) | [@yenzip](https://github.com/yenzip)       |
| GET    | `/projects/weekly` | 지난 주 인기 프로젝트 조회(최대 5개)         | [@Sehee-Lee-01](https://github.com/Sehee-Lee-01) |
| POST   | `/projects`        | 프로젝트 생성                        | [@Sehee-Lee-01](https://github.com/Sehee-Lee-01) |
| PUT    | `/projects`        | 프로젝트 수정                        | [@Sehee-Lee-01](https://github.com/Sehee-Lee-01) |
| DELETE | `/projects`        | 프로젝트 삭제                        | [@Sehee-Lee-01](https://github.com/Sehee-Lee-01) |

### 댓글 API

| Method | PATH             | 설명    | 담당            |
|--------|------------------|-------|---------------|
| POST   | `/comments`      | 댓글 생성 | [@Sehee-Lee-01](https://github.com/Sehee-Lee-01) |
| PUT    | `/comments/{id}` | 댓글 수정 | [@Sehee-Lee-01](https://github.com/Sehee-Lee-01) |
| DELETE | `/comments/{id}` | 댓글 삭제 | [@Sehee-Lee-01](https://github.com/Sehee-Lee-01) |

### 좋아요 API

| Method | PATH          | 설명     | 담당      |
|--------|---------------|--------|---------|
| POST   | `/likes`      | 좋아요 생성 | [@yenzip](https://github.com/yenzip) |
| DELETE | `/likes/{id}` | 좋아요 삭제 | [@yenzip](https://github.com/yenzip) |

### 미디어(파일) API

| Method | PATH     | 설명         | 담당            |
|--------|----------|------------|---------------|
| POST   | `/files` | 이미지 파일 업로드 | [@Sehee-Lee-01](https://github.com/Sehee-Lee-01) |

### 기술스택 API

| Method | PATH      | 설명      | 담당            |
|--------|-----------|---------|---------------|
| GET    | `/skills` | 기술스택 검색 | [@Sehee-Lee-01](https://github.com/Sehee-Lee-01) |

## 관련 링크

- Project homepage: https://www.sidepeek.site/
- Repository: https://github.com/side-peek/sidepeek_backend/
- Issue tracker: https://github.com/side-peek/sidepeek_backend/issues/
    - 보안 취약점 등의 민감한 이슈인 경우 해당 [tpfktpgml24@gmail.com](mailto:tpfktpgml24@gmail.com)으로 연락주십시오.
- 관련 프로젝트
    - SidePeek 프론트엔드: https://github.com/side-peek/sidepeek_frontend
