package org.example.proj.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class HomeController {

    @GetMapping("/api/home")
    public Map<String, Object> home(Authentication authentication) {
        String username = authentication.getName();
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        return Map.of(
                "username", username,
                "isAdmin", isAdmin,
                "message", "Добро пожаловать в защищённое приложение!"
        );
    }
}
