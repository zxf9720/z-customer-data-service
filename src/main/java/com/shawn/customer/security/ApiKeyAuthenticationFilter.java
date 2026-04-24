//package com.shawn.customer.security;
//
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.security.authentication.AbstractAuthenticationToken;
//import org.springframework.security.core.authority.AuthorityUtils;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import java.io.IOException;
//
///**
// * Authenticates internal service requests using an API key header.
// */
//public class ApiKeyAuthenticationFilter extends OncePerRequestFilter {
//
//    private static final String API_KEY_HEADER = "X-API-Key";
//
//    private final String expectedApiKey;
//
//    public ApiKeyAuthenticationFilter(String expectedApiKey) {
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
//        String apiKey = request.getHeader(API_KEY_HEADER);
//
//        if (apiKey == null || !apiKey.equals(expectedApiKey)) {
//            SecurityContextHolder.clearContext();
//            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//            response.getWriter().write("Missing or invalid API key");
//            return;
//        }
//
//        ApiKeyAuthenticationToken authentication =
//                new ApiKeyAuthenticationToken(apiKey);
//
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//
//        filterChain.doFilter(request, response);
//    }
//
//    private static class ApiKeyAuthenticationToken extends AbstractAuthenticationToken {
//
//        private final String apiKey;
//
//        private ApiKeyAuthenticationToken(String apiKey) {
//            super(AuthorityUtils.createAuthorityList("ROLE_INTERNAL_SERVICE"));
//            this.apiKey = apiKey;
//            setAuthenticated(true);
//        }
//
//        @Override
//        public Object getCredentials() {
//            return apiKey;
//        }
//
//        @Override
//        public Object getPrincipal() {
//            return "internal-service";
//        }
//    }
//}