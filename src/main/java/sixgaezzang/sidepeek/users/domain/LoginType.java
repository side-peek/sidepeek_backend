package sixgaezzang.sidepeek.users.domain;

public enum LoginType {
    GITHUB,
    EMAIL,
    KAKAO,
    GOOGLE;

    boolean isEmailType() {
        return this == EMAIL;
    }

    public boolean isGitHubType() {
        return this == GITHUB;
    }
}
