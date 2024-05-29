package com.config.instrument;

import java.io.File;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.SkipListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.skip.SkipPolicy;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.task.TaskExecutor;
import org.springframework.core.task.VirtualThreadTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import com.config.ExceptionSkipPolicy;
import com.entity.InstrumentPriceModifier;
import com.listener.StepSkipListener;
import com.repository.InstrumentRepo;

@Configuration
public class SpringBatchInstrumentConfig {

	private InstrumentRepo repository;
	private InstrumentItemWriter itemWriter;

	public SpringBatchInstrumentConfig(InstrumentRepo repository, InstrumentItemWriter itemWriter) {
		this.repository = repository;
		this.itemWriter = itemWriter;
	}

	@Bean
	@StepScope
	public FlatFileItemReader<InstrumentPriceModifier> instrumentReader(@Value("#{jobParameters[fullPathFileName]}") String pathToFile) {
		FlatFileItemReader<InstrumentPriceModifier> flatFileItemReader = new FlatFileItemReader<>();
		flatFileItemReader.setResource(new FileSystemResource(new File(pathToFile)));
		flatFileItemReader.setName("TXT-Reader");
		flatFileItemReader.setLinesToSkip(1);
		flatFileItemReader.setLineMapper(lineMapper());
		return flatFileItemReader;
	}

	private LineMapper<InstrumentPriceModifier> lineMapper() {
		DefaultLineMapper<InstrumentPriceModifier> lineMapper = new DefaultLineMapper<>();

		DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
		lineTokenizer.setDelimiter(",");
		lineTokenizer.setStrict(false);
		lineTokenizer.setNames("name", "date", "multiplier");

		BeanWrapperFieldSetMapper<InstrumentPriceModifier> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
		fieldSetMapper.setTargetType(InstrumentPriceModifier.class);

		lineMapper.setLineTokenizer(lineTokenizer);
		lineMapper.setFieldSetMapper(fieldSetMapper);

		return lineMapper;
	}

	@Bean
	public InstrumentProcessor instrumentProcessor() {
		return new InstrumentProcessor();
	}

	@Bean
	public RepositoryItemWriter<InstrumentPriceModifier> instrumentWriter() {
		RepositoryItemWriter<InstrumentPriceModifier> writer = new RepositoryItemWriter<>();
		writer.setRepository(this.repository);
		writer.setMethodName("save");
		return writer;
	}

	@Bean
	public Step instrumentStep1(FlatFileItemReader<InstrumentPriceModifier> itemReader, JobRepository jobRepository, 
															PlatformTransactionManager transactionManager) {
		return new StepBuilder("instrumentStep", jobRepository)
					.<InstrumentPriceModifier, InstrumentPriceModifier>chunk(500, transactionManager)
					.reader(itemReader)
					.processor(instrumentProcessor())
					.writer(itemWriter)
					.faultTolerant()
					.listener(instrumentSkipListener())
					.skipPolicy(instrumentSkipPolicy())
					.taskExecutor(instrumentTaskExecutor())
					.build();
	}

	@Bean
	public Job instrumentJob(FlatFileItemReader<InstrumentPriceModifier> itemReader, JobRepository jobRepository, 
															PlatformTransactionManager transactionManager) {
		return new JobBuilder("importInstrument", jobRepository)
					.flow(instrumentStep1(itemReader, jobRepository, transactionManager))
					.end()
					.build();
	}

	@Bean
	public SkipPolicy instrumentSkipPolicy() {
		return new ExceptionSkipPolicy();
	}

	@Bean
	public SkipListener<InstrumentPriceModifier, Number> instrumentSkipListener() {
		return new StepSkipListener<InstrumentPriceModifier>();
	}

	@Bean
	public TaskExecutor instrumentTaskExecutor() {
		VirtualThreadTaskExecutor executor = new VirtualThreadTaskExecutor("Instrument");
		return executor;
	}

}