package com.noobug.NooblogRebuild;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class NooblogRebuildApplication {

    public static void main(String[] args) {
        SpringApplication.run(NooblogRebuildApplication.class, args);
    }
}
