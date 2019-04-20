package com.example.roomreservations.model;

import com.example.roomreservations.util.Period;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static java.util.UUID.randomUUID;

@EqualsAndHashCode(of = "id")
@NoArgsConstructor //for Hibernate
@Getter
@Entity
@Table(name = "rooms")
public class Room {

    @Id
    private UUID id = randomUUID();

    @Column(name = "room_no")
    private Integer number;

    private BigDecimal price;

    private String city;

    private String hotel;

    @OneToMany
    private List<Reservation> reservations;

    public Room(Integer number, BigDecimal price, String city, String hotel, List<Reservation> reservations) {
        this.number = number;
        this.price = price;
        this.city = city;
        this.hotel = hotel;
        this.reservations = reservations;
    }

    //todo test
    public boolean isAvailableWithin(Period period) {
        return reservations.stream()
                .noneMatch(reservation -> reservation.overlaps(period));
    }

}
