package com.example.softwaretestingproject.payment.stripe;

import com.example.softwaretestingproject.customer.Customer;
import com.example.softwaretestingproject.customer.CustomerRegistrationRequest;
import com.example.softwaretestingproject.payment.Currency;
import com.example.softwaretestingproject.payment.Payment;
import com.example.softwaretestingproject.payment.PaymentRepository;
import com.example.softwaretestingproject.payment.PaymentRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;



import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class PaymentIntegrationTest {
    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void itShouldCreatePaymentSuccessfully() throws Exception {
        // Given
        UUID customerId = UUID.randomUUID();
        Customer customer = new Customer(customerId, "James", "+781274692347");

        CustomerRegistrationRequest customerRegistrationRequest = new CustomerRegistrationRequest(customer);
        ResultActions customerRegResultActions = mockMvc.perform(put("/api/v1/customer-registration")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Objects.requireNonNull(objectToJson(customerRegistrationRequest)))
        );

        long paymentId = 1L;
        Payment payment = new Payment(paymentId,
                customerId,
                new BigDecimal("100.00"),
                Currency.GBP,
                "x0x0x0x0",
                "Zakat");
        // ... Payment request
        PaymentRequest paymentRequest = new PaymentRequest(payment);

        // ... When payment is sent
        ResultActions resultActions = mockMvc.perform(post("api/v1/payment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Objects.requireNonNull(objectToJson(paymentRequest))));

        // Then both customer registration and requests are 200 status code
        customerRegResultActions.andExpect(status().isOk());
        resultActions.andExpect(status().isOk());

        // Payment is stored in db
        // TODO: Do not use paymentRepository instead create an endpoint to retrieve payment for customers
       assertThat(paymentRepository.findById(paymentId))
               .isPresent()
               .hasValueSatisfying(p->assertThat(p).isEqualToComparingFieldByField(payment));

       // TODO: Ensure sms is delivered
    }

    private String objectToJson(Object object) {
        try{
            return new ObjectMapper().writeValueAsString(object);
        }catch (JsonProcessingException e){
            fail("Failer to convert object to json");
            return null;
        }
    }
}
