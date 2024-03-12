package sixgaezzang.sidepeek.projects.exception.message;

import static sixgaezzang.sidepeek.common.util.CommonConstant.MIN_ID;
import static sixgaezzang.sidepeek.projects.util.ProjectConstant.MAX_MEMBER_COUNT;
import static sixgaezzang.sidepeek.projects.util.ProjectConstant.MAX_ROLE_LENGTH;
import static sixgaezzang.sidepeek.users.util.UserConstant.MAX_NICKNAME_LENGTH;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class MemberErrorMessage {

    // Member List
    public static final String MEMBER_OVER_MAX_COUNT = "멤버 수는 " + MAX_MEMBER_COUNT + "명 미만이어야 합니다.";
    public static final String MEMBER_IS_EMPTY = "멤버 목록에는 최소 1명(작성자)을 포함해야합니다.";
    public static final String MEMBER_NOT_INCLUDE_OWNER = "멤버 목록에 작성자를 필수로 포함해야합니다.";
    public static final String MEMBER_IS_DUPLICATED = "같은 역할 내에 같은 멤버가 있습니다.";

    // Member
    public static final String ROLE_OVER_MAX_LENGTH =
        "멤버의 역할 이름은 " + MAX_ROLE_LENGTH + "자 미만이어야 합니다.";
    public static final String ROLE_IS_NULL = "멤버 역할 이름을 입력해주세요.";
    public static final String USER_ID_OF_FELLOW_MEMBER_IS_NULL = "회원인 멤버의 유저 Id를 입력해주세요.";
    public static final String NON_FELLOW_MEMBER_NICKNAME_IS_NULL = "비회원 멤버 닉네임을 입력해주세요.";
    public static final String NON_FELLOW_MEMBER_NICKNAME_OVER_MAX_LENGTH =
        "비회원 멤버 닉네임은 " + MAX_NICKNAME_LENGTH + "자 미만이어야 합니다.";
    public static final String MEMBER_IS_INVALID = "회원인 멤버는 유저 Id를, 비회원인 멤버는 닉네임을 입력해주세요.";
    public static final String MEMBER_ID_NEGATIVE_OR_ZERO = "멤버 id는 " + MIN_ID + "보다 작을 수 없습니다.";
}
