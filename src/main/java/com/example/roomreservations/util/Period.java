package com.example.roomreservations.util;

import lombok.Getter;

import javax.validation.constraints.NotNull;
import java.time.Instant;

@Getter
public class Period {

    private Instant start;

    private Instant end;

    public Period(@NotNull Instant start, @NotNull Instant end) {
        if (start.equals(end) || start.isAfter(end)) {
            throw new InvalidPeriodException(start, end);
        }
        this.start = start;
        this.end = end;
    }

    public boolean overlaps(Period other) {
        return !this.end.isBefore(other.start)
                && !this.end.equals(other.start)
                && !this.start.equals(other.end)
                && !this.start.isAfter(other.end);
    }

}
