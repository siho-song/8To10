package com.eighttoten.filter;

import com.eighttoten.exception.AuthException;
import com.eighttoten.exception.ExceptionCode;
import com.eighttoten.handler.AuthFilterExceptionHandler;
import com.eighttoten.provider.TokenProvider;
import com.eighttoten.service.auth.MemberDetailsService;
import com.eighttoten.utils.BearerAuthorizationUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {
    private final MemberDetailsService memberDetailsService;
    private final BearerAuthorizationUtils bearerUtils;
    private final TokenProvider tokenProvider;
    private final AuthFilterExceptionHandler authFilterExceptionHandler;

    private static final List<String> EXCLUDE_URLS = Arrays.asList(
            "/",
            "/signup",
            "/error",
            "/login",
            "/renew",
            "/static/**",
            "/templates/**",
            "/images/**",
            "/signup/**",
            "/public/**",
            "/actuator/**",
            "/favicon.ico"
    );

    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        String requestURI = request.getRequestURI();

        if (isExcludeUrl(requestURI)) {
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader("Authorization");
        try {
            String token = bearerUtils.extractToken(authHeader);
            if(tokenProvider.isValidToken(token)){
                String loginId = tokenProvider.getUserIdFromToken(token);
                UserDetails userDetails = memberDetailsService.loadUserByUsername(loginId);
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
                filterChain.doFilter(request, response);
            }
            else {
                throw new AuthException(ExceptionCode.INVALID_ACCESS_TOKEN);
            }
        } catch (AuthenticationException e) {
            authFilterExceptionHandler.handleException(response,e);
        }
    }

    private boolean isExcludeUrl(String uri) {
        return EXCLUDE_URLS.stream().anyMatch(pattern -> pathMatcher.match(pattern, uri));
    }
}