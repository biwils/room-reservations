package com.example.roomreservations.reservation;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(path = "/reservation", produces = APPLICATION_JSON_VALUE)
class ReservationController {

    private ReservationService reservationService;

    ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    //if we had authorization then we would pass some token instead of customerId
    @GetMapping
    ResponseEntity<Page<ReservationDto>> findAll(Pageable pageable, @RequestHeader("customerId") UUID customerId) {
        return new ResponseEntity<>(reservationService.findAll(pageable, customerId), OK);
    }

    @PostMapping
    ResponseEntity<ReservationDto> makeReservation(@RequestBody @Valid AddReservationCmd addReservationCmd) {
        return new ResponseEntity<>(reservationService.add(addReservationCmd), CREATED);
    }

    @DeleteMapping(path = "/{reservationId}")
    ResponseEntity<Void> cancelReservation(@PathVariable UUID reservationId, @RequestHeader("customerId") UUID customerId) {
        reservationService.delete(reservationId, customerId);
        return new ResponseEntity<>(NO_CONTENT);
    }

}
