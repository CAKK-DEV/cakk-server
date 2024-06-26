package com.cakk.batch.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

import com.cakk.batch.job.launcher.WeeklyJobLauncher;
import com.cakk.common.response.ApiResponse;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class WeeklyJobController {

	private final WeeklyJobLauncher jobLauncher;

	@GetMapping("/weekly-job")
	public ApiResponse<Void> executeWeeklyJob() throws Exception {
		jobLauncher.launch();

		return ApiResponse.success();
	}
}
