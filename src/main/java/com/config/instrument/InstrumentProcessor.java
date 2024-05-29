package com.config.instrument;

import org.springframework.batch.item.ItemProcessor;

import com.entity.InstrumentPriceModifier;

public class InstrumentProcessor implements ItemProcessor<InstrumentPriceModifier, InstrumentPriceModifier> {

	public InstrumentProcessor() {
		super();
	}

	@Override
	public InstrumentPriceModifier process(InstrumentPriceModifier instrument) {
		return instrument;
	}

}
