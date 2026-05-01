package com.shawn.customer.interceptor;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.assertj.core.api.Assertions.assertThat;

class RequestTimingInterceptorTest {

    private final RequestTimingInterceptor interceptor = new RequestTimingInterceptor();

    @Test
    void preHandleStoresStartTimeAndAllowsRequest() {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/customers/C1001");

        boolean allowed = interceptor.preHandle(request, new MockHttpServletResponse(), new Object());

        assertThat(allowed).isTrue();
        assertThat(request.getAttribute("startTime")).isInstanceOf(Long.class);
    }

    @Test
    void afterCompletionToleratesMissingStartTime() {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/customers/C1001");

        interceptor.afterCompletion(request, new MockHttpServletResponse(), new Object(), null);

        assertThat(request.getAttribute("startTime")).isNull();
    }
}
