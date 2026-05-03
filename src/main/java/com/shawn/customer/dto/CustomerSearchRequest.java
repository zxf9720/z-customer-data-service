package com.shawn.customer.dto;

public record CustomerSearchRequest(
        String riskLevel,
        String kycStatus,
        Double minIncome,
        String investmentObjective,
        int page,
        int size
) {
}