package com.example.roomreservations.room;


import com.example.roomreservations.BaseIntegrationTest;
import com.example.roomreservations.model.Room;
import com.example.roomreservations.model.repository.RoomRepository;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.stream.IntStream;

import static com.example.roomreservations.room.SearchingForRoomInThePastException.MESSAGE;
import static java.time.Instant.now;
import static java.time.temporal.ChronoUnit.DAYS;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.rules.ExpectedException.none;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.OK;


public class RoomControllerIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private RoomController roomController;

    @Autowired
    private RoomRepository roomRepository;

    @Test
    public void should_search_for_a_room() {
        // given
        String city = "Dubai";
        IntStream.range(100, 115).forEach(i -> availableRoom(city, i));
        Instant tomorrow = now().plus(1, DAYS);
        Instant weekFromToday = now().plus(7, DAYS);

        // when
        ResponseEntity<Page<RoomDto>> firstPage = roomController.search(PageRequest.of(0, 10), city, tomorrow, weekFromToday, new BigDecimal(100.00), new BigDecimal(10000.00));


        // then
        assertThat(firstPage.getStatusCode()).isEqualTo(OK);
        assertThat(firstPage.getBody().getContent()).hasSize(10);
        assertThat(firstPage.getBody().getTotalElements()).isEqualTo(15);
        assertThat(firstPage.getBody().getTotalPages()).isEqualTo(2);

        // when
        ResponseEntity<Page<RoomDto>> secondPage = roomController.search(PageRequest.of(1, 10), city, tomorrow, weekFromToday, new BigDecimal(100.00), new BigDecimal(10000.00));

        // then
        assertThat(secondPage.getStatusCode()).isEqualTo(OK);
        assertThat(secondPage.getBody().getContent()).hasSize(5);
        assertThat(secondPage.getBody().getTotalElements()).isEqualTo(15);
        assertThat(secondPage.getBody().getTotalPages()).isEqualTo(2);
    }

    private void availableRoom(String city, Integer number) {
        Room availableRoom = new Room(number, new BigDecimal(5000.00), city, "Burj Al Arab", emptyList());
        roomRepository.save(availableRoom);
    }

    @Rule
    public ExpectedException exceptionRule = none();

    @Test
    public void should_not_search_in_the_past() {
        // given
        Instant yesterday = now().minus(1, DAYS);
        Instant weekFromToday = now().plus(7, DAYS);
        exceptionRule.expect(SearchingForRoomInThePastException.class);
        exceptionRule.expectMessage(MESSAGE);

        // when
        ResponseEntity<Page<RoomDto>> result = roomController.search(PageRequest.of(1, 10), "Paris", yesterday, weekFromToday, new BigDecimal(100.00), new BigDecimal(10000.00));

        // then
        assertThat(result.getStatusCode()).isEqualTo(FORBIDDEN);
        System.out.println(result.toString());
        assertThat(result.toString()).contains(MESSAGE);
    }

}
