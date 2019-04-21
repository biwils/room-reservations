package com.example.roomreservations.reservation;

import lombok.Value;

import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.UUID;

@Value
public class AddReservationCmd {

    @NotNull
    private UUID customerId;

    @NotNull
    private UUID roomId;

    @NotNull
    private Instant startDate;

    @NotNull
    private Instant endDate;

}
