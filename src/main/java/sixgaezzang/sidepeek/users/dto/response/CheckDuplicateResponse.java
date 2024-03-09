package sixgaezzang.sidepeek.users.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "중복 확인 응답")
public record CheckDuplicateResponse(
    @Schema(description = "중복 여부", example = "true")
    boolean isDuplicated
) {

}
