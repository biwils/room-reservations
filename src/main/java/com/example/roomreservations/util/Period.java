package com.example.roomreservations.util;

import lombok.Getter;

import javax.validation.constraints.NotNull;
import java.time.Instant;

@Getter
public class Period {

    private Instant startDate;

    private Instant endDate;

    public Period(@NotNull Instant startDate, @NotNull Instant endDate) {
        if (startDate.equals(endDate) || startDate.isAfter(endDate)) {
            throw new InvalidPeriodException(startDate, endDate);
        }
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public boolean overlaps(Period other) {
        return !this.endDate.isBefore(other.startDate)
                && !this.endDate.equals(other.startDate)
                && !this.startDate.equals(other.endDate)
                && !this.startDate.isAfter(other.endDate);
    }

}
