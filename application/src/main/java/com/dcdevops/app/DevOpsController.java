package com.dcdevops.app;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DevOpsController {

    @GetMapping("/")
    public String home() {
        return """
                Data Center DevOps Application
                Version: 1.0
                Environment: DEV
                Status: UP
                """;
    }

    @GetMapping("/health")
    public Map<String, String> health() {
        return Map.of("status", "UP");
    }
}
