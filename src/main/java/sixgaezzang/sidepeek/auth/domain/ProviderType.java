package sixgaezzang.sidepeek.auth.domain;

public enum ProviderType {
    GITHUB,
    KAKAO,
    GOOGLE;

    public static ProviderType from(String provider) {
        String upperCaseProvider = provider.toUpperCase();
        return ProviderType.valueOf(upperCaseProvider);
    }
}
