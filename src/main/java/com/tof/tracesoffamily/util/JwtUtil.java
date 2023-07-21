package com.tof.tracesoffamily.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Slf4j
public class JwtUtil {
    public static String getUserName(String token, String secretKey){
        return Jwts.parserBuilder().setSigningKey(getSigninKey(secretKey)).build()
                .parseClaimsJws(token).getBody().get("userName", String.class);
    }
    public static boolean isExpired(String token, String secretKey){
        return Jwts.parserBuilder().setSigningKey(getSigninKey(secretKey)).build()
                .parseClaimsJws(token).getBody().getExpiration().before(new Date()); //지금보다 전이면 expired해야함
    }
    public static String createJwt(String userName, String secretKey, Long expiredMs){
        Claims claims = Jwts.claims();
        claims.put("userName", userName);
        return Jwts.builder()
                .setClaims(claims)  // claims 정보 현재는 username
                .setIssuedAt(new Date(System.currentTimeMillis())) //  언제 발생
                .setExpiration(new Date(System.currentTimeMillis() + expiredMs)) // 언제 만료
                .signWith(getSigninKey(secretKey), SignatureAlgorithm.HS256) // 어떤 알고리즘
                .compact();
    }

    private static Key getSigninKey(String secretKey) {
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
