package com.shawn.customer.filter;

import jakarta.servlet.FilterChain;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

class SimpleRateLimiterFilterTest {

    private final SimpleRateLimiterFilter filter = new SimpleRateLimiterFilter();

    @Test
    void allowsRequestsUnderLimitUsingForwardedClientIp() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/customers/C1001");
        request.addHeader("X-Forwarded-For", "203.0.113.10, 10.0.0.5");
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain chain = mock(FilterChain.class);

        filter.doFilter(request, response, chain);

        assertThat(response.getStatus()).isEqualTo(200);
        verify(chain).doFilter(request, response);
    }

    @Test
    void rejectsRequestsAfterMinuteLimit() throws Exception {
        FilterChain chain = mock(FilterChain.class);
        MockHttpServletResponse lastResponse = null;

        for (int i = 0; i < 61; i++) {
            MockHttpServletRequest request = new MockHttpServletRequest("GET", "/customers/C1001");
            request.setRemoteAddr("198.51.100.20");
            lastResponse = new MockHttpServletResponse();

            filter.doFilter(request, lastResponse, chain);
        }

        assertThat(lastResponse.getStatus()).isEqualTo(429);
        assertThat(lastResponse.getContentAsString()).isEqualTo("Too many requests");
        verify(chain, times(60)).doFilter(
                org.mockito.ArgumentMatchers.any(),
                org.mockito.ArgumentMatchers.any()
        );
    }

    @Test
    void doesNotRateLimitActuatorRequests() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/actuator/health");
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain chain = mock(FilterChain.class);

        filter.doFilter(request, response, chain);

        assertThat(response.getStatus()).isEqualTo(200);
        verify(chain).doFilter(request, response);
    }
}
