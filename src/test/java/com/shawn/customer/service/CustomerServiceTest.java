package com.shawn.customer.service;

import com.shawn.customer.dto.CustomerProfileResponse;
import com.shawn.customer.dto.CustomerSearchRequest;
import com.shawn.customer.entity.CustomerEntity;
import com.shawn.customer.repository.CustomerJpaRepository;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CustomerServiceTest {

    private final CustomerJpaRepository repository = mock(CustomerJpaRepository.class);
    private final CustomerService service = new CustomerService(repository);

    @Test
    void getCustomerProfileReturnsRepositoryCustomer() {
        when(repository.findById("C1001")).thenReturn(Optional.of(entity("C1001")));

        CustomerProfileResponse response = service.getCustomerProfile("C1001");

        assertThat(response.customerId()).isEqualTo("C1001");
        assertThat(response.name()).isEqualTo("Alex Kim");
        assertThat(response.annualIncome()).isEqualTo(125000);
    }

    @Test
    void getCustomerProfileFallsBackToMockCustomer() {
        when(repository.findById("C9999")).thenReturn(Optional.empty());

        CustomerProfileResponse response = service.getCustomerProfile("C9999");

        assertThat(response.name()).isEqualTo("Mock User");
        assertThat(response.riskLevel()).isEqualTo("MEDIUM");
    }

    @Test
    void getCustomerProfileRejectsUnknownCustomer() {
        when(repository.findById("missing")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getCustomerProfile("missing"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Customer not found: missing");
    }

    @Test
    void searchCustomersNormalizesPageAndSizeAndMapsResults() {
        when(repository.findAll(anySpecification(), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(entity("C1001"))));
        CustomerSearchRequest request = new CustomerSearchRequest("HIGH", "VERIFIED", 100000.0, "GROWTH", -1, 100);

        Page<CustomerProfileResponse> result = service.searchCustomers(request);

        ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
        org.mockito.Mockito.verify(repository).findAll(anySpecification(), pageableCaptor.capture());
        assertThat(pageableCaptor.getValue().getPageNumber()).isZero();
        assertThat(pageableCaptor.getValue().getPageSize()).isEqualTo(50);
        assertThat(result.getContent()).extracting(CustomerProfileResponse::customerId).containsExactly("C1001");
    }

    @Test
    void searchCustomersUsesDefaultSizeWhenRequestSizeIsNotPositive() {
        when(repository.findAll(anySpecification(), any(Pageable.class)))
                .thenReturn(Page.empty());
        CustomerSearchRequest request = new CustomerSearchRequest(null, null, null, null, 2, 0);

        service.searchCustomers(request);

        ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
        org.mockito.Mockito.verify(repository).findAll(anySpecification(), pageableCaptor.capture());
        assertThat(pageableCaptor.getValue().getPageNumber()).isEqualTo(2);
        assertThat(pageableCaptor.getValue().getPageSize()).isEqualTo(10);
    }

    @SuppressWarnings("unchecked")
    private static Specification<CustomerEntity> anySpecification() {
        return any(Specification.class);
    }

    private static CustomerEntity entity(String customerId) {
        CustomerEntity entity = new CustomerEntity();
        entity.setCustomerId(customerId);
        entity.setName("Alex Kim");
        entity.setAge(35);
        entity.setAnnualIncome(125000);
        entity.setRiskLevel("HIGH");
        entity.setInvestmentObjective("GROWTH");
        entity.setKycStatus("VERIFIED");
        return entity;
    }
}
