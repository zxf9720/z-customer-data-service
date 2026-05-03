package com.shawn.customer.config;

import com.shawn.customer.filter.RequestLoggingFilter;
import com.shawn.customer.filter.SimpleRateLimiterFilter;
import org.junit.jupiter.api.Test;
import org.springframework.boot.web.servlet.FilterRegistrationBean;

import static org.assertj.core.api.Assertions.assertThat;

class FilterConfigTest {

    private final FilterConfig config = new FilterConfig();

    @Test
    void registersRequestLoggingFilterFirst() {
        FilterRegistrationBean<RequestLoggingFilter> registration =
                config.requestLoggingFilterRegistration();

        assertThat(registration.getFilter()).isInstanceOf(RequestLoggingFilter.class);
        assertThat(registration.getOrder()).isEqualTo(1);
        assertThat(registration.getUrlPatterns()).containsExactly("/*");
    }

    @Test
    void registersRateLimiterFilterSecond() {
        FilterRegistrationBean<SimpleRateLimiterFilter> registration =
                config.rateLimiterFilterRegistration();

        assertThat(registration.getFilter()).isInstanceOf(SimpleRateLimiterFilter.class);
        assertThat(registration.getOrder()).isEqualTo(2);
        assertThat(registration.getUrlPatterns()).containsExactly("/*");
    }
}
