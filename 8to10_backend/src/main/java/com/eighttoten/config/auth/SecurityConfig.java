package com.eighttoten.config.auth;

import java.util.Collections;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import com.eighttoten.filter.EmailPasswordAuthenticationFilter;
import com.eighttoten.filter.JwtAuthorizationFilter;
import com.eighttoten.handler.AuthFailureHandler;
import com.eighttoten.handler.AuthFilterExceptionHandler;
import com.eighttoten.handler.AuthSuccessHandler;
import com.eighttoten.provider.CustomAuthenticationProvider;
import com.eighttoten.provider.TokenProvider;
import com.eighttoten.repository.member.MemberRepository;
import com.eighttoten.service.auth.AuthService;
import com.eighttoten.service.auth.MemberDetailsService;
import com.eighttoten.utils.BearerAuthorizationUtils;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@Slf4j
public class SecurityConfig {

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
                        .requestMatchers("/signup/**", "/", "/error","/renew","/actuator/**")
                        .permitAll()
                        .requestMatchers(STATIC_RESOURCES_LOCATION)
                        .permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthorizationFilter, BasicAuthenticationFilter.class)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .httpBasic(AbstractHttpConfigurer::disable)
                .logout(logout->
                        logout.logoutUrl("/logout")
                                .logoutSuccessUrl("/")
                                .deleteCookies("refresh_token")
                )
                .addFilterBefore(emailPasswordAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addExposedHeader("Authorization");
        config.addAllowedOrigin("http://localhost:3000");
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