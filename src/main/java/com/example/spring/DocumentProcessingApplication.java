package com.example.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class DocumentProcessingApplication {

    public static void main(String[] args) {
        SpringApplication.run(DocumentProcessingApplication.class, args);
    }

}
