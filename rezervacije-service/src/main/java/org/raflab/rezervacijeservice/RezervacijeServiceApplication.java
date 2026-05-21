package org.raflab.rezervacijeservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class RezervacijeServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(RezervacijeServiceApplication.class, args);
    }

}
