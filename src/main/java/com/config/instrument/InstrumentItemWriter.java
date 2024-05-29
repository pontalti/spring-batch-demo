package com.config.instrument;

import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import com.entity.InstrumentPriceModifier;
import com.repository.InstrumentRepo;

@Component
public class InstrumentItemWriter implements ItemWriter<InstrumentPriceModifier> {

    private InstrumentRepo repository;
    
    public InstrumentItemWriter(InstrumentRepo repository) {
		super();
		this.repository = repository;
	}

	@Override
	public void write(Chunk<? extends InstrumentPriceModifier> chunk) throws Exception {
		System.out.println("Writer Thread "+Thread.currentThread().getName());
		this.repository.saveAll(chunk);
	}
}