package sixgaezzang.sidepeek.users.exception;

import static sixgaezzang.sidepeek.users.util.UserConstant.MAX_NICKNAME_LENGTH;

import sixgaezzang.sidepeek.common.exception.ErrorCode;

public enum UserErrorCode implements ErrorCode {
    DUPLICATE_NICKNAME(1001, "이미 사용 중인 닉네임입니다."),
    DUPLICATE_EMAIL(1002, "이미 사용 중인 이메일입니다."),
    INVALID_EMAIL_FORMAT(1003, "유효하지 않은 이메일 형식입니다."),
    EXCESSIVE_NICKNAME_LENGTH(1004, "닉네임은 " + MAX_NICKNAME_LENGTH + "자 이하여야 합니다."),
    EXCESSIVE_KEYWORD_LENGTH(1005, "최대 " + MAX_NICKNAME_LENGTH + "자의 키워드로 검색할 수 있습니다."),
    INVALID_PASSWORD_FORMAT(1006, "비밀번호는 8자 이상, 영문, 숫자, 특수문자를 포함해야 합니다."),
    BLANK_PASSWORD(1007, "비밀번호를 입력해주세요."),
    BLANK_NICKNAME(1008, "닉네임을 입력해주세요.");

    private final int code;
    private final String message;

    UserErrorCode(int code, String message) {
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
