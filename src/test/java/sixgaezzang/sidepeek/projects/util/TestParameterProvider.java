package sixgaezzang.sidepeek.projects.util;

import static sixgaezzang.sidepeek.common.util.CommonConstant.MAX_TEXT_LENGTH;
import static sixgaezzang.sidepeek.projects.exception.message.FileErrorMessage.OVERVIEW_IMAGE_URL_IS_INVALID;
import static sixgaezzang.sidepeek.projects.exception.message.FileErrorMessage.OVERVIEW_IMAGE_URL_OVER_MAX_LENGTH;
import static sixgaezzang.sidepeek.projects.exception.message.MemberErrorMessage.MEMBER_IS_INVALID;
import static sixgaezzang.sidepeek.projects.exception.message.MemberErrorMessage.NON_FELLOW_MEMBER_NICKNAME_OVER_MAX_LENGTH;
import static sixgaezzang.sidepeek.projects.exception.message.MemberErrorMessage.ROLE_IS_NULL;
import static sixgaezzang.sidepeek.projects.exception.message.MemberErrorMessage.ROLE_OVER_MAX_LENGTH;
import static sixgaezzang.sidepeek.projects.exception.message.ProjectErrorMessage.DEPLOY_URL_IS_INVALID;
import static sixgaezzang.sidepeek.projects.exception.message.ProjectErrorMessage.DEPLOY_URL_OVER_MAX_LENGTH;
import static sixgaezzang.sidepeek.projects.exception.message.ProjectErrorMessage.DESCRIPTION_IS_NULL;
import static sixgaezzang.sidepeek.projects.exception.message.ProjectErrorMessage.DESCRIPTION_OVER_MAX_LENGTH;
import static sixgaezzang.sidepeek.projects.exception.message.ProjectErrorMessage.DURATION_IS_INVALID;
import static sixgaezzang.sidepeek.projects.exception.message.ProjectErrorMessage.DURATION_IS_REVERSED;
import static sixgaezzang.sidepeek.projects.exception.message.ProjectErrorMessage.GITHUB_URL_IS_INVALID;
import static sixgaezzang.sidepeek.projects.exception.message.ProjectErrorMessage.GITHUB_URL_IS_NULL;
import static sixgaezzang.sidepeek.projects.exception.message.ProjectErrorMessage.GITHUB_URL_OVER_MAX_LENGTH;
import static sixgaezzang.sidepeek.projects.exception.message.ProjectErrorMessage.NAME_IS_NULL;
import static sixgaezzang.sidepeek.projects.exception.message.ProjectErrorMessage.NAME_OVER_MAX_LENGTH;
import static sixgaezzang.sidepeek.projects.exception.message.ProjectErrorMessage.OVERVIEW_IS_NULL;
import static sixgaezzang.sidepeek.projects.exception.message.ProjectErrorMessage.OVERVIEW_OVER_MAX_LENGTH;
import static sixgaezzang.sidepeek.projects.exception.message.ProjectErrorMessage.SUB_NAME_OVER_MAX_LENGTH;
import static sixgaezzang.sidepeek.projects.exception.message.ProjectErrorMessage.THUMBNAIL_URL_IS_INVALID;
import static sixgaezzang.sidepeek.projects.exception.message.ProjectErrorMessage.THUMBNAIL_URL_OVER_MAX_LENGTH;
import static sixgaezzang.sidepeek.projects.exception.message.ProjectErrorMessage.TROUBLESHOOTING_OVER_MAX_LENGTH;
import static sixgaezzang.sidepeek.projects.util.ProjectConstant.MAX_CATEGORY_LENGTH;
import static sixgaezzang.sidepeek.projects.util.ProjectConstant.MAX_OVERVIEW_LENGTH;
import static sixgaezzang.sidepeek.projects.util.ProjectConstant.MAX_PROJECT_NAME_LENGTH;
import static sixgaezzang.sidepeek.projects.util.ProjectConstant.MAX_ROLE_LENGTH;
import static sixgaezzang.sidepeek.skill.util.validation.SkillErrorMessage.CATEGORY_IS_NULL;
import static sixgaezzang.sidepeek.skill.util.validation.SkillErrorMessage.CATEGORY_OVER_MAX_LENGTH;
import static sixgaezzang.sidepeek.users.domain.User.MAX_NICKNAME_LENGTH;

import java.time.YearMonth;
import java.util.stream.Stream;
import org.junit.jupiter.params.provider.Arguments;

public class TestParameterProvider {
    //Project
    public static Stream<Arguments> createProjectsWithoutRequired() {
        String name = FakeValueProvider.createProjectName();
        String overview = FakeValueProvider.createOverview();
        String githubUrl = FakeValueProvider.createUrl();
        String description = FakeValueProvider.createLongText();

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
        String name = FakeValueProvider.createProjectName();
        String overview = FakeValueProvider.createOverview();
        String githubUrl = FakeValueProvider.createUrl();
        String description = FakeValueProvider.createLongText();

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
        String url = FakeValueProvider.createUrl();

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
            Arguments.of("비회원 멤버 닉네임이 최대 길이를 넘는 경우",
                false, "N".repeat(MAX_NICKNAME_LENGTH + 1), "role",
                NON_FELLOW_MEMBER_NICKNAME_OVER_MAX_LENGTH),
            Arguments.of("비회원 멤버 역할을 적지 않는 경우",
                false, "Nickname", null,
                ROLE_IS_NULL),
            Arguments.of("비회원 멤버 역할이 최대 길이를 넘는 경우",
                false, "Nickname", "R".repeat(MAX_ROLE_LENGTH + 1),
                ROLE_OVER_MAX_LENGTH),
            Arguments.of("회원 멤버 역할을 적지 않는 경우",
                true, null, null,
                ROLE_IS_NULL),
            Arguments.of("회원 멤버 역할이 최대 길이를 넘는 경우",
                true, null, "R".repeat(MAX_ROLE_LENGTH + 1),
                ROLE_OVER_MAX_LENGTH),
            Arguments.of("비회원/회원 정보가 모두 없는 경우",
                false, null, "role",
                MEMBER_IS_INVALID)
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

    public static Stream<Arguments> createInvalidProjectSkillInfo() {
        return Stream.of(
            Arguments.of("기술 스택 카테고리를 누락하는 경우", null,
                CATEGORY_IS_NULL),
            Arguments.of("기술 스택 카테고리가 최대 길이를 넘는 경우", "C".repeat(MAX_CATEGORY_LENGTH + 1),
                CATEGORY_OVER_MAX_LENGTH)
        );
    }

}
