package com.grepp.synapse4;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableFeignClients
@EnableJpaAuditing
@SpringBootApplication
@EnableBatchProcessing
public class App {
    
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
    
}



