package com.belkin.finch_backend.security.jwt;

import com.belkin.finch_backend.security.exception.JwtTokenCanNotBeTrustedException;
import io.jsonwebtoken.*;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.SecretKey;

@Slf4j
public class JwsDecoder {

    @Getter @Setter private String token;
    @Getter private Jws<Claims> jwsClaims;
    private SecretKey secretKey;

    public JwsDecoder(String token, SecretKey secretKey) {
        this.token = token;
        this.secretKey = secretKey;
    }

    public JwsDecoder decode() {
        log.info("Decoding JWS token...");

        try {
            this.jwsClaims = Jwts.parserBuilder()
                    .setSigningKey(secretKey).build()
                    .parseClaimsJws(token);
        } catch (IllegalArgumentException | JwtException e) {
            throw new JwtTokenCanNotBeTrustedException(token);
        }
        return this;
    }

    public Claims getBody() {
        return getJwsClaims().getBody();
    }

    public JwsHeader getHeader() {
        return getJwsClaims().getHeader();
    }

    public String getSignature() {
        return getJwsClaims().getSignature();
    }

}
