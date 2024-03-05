package sixgaezzang.sidepeek.projects.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import sixgaezzang.sidepeek.projects.domain.member.Member;
import sixgaezzang.sidepeek.users.domain.User;
import sixgaezzang.sidepeek.users.dto.response.UserSummary;

@Schema(description = "프로젝트 멤버 정보")
@Builder
public record MemberSummary(
    @Schema(description = "프로젝트 멤버 식별자", example = "1")
    Long id,
    @Schema(description = "프로젝트 멤버 역할", example = "백엔드")
    String role,
    @Schema(description = "프로젝트 멤버 회원/비회원 상세정보")
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
