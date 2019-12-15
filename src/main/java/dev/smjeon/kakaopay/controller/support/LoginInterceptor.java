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
        String token = request.getHeader(AUTHORIZATION);

        if (token != null && jwtService.isUsable(token)) {
            return true;
        }

        response.setStatus(401);
        throw new NotAuthorizedException();
    }
}
