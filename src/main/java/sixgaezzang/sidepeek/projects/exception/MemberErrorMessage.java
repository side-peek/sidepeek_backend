package sixgaezzang.sidepeek.projects.exception;

import static sixgaezzang.sidepeek.projects.util.ProjectConstant.MAX_MEMBER_COUNT;
import static sixgaezzang.sidepeek.projects.util.ProjectConstant.MAX_ROLE_LENGTH;
import static sixgaezzang.sidepeek.users.domain.User.MAX_NICKNAME_LENGTH;

public class MemberErrorMessage {
    public static final String MEMBERS_OVER_MAX_COUNT = "멤버 수는 " + MAX_MEMBER_COUNT + "명 미만이어야 합니다.";
    public static final String ROLE_OVER_MAX_LENGTH = "멤버의 역할 이름은 " + MAX_ROLE_LENGTH + "자 미만이어야 합니다.";
    public static final String ROLE_IS_NULL = "멤버 역할 이름을 입력해주세요.";
    public static final String USER_ID_OF_FELLOW_MEMBER_IS_NULL = "회원인 멤버의 유저 Id를 입력해주세요.";
    public static final String NON_FELLOW_MEMBER_NICKNAME_IS_NULL = "비회원 멤버 닉네임을 입력해주세요.";
    public static final String NON_FELLOW_MEMBER_NICKNAME_OVER_MAX_LENGTH =
        "비회원 멤버 닉네임은 " + MAX_NICKNAME_LENGTH + "자 미만이어야 합니다.";
}
