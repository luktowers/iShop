package com.client.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/check")
@AllArgsConstructor
public class HealthCheckController {

    @GetMapping
    public ResponseEntity<String> status(){
        return ResponseEntity.ok("ok");
    }
}
