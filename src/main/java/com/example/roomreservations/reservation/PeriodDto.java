package com.example.roomreservations.reservation;

import lombok.Value;

import java.time.Instant;

@Value
class PeriodDto {

    private Instant start;

    private Instant end;

}
