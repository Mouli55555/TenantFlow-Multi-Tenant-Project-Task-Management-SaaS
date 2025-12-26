package com.partnr.saas.controller;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
@RestController
public class HealthController {
    @GetMapping("/health")
    public String ping() { return "HealthController OK"; }
}
