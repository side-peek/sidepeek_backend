package sixgaezzang.sidepeek.util;

import static sixgaezzang.sidepeek.common.exception.message.CommonErrorMessage.CATEGORY_IS_NULL;
import static sixgaezzang.sidepeek.common.exception.message.CommonErrorMessage.CATEGORY_OVER_MAX_LENGTH;
import static sixgaezzang.sidepeek.common.exception.message.CommonErrorMessage.GITHUB_URL_IS_INVALID;
import static sixgaezzang.sidepeek.common.exception.message.CommonErrorMessage.GITHUB_URL_IS_NULL;
import static sixgaezzang.sidepeek.common.exception.message.CommonErrorMessage.GITHUB_URL_OVER_MAX_LENGTH;
import static sixgaezzang.sidepeek.common.util.CommonConstant.MAX_CATEGORY_LENGTH;
import static sixgaezzang.sidepeek.common.util.CommonConstant.MAX_TEXT_LENGTH;
import static sixgaezzang.sidepeek.projects.exception.message.FileErrorMessage.OVERVIEW_IMAGE_URL_IS_INVALID;
import static sixgaezzang.sidepeek.projects.exception.message.FileErrorMessage.OVERVIEW_IMAGE_URL_OVER_MAX_LENGTH;
import static sixgaezzang.sidepeek.projects.exception.message.MemberErrorMessage.ROLE_IS_NULL;
import static sixgaezzang.sidepeek.projects.exception.message.MemberErrorMessage.ROLE_OVER_MAX_LENGTH;
import static sixgaezzang.sidepeek.projects.exception.message.ProjectErrorMessage.DEPLOY_URL_IS_INVALID;
import static sixgaezzang.sidepeek.projects.exception.message.ProjectErrorMessage.DEPLOY_URL_OVER_MAX_LENGTH;
import static sixgaezzang.sidepeek.projects.exception.message.ProjectErrorMessage.DESCRIPTION_IS_NULL;
import static sixgaezzang.sidepeek.projects.exception.message.ProjectErrorMessage.DESCRIPTION_OVER_MAX_LENGTH;
import static sixgaezzang.sidepeek.projects.exception.message.ProjectErrorMessage.DURATION_IS_INVALID;
import static sixgaezzang.sidepeek.projects.exception.message.ProjectErrorMessage.DURATION_IS_REVERSED;
import static sixgaezzang.sidepeek.projects.exception.message.ProjectErrorMessage.NAME_IS_NULL;
import static sixgaezzang.sidepeek.projects.exception.message.ProjectErrorMessage.NAME_OVER_MAX_LENGTH;
import static sixgaezzang.sidepeek.projects.exception.message.ProjectErrorMessage.OVERVIEW_IS_NULL;
import static sixgaezzang.sidepeek.projects.exception.message.ProjectErrorMessage.OVERVIEW_OVER_MAX_LENGTH;
import static sixgaezzang.sidepeek.projects.exception.message.ProjectErrorMessage.SUB_NAME_OVER_MAX_LENGTH;
import static sixgaezzang.sidepeek.projects.exception.message.ProjectErrorMessage.THUMBNAIL_URL_IS_INVALID;
import static sixgaezzang.sidepeek.projects.exception.message.ProjectErrorMessage.THUMBNAIL_URL_OVER_MAX_LENGTH;
import static sixgaezzang.sidepeek.projects.exception.message.ProjectErrorMessage.TROUBLESHOOTING_OVER_MAX_LENGTH;
import static sixgaezzang.sidepeek.projects.util.ProjectConstant.MAX_OVERVIEW_LENGTH;
import static sixgaezzang.sidepeek.projects.util.ProjectConstant.MAX_PROJECT_NAME_LENGTH;
import static sixgaezzang.sidepeek.projects.util.ProjectConstant.MAX_ROLE_LENGTH;
import static sixgaezzang.sidepeek.users.exception.message.UserErrorMessage.BLOG_URL_IS_INVALID;
import static sixgaezzang.sidepeek.users.exception.message.UserErrorMessage.BLOG_URL_OVER_MAX_LENGTH;
import static sixgaezzang.sidepeek.users.exception.message.UserErrorMessage.CAREER_IS_INVALID;
import static sixgaezzang.sidepeek.users.exception.message.UserErrorMessage.INTRODUCTION_OVER_MAX_LENGTH;
import static sixgaezzang.sidepeek.users.exception.message.UserErrorMessage.JOB_IS_INVALID;
import static sixgaezzang.sidepeek.users.exception.message.UserErrorMessage.NICKNAME_IS_NULL;
import static sixgaezzang.sidepeek.users.exception.message.UserErrorMessage.NICKNAME_OVER_MAX_LENGTH;
import static sixgaezzang.sidepeek.users.exception.message.UserErrorMessage.PROFILE_IMAGE_URL_IS_INVALID;
import static sixgaezzang.sidepeek.users.exception.message.UserErrorMessage.PROFILE_IMAGE_URL_OVER_MAX_LENGTH;
import static sixgaezzang.sidepeek.users.util.UserConstant.MAX_INTRODUCTION_LENGTH;
import static sixgaezzang.sidepeek.users.util.UserConstant.MAX_NICKNAME_LENGTH;
import static sixgaezzang.sidepeek.util.FakeValueProvider.createLongText;
import static sixgaezzang.sidepeek.util.FakeValueProvider.createNickname;
import static sixgaezzang.sidepeek.util.FakeValueProvider.createOverview;
import static sixgaezzang.sidepeek.util.FakeValueProvider.createProjectName;
import static sixgaezzang.sidepeek.util.FakeValueProvider.createUrl;

import java.time.YearMonth;
import java.util.Collections;
import java.util.stream.Stream;
import org.junit.jupiter.params.provider.Arguments;
import sixgaezzang.sidepeek.users.domain.Career;
import sixgaezzang.sidepeek.users.domain.Job;
import sixgaezzang.sidepeek.users.dto.request.UpdateUserProfileRequest;

public class TestParameterProvider {
    //Project
    public static Stream<Arguments> createProjectsWithoutRequired() {
        String name = createProjectName();
        String overview = createOverview();
        String githubUrl = createUrl();
        String description = createLongText();

        return Stream.of(
            Arguments.of("프로젝트 이름",
                null, overview, githubUrl, description, NAME_IS_NULL),
            Arguments.of("프로젝트 개요",
                name, null, githubUrl, description, OVERVIEW_IS_NULL),
            Arguments.of("프로젝트 깃허브 url",
                name, overview, null, description, GITHUB_URL_IS_NULL),
            Arguments.of("프로젝트 기능 설명",
                name, overview, githubUrl, null, DESCRIPTION_IS_NULL)
        );
    }

    public static Stream<Arguments> createProjectsOnlyInvalidRequired() {
        String name = createProjectName();
        String overview = createOverview();
        String githubUrl = createUrl();
        String description = createLongText();

        return Stream.of(
            Arguments.of("프로젝트 이름이 최대 길이를 넘는 경우",
                "N".repeat(MAX_PROJECT_NAME_LENGTH + 1), overview, githubUrl, description,
                NAME_OVER_MAX_LENGTH),
            Arguments.of("프로젝트 개요가 최대 길이를 넘는 경우",
                name, "O".repeat(MAX_OVERVIEW_LENGTH + 1), githubUrl, description,
                OVERVIEW_OVER_MAX_LENGTH),
            Arguments.of("프로젝트 깃허브 url 형식이 올바르지 않은 경우",
                name, overview, "not url pattern", description,
                GITHUB_URL_IS_INVALID),
            Arguments.of("프로젝트 깃허브 url이 최대 길이를 넘는 경우",
                name, overview, githubUrl + "u".repeat(MAX_TEXT_LENGTH), description,
                GITHUB_URL_OVER_MAX_LENGTH),
            Arguments.of("프로젝트 기능 설명이 최대 길이를 넘는 경우",
                name, overview, githubUrl, "D".repeat(MAX_TEXT_LENGTH + 1),
                DESCRIPTION_OVER_MAX_LENGTH)
        );
    }

    public static Stream<Arguments> createProjectsWithInvalidOption() {
        String url = createUrl();

        return Stream.of(
            Arguments.of("프로젝트 부제목이 최대 길이를 넘는 경우",
                "S".repeat(MAX_PROJECT_NAME_LENGTH + 1), null, null, null, null, null,
                SUB_NAME_OVER_MAX_LENGTH),
            Arguments.of("프로젝트 썸네일 url 형식이 올바르지 않은 경우",
                null, "not url pattern", null, null, null, null,
                THUMBNAIL_URL_IS_INVALID),
            Arguments.of("프로젝트 썸네일 url이 최대 길이를 넘는 경우",
                null, url + "u".repeat(MAX_TEXT_LENGTH), null, null, null, null,
                THUMBNAIL_URL_OVER_MAX_LENGTH),
            Arguments.of("프로젝트 배포 url 형식이 올바르지 않은 경우",
                null, null, "not url pattern", null, null, null,
                DEPLOY_URL_IS_INVALID),
            Arguments.of("프로젝트 배포 url이 최대 길이를 넘는 경우",
                null, null, url + "u".repeat(MAX_TEXT_LENGTH), null, null, null,
                DEPLOY_URL_OVER_MAX_LENGTH),
            Arguments.of("프로젝트 트러블 슈팅 내용이 최대 길이를 넘는 경우",
                null, null, null, "T".repeat(MAX_TEXT_LENGTH + 1), null, null,
                TROUBLESHOOTING_OVER_MAX_LENGTH),
            Arguments.of("프로젝트 시작/끝 기간 중 하나만 누락된 경우",
                null, null, null, null, null, YearMonth.of(2024, 2),
                DURATION_IS_INVALID),
            Arguments.of("프로젝트 시작/끝 기간 순서가 안맞는 경우",
                null, null, null, null, YearMonth.of(2024, 2), YearMonth.of(2023, 1),
                DURATION_IS_REVERSED)
        );
    }

    // Member
    public static Stream<Arguments> createInvalidMemberInfo() {
        return Stream.of(
            Arguments.of("멤버 닉네임이 최대 길이를 넘는 경우",
                false, "N".repeat(MAX_NICKNAME_LENGTH + 1), "role",
                NICKNAME_OVER_MAX_LENGTH),
            Arguments.of("멤버 역할을 적지 않는 경우",
                true, createNickname(), null,
                ROLE_IS_NULL),
            Arguments.of(" 멤버 역할이 최대 길이를 넘는 경우",
                true, createNickname(), "R".repeat(MAX_ROLE_LENGTH + 1),
                ROLE_OVER_MAX_LENGTH),
            Arguments.of("비회원/회원 정보가 모두 없는 경우",
                false, null, "role",
                NICKNAME_IS_NULL)
        );
    }

    // File
    public static Stream<Arguments> createInvalidFileInfo() {
        return Stream.of(
            Arguments.of("프로젝트 레이아웃 이미지 URL 형식이 올바르지 않은 경우",
                "not url pattern",
                OVERVIEW_IMAGE_URL_IS_INVALID),
            Arguments.of("프로젝트 레이아웃 이미지 URL이 최대 길이를 넘는 경우",
                "https://sidepeek.file/" + "f".repeat(MAX_TEXT_LENGTH),
                OVERVIEW_IMAGE_URL_OVER_MAX_LENGTH)
        );
    }

    // TechStack
    public static Stream<Arguments> createInvalidTechStackInfo() {
        return Stream.of(
            Arguments.of("기술 스택 카테고리를 누락하는 경우", null,
                CATEGORY_IS_NULL),
            Arguments.of("기술 스택 카테고리가 최대 길이를 넘는 경우", "C".repeat(MAX_CATEGORY_LENGTH + 1),
                CATEGORY_OVER_MAX_LENGTH)
        );
    }


    // User Profile
    public static Stream<Arguments> createProfileRequestWithInvalidNickname() {
        return Stream.of(
            Arguments.of("닉네임이 최대 길이를 넘는 경우",
                new UpdateUserProfileRequest(
                    "N".repeat(MAX_NICKNAME_LENGTH + 1),
                    FakeValueProvider.createUrl(),
                    FakeValueProvider.createIntroduction(),
                    Job.BACKEND_DEVELOPER.getName(),
                    Career.JUNIOR.getDescription(),
                    FakeValueProvider.createUrl(),
                    FakeValueProvider.createUrl(),
                    Collections.emptyList()
                ), NICKNAME_OVER_MAX_LENGTH),
            Arguments.of("닉네임이 빈 문자열인 경우",
                new UpdateUserProfileRequest(
                    "",
                    FakeValueProvider.createUrl(),
                    FakeValueProvider.createIntroduction(),
                    Job.BACKEND_DEVELOPER.getName(),
                    Career.JUNIOR.getDescription(),
                    FakeValueProvider.createUrl(),
                    FakeValueProvider.createUrl(),
                    Collections.emptyList()
                ), NICKNAME_IS_NULL),
            Arguments.of("닉네임이 null인 경우",
                new UpdateUserProfileRequest(
                    null,
                    FakeValueProvider.createUrl(),
                    FakeValueProvider.createIntroduction(),
                    Job.BACKEND_DEVELOPER.getName(),
                    Career.JUNIOR.getDescription(),
                    FakeValueProvider.createUrl(),
                    FakeValueProvider.createUrl(),
                    Collections.emptyList()
                ), NICKNAME_IS_NULL)
        );
    }

    public static Stream<Arguments> createProfileRequestWithInvalidIntroduction() {
        return Stream.of(
            Arguments.of("introduction이 최대 길이를 넘는 경우",
                new UpdateUserProfileRequest(
                    FakeValueProvider.createNickname(),
                    FakeValueProvider.createUrl(),
                    "I".repeat(MAX_INTRODUCTION_LENGTH + 1),
                    Job.BACKEND_DEVELOPER.getName(),
                    Career.JUNIOR.getDescription(),
                    FakeValueProvider.createUrl(),
                    FakeValueProvider.createUrl(),
                    Collections.emptyList()
                ), INTRODUCTION_OVER_MAX_LENGTH)
        );
    }

    public static Stream<Arguments> createProfileRequestWithInvalidProfileImageUrl() {
        return Stream.of(
            Arguments.of("profileImageUrl이 최대 길이를 넘는 경우",
                new UpdateUserProfileRequest(
                    FakeValueProvider.createNickname(),
                    FakeValueProvider.createUrl() + "P".repeat(MAX_TEXT_LENGTH),
                    FakeValueProvider.createIntroduction(),
                    Job.BACKEND_DEVELOPER.getName(),
                    Career.JUNIOR.getDescription(),
                    FakeValueProvider.createUrl(),
                    FakeValueProvider.createUrl(),
                    Collections.emptyList()
                ), PROFILE_IMAGE_URL_OVER_MAX_LENGTH),
            Arguments.of("profileImageUrl이 URL 형식이 아닌 경우",
                new UpdateUserProfileRequest(
                    FakeValueProvider.createNickname(),
                    "No URL Pattern",
                    FakeValueProvider.createIntroduction(),
                    Job.BACKEND_DEVELOPER.getName(),
                    Career.JUNIOR.getDescription(),
                    FakeValueProvider.createUrl(),
                    FakeValueProvider.createUrl(),
                    Collections.emptyList()
                ), PROFILE_IMAGE_URL_IS_INVALID)
        );
    }

    public static Stream<Arguments> createProfileRequestWithInvalidJob() {
        return Stream.of(
            Arguments.of("jobName에 해당하는 Job이 없는 경우",
                new UpdateUserProfileRequest(
                    FakeValueProvider.createNickname(),
                    FakeValueProvider.createUrl(),
                    FakeValueProvider.createIntroduction(),
                    "No Job",
                    Career.JUNIOR.getDescription(),
                    FakeValueProvider.createUrl(),
                    FakeValueProvider.createUrl(),
                    Collections.emptyList()
                ), JOB_IS_INVALID)
        );
    }

    public static Stream<Arguments> createProfileRequestWithInvalidCareer() {
        return Stream.of(
            Arguments.of("careerDescription에 해당하는 Career가 없는 경우",
                new UpdateUserProfileRequest(
                    FakeValueProvider.createNickname(),
                    FakeValueProvider.createUrl(),
                    FakeValueProvider.createIntroduction(),
                    Job.BACKEND_DEVELOPER.getName(),
                    "No Career",
                    FakeValueProvider.createUrl(),
                    FakeValueProvider.createUrl(),
                    Collections.emptyList()
                ), CAREER_IS_INVALID)
        );
    }

    public static Stream<Arguments> createProfileRequestWithInvalidGithubUrl() {
        return Stream.of(
            Arguments.of("githubUrl이 최대 길이를 넘는 경우",
                new UpdateUserProfileRequest(
                    FakeValueProvider.createNickname(),
                    FakeValueProvider.createUrl(),
                    FakeValueProvider.createIntroduction(),
                    Job.BACKEND_DEVELOPER.getName(),
                    Career.JUNIOR.getDescription(),
                    FakeValueProvider.createUrl() + "G".repeat(MAX_TEXT_LENGTH),
                    FakeValueProvider.createUrl(),
                    Collections.emptyList()
                ), GITHUB_URL_OVER_MAX_LENGTH),
            Arguments.of("githubUrl이 URL 형식이 아닌 경우",
                new UpdateUserProfileRequest(
                    FakeValueProvider.createNickname(),
                    FakeValueProvider.createUrl(),
                    FakeValueProvider.createIntroduction(),
                    Job.BACKEND_DEVELOPER.getName(),
                    Career.JUNIOR.getDescription(),
                    "No URL Pattern",
                    FakeValueProvider.createUrl(),
                    Collections.emptyList()
                ), GITHUB_URL_IS_INVALID)
        );
    }

    public static Stream<Arguments> createProfileRequestWithInvalidBlogUrl() {
        return Stream.of(
            Arguments.of("blogUrl이 최대 길이를 넘는 경우",
                new UpdateUserProfileRequest(
                    FakeValueProvider.createNickname(),
                    FakeValueProvider.createUrl(),
                    FakeValueProvider.createIntroduction(),
                    Job.BACKEND_DEVELOPER.getName(),
                    Career.JUNIOR.getDescription(),
                    FakeValueProvider.createUrl(),
                    FakeValueProvider.createUrl() + "B".repeat(MAX_TEXT_LENGTH),
                    Collections.emptyList()
                ), BLOG_URL_OVER_MAX_LENGTH),
            Arguments.of("blogUrl이 URL 형식이 아닌 경우",
                new UpdateUserProfileRequest(
                    FakeValueProvider.createNickname(),
                    FakeValueProvider.createUrl(),
                    FakeValueProvider.createIntroduction(),
                    Job.BACKEND_DEVELOPER.getName(),
                    Career.JUNIOR.getDescription(),
                    FakeValueProvider.createUrl(),
                    "No URL Pattern",
                    Collections.emptyList()
                ), BLOG_URL_IS_INVALID)
        );
    }

}
