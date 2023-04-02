package com.dv.Lokana.security;

import com.dv.Lokana.entitys.User;
import com.dv.Lokana.service.CustomUserDetailsService;
import io.jsonwebtoken.io.Serializer;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;


@RequiredArgsConstructor
@Component
public class JWTAutenticationFilter extends OncePerRequestFilter {

    public static final Logger LOG = LoggerFactory.getLogger(JWTAutenticationFilter.class);

    private final JWTToken jwtToken;
    private final CustomUserDetailsService customUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
       try {
           String jwt = getJWTFromRequest(request);
           if (StringUtils.hasText(jwt) && jwtToken.validateToken(jwt)) {
               Long userId = jwtToken.getUserIdIsToken(jwt);
               User userDetails = customUserDetailsService.loadUserById(userId);

               UsernamePasswordAuthenticationToken aut = new UsernamePasswordAuthenticationToken(
                       userDetails,
                       null,
                       Collections.emptyList()
               );

               aut.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
               SecurityContextHolder.getContext().setAuthentication(aut);
           }
       }catch (Exception ex) {
           LOG.error("Could not set user authentication");
       }
       filterChain.doFilter(request, response);
    }

    private String getJWTFromRequest(HttpServletRequest request) {
        String token = request.getHeader(SecurityConstants.HEADER_STRING);

        if (StringUtils.hasText(token) && token.startsWith(SecurityConstants.TOKEN_PREFIX)) {
            return token.split(" ")[1];
        }
        return null;
    }
}
