package com.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.entity.InstrumentPriceModifier;
import com.repository.InstrumentRepo;

@RestController
public class InstrumentBatchJobController {

	private static final Logger LOG = LoggerFactory.getLogger(InstrumentBatchJobController.class);

	@Value("${app.temp-storage}")
	private String tempStorage;

	private final JobLauncher jobLauncher;
	private final Job instrumentJob;
	private final InstrumentRepo repository;
	
	public InstrumentBatchJobController(JobLauncher jobLauncher, Job instrumentJob, InstrumentRepo repository) {
		super();
		this.jobLauncher = jobLauncher;
		this.instrumentJob = instrumentJob;
		this.repository = repository;
	}

	@PostMapping(path = {"/instrument/importData"})
	public void startBatch(@RequestParam("file") MultipartFile multipartFile) {

		try {
			String originalFileName = multipartFile.getOriginalFilename();
			File fileToImport = new File(tempStorage + originalFileName);
			multipartFile.transferTo(fileToImport);

			JobParameters jobParameters = new JobParametersBuilder()
					.addString("fullPathFileName", tempStorage + originalFileName)
					.addLong("startAt", System.currentTimeMillis()).toJobParameters();

			JobExecution execution = jobLauncher.run(instrumentJob, jobParameters);

			if (execution.getExitStatus().getExitCode().equals(ExitStatus.COMPLETED)) {
				Files.deleteIfExists(Paths.get(tempStorage + originalFileName));
			}

		} catch (JobExecutionAlreadyRunningException | JobRestartException | 
					JobInstanceAlreadyCompleteException | JobParametersInvalidException | IOException e) {
			LOG.error("Error -> {}", e);
		}
	}

	@GetMapping("/instrument")
	public List<InstrumentPriceModifier> getAll() {
		return this.repository.findAll();
	}
}