package org.example.proj.repository;

import org.example.proj.entity.User;
import org.example.proj.entity.AccessToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccessTokenRepository extends JpaRepository<AccessToken, Long> {
    Optional<AccessToken> findByToken(String token);
    boolean existsByDoctorAndPatientAndConfirmedTrue(User doctor, User patient);
}

