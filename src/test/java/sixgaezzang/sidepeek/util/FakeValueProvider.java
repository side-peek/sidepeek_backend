package sixgaezzang.sidepeek.util;

import static sixgaezzang.sidepeek.comments.util.CommentConstant.MAX_CONTENT_LENGTH;
import static sixgaezzang.sidepeek.common.util.CommonConstant.GITHUB_URL;
import static sixgaezzang.sidepeek.common.util.CommonConstant.MAX_CATEGORY_LENGTH;
import static sixgaezzang.sidepeek.common.util.CommonConstant.MAX_TEXT_LENGTH;
import static sixgaezzang.sidepeek.projects.util.ProjectConstant.MAX_OVERVIEW_LENGTH;
import static sixgaezzang.sidepeek.projects.util.ProjectConstant.MAX_PROJECT_NAME_LENGTH;
import static sixgaezzang.sidepeek.projects.util.ProjectConstant.MAX_ROLE_LENGTH;
import static sixgaezzang.sidepeek.skill.domain.Skill.MAX_SKILL_NAME_LENGTH;
import static sixgaezzang.sidepeek.users.util.UserConstant.MAX_INTRODUCTION_LENGTH;
import static sixgaezzang.sidepeek.users.util.UserConstant.MAX_NICKNAME_LENGTH;

import java.util.ArrayList;
import java.util.List;
import net.datafaker.Faker;
import sixgaezzang.sidepeek.common.dto.request.SaveTechStackRequest;
import sixgaezzang.sidepeek.projects.domain.UserProjectSearchType;
import sixgaezzang.sidepeek.projects.dto.request.SaveMemberRequest;

public class FakeValueProvider {

    private static final Faker FAKER = new Faker();

    // Common
    public static String checkAndCutLength(String value, int maxLength) {
        return (value.length() <= maxLength) ? value :
            value.substring(value.length() % maxLength);
    }

    public static String createLongText() { // 기능 설명, 트러블 슈팅
        return FAKER.text().text(1, MAX_TEXT_LENGTH);
    }

    public static List<String> createUrls(int length) { // 파일, 깃허브, 배포 등등 url 목록
        List<String> urls = new ArrayList<>();
        while (length-- > 0) {
            urls.add(createUrl());
        }

        return urls;
    }

    public static Long createId() {
        return FAKER.random().nextLong(Long.MAX_VALUE);
    }

    public static String createUrl() { // 파일, 깃허브, 배포 등등 url
        return FAKER.internet().url();
    }

    public static String createGithubUrl() {
        return GITHUB_URL + "/" + createEnglishKeyword();
    }

    public static String createEnglishKeyword() { // 검색 키워드(영어)
        return FAKER.text().text(1, 2);
    }

    public static Boolean createBoolean() {
        return FAKER.bool().bool();
    }

    // Project
    public static String createProjectName() { // 프로젝트 제목/부제목
        return checkAndCutLength(FAKER.name().title(), MAX_PROJECT_NAME_LENGTH);
    }

    public static UserProjectSearchType createUserProjectSearchType() {
        UserProjectSearchType[] userProjectSearchTypes = {UserProjectSearchType.JOINED,
            UserProjectSearchType.LIKED, UserProjectSearchType.COMMENTED};
        return userProjectSearchTypes[FAKER.random().nextInt(userProjectSearchTypes.length)];
    }

    public static String createOverview() {
        return FAKER.text().text(1, MAX_OVERVIEW_LENGTH);
    }

    // Project Skill
    public static String createSkillCategory() {
        return checkAndCutLength(FAKER.job().position(), MAX_CATEGORY_LENGTH);
    }

    public static String createSkillName() {
        return FAKER.text().text(10, MAX_SKILL_NAME_LENGTH);
    }

    public static int getSkillCountByCategory(List<SaveTechStackRequest> techStacks) {
        return (int) techStacks.stream()
            .map(SaveTechStackRequest::category)
            .distinct()
            .count();
    }

    // Member
    public static String createRole() {
        return checkAndCutLength(FAKER.job().position(), MAX_ROLE_LENGTH);
    }

    public static String createNickname() {
        return FAKER.text().text(10, MAX_NICKNAME_LENGTH);
    }

    public static String createEmail() {
        return FAKER.internet().emailAddress();
    }

    public static String createPassword() {
        return FAKER.internet().password(8, 100, true, true, true);
    }

    public static int getMemberCountByRole(List<SaveMemberRequest> members) {
        return (int) members.stream()
            .map(SaveMemberRequest::role)
            .distinct()
            .count();
    }

    // Comment
    public static String createContent() {
        return FAKER.text().text(1, MAX_CONTENT_LENGTH);
    }

    // User
    public static String createIntroduction() {
        return FAKER.text().text(1, MAX_INTRODUCTION_LENGTH);
    }

}
