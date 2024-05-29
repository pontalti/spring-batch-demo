package com.repository;

import java.util.Optional;

import com.record.InstrumentRecord;

public interface InstrumentCacheRepository {
	public Optional<InstrumentRecord> getInstrument(String instrument);
}
