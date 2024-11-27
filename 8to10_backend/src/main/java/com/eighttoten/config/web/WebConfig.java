package com.eighttoten.config.web;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@RequiredArgsConstructor
@Slf4j
@Configuration
public class WebConfig implements WebMvcConfigurer {
    public static final String[] CLASSPATH_RESOURCE_LOCATIONS = {
            "classpath:/static/",
            "classpath:/",
            "classpath:/resources/",
            "classpath:/public/",
    };

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**").addResourceLocations(CLASSPATH_RESOURCE_LOCATIONS);
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(memberArgumentResolver());
    }

    @Bean
    public MemberArgumentResolver memberArgumentResolver(){
        return new MemberArgumentResolver();
    }
}