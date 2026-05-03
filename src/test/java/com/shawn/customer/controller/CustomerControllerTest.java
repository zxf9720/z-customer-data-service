package com.shawn.customer.controller;

import com.shawn.customer.dto.CustomerProfileResponse;
import com.shawn.customer.dto.CustomerSearchRequest;
import com.shawn.customer.service.CustomerService;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CustomerControllerTest {

    private final CustomerService service = mock(CustomerService.class);
    private final CustomerController controller = new CustomerController(service);

    @Test
    void getCustomerProfileDelegatesToService() {
        CustomerProfileResponse response = response("C1001");
        when(service.getCustomerProfile("C1001")).thenReturn(response);

        assertThat(controller.getCustomerProfile("C1001")).isSameAs(response);
        verify(service).getCustomerProfile("C1001");
    }

    @Test
    void searchCustomersDelegatesToService() {
        CustomerSearchRequest request = new CustomerSearchRequest("HIGH", "VERIFIED", 100000.0, "GROWTH", 0, 10);
        Page<CustomerProfileResponse> page = new PageImpl<>(List.of(response("C1001")));
        when(service.searchCustomers(request)).thenReturn(page);

        assertThat(controller.searchCustomers(request)).isSameAs(page);
        verify(service).searchCustomers(request);
    }

    private static CustomerProfileResponse response(String customerId) {
        return new CustomerProfileResponse(customerId, "Alex Kim", 35, 125000, "HIGH", "GROWTH", "VERIFIED");
    }
}
