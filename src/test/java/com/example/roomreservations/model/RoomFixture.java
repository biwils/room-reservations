package com.example.roomreservations.model;

import java.math.BigDecimal;
import java.util.ArrayList;

public class RoomFixture {

    public static Room cocoChanelSuite() {
        return new Room(200, new BigDecimal(40000.00), "Paris", "Ritz", new ArrayList<>());
    }

}
