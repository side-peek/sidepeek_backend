package sixgaezzang.sidepeek.users.domain;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Job {
    BACKEND_DEVELOPER("백엔드 개발자"),
    FRONTEND_DEVELOPER("프론트엔드 개발자"),
    IOS_DEVELOPER("IOS 개발자"),
    ANDROID_DEVELOPER("안드로이드 개발자"),
    DESIGNER("디자이너"),
    DATA_SCIENTIST("데이터 분석가"),
    ETC("기타");

    private final String name;

    Job(String name) {
        this.name = name;
    }

    @JsonValue
    public String getName() {
        return name;
    }
}
