package com.example.roomreservations.customer;

import lombok.Value;

import java.util.UUID;

@Value
class CustomerDto {

    private UUID id;

    private String emailAddress;

    private String firstName;

    private String lastName;

}
