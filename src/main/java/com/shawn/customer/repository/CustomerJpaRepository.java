package com.shawn.customer.repository;

import com.shawn.customer.entity.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CustomerJpaRepository extends JpaRepository<CustomerEntity, String>,
        JpaSpecificationExecutor<CustomerEntity> {
}