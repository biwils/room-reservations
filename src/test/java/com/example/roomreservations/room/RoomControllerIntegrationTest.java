package com.example.roomreservations.room;


import com.example.roomreservations.BaseIntegrationTest;
import com.example.roomreservations.model.Room;
import com.example.roomreservations.model.repository.RoomRepository;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.stream.IntStream;

import static java.time.Instant.now;
import static java.time.temporal.ChronoUnit.DAYS;
import static java.util.Collections.emptyList;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class RoomControllerIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private RoomController roomController;

    @Autowired
    private RoomRepository roomRepository;

    @Test
    public void should_search_for_a_room() throws Exception {
        // given
        String city = "Dubai";
        IntStream.range(100, 115).forEach(i -> availableRoom(city, i));
        Instant tomorrow = now().plus(1, DAYS);
        Instant weekFromToday = now().plus(7, DAYS);
        BigDecimal minPrice = new BigDecimal(100.00);
        BigDecimal maxPrice = new BigDecimal(10000.00);

        RequestBuilder firstPageRequest = MockMvcRequestBuilders
                .get("/room/search")
                .param("city", city)
                .param("startDate", tomorrow.toString())
                .param("endDate", weekFromToday.toString())
                .param("minPrice", minPrice.toString())
                .param("maxPrice", maxPrice.toString())
                .param("page", "0")
                .param("size", "10")
                .accept(APPLICATION_JSON);

        RequestBuilder secondPageRequest = MockMvcRequestBuilders
                .get("/room/search")
                .param("city", city)
                .param("startDate", tomorrow.toString())
                .param("endDate", weekFromToday.toString())
                .param("minPrice", minPrice.toString())
                .param("maxPrice", maxPrice.toString())
                .param("page", "1")
                .param("size", "10")
                .accept(APPLICATION_JSON);

        // when
        ResultActions firstPageResult = mvc.perform(firstPageRequest);

        // then
        firstPageResult
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(10)))
                .andExpect(jsonPath("$.number", equalTo(0)))
                .andExpect(jsonPath("$.totalElements", equalTo(15)))
                .andExpect(jsonPath("$.totalPages", equalTo(2)));

        // when
        ResultActions secondPageResult = mvc.perform(secondPageRequest);

        // then
        secondPageResult
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(5)))
                .andExpect(jsonPath("$.number", equalTo(1)))
                .andExpect(jsonPath("$.totalElements", equalTo(15)))
                .andExpect(jsonPath("$.totalPages", equalTo(2)));
    }

    private void availableRoom(String city, Integer number) {
        Room availableRoom = new Room(number, new BigDecimal(5000.00), city, "Burj Al Arab", emptyList());
        roomRepository.save(availableRoom);
    }

    @Test
    public void should_not_search_in_the_past() throws Exception {
        // given
        Instant yesterday = now().minus(1, DAYS);
        Instant weekFromToday = now().plus(7, DAYS);
        BigDecimal minPrice = new BigDecimal(100.00);
        BigDecimal maxPrice = new BigDecimal(10000.00);

        RequestBuilder request = MockMvcRequestBuilders
                .get("/room/search")
                .param("city", "Paris")
                .param("startDate", yesterday.toString())
                .param("endDate", weekFromToday.toString())
                .param("minPrice", minPrice.toString())
                .param("maxPrice", maxPrice.toString())
                .accept(APPLICATION_JSON);

        // when
        ResultActions result = mvc.perform(request);

        // then
        result
                .andExpect(status().isForbidden());
    }

}
