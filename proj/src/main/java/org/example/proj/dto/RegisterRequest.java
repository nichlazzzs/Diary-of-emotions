package org.example.proj.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class RegisterRequest {
    @NotBlank(message = "Имя пользователя обязательно")
    private String username;

    @Email(message = "Неверный формат email")
    @NotBlank(message = "Email обязателен")
    private String email;

    @Size(min = 6, message = "Пароль должен содержать минимум 6 символов")
    private String password;

    @NotBlank(message = "Подтверждение пароля обязательно")
    private String confirmPassword;

    public RegisterRequest() {}

    public RegisterRequest(String username, String email, String password, String confirmPassword) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.confirmPassword = confirmPassword;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}
