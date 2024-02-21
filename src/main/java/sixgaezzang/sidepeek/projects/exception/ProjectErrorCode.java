package sixgaezzang.sidepeek.projects.exception;

import sixgaezzang.sidepeek.common.exception.ErrorCode;

public enum ProjectErrorCode implements ErrorCode {

    ID_NOT_EXISTING(2001, "해당 프로젝트가 존재하지 않습니다.");

    private final int code;
    private final String message;

    ProjectErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
