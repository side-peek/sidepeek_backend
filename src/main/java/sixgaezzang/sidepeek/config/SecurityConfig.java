package sixgaezzang.sidepeek.config;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import sixgaezzang.sidepeek.auth.filter.AuthExceptionHandlerFilter;
import sixgaezzang.sidepeek.auth.filter.JWTValidationFilter;
import sixgaezzang.sidepeek.auth.oauth.handler.OAuth2LoginSuccessHandler;
import sixgaezzang.sidepeek.auth.oauth.service.OAuth2UserServiceImpl;

@Configuration
@EnableWebSecurity
@ConfigurationPropertiesScan
@RequiredArgsConstructor
public class SecurityConfig {

    private final JWTValidationFilter jwtValidationFilter;
    private final OAuth2UserServiceImpl oauth2UserServiceImpl;
    private final AuthExceptionHandlerFilter authExceptionHandlerFilter;
    private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
            .csrf(AbstractHttpConfigurer::disable)
            .formLogin(AbstractHttpConfigurer::disable)
            .headers(AbstractHttpConfigurer::disable)
            .httpBasic(AbstractHttpConfigurer::disable)
            .rememberMe(AbstractHttpConfigurer::disable)
            .logout(AbstractHttpConfigurer::disable)
            .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
            .addFilterBefore(jwtValidationFilter, BasicAuthenticationFilter.class)
            .addFilterBefore(authExceptionHandlerFilter, JWTValidationFilter.class)
            .oauth2Login(oauth2 -> oauth2
                .userInfoEndpoint(userInfoEndpoint -> userInfoEndpoint
                    .userService(oauth2UserServiceImpl))
                .successHandler(oAuth2LoginSuccessHandler)
            );

        return httpSecurity.build();
    }
}
