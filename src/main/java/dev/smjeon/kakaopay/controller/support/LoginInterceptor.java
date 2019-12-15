package dev.smjeon.kakaopay.controller.support;

import dev.smjeon.kakaopay.controller.support.exception.NotAuthorizedException;
import dev.smjeon.kakaopay.service.JwtService;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class LoginInterceptor implements HandlerInterceptor {
    private static final String AUTHORIZATION = "Authorization";
    private final JwtService jwtService;

    public LoginInterceptor(final JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String headerValue = request.getHeader(AUTHORIZATION);

        if (headerValue != null && headerValue.contains("Bearer")) {
            String refreshToken = jwtService.refreshToken(headerValue);
            response.setHeader(AUTHORIZATION, refreshToken);
            return true;
        }

        if (headerValue != null && jwtService.isUsable(headerValue)) {
            return true;
        }

        response.setStatus(401);
        throw new NotAuthorizedException();
    }
}
