package com.shawn.customer.repository.spec;

import com.shawn.customer.entity.CustomerEntity;
import org.springframework.data.jpa.domain.Specification;

public class CustomerSpecifications {

    private CustomerSpecifications() {
    }

    public static Specification<CustomerEntity> hasRiskLevel(String riskLevel) {
        return (root, query, criteriaBuilder) -> {
            if (riskLevel == null || riskLevel.isBlank()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(
                    criteriaBuilder.lower(root.get("riskLevel")),
                    riskLevel.toLowerCase()
            );
        };
    }

    public static Specification<CustomerEntity> hasKycStatus(String kycStatus) {
        return (root, query, criteriaBuilder) -> {
            if (kycStatus == null || kycStatus.isBlank()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(
                    criteriaBuilder.lower(root.get("kycStatus")),
                    kycStatus.toLowerCase()
            );
        };
    }

    public static Specification<CustomerEntity> incomeGreaterThanOrEqual(Double minIncome) {
        return (root, query, criteriaBuilder) -> {
            if (minIncome == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.greaterThanOrEqualTo(root.get("annualIncome"), minIncome);
        };
    }

    public static Specification<CustomerEntity> objectiveEquals(String investmentObjective) {
        return (root, query, criteriaBuilder) -> {
            if (investmentObjective == null || investmentObjective.isBlank()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(
                    criteriaBuilder.lower(root.get("investmentObjective")),
                    investmentObjective.toLowerCase()
            );
        };
    }
}