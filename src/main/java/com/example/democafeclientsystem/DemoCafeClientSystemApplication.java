package com.example.democafeclientsystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@EntityScan
@SpringBootApplication
public class DemoCafeClientSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoCafeClientSystemApplication.class, args);
    }

}
