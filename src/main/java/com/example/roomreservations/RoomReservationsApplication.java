package com.example.roomreservations;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories(basePackages = "com.example.roomreservations.model")
@SpringBootApplication
public class RoomReservationsApplication {

    public static void main(String[] args) {
        SpringApplication.run(RoomReservationsApplication.class, args);
    }

}
