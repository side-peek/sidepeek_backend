package sixgaezzang.sidepeek.users.domain;

import com.fasterxml.jackson.annotation.JsonValue;

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
}
