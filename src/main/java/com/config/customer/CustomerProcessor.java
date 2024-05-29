package com.config.customer;

import org.springframework.batch.item.ItemProcessor;

import com.entity.Customer;

public class CustomerProcessor implements ItemProcessor<Customer, Customer> {
	
	public CustomerProcessor() {
		super();
	}
	
    @Override
    public Customer process(Customer customer) {
        int age = Integer.parseInt(customer.getAge());
        if (age >= 18) {
            return customer;
        }
        return null;
    }
    
}
