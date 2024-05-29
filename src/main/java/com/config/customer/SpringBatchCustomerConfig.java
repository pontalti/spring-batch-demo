package com.config.customer;

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
import com.entity.Customer;
import com.listener.StepSkipListener;
import com.repository.CustomerRepo;

@Configuration
public class SpringBatchCustomerConfig {

	private CustomerRepo customerRepository;
	private CustomerItemWriter customerItemWriter;

	public SpringBatchCustomerConfig(CustomerRepo customerRepository, CustomerItemWriter customerItemWriter) {
		this.customerRepository = customerRepository;
		this.customerItemWriter = customerItemWriter;
	}

	@Bean
	@StepScope
	public FlatFileItemReader<Customer> itemReader(@Value("#{jobParameters[fullPathFileName]}") String pathToFile) {
		FlatFileItemReader<Customer> flatFileItemReader = new FlatFileItemReader<>();
		flatFileItemReader.setResource(new FileSystemResource(new File(pathToFile)));
		flatFileItemReader.setName("CSV-Reader");
		flatFileItemReader.setLinesToSkip(1);
		flatFileItemReader.setLineMapper(lineMapper());
		return flatFileItemReader;
	}

	private LineMapper<Customer> lineMapper() {
		DefaultLineMapper<Customer> lineMapper = new DefaultLineMapper<>();

		DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
		lineTokenizer.setDelimiter(",");
		lineTokenizer.setStrict(false);
		lineTokenizer.setNames("id", "firstName", "lastName", "email", "gender", "contactNo", "country", "dob", "age");

		BeanWrapperFieldSetMapper<Customer> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
		fieldSetMapper.setTargetType(Customer.class);

		lineMapper.setLineTokenizer(lineTokenizer);
		lineMapper.setFieldSetMapper(fieldSetMapper);

		return lineMapper;
	}

	@Bean
	public CustomerProcessor processor() {
		return new CustomerProcessor();
	}

	@Bean
	public RepositoryItemWriter<Customer> writer() {
		RepositoryItemWriter<Customer> writer = new RepositoryItemWriter<>();
		writer.setRepository(customerRepository);
		writer.setMethodName("save");
		return writer;
	}

	@Bean
	public Step customerStep(FlatFileItemReader<Customer> itemReader, JobRepository jobRepository, 
											PlatformTransactionManager transactionManager) {
		return new StepBuilder("customerStep", jobRepository)
					.<Customer, Customer>chunk(10, transactionManager)
					.reader(itemReader)
					.processor(processor())
					.writer(customerItemWriter)
					.faultTolerant()
					.listener(skipListener())
					.skipPolicy(skipPolicy())
					.taskExecutor(taskExecutor())
					.build();
	}

	@Bean
	public Job customerJob(FlatFileItemReader<Customer> itemReader, JobRepository jobRepository, 
											PlatformTransactionManager transactionManager) {
		return new JobBuilder("importCustomer", jobRepository)
					.flow(customerStep(itemReader, jobRepository, transactionManager))
					.end()
					.build();
	}

	@Bean
	public SkipPolicy skipPolicy() {
		return new ExceptionSkipPolicy();
	}

	@Bean
	public SkipListener<Customer, Number> skipListener() {
		return new StepSkipListener<Customer>();
	}

	@Bean
	public TaskExecutor taskExecutor() {
		VirtualThreadTaskExecutor executor = new VirtualThreadTaskExecutor("Customer");
		return executor;
	}

}