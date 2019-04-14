package com.example.roomreservations.customer;

import com.example.roomreservations.model.Customer;

class CustomerFixture {

    static Customer johnDoe() {
        return new Customer("john@example.com", "John", "Doe");
    }

}
