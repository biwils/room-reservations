package com.example.roomreservations.room;

import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.FORBIDDEN;

@ResponseStatus(FORBIDDEN)
class SearchingForRoomInThePastException extends IllegalArgumentException {

    static final String MESSAGE = "Why would anyone want to book a room for the past?";

    SearchingForRoomInThePastException() {
        super(MESSAGE);
    }

}
