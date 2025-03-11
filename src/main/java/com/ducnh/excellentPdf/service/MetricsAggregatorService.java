package com.ducnh.excellentPdf.service;

import org.springframework.stereotype.Service;

@Service
public class MetricsAggregatorService {

	private final MeterRegistry meterRegistry;
	private final PostHogService postHogService;
}
