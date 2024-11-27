package com.eighttoten.service.auth;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.eighttoten.domain.auth.Auth;
import com.eighttoten.exception.InvalidTokenException;
import com.eighttoten.provider.TokenProvider;
import com.eighttoten.repository.auth.AuthRepository;
import com.eighttoten.utils.BearerAuthorizationUtils;

@ExtendWith(MockitoExtension.class)
@DisplayName("인증 서비스 테스트")
class AuthServiceTest {
    @Mock
    AuthRepository authRepository;

    @Mock
    TokenProvider tokenProvider;

    @Mock
    BearerAuthorizationUtils bearerAuthorizationUtils;

    @InjectMocks
    AuthService authService;

    String refreshToken = "RefreshToken";
    String accessTokenHeader = "Bearer AccessToken";
    String accessToken = "AccessToken";

    @BeforeEach
    void init() {
        when(bearerAuthorizationUtils.extractToken(any())).thenReturn(accessToken);
    }

    @Test
    @DisplayName("Refresh 토큰이 유효하고 Access 토큰이 만료 되었을 때 새로운 AccessToken 을 반환한다.")
    void getRenewalAccessToken() {
        //given
        doNothing().when(tokenProvider).validateRefreshToken(any());
        when(tokenProvider.isExpiredToken(any())).thenReturn(true);

        Auth auth = Auth.from("normal@example.com", null);
        when(authRepository.findByRefreshToken(any())).thenReturn(Optional.of(auth));

        when(tokenProvider.generateAccessToken(any())).thenReturn("New AccessToken");
        //when
        String renewalAccessToken = authService.getRenewalAccessToken(refreshToken, accessToken);

        //then
        verify(tokenProvider, times(1)).validateRefreshToken(refreshToken);
        verify(tokenProvider, times(1)).isExpiredToken(accessToken);
        verify(authRepository, times(1)).findByRefreshToken(refreshToken);
        assertThat(renewalAccessToken).isEqualTo("New AccessToken");
    }

    @Test
    @DisplayName("RefreshToken이 유효하지 않은 경우 예외가 발생한다.")
    void inValidRefreshToken() {
        //given
        doThrow(InvalidTokenException.class).when(tokenProvider).validateRefreshToken(refreshToken);

        //when,then
        assertThatThrownBy(() -> authService.getRenewalAccessToken(refreshToken, accessTokenHeader))
                .isInstanceOf(InvalidTokenException.class);
        verify(tokenProvider,times(1)).validateRefreshToken(refreshToken);
    }

    @Test
    @DisplayName("AccessToken이 유효하지 않은 경우 예외가 발생한다.")
    void inValidAccessToken() {
        //given
        doNothing().when(tokenProvider).validateRefreshToken(any());
        when(tokenProvider.isExpiredToken(accessToken)).thenReturn(false);

        //when,then
        assertThatThrownBy(() -> authService.getRenewalAccessToken(refreshToken, accessToken))
                .isInstanceOf(InvalidTokenException.class);
        verify(tokenProvider, times(1)).validateRefreshToken(refreshToken);
        verify(tokenProvider, times(1)).isExpiredToken(accessToken);
    }

    @Test
    @DisplayName("RefreshToken이 유효하고 ,만료되지 않은 AccessToken인 경우 기존의 AccessToken을 반환한다.")
    void validAccessToken() {
        //given
        doNothing().when(tokenProvider).validateRefreshToken(any());
        when(tokenProvider.isValidToken(accessToken)).thenReturn(true);

        //when
        String renewalAccessToken = authService.getRenewalAccessToken(refreshToken, accessToken);

        //then
        assertThat(renewalAccessToken).isEqualTo(accessToken);
        verify(tokenProvider,times(1)).validateRefreshToken(refreshToken);
        verify(tokenProvider, times(1)).isValidToken(accessToken);
    }
}