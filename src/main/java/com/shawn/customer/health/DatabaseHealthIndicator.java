//package com.shawn.customer.health;
//
//import org.springframework.boot.actuate.health.Health;
//import org.springframework.boot.actuate.health.HealthIndicator;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.stereotype.Component;
//
///**
// * Custom health indicator to check database connectivity.
// */
//@Component
//public class DatabaseHealthIndicator implements HealthIndicator {
//
//    private final JdbcTemplate jdbcTemplate;
//
//    public DatabaseHealthIndicator(JdbcTemplate jdbcTemplate) {
//        this.jdbcTemplate = jdbcTemplate;
//    }
//
//    @Override
//    public Health health() {
//        try {
//            Integer result = jdbcTemplate.queryForObject("SELECT 1", Integer.class);
//
//            return Health.up()
//                    .withDetail("database", "UP")
//                    .withDetail("dbCheck", result)
//                    .build();
//
//        } catch (Exception e) {
//            return Health.down()
//                    .withDetail("database", "DOWN")
//                    .withDetail("error", e.getMessage())
//                    .build();
//        }
//    }
//}