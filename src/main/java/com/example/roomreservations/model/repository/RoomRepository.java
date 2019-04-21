package com.example.roomreservations.model.repository;

import com.example.roomreservations.model.Room;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Repository
public interface RoomRepository extends JpaRepository<Room, UUID> {

    @Query("select ro from Room ro" +
            " where ro.city = :city" +
            " and ro.price > :minPrice" +
            " and ro.price < :maxPrice" +
            " and not exists (" +
            "   select 1 from Reservation re" +
            "       where re.roomId = ro.id" +
            "       and re.endDate > :startDate" +
            "       and re.startDate < :endDate" +
            ")")
    Page<Room> findByPeriodAndCityAndPrice(Pageable pageable,
                                           @Param("startDate") Instant startDate,
                                           @Param("endDate") Instant endDate,
                                           @Param("city") String city,
                                           @Param("minPrice") BigDecimal minPrice,
                                           @Param("maxPrice") BigDecimal maxPrice);

}
