package com.example.roomreservations.reservation;

import com.example.roomreservations.util.Period;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.UUID;

import static java.lang.String.format;
import static org.springframework.http.HttpStatus.FORBIDDEN;

@ResponseStatus(FORBIDDEN)
class RoomUnavailableDuringPeriodException extends IllegalArgumentException {

    RoomUnavailableDuringPeriodException(UUID roomId, Period period) {
        super(format("Room %s is unavailable between %s and %s", roomId, period.getStartDate(), period.getEndDate()));
    }

}
