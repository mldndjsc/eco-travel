package org.raflab.finansijeservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class FinansijeServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(FinansijeServiceApplication.class, args);
    }

}
