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
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.Instant;

import static com.example.roomreservations.InstantsFixture.JAN_1;
import static com.example.roomreservations.InstantsFixture.JAN_3;
import static com.example.roomreservations.model.CustomerFixture.johnDoe;
import static com.example.roomreservations.model.RoomFixture.cocoChanelSuite;
import static java.time.Instant.now;
import static java.time.temporal.ChronoUnit.DAYS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class ReservationControllerIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Test
    public void should_find_customers_reservations() throws Exception {
        // given
        Customer customer = johnDoe();
        customerRepository.save(customer);
        Room room = cocoChanelSuite();
        roomRepository.save(room);
        Reservation reservation = new Reservation(customer.getId(), room.getId(), JAN_1, JAN_3);
        reservationRepository.save(reservation);

        RequestBuilder request = MockMvcRequestBuilders
                .get("/reservation")
                .header("customerId", customer.getId().toString())
                .accept(APPLICATION_JSON);

        // when
        ResultActions result = mvc.perform(request);

        // then
        result
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].id", equalTo(reservation.getId().toString())));
    }

    @Test
    public void should_make_reservation() throws Exception {
        // given
        Customer customer = johnDoe();
        customerRepository.save(customer);
        Room room = cocoChanelSuite();
        roomRepository.save(room);
        Instant tomorrow = now().plus(1, DAYS);
        Instant weekFromToday = now().plus(7, DAYS);
        AddReservationCmd addReservationCmd = new AddReservationCmd(customer.getId(), room.getId(), tomorrow, weekFromToday);

        RequestBuilder request = MockMvcRequestBuilders
                .post("/reservation")
                .contentType(APPLICATION_JSON_UTF8_VALUE)
                .content(objectMapper.writeValueAsString(addReservationCmd))
                .accept(APPLICATION_JSON);

        // when
        ResultActions result = mvc.perform(request);

        // then
        result
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.customerId", equalTo(customer.getId().toString())))
                .andExpect(jsonPath("$.roomId", equalTo(room.getId().toString())))
                .andExpect(jsonPath("$.period.startDate", equalTo(tomorrow.toString())))
                .andExpect(jsonPath("$.period.endDate", equalTo(weekFromToday.toString())));
    }

    @Test
    public void should_cancel_reservation() throws Exception {
        // given
        Customer customer = johnDoe();
        customerRepository.save(customer);
        Room room = cocoChanelSuite();
        roomRepository.save(room);
        Reservation reservation = new Reservation(customer.getId(), room.getId(), JAN_1, JAN_3);
        reservationRepository.save(reservation);

        RequestBuilder request = MockMvcRequestBuilders
                .delete("/reservation/{reservationId}", reservation.getId())
                .header("customerId", customer.getId().toString())
                .accept(APPLICATION_JSON);

        // when
        ResultActions result = mvc.perform(request);

        // then
        result.andExpect(status().isNoContent());
        assertThat(reservationRepository.findById(reservation.getId())).isEmpty();
    }

}
