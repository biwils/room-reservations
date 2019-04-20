package com.example.roomreservations.util;

import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.Instant;

import static java.lang.String.format;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ResponseStatus(BAD_REQUEST)
class InvalidPeriodException extends IllegalArgumentException {

    InvalidPeriodException(Instant start, Instant end) {
        super(format("Period cannot end before or at the same time it starts, start %s, end %s", start, end));
    }

}
