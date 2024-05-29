package com.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.entity.InstrumentPriceModifier;

@Repository
public interface InstrumentRepo extends JpaRepository<InstrumentPriceModifier, Integer>{

	
}