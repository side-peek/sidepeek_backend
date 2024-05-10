
![image](https://github.com/side-peek/sidepeek_backend/assets/85275893/264774cd-f9e7-4351-9032-c7bea5d8b704)

> [!NOTE]
> 사이드픽 Backend 레포지토리입니다!

[사이드픽(Side Peek)👀](https://www.sidepeek.site/)은 다른 개발자들의 사이드 프로젝트에서 인사이트를 얻고 싶은 개발자들을 위한 사이드 프로젝트 공유 플랫폼입니다.

## 👥 구성원

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

## 🪄 팀 문화

### 1. 문제가 발생하면 해결방안 기록하고 공유하자🚨

- 팀 내에서 새로 배운 내용, 트러블 슈팅을 노션에 기록해서 공유하고 있어요.

    <img src="https://github.com/side-peek/sidepeek_backend/assets/85275893/cbb37b88-0455-449e-a238-5cec076a531e" width="60%">

### 2. 우리만의 방식으로 개발해보자🫠
- 지속적인 회의를 통해 우리 팀만의 룰을 만들어서 개발하고 있어요.

    <img src="https://github.com/side-peek/sidepeek_backend/assets/85275893/241f9b5f-a28d-4c0d-8e7b-1de5681380ba" width="50%">

## 📐 개발 컨벤션

### 1. **Github Flow** 전략 도입 및 **Github Action**을 통한 **CI/CD**
- 기능 개발, 버그 픽스 등 소스코드 변경이 필요할 때 **새로운 브랜치**(ex. `feat#1-xxxx`) 생성 후 개발 환경(`dev`) 등의 Backbone 브랜치에 Merge
- **CI** 테스트 통과 후, **최소 1명**에게 Approve 받고 Merge 하기(@yenzip CI 설정)
- 일주일에 한 번씩 배포 환경(`main`) 브랜치에 `dev` 브랜치 Merge 하기(@uijin-j CD 설정)
### 2. 코드 스타일
- [**CheckStyle**](https://checkstyle.sourceforge.io/) 도입하여 build 실행마다 코드 컨벤션 확인 및 관리(@uijin-j 설정)
- 구글 자바 컨벤션 도입(Indent, Javadoc 등 팀 상황에 맞게 일부 수정)
- 그 외 커밋 컨벤션, 애너테이션 순서, 생성자, 메서드 네임 등 **팀 방향성**에 맞게 설정
### 3. 테스트
- [**JaCoCo**](https://www.jacoco.org/jacoco/) 도입으로 build 실행마다 테스트 커버리지 확인 및 관리(@Sehee-Lee-01 설정)
- **Service 레이어** 중심으로 테스트 커버리지 **최소 90% 이상** 되도록 작성

## 🛠️ 기술 스택

- **Language:** JAVA 17
- **Server:** Spring Boot 3.2.2, Spring Security
- **ORM:** Spring Data JPA, QueryDSL
- **API Docs:** Swagger
- **DB:** Flyway, MySQL, Redis
- **Infra:** Docker, GiHub Action, AWS(EC2, S3, RDS, CodeDeploy, ElasticCache, ACM, Route 53, IAM)
- **Test:** JUnit5, Mockito, Data Faker
- **Etc:** Slack API Client, Sentry

## ⭐ 핵심 기능
### 1. 게시글
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

### 2. 회원(사용자)
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

## 🏗️ 아키텍쳐

<img src="https://github.com/side-peek/sidepeek_backend/assets/85275893/9247c6c6-831b-41f4-9bbf-8c5da1317b74" width="80%">


### **역할 분리, 가독성**에 초점을 두고 개발 진행
- 외부 의존성 추가시 Config Class 정의
    - Config를 통해 추후 외부 의존성을 선택적으로 불러올 수 있는 환경을 구성
- 도메인별 패키지 구성
    - 복잡한 서비스 구조를 단순화하고 가독성을 높이고자 도메인별 패키지 분리
- 클래스 안의 역할을 최소화하고자 Validator, Constant, ErrorMessage 클래스 구현 및 적용 ⇒ 덕분에 코드 수정이 더욱 간편해짐
    - ex) 기획에서 닉네임 최대 길이 변경 → 닉네임 최대길이를 정의하는 상수 값만 바꾸면 전역으로 수정된다.(비즈니스 로직 구현 메서드, Swagger, 테스트 코드 등)
### 인프라
- AWS DEV, PROD 환경 분리
    - PROD 환경은 서브넷 구성하여 RDS 외부 접근 제한
- CodeDeploy 설정으로 CD 구현

## 🚨 개선 사항 및 트러블슈팅
### 1. 세희
<details>
    <summary><b>✅ 예외 인지 시간 단축</b></summary>

- **상황:** 기존 Prod(운영), Dev(개발) 서버에서 **핸들링 되지 않던 예외**나 핸등링 되더라도 **서버 운영상 문제가 있는 예외**를 쉽게 인지하고자 Sentrty 연동 및 예외 자동 알림 설정 기능을 구현했습니다.
- **방법**
    - **Sentry**를 연동하여 서버에서 발생한 **예외의 원인을 분석**할 수 있도록 설정했습니다.
      
      <img src="https://github.com/side-peek/sidepeek_backend/assets/85275893/574f7e65-ae88-410e-a0c9-769975a88ae8" width="60%">
      
    - **Slack API Client**를 사용하여 발생한 예외를 빨리 인지할 수 있도록 **자동 Slack 알림**음 구현했습니다.
      
      <img src="https://github.com/side-peek/sidepeek_backend/assets/85275893/87ce3fef-2eaf-4d3e-8c11-df38d17fe37a" width="60%">
      
- **결과**: 기존에는 개발자가 직접 로컬에서 확인하거나 프론트엔드 개발자로부터 발생한 예외를 전달받았지만 Sentrty 연동 및 자동 알림 설정으로 동료들과의 예외 발생 상황과 관련한 불필요한 소통을 줄이고 **예외 해결**에만 집중할 수 있는 환경을 조성했습니다. 
- **배운점**: 예외 알림을 설정하면서 개발자가 인지하고 곧바로 처리해야 항 예외, 사용자 단에서 해결할 수 있는 예외 등을 구분하면서 서버 예외 처리에 대한 생각을 되짚어볼 수 있었습니다.
- **블로그**
    - [[Spring] 서버에 Sentry를 적용해보자](https://medium.com/@Hailey24/spring-dev-%EC%84%9C%EB%B2%84%EC%97%90-sentry%EB%A5%BC-%EC%A0%81%EC%9A%A9%ED%95%B4%EB%B3%B4%EC%9E%90-1-100ab28a3fa8)
    - [[Spring] 서버에서 발생한 예외, Sentry Issue 정보 Slack 알림으로 보내기](https://medium.com/@Hailey24/spring-%EC%84%9C%EB%B2%84%EC%97%90%EC%84%9C-%EB%B0%9C%EC%83%9D%ED%95%9C-%EC%98%88%EC%99%B8-sentry-issue-%EC%A0%95%EB%B3%B4-slack-%EC%95%8C%EB%A6%BC%EC%9C%BC%EB%A1%9C-%EB%B3%B4%EB%82%B4%EA%B8%B0-dbb7e50ee49b)
  
</details>

<details>
    <summary><b>✅ 테스트 코드 중복 개선</b></summary>

- **상황:** 각 테스트 코드에서 Entity, Dto, 값 생성 코드 중복이 발생하는 것을 확인했습니다. 서비스 특성상 Entity의 속성이 많았기 때문에 생성자가 긴 것이 원인이었습니다. 서비스 로직도 복잡하여 테스트 코드가 길어지니 가독성이 점점 안좋아졌습니다.
- **방법**: 각각 테스트 클래스에서 중복되는 코드를 찾아서 테스트 환경 전역에서 사용할 수 있는 static 메서드로 정의하고 이러한 메서드를 제공하는 [테스트 Fixture 제공 클래스🔗](https://github.com/side-peek/sidepeek_backend/tree/dev/src/test/java/sixgaezzang/sidepeek/util)를 구현하여 팀원들에게 사용법을 공유했습니다. 이후 팀원들과 함께 Fixture 클래스를 구현해나가면서 중복을 줄였습니다.
  
    <img src="https://github.com/side-peek/sidepeek_backend/assets/85275893/e6358205-35d1-42de-8e63-71c5aed07a33" width="30%">

- **결과**: 각 테스트 코드에서 대략 633줄 이상의 코드 중복을 줄임으로써 가독성을 확보할 수 있었습니다. 또한 서비스 로직 테스트에만 집중할 수 있게 되었습니다. 
- **배운점**: 단위 테스트 원칙(FIRST)에서의 Fast는 테스트 실행 시간을 이야기하지만 실질적으로 개발자가 테스트 코드를 해석하고 작성하는 시간도 중요하다는 것을 알게 되었습니다. 
  
</details>

### 2. 의진
### 3. 예림


## 💿 ERD

<img src="https://github.com/side-peek/sidepeek_backend/assets/85275893/d384b7a3-c941-4baf-83b8-64de5a2876ab" width="50%">

## 📠 API 목록

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
