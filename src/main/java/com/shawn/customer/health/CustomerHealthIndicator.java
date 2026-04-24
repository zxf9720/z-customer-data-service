package com.shawn.customer.health;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

@Component
public class CustomerHealthIndicator implements HealthIndicator {

    private final JdbcTemplate jdbcTemplate;
    private final RedisConnectionFactory redisConnectionFactory;
    private final String kafkaBootstrapServers;

    public CustomerHealthIndicator(
            JdbcTemplate jdbcTemplate,
            RedisConnectionFactory redisConnectionFactory,
            @Value("${spring.kafka.bootstrap-servers}") String kafkaBootstrapServers
    ) {
        this.jdbcTemplate = jdbcTemplate;
        this.redisConnectionFactory = redisConnectionFactory;
        this.kafkaBootstrapServers = kafkaBootstrapServers;
    }

    @Override
    public Health health() {
        boolean dbUp = checkDatabase();
        boolean redisUp = checkRedis();
        boolean kafkaUp = checkKafka();

        Health.Builder builder = dbUp && redisUp && kafkaUp ? Health.up() : Health.down();

        return builder
                .withDetail("database", dbUp ? "UP" : "DOWN")
                .withDetail("redis", redisUp ? "UP" : "DOWN")
                .withDetail("kafka", kafkaUp ? "UP" : "DOWN")
                .build();
    }

    private boolean checkDatabase() {
        try {
            Integer result = jdbcTemplate.queryForObject("SELECT 1", Integer.class);
            return result != null && result == 1;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean checkRedis() {
        try (RedisConnection connection = redisConnectionFactory.getConnection()) {
            String result = connection.ping();
            return "PONG".equalsIgnoreCase(result);
        } catch (Exception e) {
            return false;
        }
    }

    private boolean checkKafka() {
        try {
            Properties properties = new Properties();
            properties.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaBootstrapServers);
            properties.put(AdminClientConfig.REQUEST_TIMEOUT_MS_CONFIG, "2000");
            properties.put(AdminClientConfig.DEFAULT_API_TIMEOUT_MS_CONFIG, "2000");

            try (AdminClient adminClient = AdminClient.create(properties)) {
                adminClient.describeCluster()
                        .clusterId()
                        .get(2, TimeUnit.SECONDS);
                return true;
            }
        } catch (Exception e) {
            return false;
        }
    }
}