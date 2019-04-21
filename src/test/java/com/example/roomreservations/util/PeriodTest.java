package com.example.roomreservations.util;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

import java.time.Instant;

import static com.example.roomreservations.InstantsFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.rules.ExpectedException.none;

@RunWith(JUnitParamsRunner.class)
public class PeriodTest {

    @Rule
    public ExpectedException exceptionRule = none();


    @Test
    @Parameters(method = "periods")
    public void should_check_if_overlaps(Instant p1StartDate, Instant p1EndDate, Instant p2StartDate, Instant p2EndDate, boolean expected) {
        // given
        Period p1 = new Period(p1StartDate, p1EndDate);
        Period p2 = new Period(p2StartDate, p2EndDate);

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
    public void should_not_construct_period(Instant startDate, Instant endDate) {
        //given
        exceptionRule.expect(InvalidPeriodException.class);

        //when
        new Period(startDate, endDate);

        //then exception thrown
    }

    private Object[] instants() {
        return new Object[]{
                new Object[]{JAN_1, JAN_1},
                new Object[]{JAN_3, JAN_1}
        };
    }

}