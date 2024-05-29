package com.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.entity.Customer;

public interface CustomerRepo extends JpaRepository<Customer,Integer> {
}