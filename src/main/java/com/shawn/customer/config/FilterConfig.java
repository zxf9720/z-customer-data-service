package com.shawn.customer.config;

//import com.shawn.customer.filter.ApiKeyFilter;
import com.shawn.customer.filter.RequestLoggingFilter;
import com.shawn.customer.filter.SimpleRateLimiterFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    /**
     * 1. Request Logging Filter
     * Executes first to log incoming requests and generate correlationId
     */
    @Bean
    public FilterRegistrationBean<RequestLoggingFilter> requestLoggingFilterRegistration() {
        FilterRegistrationBean<RequestLoggingFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new RequestLoggingFilter());
        registration.setOrder(1);
        registration.addUrlPatterns("/*");
        return registration;
    }

    /**
     * 2. Rate Limiter Filter
     * Executes second to protect the system from excessive requests
     */
    @Bean
    public FilterRegistrationBean<SimpleRateLimiterFilter> rateLimiterFilterRegistration() {
        FilterRegistrationBean<SimpleRateLimiterFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new SimpleRateLimiterFilter());
        registration.setOrder(2);
        registration.addUrlPatterns("/*");
        return registration;
    }

//    /**
//     * 3. API Key Filter
//     * Executes third to validate API key authentication
//     */
//    @Bean
//    public FilterRegistrationBean<ApiKeyFilter> apiKeyFilterRegistration(
//            @Value("${security.api-key:dev-api-key}") String apiKey
//    ) {
//        FilterRegistrationBean<ApiKeyFilter> registration = new FilterRegistrationBean<>();
//        registration.setFilter(new ApiKeyFilter(apiKey));
//        registration.setOrder(3);
//        registration.addUrlPatterns("/*");
//        return registration;
//    }
}