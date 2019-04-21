package com.example.roomreservations.reservation;

import lombok.Value;

import java.util.UUID;

@Value
class ReservationDto {

    private UUID id;

    private UUID customerId;

    private UUID roomId;

    private PeriodDto period;

}
