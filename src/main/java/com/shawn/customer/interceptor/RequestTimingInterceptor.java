package com.shawn.customer.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * Simple interceptor to measure request processing time.
 */
@Component
public class RequestTimingInterceptor implements HandlerInterceptor {

    private static final String START_TIME = "startTime";

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) {

        request.setAttribute(START_TIME, System.currentTimeMillis());
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response,
                                Object handler,
                                Exception ex) {

        Long startTime = (Long) request.getAttribute(START_TIME);

        if (startTime != null) {
            long duration = System.currentTimeMillis() - startTime;

            System.out.println("Request processed: " +
                    request.getMethod() + " " +
                    request.getRequestURI() +
                    " | durationMs=" + duration);
        }
    }
}