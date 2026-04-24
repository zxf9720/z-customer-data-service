//package com.shawn.customer.filter;
//
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import java.io.IOException;
//
//@Component
//public class ApiKeyFilter extends OncePerRequestFilter {
//
//    private final String expectedApiKey;
//
//    public ApiKeyFilter(@Value("${security.api-key:dev-api-key}") String expectedApiKey) {
//        this.expectedApiKey = expectedApiKey;
//    }
//
//    @Override
//    protected boolean shouldNotFilter(HttpServletRequest request) {
//        return request.getRequestURI().startsWith("/actuator");
//    }
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request,
//                                    HttpServletResponse response,
//                                    FilterChain filterChain)
//            throws ServletException, IOException {
//
//        String apiKey = request.getHeader("X-API-Key");
//
//        if (!expectedApiKey.equals(apiKey)) {
//            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//            response.getWriter().write("Missing or invalid API key");
//            return;
//        }
//
//        filterChain.doFilter(request, response);
//    }
//}