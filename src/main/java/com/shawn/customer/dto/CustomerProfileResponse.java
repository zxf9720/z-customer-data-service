package com.shawn.customer.dto;

public record CustomerProfileResponse(
        String customerId,
        String name,
        int age,
        double annualIncome,
        String riskLevel,
        String investmentObjective,
        String kycStatus
) {
}