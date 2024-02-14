package sixgaezzang.sidepeek.users.domain;

public enum Provider {
    GITHUB,
    BASIC,
    KAKAO,
    GOOGLE;

    public static boolean isBasic(Provider provider) {
        return provider == BASIC;
    }
}
