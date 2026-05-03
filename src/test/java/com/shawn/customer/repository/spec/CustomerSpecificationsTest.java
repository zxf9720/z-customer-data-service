package com.shawn.customer.repository.spec;

import com.shawn.customer.entity.CustomerEntity;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CustomerSpecificationsTest {

    @Test
    void hasRiskLevelUsesCaseInsensitiveMatch() {
        CriteriaObjects criteria = new CriteriaObjects();
        when(criteria.root.<String>get("riskLevel")).thenReturn(criteria.stringPath);
        when(criteria.criteriaBuilder.lower(criteria.stringPath)).thenReturn(criteria.lowerExpression);
        when(criteria.criteriaBuilder.equal(criteria.lowerExpression, "high")).thenReturn(criteria.predicate);

        Predicate result = CustomerSpecifications.hasRiskLevel("HIGH")
                .toPredicate(criteria.root, criteria.query, criteria.criteriaBuilder);

        assertThat(result).isSameAs(criteria.predicate);
    }

    @Test
    void hasKycStatusReturnsConjunctionForBlankInput() {
        CriteriaObjects criteria = new CriteriaObjects();
        when(criteria.criteriaBuilder.conjunction()).thenReturn(criteria.predicate);

        Predicate result = CustomerSpecifications.hasKycStatus(" ")
                .toPredicate(criteria.root, criteria.query, criteria.criteriaBuilder);

        assertThat(result).isSameAs(criteria.predicate);
        verify(criteria.criteriaBuilder).conjunction();
    }

    @Test
    void incomeGreaterThanOrEqualUsesMinimumIncome() {
        CriteriaObjects criteria = new CriteriaObjects();
        Path<Double> incomePath = mock(Path.class);
        when(criteria.root.<Double>get("annualIncome")).thenReturn(incomePath);
        when(criteria.criteriaBuilder.greaterThanOrEqualTo(incomePath, 100000.0)).thenReturn(criteria.predicate);

        Predicate result = CustomerSpecifications.incomeGreaterThanOrEqual(100000.0)
                .toPredicate(criteria.root, criteria.query, criteria.criteriaBuilder);

        assertThat(result).isSameAs(criteria.predicate);
    }

    @Test
    void objectiveEqualsUsesCaseInsensitiveMatch() {
        CriteriaObjects criteria = new CriteriaObjects();
        when(criteria.root.<String>get("investmentObjective")).thenReturn(criteria.stringPath);
        when(criteria.criteriaBuilder.lower(criteria.stringPath)).thenReturn(criteria.lowerExpression);
        when(criteria.criteriaBuilder.equal(criteria.lowerExpression, "growth")).thenReturn(criteria.predicate);

        Predicate result = CustomerSpecifications.objectiveEquals("GROWTH")
                .toPredicate(criteria.root, criteria.query, criteria.criteriaBuilder);

        assertThat(result).isSameAs(criteria.predicate);
    }

    private static class CriteriaObjects {
        private final Root<CustomerEntity> root = mock(Root.class);
        private final jakarta.persistence.criteria.CriteriaQuery<?> query = mock(jakarta.persistence.criteria.CriteriaQuery.class);
        private final CriteriaBuilder criteriaBuilder = mock(CriteriaBuilder.class);
        private final Path<String> stringPath = mock(Path.class);
        private final Expression<String> lowerExpression = mock(Expression.class);
        private final Predicate predicate = mock(Predicate.class);
    }
}
