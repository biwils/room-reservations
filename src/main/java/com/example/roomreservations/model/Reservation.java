package com.example.roomreservations.model;

import com.example.roomreservations.util.Period;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.Instant;
import java.util.UUID;

import static java.util.UUID.randomUUID;

@EqualsAndHashCode(of = "id")
@NoArgsConstructor //for Hibernate
@Getter
@Entity
@Table(name = "reservations")
public class Reservation {

    @Id
    private UUID id = randomUUID();

    private UUID customerId;

    private UUID roomId;

    private Instant start;

    private Instant end;

    public Reservation(UUID customerId, UUID roomId, Instant start, Instant end) {
        this.customerId = customerId;
        this.roomId = roomId;
        this.start = start;
        this.end = end;
    }

    //todo test
    public boolean overlaps(Period period) {
        return new Period(start, end).overlaps(period);
    }
}
