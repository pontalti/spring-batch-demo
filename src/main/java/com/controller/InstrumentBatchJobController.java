package com.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.entity.InstrumentPriceModifier;
import com.repository.InstrumentRepo;

@RestController
@RequestMapping({"/instrument/","/instrument"})
public class InstrumentBatchJobController {

	private final String TEMP_STORAGE = "C:\\dev\\temp_storage_spring_batch";

	private JobLauncher jobLauncher;
	private Job instrumentJob;
	private InstrumentRepo repository;
	
	public InstrumentBatchJobController(JobLauncher jobLauncher, Job instrumentJob, InstrumentRepo repository) {
		super();
		this.jobLauncher = jobLauncher;
		this.instrumentJob = instrumentJob;
		this.repository = repository;
	}

	@PostMapping(path = {"/importData", "/importData/"})
	public void startBatch(@RequestParam("file") MultipartFile multipartFile) {

		try {
			String originalFileName = multipartFile.getOriginalFilename();
			File fileToImport = new File(TEMP_STORAGE + originalFileName);
			multipartFile.transferTo(fileToImport);

			JobParameters jobParameters = new JobParametersBuilder()
					.addString("fullPathFileName", TEMP_STORAGE + originalFileName)
					.addLong("startAt", System.currentTimeMillis()).toJobParameters();

			JobExecution execution = jobLauncher.run(instrumentJob, jobParameters);

			if (execution.getExitStatus().getExitCode().equals(ExitStatus.COMPLETED)) {
				Files.deleteIfExists(Paths.get(TEMP_STORAGE + originalFileName));
			}

		} catch (JobExecutionAlreadyRunningException | JobRestartException | 
					JobInstanceAlreadyCompleteException | JobParametersInvalidException | IOException e) {
			e.printStackTrace();
		}
	}

	@GetMapping
	public List<InstrumentPriceModifier> getAll() {
		return this.repository.findAll();
	}
}