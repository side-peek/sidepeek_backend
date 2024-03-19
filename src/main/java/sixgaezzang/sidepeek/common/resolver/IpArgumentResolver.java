package sixgaezzang.sidepeek.common.resolver;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import sixgaezzang.sidepeek.common.annotation.Ip;
import sixgaezzang.sidepeek.common.exception.InvalidAuthenticationException;

public class IpArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        boolean hasIpAnnotation = parameter.hasParameterAnnotation(Ip.class);
        boolean hasStringType = parameter.getParameterType()
            .equals(String.class);

        return hasIpAnnotation && hasStringType;
    }

    @Override
    public Object resolveArgument(
        MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest,
        WebDataBinderFactory binderFactory
    ) throws InvalidAuthenticationException {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        String ip = request.getHeader("X-Forwarded-For");   // 클라이언트가 서버로 전달되는 IP 주소를 포함

        if (ip != null && !ip.isBlank()) {
            return ip.split(",")[0].trim(); // IP 주소 목록에서 첫 번째 IP 주소인 실제 클라이언트 IP 주소를 반환
        }

        return request.getRemoteAddr(); // 요청의 원격 주소를 가져와 반환
    }
}
