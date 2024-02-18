package sixgaezzang.sidepeek.users.dto.request;

import jakarta.validation.constraints.Email;

public record CheckEmailRequest(
    @Email
    String email
) {

}
