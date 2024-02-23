package sixgaezzang.sidepeek.projects.dto.request;

import static sixgaezzang.sidepeek.common.util.CommonConstant.MIN_ID;
import static sixgaezzang.sidepeek.projects.util.ProjectConstant.MAX_CATEGORY_LENGTH;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ProjectSkillSaveRequest(
    @Min(value = MIN_ID, message = "스킬 id는 " + MIN_ID + "보다 작을 수 없습니다.")
    @NotNull(message = "스킬 id를 입력해주세요.")
    Long skillId,

    @Size(max = MAX_CATEGORY_LENGTH,
        message = "스킬 카테고리는 " + MAX_CATEGORY_LENGTH + "자를 넘을 수 없습니다.")
    @NotBlank(message = "스킬 카테고리를 입력해주세요.")
    String category
) {
}
