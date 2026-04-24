package com.shawn.customer.config;

import com.shawn.customer.interceptor.RequestTimingInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

/**
 * Register Interceptors
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final RequestTimingInterceptor interceptor;

    public WebConfig(RequestTimingInterceptor interceptor) {
        this.interceptor = interceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(interceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/actuator/**");
    }
}