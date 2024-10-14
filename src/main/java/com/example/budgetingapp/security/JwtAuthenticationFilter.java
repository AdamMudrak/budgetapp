package com.example.budgetingapp.security;

import static com.example.budgetingapp.constants.security.SecurityConstants.ACCESS;
import static com.example.budgetingapp.constants.security.SecurityConstants.BEGIN_INDEX;

import com.example.budgetingapp.constants.controllers.AuthControllerConstants;
import com.example.budgetingapp.security.jwtutils.abstr.JwtAbstractUtil;
import com.example.budgetingapp.security.jwtutils.strategy.JwtStrategy;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final UserDetailsService userDetailsService;
    private final JwtAbstractUtil jwtAbstractUtil;

    public JwtAuthenticationFilter(@Autowired JwtStrategy jwtStrategy,
            @Autowired UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
        this.jwtAbstractUtil = jwtStrategy.getStrategy(ACCESS);
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        String token = getToken(request);

        if (token != null && jwtAbstractUtil.isValidToken(token)) {
            String username = jwtAbstractUtil.getUsername(token);
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities()
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }

    private String getToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.hasText(bearerToken)
                && bearerToken.startsWith(AuthControllerConstants.BEARER)) {
            return bearerToken.substring(BEGIN_INDEX);
        }
        return null;
    }
}
