package sixgaezzang.sidepeek.projects.util.validation;

import static sixgaezzang.sidepeek.common.util.validation.ValidationUtils.validateCollection;
import static sixgaezzang.sidepeek.common.util.validation.ValidationUtils.validateInclude;
import static sixgaezzang.sidepeek.common.util.validation.ValidationUtils.validateKeyDuplicate;
import static sixgaezzang.sidepeek.common.util.validation.ValidationUtils.validateMaxLength;
import static sixgaezzang.sidepeek.common.util.validation.ValidationUtils.validateNotBlank;
import static sixgaezzang.sidepeek.projects.exception.message.MemberErrorMessage.MEMBER_IS_DUPLICATED;
import static sixgaezzang.sidepeek.projects.exception.message.MemberErrorMessage.MEMBER_IS_EMPTY;
import static sixgaezzang.sidepeek.projects.exception.message.MemberErrorMessage.MEMBER_NOT_INCLUDE_OWNER;
import static sixgaezzang.sidepeek.projects.exception.message.MemberErrorMessage.MEMBER_OVER_MAX_COUNT;
import static sixgaezzang.sidepeek.projects.exception.message.MemberErrorMessage.ROLE_IS_NULL;
import static sixgaezzang.sidepeek.projects.exception.message.MemberErrorMessage.ROLE_OVER_MAX_LENGTH;
import static sixgaezzang.sidepeek.projects.util.ProjectConstant.MAX_MEMBER_COUNT;
import static sixgaezzang.sidepeek.projects.util.ProjectConstant.MAX_ROLE_LENGTH;
import static sixgaezzang.sidepeek.users.util.validation.UserValidator.validateNickname;

import io.jsonwebtoken.lang.Assert;
import java.util.List;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import sixgaezzang.sidepeek.projects.dto.request.SaveMemberRequest;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MemberValidator {

    public static void validateMembers(Long ownerId, List<SaveMemberRequest> members) {
        Assert.notEmpty(members, MEMBER_IS_EMPTY);
        Assert.isTrue(members.size() <= MAX_MEMBER_COUNT, MEMBER_OVER_MAX_COUNT);

        validateInclude(members, (member, id) -> Objects.equals(member.userId(), id), ownerId,
            MEMBER_NOT_INCLUDE_OWNER);
        validateCollection(members, MemberValidator::validateSaveMemberRequest);
        validateKeyDuplicate(members, (member) -> member.nickname() + " " + member.role(), MEMBER_IS_DUPLICATED);
    }

    // Required
    public static void validateSaveMemberRequest(SaveMemberRequest member) {
        validateNickname(member.nickname());
        validateRole(member.role());
    }

    public static void validateRole(String role) {
        validateNotBlank(role, ROLE_IS_NULL);
        validateMaxLength(role, MAX_ROLE_LENGTH, ROLE_OVER_MAX_LENGTH);
    }

}
