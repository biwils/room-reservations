package com.example.roomreservations.customer;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(path = "/customer", produces = APPLICATION_JSON_VALUE)
class CustomerController {

    private CustomerService customerService;

    CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping
    ResponseEntity<CustomerDto> register(@RequestBody @Valid RegisterCustomerCmd registerCustomerCmd) {
        return new ResponseEntity<>(customerService.add(registerCustomerCmd), CREATED);
    }

}
