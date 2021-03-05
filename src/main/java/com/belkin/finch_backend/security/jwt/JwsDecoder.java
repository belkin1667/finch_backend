package com.belkin.finch_backend.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.Jwts;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

import javax.crypto.SecretKey;


public class JwsDecoder {

    @Getter @Setter private String token;
    @Getter private Jws<Claims> jwsClaims;

    public JwsDecoder(String token, SecretKey secretKey) {
        this.token = token;
        this.jwsClaims = Jwts.parserBuilder()
                .setSigningKey(secretKey).build()
                .parseClaimsJws(token);
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
