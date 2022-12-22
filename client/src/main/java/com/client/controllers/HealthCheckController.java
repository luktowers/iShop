package com.client.controllers;

import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/check")
public class HealthCheckController {

    @GetMapping
    public ResponseEntity<String> status(){
        return ResponseEntity.ok("ok");
    }
}
