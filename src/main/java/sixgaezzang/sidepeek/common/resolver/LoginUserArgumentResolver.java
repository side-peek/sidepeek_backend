package sixgaezzang.sidepeek.common.resolver;

import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import sixgaezzang.sidepeek.common.annotation.Login;
import sixgaezzang.sidepeek.common.exception.InvalidAuthenticationException;

public class LoginUserArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        boolean hasLoginAnnotation = parameter.getParameterAnnotation(Login.class) != null;
        boolean hasLongType = parameter.getParameterType()
            .equals(Long.class);

        return hasLoginAnnotation && hasLongType;
    }

    @Override
    public Object resolveArgument(
        MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest,
        WebDataBinderFactory binderFactory
    ) throws InvalidAuthenticationException {
        Authentication authentication = SecurityContextHolder.getContext()
            .getAuthentication();

        if (authentication != null) {
            Long principal = (Long) authentication.getPrincipal();
            if (principal != null) {
                return principal;
            }
        }

        throw new InvalidAuthenticationException("로그인이 필요합니다.");
    }

}
