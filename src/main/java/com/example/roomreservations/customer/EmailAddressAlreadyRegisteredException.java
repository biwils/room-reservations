package com.example.roomreservations.customer;

import org.springframework.web.bind.annotation.ResponseStatus;

import static java.lang.String.format;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ResponseStatus(BAD_REQUEST)
class EmailAddressAlreadyRegisteredException extends IllegalArgumentException {

    EmailAddressAlreadyRegisteredException(String emailAddress) {
        super(format("Customer with email address %s is already registered", emailAddress));
    }

}
