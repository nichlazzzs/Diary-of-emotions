package org.example.proj.service;

import lombok.RequiredArgsConstructor;
import org.example.proj.entity.AccessToken;
import org.example.proj.entity.User;
import org.example.proj.repository.AccessTokenRepository;
import org.example.proj.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccessService {

    private final AccessTokenRepository accessTokenRepository;
    private final UserRepository userRepository;

    public String generateLink(String patientUsername, String doctorUsername) {
        User patient = userRepository.findByUsername(patientUsername)
                .orElseThrow();
        User doctor = userRepository.findByUsername(doctorUsername)
                .orElseThrow();

        String token = UUID.randomUUID().toString();

        AccessToken accessToken = new AccessToken();
        accessToken.setToken(token);
        accessToken.setPatient(patient);
        accessToken.setDoctor(doctor);
        accessToken.setCreatedAt(LocalDateTime.now());
        accessToken.setConfirmed(false);

        accessTokenRepository.save(accessToken);
        return "https://yourdomain.com/access/confirm?token=" + token;
    }

    public void confirmAccess(String token) {
        AccessToken accessToken = accessTokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid token"));
        accessToken.setConfirmed(true);
        accessTokenRepository.save(accessToken);
    }

    public boolean hasAccess(Object principal, Long patientId) {
        String doctorUsername = principal.toString();
        User doctor = userRepository.findByUsername(doctorUsername)
                .orElseThrow();
        User patient = userRepository.findById(patientId)
                .orElseThrow();
        return accessTokenRepository.existsByDoctorAndPatientAndConfirmedTrue(doctor, patient);
    }

}

