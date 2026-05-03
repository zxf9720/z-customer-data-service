package com.shawn.customer.controller;

import com.shawn.customer.dto.CustomerProfileResponse;
import com.shawn.customer.dto.CustomerSearchRequest;
import com.shawn.customer.service.CustomerService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/{customerId}")
    public CustomerProfileResponse getCustomerProfile(@PathVariable String customerId) {
        return customerService.getCustomerProfile(customerId);
    }

    @PostMapping("/search")
    public Page<CustomerProfileResponse> searchCustomers(@RequestBody CustomerSearchRequest request) {
        return customerService.searchCustomers(request);
    }
}