package com.shawn.customer.filter;

import jakarta.servlet.FilterChain;
import org.junit.jupiter.api.Test;
import org.slf4j.MDC;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class RequestLoggingFilterTest {

    private final RequestLoggingFilter filter = new RequestLoggingFilter();

    @Test
    void reusesIncomingCorrelationIdAndClearsMdc() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/customers/C1001");
        request.addHeader("X-Correlation-Id", "corr-123");
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain chain = mock(FilterChain.class);

        filter.doFilter(request, response, chain);

        assertThat(response.getHeader("X-Correlation-Id")).isEqualTo("corr-123");
        assertThat(MDC.get("correlationId")).isNull();
        verify(chain).doFilter(request, response);
    }

    @Test
    void generatesCorrelationIdWhenHeaderIsBlank() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("POST", "/customers/search");
        request.addHeader("X-Correlation-Id", " ");
        MockHttpServletResponse response = new MockHttpServletResponse();

        filter.doFilter(request, response, mock(FilterChain.class));

        assertThat(response.getHeader("X-Correlation-Id")).isNotBlank();
    }
}
