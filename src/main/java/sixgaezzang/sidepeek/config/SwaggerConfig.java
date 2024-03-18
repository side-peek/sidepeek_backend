package sixgaezzang.sidepeek.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        String authenticationScheme = "Access Token";
        SecurityRequirement securityRequirement = new SecurityRequirement().addList(
            authenticationScheme);
        Components components = new Components().addSecuritySchemes(authenticationScheme,
            createAPIKeyScheme());

        return new OpenAPI()
            .info(apiInfo())
            .addSecurityItem(securityRequirement)
            .components(components);
    }

    private Info apiInfo() {
        return new Info()
            .title("Sidepeek API")
            .description("사이드픽 API 명세서 \uD83D\uDC40")
            .version("v1");
    }

    private SecurityScheme createAPIKeyScheme() {
        return new SecurityScheme()
            .type(SecurityScheme.Type.HTTP)
            .bearerFormat("JWT")
            .scheme("bearer")
            .in(SecurityScheme.In.HEADER)
            .name("Authorization");
    }
}
