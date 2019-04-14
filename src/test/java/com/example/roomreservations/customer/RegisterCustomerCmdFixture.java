package com.example.roomreservations.customer;

class RegisterCustomerCmdFixture {

    static RegisterCustomerCmd johnDoe() {
        return new RegisterCustomerCmd("john@example.com", "John", "Doe");
    }

}