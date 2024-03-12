package sixgaezzang.sidepeek.users.domain;

import static sixgaezzang.sidepeek.users.exception.message.UserErrorMessage.CAREER_IS_INVALID;

import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Arrays;
import java.util.Objects;

public enum Career {
    ENTRY_LEVEL("0년차"),
    JUNIOR("1-3년차"),
    MID_LEVEL("4-6년차"),
    SENIOR("7-9년차"),
    EXPERT("10년차 이상");

    private final String description;

    Career(String description) {
        this.description = description;
    }

    @JsonValue
    public String getDescription() {
        return description;
    }

    public static Career get(String description) {
        return Arrays.stream(Career.values())
            .filter(career -> Objects.equals(career.getDescription(), description))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException(CAREER_IS_INVALID));
    }

}
