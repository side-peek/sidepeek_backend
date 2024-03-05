package sixgaezzang.sidepeek.projects.dto.response;

import lombok.Builder;
import sixgaezzang.sidepeek.projects.domain.member.Member;
import sixgaezzang.sidepeek.users.domain.User;
import sixgaezzang.sidepeek.users.dto.response.UserSummary;

@Builder
public record MemberSummary(
    Long id,
    String role,
    UserSummary userSummary
) {

    public static MemberSummary from(Member member) {
        User user = member.getUser();
        UserSummary userSummary = (user == null)
            ? UserSummary.from(member.getNickname())
            : UserSummary.from(user);
        return MemberSummary.from(member, userSummary);
    }

    public static MemberSummary from(Member member, UserSummary userSummary) {
        return MemberSummary.builder()
            .id(member.getId())
            .role(member.getRole())
            .userSummary(userSummary)
            .build();
    }

}
