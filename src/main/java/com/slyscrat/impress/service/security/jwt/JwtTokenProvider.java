package com.slyscrat.impress.service.security.jwt;

import com.slyscrat.impress.exception.JwtAuthenticationException;
import com.slyscrat.impress.model.dto.auth.UserRole;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

@Component
@PropertySource("classpath:security.properties")
@RequiredArgsConstructor
public class JwtTokenProvider {
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER = "Bearer";
    private final UserDetailsService userDetailsService;
    private final Set<String> blackList = new HashSet<>();

    @Value("${security.jwt.token.secret-key}")
    private String secretKey;

    @Value("${security.jwt.token.expire-length}")
    private int expireLength;

    @PostConstruct
    void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    public String createToken(String username, List<UserRole> authorities) {
        Claims claims = Jwts.claims().setSubject(username);
        claims.put("roles", authorities.stream().map(UserRole::toString).collect(Collectors.toList()));

        Date currentDate = new Date();
        Date expirationDate = new Date(currentDate.getTime() + expireLength);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(currentDate)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    //@Scheduled(cron = "0 0 0/12 * * ?")
    private void cleanBlackList() {
        Iterator<String> iterator = blackList.iterator();
        while (iterator.hasNext()) {
            String token = iterator.next();
            try {
                Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            } catch (ExpiredJwtException ex) {
                iterator.remove();
            }
        }
    }

    public void invalidateToken(HttpServletRequest request) {
        String token = parseTokenFromRequest(request);
        validateToken(token);
        blackList.add(token);
    }

    String parseTokenFromRequest(HttpServletRequest request) {
        String authorizationHeader = request.getHeader(AUTHORIZATION_HEADER);
        if (authorizationHeader != null && authorizationHeader.startsWith(BEARER) && authorizationHeader.length() > (BEARER.length() + 1)) {
            return authorizationHeader.substring(BEARER.length() + 1);
        }
        return null;
    }

    Authentication getAuthentication(String token) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(parseUsernameFromToken(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    void validateToken(String token) {
        try {
            if (blackList.contains(token)) throw new IllegalArgumentException();
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
        } catch (IllegalArgumentException | MalformedJwtException e) {
            throw new JwtAuthenticationException("JWT token: [" + token + "] is invalid", e);
        } catch (ExpiredJwtException e) {
            throw new JwtAuthenticationException("JWT token: [" + token + "] has expired", e);
        } catch (SignatureException e) {
            throw new JwtAuthenticationException("JWT token: [" + token + "] has failed JWS signature validation", e);
        }
    }

    private String parseUsernameFromToken(String token) {
        validateToken(token);
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }
}
