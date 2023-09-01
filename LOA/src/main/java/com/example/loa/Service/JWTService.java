package com.example.loa.Service;

import java.time.Duration;
import java.util.Date;
import io.jsonwebtoken.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;

public class JWTService {
    public String makeJwtToken(Integer id, String charName) {
        Date now = new Date();

        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE) // 헤더 타입 지정
                .setIssuer("redpanda") // issuer 설정
                .setIssuedAt(now) // 발급 시간 설정 (Date 객체만 가능)
                .setExpiration(new Date(now.getTime() + Duration.ofMinutes(720).toMillis())) // 만료 시 설정 (Date 객체만 가능)
                .claim("id", id)
                .claim("mainChar", charName) // 비공개 클레임 설정 가능 (value 값이 null이면 payload에 저장되지 않음)
                .signWith(SignatureAlgorithm.HS256, "secret") // 시크릿 키 설정
                .compact();
    }


    private Claims parseJwtToken(String authorizationHeader) {
        validationAuthorizationHeader(authorizationHeader); // (1)
        String token = extractToken(authorizationHeader); // (2)

        return Jwts.parser()
                .setSigningKey("secret") // (3)
                .parseClaimsJws(token) // (4)
                .getBody();
    }

    // JWT 토큰 확인
    public Claims checkAuthorizationHeader (HttpServletRequest request){
        try{
            String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
            String token = authorizationHeader.split(" ")[1];
            if(token.equals("null")){
                return null;
            }
            return parseJwtToken(authorizationHeader);
        }catch(ExpiredJwtException e) {
            return null;
        }
    }


    private void validationAuthorizationHeader(String header) {
        if (header == null || !header.startsWith("Bearer ")) {
            throw new IllegalArgumentException();
        }
    }

    private String extractToken(String authorizationHeader) {
        return authorizationHeader.substring("Bearer ".length());
    }

    public Claims jwtCheckFunc(HttpServletRequest request){
        Claims token = checkAuthorizationHeader(request);
        if(token == null){
            System.out.println("[Error] Token is Missed");
            return null;
        }
        return token;
    }
}
