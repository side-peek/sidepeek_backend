package sixgaezzang.sidepeek.users.domain;

import static sixgaezzang.sidepeek.users.exception.message.UserErrorMessage.JOB_IS_INVALID;

import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Arrays;
import java.util.Objects;

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

    public static Job get(String jobName) {
        return Arrays.stream(Job.values())
            .filter(job -> Objects.equals(job.getName(), jobName))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException(JOB_IS_INVALID));
    }

}
