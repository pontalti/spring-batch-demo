package com.config.customer;

import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import com.entity.Customer;
import com.repository.CustomerRepo;

@Component
public class CustomerItemWriter implements ItemWriter<Customer> {

    private CustomerRepo repository;
    
    public CustomerItemWriter(CustomerRepo repository) {
		super();
		this.repository = repository;
	}

	@Override
	public void write(Chunk<? extends Customer> chunk) throws Exception {
		System.out.println("Writer Thread "+Thread.currentThread().getName());
        repository.saveAll(chunk);
	}
}