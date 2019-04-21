package com.example.roomreservations.reservation;

import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.UUID;

import static java.lang.String.format;
import static org.springframework.http.HttpStatus.FORBIDDEN;

@ResponseStatus(FORBIDDEN)
class ReservationBelongsToDifferentCustomerException extends IllegalArgumentException {

    ReservationBelongsToDifferentCustomerException(UUID reservationId, UUID customerId) {
        super(format("Reservation %s doesn't belong to customer %s", reservationId, customerId));
    }

}
