package org.example.proj.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.example.proj.service.AccessService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/access")
@RequiredArgsConstructor
public class AccessController {

    private final AccessService accessService;

    @PostMapping("/generate")
    public ResponseEntity<String> generate(@RequestParam String doctorUsername,
                                           Authentication auth) {
        String patientUsername = auth.getName();
        String link = accessService.generateLink(patientUsername, doctorUsername);
        return ResponseEntity.ok(link);
    }

    @PostMapping("/confirm")
    public ResponseEntity<String> confirm(@RequestParam String token) {
        accessService.confirmAccess(token);
        return ResponseEntity.ok("Доступ подтверждён");
    }
}

