package com.app.dms.filter;



import com.app.dms.advice.ErrorDetails;
import com.app.dms.advice.JwtTokenException;
import com.app.dms.utility.JwtTokenUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.ServletException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        log.info("JWT Filter");
        final String authorizationHeader = request.getHeader("Authorization");

        String token = null;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7);
        }

        if (token != null && jwtTokenUtil.validateToken(token)) {
            Claims claims = jwtTokenUtil.getClaimsFromToken(token);
            String userId = claims.getId();
            String firstName = claims.get("firstName", String.class);
            String lastName = claims.get("lastName", String.class);

            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(userId, null, Collections.emptyList());
            authenticationToken.setDetails(Map.of("firstName", firstName, "lastName", lastName));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            log.info("JWT Filter test");

        }
        else{
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("error", "Invalid Jwt Token");
            responseBody.put("status", HttpServletResponse.SC_UNAUTHORIZED);
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonResponse = objectMapper.writeValueAsString(responseBody);
            response.getWriter().write(jsonResponse);
            return;
        }
        chain.doFilter(request, response);


    }
}

