package org.example.proj.controller;
import org.example.proj.dto.RegisterRequest;
import org.example.proj.entity.User;
import org.example.proj.repository.UserRepository;
import org.example.proj.security.JwtService;
import org.example.proj.dto.LoginRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;


import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthController(UserRepository userRepository,
                          PasswordEncoder passwordEncoder,
                          JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request, BindingResult result) {
        Map<String, String> errors = new HashMap<>();

        if (result.hasErrors()) {
            result.getFieldErrors().forEach(error ->
                    errors.put(error.getField(), error.getDefaultMessage()));
        }

        if (!request.getPassword().equals(request.getConfirmPassword())) {
            errors.put("confirmPassword", "Пароли не совпадают");
        }

        if (userRepository.existsByUsername(request.getUsername())) {
            errors.put("username", "Имя пользователя уже занято");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            errors.put("email", "Email уже зарегистрирован");
        }

        if (!errors.isEmpty()) {
            return ResponseEntity.badRequest().body(errors);
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        userRepository.save(user);

        String token = jwtService.generateToken(user);

        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "message", "Пользователь успешно зарегистрирован",
                "token", token
        ));
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        Optional<User> optionalUser = userRepository.findByUsername(request.getUsername());

        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Пользователь не найден"));
        }

        User user = optionalUser.get();

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Неверный пароль"));
        }

        String token = jwtService.generateToken(user);

        return ResponseEntity.ok(Map.of("token", token));
    }
}
