package show.schedulemanagement.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import java.io.IOException;
import show.schedulemanagement.exception.UserAuthenticationException;

@Slf4j
public class AuthFailureHandler implements AuthenticationFailureHandler {

    private final AuthFilterExceptionHandler authFilterExceptionHandler;

    public AuthFailureHandler(AuthFilterExceptionHandler authFilterExceptionHandler) {
        this.authFilterExceptionHandler = authFilterExceptionHandler;
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
        authFilterExceptionHandler.handleException(response, exception);
    }
}
