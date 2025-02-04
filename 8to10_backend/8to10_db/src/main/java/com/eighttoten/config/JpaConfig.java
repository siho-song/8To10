package com.eighttoten.config;

import com.eighttoten.support.AuditorAwareImpl;
import com.eighttoten.support.CustomAuditorAware;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@RequiredArgsConstructor
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
public class JpaConfig {
    private final CustomAuditorAware<String> customAuditorAware;

    @Bean
    public AuditorAware<String> auditorAware(){
        return new AuditorAwareImpl(customAuditorAware);
    }
}
