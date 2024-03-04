package sixgaezzang.sidepeek.projects.util.validation;

import static sixgaezzang.sidepeek.common.util.ValidationUtils.validateMaxLength;
import static sixgaezzang.sidepeek.common.util.ValidationUtils.validateNotBlank;
import static sixgaezzang.sidepeek.projects.exception.message.MemberErrorMessage.MEMBER_NOT_INCLUDE_OWNER;
import static sixgaezzang.sidepeek.projects.exception.message.MemberErrorMessage.MEMBER_OVER_MAX_COUNT;
import static sixgaezzang.sidepeek.projects.exception.message.MemberErrorMessage.NON_FELLOW_MEMBER_NICKNAME_IS_NULL;
import static sixgaezzang.sidepeek.projects.exception.message.MemberErrorMessage.NON_FELLOW_MEMBER_NICKNAME_OVER_MAX_LENGTH;
import static sixgaezzang.sidepeek.projects.exception.message.MemberErrorMessage.ROLE_IS_NULL;
import static sixgaezzang.sidepeek.projects.exception.message.MemberErrorMessage.ROLE_OVER_MAX_LENGTH;
import static sixgaezzang.sidepeek.projects.exception.message.MemberErrorMessage.USER_ID_OF_FELLOW_MEMBER_IS_NULL;
import static sixgaezzang.sidepeek.projects.util.ProjectConstant.MAX_MEMBER_COUNT;
import static sixgaezzang.sidepeek.projects.util.ProjectConstant.MAX_ROLE_LENGTH;
import static sixgaezzang.sidepeek.users.util.UserConstant.MAX_NICKNAME_LENGTH;

import io.jsonwebtoken.lang.Assert;
import java.util.List;
import java.util.Objects;
import sixgaezzang.sidepeek.projects.dto.request.MemberSaveRequest;
import sixgaezzang.sidepeek.users.domain.User;

public class MemberValidator {

    public static void validateMembers(Long ownerId, List<MemberSaveRequest> members) {
        Assert.isTrue(members.size() < MAX_MEMBER_COUNT, MEMBER_OVER_MAX_COUNT);
        validateIncludeOwner(ownerId, members);
    }

    public static void validateIncludeOwner(Long ownerId, List<MemberSaveRequest> members) {
        members.stream().filter(member -> Objects.equals(member.userId(), ownerId))
            .findAny()
            .orElseThrow(() -> new IllegalArgumentException(MEMBER_NOT_INCLUDE_OWNER));
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
