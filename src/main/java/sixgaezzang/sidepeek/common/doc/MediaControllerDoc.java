package sixgaezzang.sidepeek.common.doc;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import sixgaezzang.sidepeek.media.dto.response.MediaUploadResponse;

@Tag(name = "Media", description = "미디어(파일 업로드) API")
public interface MediaControllerDoc {

    @Operation(summary = "파일 업로드")
    @ApiResponse(responseCode = "200", description = "파일 업로드 성공")
    @ApiResponse(responseCode = "400",
        description = "요청이 잘못된 경우(파일이 없거나 비어있는 경우. 이미지/비디오 형식이 아닌 경우. 최대 용량(100MB)를 넘은 경우.)",
        content = @Content)
    @ApiResponse(responseCode = "401", description = "로그인을 하지 않은 경우", content = @Content)
    @ApiResponse(responseCode = "5XX", description = "파일 업로드 중 그 외 예외가 발생한 경우", content = @Content)
    ResponseEntity<MediaUploadResponse> uploadFile(
        @Parameter(name = "loginId", description = "로그인한 회원 식별자", in = ParameterIn.HEADER) Long loginId,
        @Parameter(name = "file", description = "업로드할 이미지 파일") MultipartFile file
    );

}
