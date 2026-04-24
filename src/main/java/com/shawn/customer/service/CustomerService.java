package com.shawn.customer.service;

import com.shawn.customer.dto.CustomerProfileResponse;
import com.shawn.customer.dto.CustomerSearchRequest;
import com.shawn.customer.entity.CustomerEntity;
import com.shawn.customer.repository.CustomerJpaRepository;
import com.shawn.customer.repository.spec.CustomerSpecifications;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
public class CustomerService {

    private final CustomerJpaRepository customerJpaRepository;

    private final Map<String, CustomerProfileResponse> mockCustomers = Map.of(
            "C9999", new CustomerProfileResponse(
                    "C9999",
                    "Mock User",
                    40,
                    100000,
                    "MEDIUM",
                    "BALANCED",
                    "VERIFIED"
            )
    );

    public CustomerService(CustomerJpaRepository customerJpaRepository) {
        this.customerJpaRepository = customerJpaRepository;
    }

    public CustomerProfileResponse getCustomerProfile(String customerId) {
        return customerJpaRepository.findById(customerId)
                .map(this::toResponse)
                .orElseGet(() -> fallback(customerId));
    }

    public Page<CustomerProfileResponse> searchCustomers(CustomerSearchRequest request) {
        int page = Math.max(request.page(), 0);
        int size = request.size() <= 0 ? 10 : Math.min(request.size(), 50);

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(Sort.Direction.ASC, "customerId")
        );

        // Specification.allOf is for "AND"
        // Specification.anyOf is for "OR"
        Specification<CustomerEntity> spec = Specification.allOf(
                CustomerSpecifications.hasRiskLevel(request.riskLevel()),
                CustomerSpecifications.hasKycStatus(request.kycStatus()),
                CustomerSpecifications.incomeGreaterThanOrEqual(request.minIncome()),
                CustomerSpecifications.objectiveEquals(request.investmentObjective())
        );

        return customerJpaRepository.findAll(spec, pageable)
                .map(this::toResponse);
    }

    private CustomerProfileResponse fallback(String customerId) {
        CustomerProfileResponse mock = mockCustomers.get(customerId);

        if (mock == null) {
            throw new IllegalArgumentException("Customer not found: " + customerId);
        }

        return mock;
    }

    private CustomerProfileResponse toResponse(CustomerEntity entity) {
        return new CustomerProfileResponse(
                entity.getCustomerId(),
                entity.getName(),
                entity.getAge(),
                entity.getAnnualIncome(),
                entity.getRiskLevel(),
                entity.getInvestmentObjective(),
                entity.getKycStatus()
        );
    }



    // JDBCTemplate version:
//    private final JdbcTemplate jdbcTemplate;
//
//    private final Map<String, CustomerProfileResponse> mockCustomers = Map.of(
//            "C1001", new CustomerProfileResponse(
//                    "C1001",
//                    "John Smith",
//                    45,
//                    120000,
//                    "MEDIUM",
//                    "BALANCED",
//                    "VERIFIED"
//            ),
//            "C1002", new CustomerProfileResponse(
//                    "C1002",
//                    "Mary Chen",
//                    62,
//                    85000,
//                    "LOW",
//                    "INCOME",
//                    "VERIFIED"
//            )
//    );
//
//    public CustomerService(JdbcTemplate jdbcTemplate) {
//        this.jdbcTemplate = jdbcTemplate;
//    }
//
//    public CustomerProfileResponse getCustomerProfile(String customerId) {
//        Optional<CustomerProfileResponse> dbCustomer = findCustomerFromDatabase(customerId);
//
//        if (dbCustomer.isPresent()) {
//            return dbCustomer.get();
//        }
//
//        CustomerProfileResponse mockCustomer = mockCustomers.get(customerId);
//        if (mockCustomer != null) {
//            return mockCustomer;
//        }
//
//        throw new IllegalArgumentException("Customer not found: " + customerId);
//    }
//
//    private Optional<CustomerProfileResponse> findCustomerFromDatabase(String customerId) {
//        String sql = """
//                SELECT
//                    customer_id,
//                    name,
//                    age,
//                    annual_income,
//                    risk_level,
//                    investment_objective,
//                    kyc_status
//                FROM customer_profile
//                WHERE customer_id = ?
//                """;
//
//        return jdbcTemplate.query(sql, rs -> {
//            if (!rs.next()) {
//                return Optional.empty();
//            }
//
//            return Optional.of(new CustomerProfileResponse(
//                    rs.getString("customer_id"),
//                    rs.getString("name"),
//                    rs.getInt("age"),
//                    rs.getDouble("annual_income"),
//                    rs.getString("risk_level"),
//                    rs.getString("investment_objective"),
//                    rs.getString("kyc_status")
//            ));
//        }, customerId);
//    }
}