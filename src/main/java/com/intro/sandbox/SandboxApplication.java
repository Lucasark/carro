package com.intro.sandbox;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories
public class SandboxApplication {

    public static void main(String[] args) {
        SpringApplication.run(SandboxApplication.class, args);
    }

}
