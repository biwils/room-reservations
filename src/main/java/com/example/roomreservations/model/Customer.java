package com.example.roomreservations.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

import static java.util.UUID.randomUUID;

@EqualsAndHashCode(of = "id")
@NoArgsConstructor //for Hibernate
@Getter
@Entity
@Table(name = "customers")
public class Customer {

    @Id
    private UUID id = randomUUID();

    private String emailAddress;

    private String firstName;

    private String lastName;

    public Customer(String emailAddress, String firstName, String lastName) {
        this.emailAddress = emailAddress;
        this.firstName = firstName;
        this.lastName = lastName;
    }

}
