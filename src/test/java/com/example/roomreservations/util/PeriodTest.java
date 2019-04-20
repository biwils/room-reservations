package com.example.roomreservations.util;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

import java.time.Instant;

import static java.time.Instant.parse;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.rules.ExpectedException.none;

@RunWith(JUnitParamsRunner.class)
public class PeriodTest {

    private static final Instant JAN_1 = parse("2019-01-01T10:00:00Z");
    private static final Instant JAN_3 = parse("2019-01-03T10:00:00Z");
    private static final Instant JAN_5 = parse("2019-01-05T10:00:00Z");
    private static final Instant JAN_7 = parse("2019-01-07T10:00:00Z");
    private static final Instant JAN_9 = parse("2019-01-09T10:00:00Z");
    private static final Instant JAN_11 = parse("2019-01-11T10:00:00Z");
    private static final Instant JAN_13 = parse("2019-01-13T10:00:00Z");
    private static final Instant JAN_15 = parse("2019-01-15T10:00:00Z");

    @Rule
    public ExpectedException exceptionRule = none();


    @Test
    @Parameters(method = "periods")
    public void should_check_if_overlaps(Instant p1Start, Instant p1End, Instant p2Start, Instant p2End, boolean expected) {
        // given
        Period p1 = new Period(p1Start, p1End);
        Period p2 = new Period(p2Start, p2End);

        // when
        boolean result = p1.overlaps(p2);

        // then
        assertThat(result).isEqualTo(expected);
    }

    private Object[] periods() {
        return new Object[]{
                new Object[]{JAN_1, JAN_3, JAN_5, JAN_11, false},
                new Object[]{JAN_1, JAN_3, JAN_3, JAN_11, false},
                new Object[]{JAN_13, JAN_15, JAN_5, JAN_11, false},
                new Object[]{JAN_13, JAN_15, JAN_5, JAN_13, false},
                new Object[]{JAN_3, JAN_7, JAN_5, JAN_11, true},
                new Object[]{JAN_5, JAN_7, JAN_5, JAN_11, true},
                new Object[]{JAN_9, JAN_13, JAN_5, JAN_11, true},
                new Object[]{JAN_9, JAN_11, JAN_5, JAN_11, true},
                new Object[]{JAN_7, JAN_9, JAN_5, JAN_11, true},
                new Object[]{JAN_7, JAN_11, JAN_5, JAN_11, true},
                new Object[]{JAN_5, JAN_9, JAN_5, JAN_11, true},
                new Object[]{JAN_5, JAN_9, JAN_5, JAN_9, true}
        };
    }

    @Test
    @Parameters(method = "instants")
    public void should_not_construct_period(Instant start, Instant end) {
        //given
        exceptionRule.expect(InvalidPeriodException.class);

        //when
        new Period(start, end);

        //then exception thrown
    }

    private Object[] instants() {
        return new Object[]{
                new Object[]{JAN_1, JAN_1},
                new Object[]{JAN_3, JAN_1}
        };
    }

}