package com.shawn.wealth.customer.dto;

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