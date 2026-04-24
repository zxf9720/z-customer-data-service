package com.shawn.wealth.customer.controller;

import com.shawn.wealth.customer.dto.CustomerProfileResponse;
import com.shawn.wealth.customer.service.CustomerService;
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
}