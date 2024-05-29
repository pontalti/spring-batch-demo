package com.config;

import org.springframework.batch.core.step.skip.SkipLimitExceededException;
import org.springframework.batch.core.step.skip.SkipPolicy;

//@Slf4j
public class ExceptionSkipPolicy implements SkipPolicy {

	public ExceptionSkipPolicy() {
		super();
	}
	
	@Override
	public boolean shouldSkip(Throwable t, long skipCount) throws SkipLimitExceededException {
		return t instanceof NumberFormatException;
	}
}