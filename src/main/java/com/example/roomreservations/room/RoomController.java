package com.example.roomreservations.room;

import com.example.roomreservations.util.Period;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.Instant;

import static java.time.Instant.now;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(path = "/room", produces = APPLICATION_JSON_VALUE)
public class RoomController {

    private RoomQueryService roomQueryService;

    public RoomController(RoomQueryService roomQueryService) {
        this.roomQueryService = roomQueryService;
    }

    @GetMapping
    ResponseEntity<Page<RoomDto>> search(Pageable pageable,
                                         @RequestParam String city,
                                         @RequestParam Instant startDate,
                                         @RequestParam Instant endDate,
                                         @RequestParam BigDecimal minPrice,
                                         @RequestParam BigDecimal maxPrice) {
        if (startDate.isBefore(now())) {
            throw new IllegalArgumentException("Why would anyone want to book a room for the past?");
        }
        Period period = new Period(startDate, endDate);
        return new ResponseEntity<>(roomQueryService.find(pageable, period, city, minPrice, maxPrice), OK);
    }

}
