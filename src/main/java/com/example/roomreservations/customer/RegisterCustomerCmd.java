package com.example.roomreservations.customer;

import lombok.NonNull;
import lombok.Value;

@Value
class RegisterCustomerCmd {

    @NonNull
    private String emailAddress;

    @NonNull
    private String firstName;

    @NonNull
    private String lastName;

}
