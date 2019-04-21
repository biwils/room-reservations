package com.example.roomreservations.room;

import com.example.roomreservations.model.repository.RoomRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class RoomConfiguration {

    @Bean
    RoomQueryService roomQueryService(RoomRepository roomRepository) {
        return new RoomQueryService(roomRepository);
    }

}
