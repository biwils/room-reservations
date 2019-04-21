package com.example.roomreservations.room;

import com.example.roomreservations.model.Room;
import com.example.roomreservations.model.repository.RoomRepository;
import com.example.roomreservations.util.Period;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;

class RoomQueryService {

    private static final Function<Room, RoomDto> TO_DTO =
            room -> new RoomDto(room.getId(), room.getNumber(), room.getPrice(), room.getCity(), room.getHotel());

    private RoomRepository roomRepository;

    RoomQueryService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    @Transactional(readOnly = true)
    public Page<RoomDto> find(Pageable pageable, Period period, String city, BigDecimal minPrice, BigDecimal maxPrice) {
        Page<Room> page = roomRepository.findByPeriodAndCityAndPrice(pageable, period.getStartDate(), period.getEndDate(), city, minPrice, maxPrice);
        List<RoomDto> rooms = page.stream()
                .map(TO_DTO)
                .collect(toList());
        return new PageImpl<>(rooms, pageable, page.getTotalElements());
    }

}
