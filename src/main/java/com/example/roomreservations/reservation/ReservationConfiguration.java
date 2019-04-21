package com.example.roomreservations.reservation;

import com.example.roomreservations.model.repository.CustomerRepository;
import com.example.roomreservations.model.repository.ReservationRepository;
import com.example.roomreservations.model.repository.RoomRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class ReservationConfiguration {

    @Bean
    ReservationService reservationService(ReservationRepository reservationRepository, CustomerRepository customerRepository, RoomRepository roomRepository) {
        return new ReservationService(reservationRepository, customerRepository, roomRepository);
    }

}
