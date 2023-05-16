package com.example.demo.config;

import com.example.demo.interceptor.BaseInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author zhouwenyu
 * date 2021-11-16
 */
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    @Bean
    public BaseInterceptor baseInterceptor(){
        return new BaseInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(baseInterceptor()).addPathPatterns("/**")
                .excludePathPatterns("/error/*");
    }
}
