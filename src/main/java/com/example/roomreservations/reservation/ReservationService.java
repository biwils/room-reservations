package com.example.roomreservations.reservation;

import com.example.roomreservations.model.Reservation;
import com.example.roomreservations.model.Room;
import com.example.roomreservations.model.repository.CustomerRepository;
import com.example.roomreservations.model.repository.ReservationRepository;
import com.example.roomreservations.model.repository.RoomRepository;
import com.example.roomreservations.util.Period;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;

class ReservationService {

    private static final Function<Reservation, ReservationDto> TO_DTO =
            reservation -> new ReservationDto(reservation.getId(), reservation.getCustomerId(), reservation.getRoomId(), new PeriodDto(reservation.getStartDate(), reservation.getEndDate()));

    private ReservationRepository reservationRepository;
    private CustomerRepository customerRepository;
    private RoomRepository roomRepository;

    ReservationService(ReservationRepository reservationRepository, CustomerRepository customerRepository, RoomRepository roomRepository) {
        this.reservationRepository = reservationRepository;
        this.customerRepository = customerRepository;
        this.roomRepository = roomRepository;
    }

    @Transactional(readOnly = true)
    public Page<ReservationDto> findAll(Pageable pageable, UUID customerId) {
        Page<Reservation> page = reservationRepository.findByCustomerId(pageable, customerId);
        List<ReservationDto> reservations = page.stream()
                .map(ReservationService.TO_DTO)
                .collect(toList());
        return new PageImpl<>(reservations, pageable, page.getTotalElements());
    }

    @Transactional
    public ReservationDto add(AddReservationCmd addReservationCmd) {
        Instant startDate = addReservationCmd.getStartDate();
        Instant endDate = addReservationCmd.getEndDate();
        Period period = new Period(startDate, endDate);
        UUID customerId = addReservationCmd.getCustomerId();
        customerRepository.findById(customerId).orElseThrow(() -> new IllegalArgumentException("Customer with id " + customerId + " not found"));
        UUID roomId = addReservationCmd.getRoomId();
        Room room = roomRepository.findById(roomId).orElseThrow(() -> new IllegalArgumentException("Room with id " + roomId + " not found"));

        if (!room.isAvailableWithin(period)) {
            throw new RoomUnavailableDuringPeriodException(roomId, period);
        }

        Reservation reservation = reservationRepository.save(new Reservation(customerId, roomId, startDate, endDate));
        return TO_DTO.apply(reservation);
    }

    @Transactional
    public void delete(UUID reservationId, UUID customerId) {
        reservationRepository.findById(reservationId).ifPresent(reservation -> {
            if (customerId.equals(reservation.getCustomerId())) {
                reservationRepository.delete(reservation);
            } else {
                throw new ReservationBelongsToDifferentCustomerException(reservationId, customerId);
            }
        });
    }

}
