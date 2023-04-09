package com.example.softwaretestingproject.customer;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Optional;
import java.util.UUID;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.hamcrest.MatcherAssert.assertThat;


@DataJdbcTest(
        properties = {
                "spring.jpa.properties.javax.persistence.validation.mode=none"
        }
)
class CustomerRepositoryTest {
    @Autowired
    private CustomerRepository underTest;

    @Test
    void itShouldSelectCustomerByPhoneNumber() {
        // Given
        // When
        // Then
    }

    @Test
    void itShouldSaveCustomer() {
        // Given
        UUID id = UUID.randomUUID();
        Customer customer = new Customer(id, "Abel", "0000");

        // When
        underTest.save(customer);

        // Then
        Optional<Customer> optionalCustomer = underTest.findById(id);
        assertThat(optionalCustomer)
                .isPresent()
                .hasValueSatisfying(c->{
                    assertThat(c).isEqualTo(customer);
                })
        ;
    }

    @Test
    void itShouldNotSaveCustomerWhenNameIsNull() {
        // Given
        UUID id = UUID.randomUUID();
        Customer customer = new Customer(id, null, "0000");

        // When
        underTest.save(customer);

        // Then
        assertThatThrownBy(()-> underTest.save(customer))
                .hasMessageContaining("not-null property references a null or transient value: Name ")
                .isInstanceOf(DataIntegrityViolationException.class);
    }
    void itShouldNotSaveCustomerWhenPhoneIsNull() {
        // Given
        UUID id = UUID.randomUUID();
        Customer customer = new Customer(id, "Alex", null);

        // When
        underTest.save(customer);

        // Then
        assertThatThrownBy(()-> underTest.save(customer))
                .hasMessageContaining("not-null property references a null or transient value: Phone")
                .isInstanceOf(DataIntegrityViolationException.class);
    }
}