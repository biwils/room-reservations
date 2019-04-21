package com.example.roomreservations.room;

import com.example.roomreservations.BaseIntegrationTest;
import com.example.roomreservations.model.Customer;
import com.example.roomreservations.model.Reservation;
import com.example.roomreservations.model.Room;
import com.example.roomreservations.model.repository.CustomerRepository;
import com.example.roomreservations.model.repository.ReservationRepository;
import com.example.roomreservations.model.repository.RoomRepository;
import com.example.roomreservations.util.Period;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.time.Instant;

import static com.example.roomreservations.InstantsFixture.*;
import static com.example.roomreservations.model.CustomerFixture.johnDoe;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;


public class RoomQueryServiceIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private RoomQueryService roomQueryService;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    public void should_find_available_room() {
        // given
        Customer customer = johnDoe();
        customerRepository.save(customer);

        String city = "Dubai";

        Room availableRoom = new Room(100, new BigDecimal(5000.00), city, "Burj Al Arab", emptyList());
        roomRepository.save(availableRoom);

        unavailableRoom(customer, city, 101, JAN_1, JAN_7);
        unavailableRoom(customer, city, 102, JAN_9, JAN_15);
        unavailableRoom(customer, city, 103, JAN_7, JAN_9);
        unavailableRoom(customer, city, 104, JAN_3, JAN_13);

        // when
        Page<RoomDto> result = roomQueryService.find(PageRequest.of(0, 20), new Period(JAN_5, JAN_11), city, new BigDecimal(100.00), new BigDecimal(10000.00));

        // then
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getId()).isEqualTo(availableRoom.getId());
    }

    private void unavailableRoom(Customer customer, String city, Integer roomNumber, Instant startDate, Instant endDate) {
        Room room = new Room(roomNumber, new BigDecimal(5000.00), city, "Burj Al Arab", emptyList());
        roomRepository.save(room);
        Reservation reservation = new Reservation(customer.getId(), room.getId(), startDate, endDate);
        reservationRepository.save(reservation);
    }

}
