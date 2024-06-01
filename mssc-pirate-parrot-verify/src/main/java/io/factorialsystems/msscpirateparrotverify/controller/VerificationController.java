package io.factorialsystems.msscpirateparrotverify.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/verify")
public class VerificationController {

    @GetMapping
    public String verify() {
        return "Verify";
    }
}
