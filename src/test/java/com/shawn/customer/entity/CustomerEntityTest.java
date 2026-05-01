package com.shawn.customer.entity;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CustomerEntityTest {

    @Test
    void gettersReturnAssignedValues() {
        CustomerEntity entity = new CustomerEntity();

        entity.setCustomerId("C1001");
        entity.setName("Alex Kim");
        entity.setAge(35);
        entity.setAnnualIncome(125000);
        entity.setRiskLevel("HIGH");
        entity.setInvestmentObjective("GROWTH");
        entity.setKycStatus("VERIFIED");

        assertThat(entity.getCustomerId()).isEqualTo("C1001");
        assertThat(entity.getName()).isEqualTo("Alex Kim");
        assertThat(entity.getAge()).isEqualTo(35);
        assertThat(entity.getAnnualIncome()).isEqualTo(125000);
        assertThat(entity.getRiskLevel()).isEqualTo("HIGH");
        assertThat(entity.getInvestmentObjective()).isEqualTo("GROWTH");
        assertThat(entity.getKycStatus()).isEqualTo("VERIFIED");
    }
}
