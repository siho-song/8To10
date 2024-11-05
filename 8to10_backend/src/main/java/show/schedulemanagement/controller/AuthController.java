package show.schedulemanagement.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import show.schedulemanagement.dto.auth.AccessTokenResponse;
import show.schedulemanagement.service.auth.AuthService;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @GetMapping(value = "/renew")
    public ResponseEntity<AccessTokenResponse> renewAccessToken(
            @CookieValue(name = "refresh_token") String refreshToken,
            @RequestHeader("Authorization") String accessTokenHeader)
    {

        String renewAccessToken = authService.getRenewalAccessToken(refreshToken, accessTokenHeader);
        return ResponseEntity.ok(AccessTokenResponse.from(renewAccessToken));
    }
}
