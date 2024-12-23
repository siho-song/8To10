package com.eighttoten.infrastructure.security.config;

import com.eighttoten.auth.service.AuthService;
import com.eighttoten.global.utils.BearerAuthorizationUtils;
import com.eighttoten.infrastructure.TokenProvider;
import com.eighttoten.infrastructure.security.filter.EmailPasswordAuthenticationFilter;
import com.eighttoten.infrastructure.security.filter.JwtAuthorizationFilter;
import com.eighttoten.infrastructure.security.handler.AuthFailureHandler;
import com.eighttoten.infrastructure.security.handler.AuthFilterExceptionHandler;
import com.eighttoten.infrastructure.security.handler.AuthSuccessHandler;
import com.eighttoten.infrastructure.security.provider.CustomAuthenticationProvider;
import com.eighttoten.infrastructure.security.service.MemberDetailsService;
import com.eighttoten.member.domain.repository.MemberRepository;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@Slf4j
public class SecurityConfig {

    @Value(value = "${cors.allowed-origin}")
    private String corsAllowedOrigin;

    private static final String[] STATIC_RESOURCES_LOCATION = new String[]{
            "/css/**", "/resources/**",
            "/static/**", "/images/**",
            "/js/**", "/public/**",
            "/favicon.ico"
    };

    @Bean
    public SecurityFilterChain filterChain(
            HttpSecurity http,
            EmailPasswordAuthenticationFilter emailPasswordAuthenticationFilter,
            JwtAuthorizationFilter jwtAuthorizationFilter
    ) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers("/signup/**", "/", "/error", "/renew", "/actuator/**", "/health",
                                "/notification/subscribe")
                        .permitAll()
                        .requestMatchers(STATIC_RESOURCES_LOCATION)
                        .permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthorizationFilter, BasicAuthenticationFilter.class)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .httpBasic(AbstractHttpConfigurer::disable)
                .logout(logout ->
                        logout.logoutUrl("/logout")
                                .logoutSuccessHandler(((request, response, authentication) ->
                                        response.setStatus(HttpServletResponse.SC_OK)
                                ))
                )
                .addFilterBefore(emailPasswordAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addExposedHeader("Authorization");
        config.setAllowedOrigins(Arrays.stream(corsAllowedOrigin.split(","))
                .map(String::trim)
                .toList());
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public EmailPasswordAuthenticationFilter customAuthenticationFilter(
            AuthenticationManager authenticationManager,
            AuthSuccessHandler authSuccessHandler,
            AuthFailureHandler authFailureHandler,
            AuthFilterExceptionHandler authFilterExceptionHandler
    ) {
        EmailPasswordAuthenticationFilter emailPasswordAuthenticationFilter = new EmailPasswordAuthenticationFilter(
                authenticationManager, authFilterExceptionHandler);
        emailPasswordAuthenticationFilter.setAuthenticationSuccessHandler(authSuccessHandler);
        emailPasswordAuthenticationFilter.setAuthenticationFailureHandler(authFailureHandler);
        emailPasswordAuthenticationFilter.afterPropertiesSet();
        return emailPasswordAuthenticationFilter;
    }

    @Bean
    public MemberDetailsService memberDetailsService(MemberRepository memberRepository) {
        return new MemberDetailsService(memberRepository);
    }

    @Bean
    public CustomAuthenticationProvider customAuthenticationProvider(MemberDetailsService memberDetailsService) {
        return new CustomAuthenticationProvider(
                memberDetailsService,
                bCryptPasswordEncoder()
        );
    }

    @Bean
    public AuthenticationManager authenticationManager(CustomAuthenticationProvider customAuthenticationProvider) {
        return new ProviderManager(Collections.singletonList(customAuthenticationProvider));
    }

    @Bean
    public AuthSuccessHandler customAuthSuccessHandler(
            TokenProvider tokenProvider, AuthService authService) {
        return new AuthSuccessHandler(tokenProvider,authService);
    }

    @Bean
    public AuthFailureHandler customAuthFailureHandler(AuthFilterExceptionHandler authFilterExceptionHandler) {
        return new AuthFailureHandler(authFilterExceptionHandler);
    }

    @Bean
    public BearerAuthorizationUtils bearerAuthorizationUtils(){
        return new BearerAuthorizationUtils();
    }

    @Bean
    public JwtAuthorizationFilter jwtAuthorizationFilter(
            MemberDetailsService memberDetailsService,
            TokenProvider tokenProvider,
            BearerAuthorizationUtils bearerAuthorizationUtils,
            AuthFilterExceptionHandler authFilterExceptionHandler)
    {
        return new JwtAuthorizationFilter(
                memberDetailsService,
                bearerAuthorizationUtils,
                tokenProvider,
                authFilterExceptionHandler
        );
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}