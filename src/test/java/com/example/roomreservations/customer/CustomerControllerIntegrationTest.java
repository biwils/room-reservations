package com.example.roomreservations.customer;


import com.example.roomreservations.BaseIntegrationTest;
import org.junit.Test;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class CustomerControllerIntegrationTest extends BaseIntegrationTest {

    @Test
    // This is quite unpopular method naming convention (I mean the snake_case in Java) but I find it better readable when
    // we treat tests as specs (and they tend to have more descriptive names than just a few words for which camelCase is good enough).
    // However I'm not overly attached to it.
    public void should_register_customer() throws Exception {
        // given
        String emailAddress = "john@example.com";
        String firstName = "John";
        String lastName = "Doe";
        RegisterCustomerCmd registerCustomerCmd = new RegisterCustomerCmd(emailAddress, firstName, lastName);

        RequestBuilder request = MockMvcRequestBuilders
                .post("/customer")
                .contentType(APPLICATION_JSON_UTF8_VALUE)
                .content(objectMapper.writeValueAsString(registerCustomerCmd))
                .accept(APPLICATION_JSON);

        // when
        ResultActions result = mvc.perform(request);

        // then
        result
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.emailAddress", equalTo(emailAddress)))
                .andExpect(jsonPath("$.firstName", equalTo(firstName)))
                .andExpect(jsonPath("$.lastName", equalTo(lastName)));
    }

}
