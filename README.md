![image](https://github.com/side-peek/sidepeek_backend/assets/85275893/bec19891-9987-49bc-a55c-a4edd83adf80)

> [!NOTE]
> 사이드픽(Side Peek)은 다른 개발자들의 사이드 프로젝트에서 인사이트를 얻고 싶은 개발자들을 위한 사이드 프로젝트 공유 플랫폼입니다.

## Backend 구성원

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


> [!IMPORTANT]
> 총 3명의 구성원으로 2024년 2월 1일부터 백엔드 개발을 진행하고 있습니다.

## 팀 문화

> [!TIP]
> 팀 문화를 기반으로 팀원 간의 신뢰를 쌓으며 생산성을 높여왔습니다.

1. 문제가 발생하면 해결방안 기록하고 공유하자🚨
2. 혼자 고민해보고 안되면 빠르게 공유하자🫠

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

- JAVA 17
- Spring Boot 3.2.2
- Spring Security
- Spring Data JPA
- MySQL
- Redis
- Flyway
- QueryDSL
- AWS
- Swagger
- Data Faker
- Docker
- GiHub Action
- Slack API
- Sentry

## 핵심 기능

> [!CAUTION]
> 내용 정리 필요

- 게시글
    - 게시글 생성/수정/삭제
    - 게시글 단건 조회
    - 게시글 목록 조회 및 검색
    - 댓글 생성/수정/삭제
    - 댓글 조회
    - 게시글 좋아요

- 회원 정보
    - 회원가입
    - 로그인
    - 프로필 수정
    - 프로필 조회

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

## ERD

> [!CAUTION]
> 이미지 추가 필요

## 트러블슈팅

> [!CAUTION]
> 각자 대표적인 트러블 슈팅 하나씩 작성해주세용!

## API 목록

### 인증/인가 API

| Method | PATH                     | 설명                       | 담당       |
|--------|--------------------------|--------------------------|----------|
| POST   | `/auth/reissue`          | Access/Refresh Token 재발급 | @uijin-j |
| POST   | `/auth/me`               | Access Token 검증          | @uijin-j |
| POST   | `/auth/login`            | 이메일 로그인(기본 로그인)          | @uijin-j |
| POST   | `/auth/login/{provider}` | 소셜 로그인                   | @uijin-j |

### 회원 API

| Method | PATH                    | 설명                       | 담당            |
|--------|-------------------------|--------------------------|---------------|
| GET    | `/users/{id}`           | 회원 프로필 상세 정보 조회          | @Sehee-Lee-01 |
| GET    | `/users/{id}/projects`  | 회원 관련 프로젝트 조회(참여/좋아요/댓글) | @uijin-j      |
| GET    | `/users/nickname`       | 회원 닉네임 검색                | @Sehee-Lee-01 |
| POST   | `/users`                | 회원 가입                    | @uijin-j      |
| POST   | `/users/nickname/check` | 닉네임 중복 확인                | @uijin-j      |
| POST   | `/users/email/check`    | 이메일 중복 확인                | @uijin-j      |
| PUT    | `/users/{id}`           | 회원 프로필 상세 정보 수정          | @Sehee-Lee-01 |
| PUT    | `/users/{id}/password`  | 비밀번호 수정                  | @uijin-j      |

### 프로젝트(게시글) API

| Method | PATH               | 설명                             | 담당            |
|--------|--------------------|--------------------------------|---------------|
| GET    | `/projects`        | 프로젝트 전체 조회(검색)                 | @yenzip       |
| GET    | `/projects/{id}`   | 프로젝트 상세 조회(조회수, 좋아요 수, 댓글/대댓글) | @yenzip       |
| GET    | `/projects/weekly` | 지난 주 인기 프로젝트 조회(최대 5개)         | @Sehee-Lee-01 |
| POST   | `/projects`        | 프로젝트 생성                        | @Sehee-Lee-01 |
| PUT    | `/projects`        | 프로젝트 수정                        | @Sehee-Lee-01 |
| DELETE | `/projects`        | 프로젝트 삭제                        | @Sehee-Lee-01 |

### 댓글 API

| Method | PATH             | 설명    | 담당            |
|--------|------------------|-------|---------------|
| POST   | `/comments`      | 댓글 생성 | @Sehee-Lee-01 |
| PUT    | `/comments/{id}` | 댓글 수정 | @Sehee-Lee-01 |
| DELETE | `/comments/{id}` | 댓글 삭제 | @Sehee-Lee-01 |

### 좋아요 API

| Method | PATH          | 설명     | 담당      |
|--------|---------------|--------|---------|
| POST   | `/likes`      | 좋아요 생성 | @yenzip |
| DELETE | `/likes/{id}` | 좋아요 삭제 | @yenzip |

### 미디어(파일) API

| Method | PATH     | 설명         | 담당            |
|--------|----------|------------|---------------|
| POST   | `/files` | 이미지 파일 업로드 | @Sehee-Lee-01 |

### 기술스택 API

| Method | PATH      | 설명      | 담당            |
|--------|-----------|---------|---------------|
| GET    | `/skills` | 기술스택 검색 | @Sehee-Lee-01 |

## 관련 링크

- Project homepage: https://www.sidepeek.site/
- Repository: https://github.com/side-peek/sidepeek_backend/
- Issue tracker: https://github.com/side-peek/sidepeek_backend/issues/
    - 보안 취약점 등의 민감한 이슈인 경우 해당 [tpfktpgml24@gmail.com](mailto:tpfktpgml24@gmail.com)으로 연락주십시오.
- 관련 프로젝트
    - SidePeek 프론트엔드: https://github.com/side-peek/sidepeek_frontend
