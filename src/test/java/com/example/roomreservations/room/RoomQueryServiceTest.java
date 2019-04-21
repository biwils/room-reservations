package com.example.roomreservations.room;

import com.example.roomreservations.model.Room;
import com.example.roomreservations.model.repository.RoomRepository;
import com.example.roomreservations.util.Period;
import org.junit.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;

import static com.example.roomreservations.InstantsFixture.JAN_1;
import static com.example.roomreservations.InstantsFixture.JAN_7;
import static com.example.roomreservations.model.RoomFixture.cocoChanelSuite;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RoomQueryServiceTest {

    private RoomRepository roomRepository = mock(RoomRepository.class);
    private RoomQueryService roomQueryService = new RoomQueryService(roomRepository);

    @Test
    public void should_find_available_room() {
        // given
        Pageable pageable = mock(Pageable.class);
        Period period = new Period(JAN_1, JAN_7);
        String city = "Paris";
        BigDecimal minPrice = new BigDecimal(100.00);
        BigDecimal maxPrice = new BigDecimal(1000.00);
        Room room = cocoChanelSuite();
        when(roomRepository.findByPeriodAndCityAndPrice(pageable, period.getStartDate(), period.getEndDate(), city, minPrice, maxPrice))
                .thenReturn(new PageImpl<>(singletonList(room)));

        // when
        Page<RoomDto> result = roomQueryService.find(pageable, period, city, minPrice, maxPrice);

        // then
        assertThat(result.getContent()).hasSize(1);
        RoomDto dto = result.getContent().get(0);
        assertThat(dto.getId()).isEqualTo(room.getId());
        assertThat(dto.getNumber()).isEqualTo(room.getNumber());
        assertThat(dto.getPrice()).isEqualTo(room.getPrice());
        assertThat(dto.getCity()).isEqualTo(room.getCity());
        assertThat(dto.getHotel()).isEqualTo(room.getHotel());
    }

}