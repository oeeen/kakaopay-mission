package dev.smjeon.kakaopay.service;

import dev.smjeon.kakaopay.controller.support.exception.NotAuthorizedException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
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
    private static final String SECRET = "kakaopay_secret";
    private static final String CHARSET = "UTF-8";
    public static final String PREFIX = "Bearer ";

    public String generateToken(String userId) {
        try {
            return Jwts.builder()
                    .setHeaderParam("typ", "JWT")
                    .setExpiration(new Date(System.currentTimeMillis() + (6 * 60 * 60 * 1000))) // 6 hours
                    .claim("name", userId)
                    .claim("scope", "normal")
                    .signWith(
                            SignatureAlgorithm.HS256,
                            genKey())
                    .compact();
        } catch (UnsupportedEncodingException e) {
            logger.error("Jwt Exception", e);
            throw new IllegalArgumentException();
        }
    }

    public String refreshToken(String headerValue) {
        Jws<Claims> claims = null;
        String token = headerValue.substring(PREFIX.length());
        try {
            claims = Jwts.parser()
                    .setSigningKey(genKey())
                    .parseClaimsJws(token);
        } catch (Exception e) {
            throw new NotAuthorizedException();
        }

        String userId = (String)(claims.getBody().get("name"));
        return generateToken(userId);
    }

    public boolean isUsable(String token) {
        try {
            Jws<Claims> claims = Jwts.parser()
                    .setSigningKey(genKey())
                    .parseClaimsJws(token);
            return true;

        } catch (Exception e) {
            throw new NotAuthorizedException();
        }
    }

    private byte[] genKey() throws UnsupportedEncodingException {
        return SECRET.getBytes(CHARSET);
    }
}
