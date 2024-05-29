package com.repository;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.record.InstrumentRecord;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.NonUniqueResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

@Repository
public class InstrumentCacheRepositoryImpl implements InstrumentCacheRepository{

	@PersistenceContext
    private EntityManager em;
	
	public InstrumentCacheRepositoryImpl() {
		super();
	}
	
	@Override
	public Optional<InstrumentRecord> getInstrument(String instrument){
		Optional<InstrumentRecord> optional;
		Query q = this.em.createNamedQuery("InstrumentPriceModifier.findAll");
		q.setParameter(1, instrument);
		try {
			optional = Optional.of((InstrumentRecord) q.getSingleResult());			
		} catch (NoResultException | NonUniqueResultException e) {
			optional = Optional.empty();
		}
		return optional;
	}
	
}
