package com.example.roomreservations.room;

import lombok.Value;

import java.math.BigDecimal;
import java.util.UUID;

@Value
class RoomDto {

    private UUID id;

    private Integer number;

    private BigDecimal price;

    private String city;

    private String hotel;

}
