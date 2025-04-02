package com.monitor.sample;

import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletResponse;

@RestController
public class SampleController {

	private static final Logger logger = LoggerFactory.getLogger(SampleController.class);

	@GetMapping("/")
	public String hello(HttpServletResponse response) throws IOException {
		logger.info("Attemped succees to / endpoint resulted in 403 forbieedn");
		response.sendError(HttpServletResponse.SC_FORBIDDEN,"ACCESS DENIED");
		return null;

	}
}
