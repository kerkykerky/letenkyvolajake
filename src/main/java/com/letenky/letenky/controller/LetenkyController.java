package com.letenky.letenky.controller;

import com.letenky.letenky.model.HealthCheckResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LetenkyController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @GetMapping("/healthcheck")
    public ResponseEntity<HealthCheckResponse> healthcheck() {
        this.logger.info("healthcheck");
        HealthCheckResponse healthCheckResponse = new HealthCheckResponse();
        healthCheckResponse.setStatus("OK");
        return ResponseEntity.ok().body(healthCheckResponse);
    }
}
