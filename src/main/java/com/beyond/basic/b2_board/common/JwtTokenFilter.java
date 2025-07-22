package com.beyond.basic.b2_board.common;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class JwtTokenFilter extends GenericFilter {
    @Value("${jwt.secretKeyAt}")
    private String secretKey;
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        String bearerToken = req.getHeader("Authorization");
        if(bearerToken==null){
//            토큰이 없는 경우 다시 filterChain으로 되돌아가는 로직
            chain.doFilter(request, response);
            return;
        }
        
//        토큰이 있는 경우 토큰 검증 후 Authentication객체 생성
        String token = bearerToken.substring(7);
//        토큰 검증 및 claims 추출
        Claims claims = Jwts.parserBuilder()
                        .setSigningKey(secretKey)
                        .build()
                        .parseClaimsJws(token)
                        .getBody();

        List<GrantedAuthority> authorities = new ArrayList<>();
//        authentication객체를 만들 때 권한은 ROLE_ 라는 키워드를 만들어 주는 것이 추후 문제 발생X
        authorities.add(new SimpleGrantedAuthority("ROLE_"+claims.get("role")));
        Authentication authentication = new UsernamePasswordAuthenticationToken(claims.getSubject(), "", authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(request, response);
    }
}
