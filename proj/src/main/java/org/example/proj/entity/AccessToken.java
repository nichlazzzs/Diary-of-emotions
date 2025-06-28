package org.example.proj.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@Entity
public class AccessToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token; // UUID

    @ManyToOne
    private User patient;

    @ManyToOne
    private User doctor;

    private boolean confirmed;

    private LocalDateTime createdAt;

    public AccessToken() {}
    public AccessToken(String token, User patient, User doctor) {
        this.token = token;
        this.patient = patient;
        this.doctor = doctor;
        this.confirmed = false;
        this.createdAt = LocalDateTime.now();
    }

}
