package sixgaezzang.sidepeek.projects.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Builder;
import sixgaezzang.sidepeek.users.dto.response.UserSummary;

@Schema(description = "프로젝트 멤버 정보")
@Builder
public record MemberSummary(
    @Schema(description = "프로젝트 멤버 역할", example = "백엔드")
    String role,
    @Schema(description = "프로젝트 멤버 회원/비회원 상세 정보")
    List<UserSummary> userSummary
) {

    public static MemberSummary of(String role, List<UserSummary> members) {
        return MemberSummary.builder()
            .role(role)
            .userSummary(members)
            .build();
    }

}
