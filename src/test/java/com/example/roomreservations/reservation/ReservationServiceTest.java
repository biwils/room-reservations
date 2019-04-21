package com.example.roomreservations.reservation;

import com.example.roomreservations.model.Customer;
import com.example.roomreservations.model.Reservation;
import com.example.roomreservations.model.Room;
import com.example.roomreservations.model.repository.CustomerRepository;
import com.example.roomreservations.model.repository.ReservationRepository;
import com.example.roomreservations.model.repository.RoomRepository;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static com.example.roomreservations.InstantsFixture.*;
import static com.example.roomreservations.model.CustomerFixture.johnDoe;
import static com.example.roomreservations.model.RoomFixture.cocoChanelSuite;
import static java.util.Collections.singletonList;
import static java.util.UUID.randomUUID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.rules.ExpectedException.none;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ReservationServiceTest {

    private ReservationRepository reservationRepository = mock(ReservationRepository.class);
    private CustomerRepository customerRepository = mock(CustomerRepository.class);
    private RoomRepository roomRepository = mock(RoomRepository.class);
    private ReservationService reservationService = new ReservationService(reservationRepository, customerRepository, roomRepository);

    @Rule
    public ExpectedException exceptionRule = none();

    @Test
    public void should_list_customers_reservations() {
        // given
        Pageable pageable = mock(Pageable.class);
        UUID customerId = randomUUID();
        UUID roomId = randomUUID();
        Reservation reservation = new Reservation(customerId, roomId, JAN_1, JAN_3);
        when(reservationRepository.findByCustomerId(pageable, customerId)).thenReturn(new PageImpl<>(singletonList(reservation)));

        // when
        Page<ReservationDto> result = reservationService.findAll(pageable, customerId);

        // then
        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent().get(0).getId()).isNotNull();
        assertThat(result.getContent().get(0).getCustomerId()).isEqualTo(customerId);
        assertThat(result.getContent().get(0).getRoomId()).isEqualTo(roomId);
        assertThat(result.getContent().get(0).getPeriod()).isNotNull();
        assertThat(result.getContent().get(0).getPeriod().getStartDate()).isEqualTo(reservation.getStartDate());
        assertThat(result.getContent().get(0).getPeriod().getEndDate()).isEqualTo(reservation.getEndDate());
    }

    @Test
    public void should_make_reservation() {
        // given
        Customer customer = johnDoe();
        Room room = cocoChanelSuite();
        Instant startDate = JAN_1;
        Instant endDate = JAN_3;
        AddReservationCmd cmd = new AddReservationCmd(customer.getId(), room.getId(), startDate, endDate);
        when(customerRepository.findById(cmd.getCustomerId())).thenReturn(Optional.of(customer));
        when(roomRepository.findById(cmd.getRoomId())).thenReturn(Optional.of(room));
        Reservation reservation = new Reservation(customer.getId(), room.getId(), startDate, endDate);
        when(reservationRepository.save(any())).thenReturn(reservation);

        // when
        ReservationDto result = reservationService.add(cmd);

        // then
        assertThat(result.getId()).isNotNull();
        assertThat(result.getCustomerId()).isEqualTo(customer.getId());
        assertThat(result.getRoomId()).isEqualTo(room.getId());
        assertThat(result.getPeriod()).isNotNull();
        assertThat(result.getPeriod().getStartDate()).isEqualTo(startDate);
        assertThat(result.getPeriod().getEndDate()).isEqualTo(endDate);
    }

    @Test
    public void should_reject_reservation_when_customer_not_found() {
        // given
        UUID customerId = randomUUID();
        UUID roomId = randomUUID();
        AddReservationCmd cmd = new AddReservationCmd(customerId, roomId, JAN_1, JAN_3);
        when(customerRepository.findById(cmd.getCustomerId())).thenReturn(Optional.empty());
        exceptionRule.expect(IllegalArgumentException.class);
        exceptionRule.expectMessage("Customer with id " + customerId + " not found");

        // when
        reservationService.add(cmd);

        // then exception thrown
    }

    @Test
    public void should_reject_reservation_when_room_not_found() {
        // given
        Customer customer = johnDoe();
        UUID customerId = customer.getId();
        UUID roomId = randomUUID();
        AddReservationCmd cmd = new AddReservationCmd(customerId, roomId, JAN_1, JAN_3);
        when(customerRepository.findById(cmd.getCustomerId())).thenReturn(Optional.of(customer));
        when(roomRepository.findById(cmd.getRoomId())).thenReturn(Optional.empty());
        exceptionRule.expect(IllegalArgumentException.class);
        exceptionRule.expectMessage("Room with id " + roomId + " not found");

        // when
        reservationService.add(cmd);

        // then exception thrown
    }

    @Test
    public void should_reject_reservation_when_room_unavailable() {
        // given
        Customer customer = johnDoe();
        Room room = cocoChanelSuite();
        UUID roomId = room.getId();
        room.getReservations().add(new Reservation(randomUUID(), roomId, JAN_1, JAN_7));
        Instant startDate = JAN_3;
        Instant endDate = JAN_5;
        AddReservationCmd cmd = new AddReservationCmd(customer.getId(), roomId, startDate, endDate);
        when(customerRepository.findById(cmd.getCustomerId())).thenReturn(Optional.of(customer));
        when(roomRepository.findById(cmd.getRoomId())).thenReturn(Optional.of(room));
        Reservation reservation = new Reservation(customer.getId(), roomId, startDate, endDate);
        when(reservationRepository.save(any())).thenReturn(reservation);
        exceptionRule.expect(RoomUnavailableDuringPeriodException.class);
        exceptionRule.expectMessage("Room " + roomId + " is unavailable between " + startDate + " and " + endDate);

        // when
        reservationService.add(cmd);

        // then exception thrown
    }

}