package sixgaezzang.sidepeek.common.doc.description;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ResponseCodeDescription {
    public static final String OK_DESCRIPTION = "요청 성공 후 Body와 함께 응답";
    public static final String CREATED_DESCRIPTION = "생성 요청 성공 후 Location(Header), Body(옵션)와 함께 응답";
    public static final String NO_CONTENT_DESCRIPTION = "요청 성공 후 Body 없이 응답";
    public static final String BAD_REQUEST_DESCRIPTION =
        "요청 형식이 잘못되었거나, 요청 값이 유효하지 않은 경우 메시지와 응답(여러 필드 값이 유효하지 않은 경우, List로 응답)";
    public static final String BAD_REQUEST_DESCRIPTION1 = "Example1: 하나의 원인에 대한 오류";
    public static final String BAD_REQUEST_DESCRIPTION2 = "Example2: 여러 원인에 대한 오류";
    public static final String UNAUTHORIZED_DESCRIPTION = "로그인이 필요한 요청에서 로그인하지 않고 요청한 경우";
    public static final String FORBIDDEN_DESCRIPTION = "로그인했지만 요청 권한이 없는 경우";
    public static final String NOT_FOUND_DESCRIPTION = "요청한 자원이 없는 자원인 경우";
    public static final String CONFLICT_DESCRIPTION = "같은 요청을 다시 요청한 경우(ex. 좋아요 등록 재요청)";
    public static final String INTERNAL_SERVER_ERROR_DESCRIPTION =
        "서버에서 예측하지 못한 예외, 외부 시스템 문제가 발생한 경우";
}
