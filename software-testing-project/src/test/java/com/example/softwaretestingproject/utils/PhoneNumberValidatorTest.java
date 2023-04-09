package com.example.softwaretestingproject.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;
class PhoneNumberValidatorTest {

    private PhoneNumberValidator underTest;

    @BeforeEach
    void setUp() {
        underTest = new PhoneNumberValidator();
    }

    @ParameterizedTest
    @CsvSource({"+781274692347, TRUE"})
    void itShouldValidatePhoneNumber(String phoneNumber, String expected) {
                // When
        boolean isValid =  underTest.test(phoneNumber);

        // Then
        assertThat(isValid).isEqualTo(Boolean.valueOf(expected));
    }
 }
