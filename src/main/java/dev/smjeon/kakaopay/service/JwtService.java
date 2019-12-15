package dev.smjeon.kakaopay.service;

import dev.smjeon.kakaopay.dto.UserRequestDto;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.Date;

@Service
public class JwtService {
    private static final Logger logger = LoggerFactory.getLogger(JwtService.class);

    public String signUp(UserRequestDto userRequestDto) {
        try {
            return Jwts.builder()
                    .setHeaderParam("typ", "JWT")
                    .setExpiration(new Date(System.currentTimeMillis() + (6 * 60 * 60 * 1000))) // 6 hours
                    .claim("name", userRequestDto.getUserId())
                    .claim("scope", "normal")
                    .signWith(
                            SignatureAlgorithm.HS256,
                            "secret".getBytes("UTF-8"))
                    .compact();
        } catch (UnsupportedEncodingException e) {
            logger.error("Jwt Exception", e);
            throw new IllegalArgumentException();
        }
    }
}
