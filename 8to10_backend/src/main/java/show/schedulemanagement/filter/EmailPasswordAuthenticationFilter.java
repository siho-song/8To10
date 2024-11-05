package show.schedulemanagement.filter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Slf4j
public class EmailPasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    public EmailPasswordAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
        setFilterProcessesUrl("/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        UsernamePasswordAuthenticationToken authRequest;
        try {
            authRequest = getAuthRequest(request);
            setDetails(request, authRequest);
        } catch (Exception e) {
            log.error("Error reading login request", e);
            throw new RuntimeException(e);
        }
        return this.getAuthenticationManager().authenticate(authRequest);
    }

    private UsernamePasswordAuthenticationToken getAuthRequest(HttpServletRequest request) {
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        return new UsernamePasswordAuthenticationToken(email, password);
    }
}
