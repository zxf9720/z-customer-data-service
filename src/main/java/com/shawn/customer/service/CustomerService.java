package com.shawn.customer.service;

import com.shawn.customer.dto.CustomerProfileResponse;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class CustomerService {

    private final Map<String, CustomerProfileResponse> customers = Map.of(
            "C1001", new CustomerProfileResponse(
                    "C1001",
                    "John Smith",
                    45,
                    120000,
                    "MEDIUM",
                    "BALANCED",
                    "VERIFIED"
            ),
            "C1002", new CustomerProfileResponse(
                    "C1002",
                    "Mary Chen",
                    62,
                    85000,
                    "LOW",
                    "INCOME",
                    "VERIFIED"
            )
    );

    public CustomerProfileResponse getCustomerProfile(String customerId) {
        CustomerProfileResponse customer = customers.get(customerId);

        if (customer == null) {
            throw new IllegalArgumentException("Customer not found: " + customerId);
        }

        return customer;
    }
}