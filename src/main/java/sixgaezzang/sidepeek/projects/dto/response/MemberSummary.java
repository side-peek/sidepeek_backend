package sixgaezzang.sidepeek.projects.dto.response;

import lombok.Builder;
import sixgaezzang.sidepeek.projects.domain.member.Member;
import sixgaezzang.sidepeek.users.dto.response.UserSummary;

@Builder
public record MemberSummary(
    Long id,
    String category,
    UserSummary userSummary
) {

    public static MemberSummary from(Member member, UserSummary userSummary) {
        return MemberSummary.builder()
            .id(member.getId())
            .category(member.getRole())
            .userSummary(userSummary)
            .build();
    }

}
