package sixgaezzang.sidepeek.projects.util.validation;

import static sixgaezzang.sidepeek.common.util.ValidationUtils.validateMaxLength;
import static sixgaezzang.sidepeek.common.util.ValidationUtils.validateNotBlank;
import static sixgaezzang.sidepeek.projects.exception.MemberErrorMessage.MEMBERS_OVER_MAX_COUNT;
import static sixgaezzang.sidepeek.projects.exception.MemberErrorMessage.NON_FELLOW_MEMBER_NICKNAME_IS_NULL;
import static sixgaezzang.sidepeek.projects.exception.MemberErrorMessage.NON_FELLOW_MEMBER_NICKNAME_OVER_MAX_LENGTH;
import static sixgaezzang.sidepeek.projects.exception.MemberErrorMessage.ROLE_IS_NULL;
import static sixgaezzang.sidepeek.projects.exception.MemberErrorMessage.ROLE_OVER_MAX_LENGTH;
import static sixgaezzang.sidepeek.projects.exception.MemberErrorMessage.USER_ID_OF_FELLOW_MEMBER_IS_NULL;
import static sixgaezzang.sidepeek.projects.util.ProjectConstant.MAX_MEMBER_COUNT;
import static sixgaezzang.sidepeek.projects.util.ProjectConstant.MAX_ROLE_LENGTH;
import static sixgaezzang.sidepeek.users.domain.User.MAX_NICKNAME_LENGTH;

import io.jsonwebtoken.lang.Assert;
import java.util.List;
import sixgaezzang.sidepeek.projects.dto.request.MemberSaveRequest;
import sixgaezzang.sidepeek.users.domain.User;

public class MemberValidator {

    public static void validateMembers(List<MemberSaveRequest> members) {
        Assert.isTrue(members.size() < MAX_MEMBER_COUNT, MEMBERS_OVER_MAX_COUNT);
    }

    // Required
    public static void validateRole(String role) {
        validateNotBlank(role, ROLE_IS_NULL);
        validateMaxLength(role, MAX_ROLE_LENGTH, ROLE_OVER_MAX_LENGTH);
    }

    // Option
    public static void validateFellowMemberUser(User user) {
        Assert.notNull(user, USER_ID_OF_FELLOW_MEMBER_IS_NULL);
    }

    public static void validateNonFellowMemberNickname(String nickname) {
        validateNotBlank(nickname, NON_FELLOW_MEMBER_NICKNAME_IS_NULL);
        validateMaxLength(nickname, MAX_NICKNAME_LENGTH, NON_FELLOW_MEMBER_NICKNAME_OVER_MAX_LENGTH);
    }

}
