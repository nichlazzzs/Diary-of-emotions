package org.example.proj;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "org.example.proj.repository")
@EntityScan(basePackages = "org.example.proj.entity")
public class ProjApplication {
    public static void main(String[] args) {
        SpringApplication.run(ProjApplication.class, args);
    }
}
