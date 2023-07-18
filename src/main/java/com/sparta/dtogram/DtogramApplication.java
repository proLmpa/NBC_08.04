package com.sparta.dtogram;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class DtogramApplication {

    public static void main(String[] args) {
        SpringApplication.run(DtogramApplication.class, args);
    }

}
