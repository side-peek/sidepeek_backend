package sixgaezzang.sidepeek.projects.dto.request;

import jakarta.validation.constraints.NotBlank;

public record MemberSaveRequest(
    Long userId,
    String nickname,
    @NotBlank(message = "멤버 직군을 설정해주세요.")
    String role
) {
}
