package sixgaezzang.sidepeek.projects.util.validation;

import static sixgaezzang.sidepeek.common.util.ValidationUtils.validateBlank;
import static sixgaezzang.sidepeek.common.util.ValidationUtils.validateMaxLength;
import static sixgaezzang.sidepeek.projects.util.ProjectConstant.MAX_ROLE_LENGTH;
import static sixgaezzang.sidepeek.users.domain.User.MAX_NICKNAME_LENGTH;

import io.jsonwebtoken.lang.Assert;
import sixgaezzang.sidepeek.users.domain.User;

public class MemberValidator {

    // Required
    public static void validateRole(String role) {
        validateBlank(role, "멤버 역할 이름을 입력해주세요.");
        validateMaxLength(role, MAX_ROLE_LENGTH,
            "멤버의 역할 이름은 " + MAX_ROLE_LENGTH + "자를 넘을 수 없습니다.");
    }

    // Option
    public static void validateFellowMemberUser(User user) {
        Assert.notNull(user, "회원인 멤버의 유저 Id를 입력해주세요.");
    }

    public static void validateNonFellowMemberNickname(String nickname) {
        validateBlank(nickname, "비회원 멤버 닉네임을 입력해주세요.");
        validateMaxLength(nickname, MAX_NICKNAME_LENGTH,
            "비회원 멤버 닉네임은 " + MAX_NICKNAME_LENGTH + "자를 넘을 수 없습니다.");
    }

}
