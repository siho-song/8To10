package show.schedulemanagement.filter;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.security.SignatureException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import show.schedulemanagement.service.auth.MemberDetailsService;
import show.schedulemanagement.utils.TokenUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import org.json.JSONObject;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final MemberDetailsService memberDetailsService;
    private final TokenUtils tokenUtils;

    private static final List<String> EXCLUDE_URLS = Arrays.asList(
            "/",
            "/signup",
            "/error",
            "/login",
            "/static/**",
            "/js/**",
            "/css/**",
            "/templates/**",
            "/images/**",
            "/signup/**"
    );

    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        String requestURI = request.getRequestURI();
        log.info("request.getRequestURI() : {}", requestURI);

        if (shouldExclude(requestURI)) {
            filterChain.doFilter(request, response);
            return;
        }

        Cookie[] cookies = request.getCookies();
        String token = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("jwt".equals(cookie.getName())) {
                    token = cookie.getValue();
                    break;
                }
            }
        }

        try {
            if (token != null && !token.isEmpty()) {
                log.debug("Token found: {}", token);
                if (tokenUtils.isValidToken(token)) {
                    String loginId = tokenUtils.getUserIdFromToken(token);
                    log.debug("[+] loginId Check: {}", loginId);

                    if (loginId != null && !loginId.isEmpty()) {
                        UserDetails userDetails = memberDetailsService.loadUserByUsername(loginId);
                        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                        filterChain.doFilter(request, response);
                    } else {
                        throw new UsernameNotFoundException("User Not Found");
                    }
                } else {
                    throw new JwtException("Invalid JWT token");
                }
            } else {
                throw new JwtException("JWT token not found");
            }
        } catch (Exception e) {
            handleException(response, e);
        }
    }

    private boolean shouldExclude(String uri) {
        return EXCLUDE_URLS.stream().anyMatch(pattern -> pathMatcher.match(pattern, uri));
    }

    private void handleException(HttpServletResponse response, Exception e) throws IOException {
        String logMessage = jsonResponseWrapper(e).getString("message");
        log.error(logMessage, e);

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");

        PrintWriter printWriter = response.getWriter();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("error", true);
        jsonObject.put("message", "로그인 에러");

        printWriter.print(jsonObject);
        printWriter.flush();
        printWriter.close();
    }

    private JSONObject jsonResponseWrapper(Exception e) {
        String resultMessage = getResultMessage(e);

        HashMap<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("status", 401);
        jsonMap.put("code", "401");
        jsonMap.put("message", resultMessage);
        jsonMap.put("reason", e.getMessage());
        JSONObject jsonObject = new JSONObject(jsonMap);
        log.error(resultMessage, e);
        return jsonObject;
    }

    private String getResultMessage(Exception e) {
        if (e instanceof ExpiredJwtException) {
            return "TOKEN Expired";
        } else if (e instanceof SignatureException) {
            return "TOKEN SignatureException Login";
        } else if (e instanceof JwtException) {
            return "TOKEN Parsing JwtException";
        } else {
            return "OTHER ERROR";
        }
    }
}
