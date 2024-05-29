package com.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.SkipListener;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.SneakyThrows;

public class StepSkipListener <T> implements SkipListener<T, Number> {

	Logger logger = LoggerFactory.getLogger(StepSkipListener.class);
	
	public StepSkipListener() {
		super();
	}

	@Override // item reader
	public void onSkipInRead(Throwable throwable) {
		logger.info("A failure on read {} ", throwable.getMessage());
	}

	@Override // item writter
	public void onSkipInWrite(Number item, Throwable throwable) {
		logger.info("A failure on write {} , {}", throwable.getMessage(), item);
	}

	@SneakyThrows
	@Override // item processor
	public void onSkipInProcess(T t, Throwable throwable) {
		logger.info("Item {}  was skipped due to the exception  {}", new ObjectMapper().writeValueAsString(t),
				throwable.getMessage());
	}
}