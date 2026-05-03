package com.shawn.customer.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "customer_profile")
public class CustomerEntity {

    @Id
    @Column(name = "customer_id")
    private String customerId;

    private String name;

    private int age;

    @Column(name = "annual_income")
    private double annualIncome;

    @Column(name = "risk_level")
    private String riskLevel;

    @Column(name = "investment_objective")
    private String investmentObjective;

    @Column(name = "kyc_status")
    private String kycStatus;

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public double getAnnualIncome() {
        return annualIncome;
    }

    public void setAnnualIncome(double annualIncome) {
        this.annualIncome = annualIncome;
    }

    public String getRiskLevel() {
        return riskLevel;
    }

    public void setRiskLevel(String riskLevel) {
        this.riskLevel = riskLevel;
    }

    public String getInvestmentObjective() {
        return investmentObjective;
    }

    public void setInvestmentObjective(String investmentObjective) {
        this.investmentObjective = investmentObjective;
    }

    public String getKycStatus() {
        return kycStatus;
    }

    public void setKycStatus(String kycStatus) {
        this.kycStatus = kycStatus;
    }
}