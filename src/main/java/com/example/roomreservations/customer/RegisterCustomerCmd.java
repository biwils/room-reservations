package com.example.roomreservations.customer;

import lombok.Value;

import javax.validation.constraints.NotNull;

@Value
class RegisterCustomerCmd {

    @NotNull
    private String emailAddress;

    @NotNull
    private String firstName;

    @NotNull
    private String lastName;

}
