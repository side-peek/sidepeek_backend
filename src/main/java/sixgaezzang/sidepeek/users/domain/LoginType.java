package sixgaezzang.sidepeek.users.domain;

public enum LoginType {
    GITHUB,
    BASIC,
    KAKAO,
    GOOGLE;

    boolean isBasicType() {
        return this == BASIC;
    }

    public boolean isGitHubType() {
        return this == GITHUB;
    }
}
