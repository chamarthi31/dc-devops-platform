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

    @GetMapping("/test-error")
    public String testError() {
        throw new RuntimeException("Intentional test error for monitoring");
    }

    @GetMapping("/test-cpu")
    public String testCpu() {
        long end = System.nanoTime() + 120_000_000_000L;
        double result = 0;

        while (System.nanoTime() < end) {
            result += Math.sqrt(Math.random());
        }

        return "CPU test completed: " + result;
    }
}
