package com.example.roomreservations.reservation;


import com.example.roomreservations.BaseIntegrationTest;
import com.example.roomreservations.model.Customer;
import com.example.roomreservations.model.Reservation;
import com.example.roomreservations.model.Room;
import com.example.roomreservations.model.repository.CustomerRepository;
import com.example.roomreservations.model.repository.ReservationRepository;
import com.example.roomreservations.model.repository.RoomRepository;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;

import java.time.Instant;

import static com.example.roomreservations.InstantsFixture.JAN_1;
import static com.example.roomreservations.InstantsFixture.JAN_3;
import static com.example.roomreservations.model.CustomerFixture.johnDoe;
import static com.example.roomreservations.model.RoomFixture.cocoChanelSuite;
import static java.time.Instant.now;
import static java.time.temporal.ChronoUnit.DAYS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.*;


public class ReservationControllerIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private ReservationController reservationController;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Test
    public void should_find_customers_reservations() {
        // given
        Customer customer = johnDoe();
        customerRepository.save(customer);
        Room room = cocoChanelSuite();
        roomRepository.save(room);
        Reservation reservation = new Reservation(customer.getId(), room.getId(), JAN_1, JAN_3);
        reservationRepository.save(reservation);

        // when
        ResponseEntity<Page<ReservationDto>> result = reservationController.findAll(PageRequest.of(0, 20), customer.getId());

        // then
        assertThat(result.getStatusCode()).isEqualTo(OK);
        assertThat(result.getBody().getTotalElements()).isEqualTo(1);
        assertThat(result.getBody().getTotalPages()).isEqualTo(1);
        assertThat(result.getBody().getContent().get(0).getId()).isEqualTo(reservation.getId());
    }

    @Test
    public void should_make_reservation() {
        // given
        Customer customer = johnDoe();
        customerRepository.save(customer);
        Room room = cocoChanelSuite();
        roomRepository.save(room);
        Instant tomorrow = now().plus(1, DAYS);
        Instant weekFromToday = now().plus(7, DAYS);
        AddReservationCmd addReservationCmd = new AddReservationCmd(customer.getId(), room.getId(), tomorrow, weekFromToday);

        // when
        ResponseEntity<ReservationDto> result = reservationController.makeReservation(addReservationCmd);

        // then
        assertThat(result.getStatusCode()).isEqualTo(CREATED);
        assertThat(result.getBody().getId()).isNotNull();
        assertThat(result.getBody().getCustomerId()).isEqualTo(customer.getId());
        assertThat(result.getBody().getRoomId()).isEqualTo(room.getId());
        assertThat(result.getBody().getPeriod()).isEqualTo(new PeriodDto(tomorrow, weekFromToday));
    }

    @Test
    public void should_cancel_reservation() {
        // given
        Customer customer = johnDoe();
        customerRepository.save(customer);
        Room room = cocoChanelSuite();
        roomRepository.save(room);
        Reservation reservation = new Reservation(customer.getId(), room.getId(), JAN_1, JAN_3);
        reservationRepository.save(reservation);

        // when
        ResponseEntity<Void> result = reservationController.cancelReservation(reservation.getId(), customer.getId());

        // then
        assertThat(result.getStatusCode()).isEqualTo(NO_CONTENT);
        assertThat(reservationRepository.findById(reservation.getId())).isEmpty();
    }

}
