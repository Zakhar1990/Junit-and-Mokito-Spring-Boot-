package com.example.softwaretestingproject.payment;

import com.stripe.exception.StripeException;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
public interface CardPaymentCharger {

    CardPaymentCharge chargeCard(
            String cardSource,
            BigDecimal amount,
            Currency currency,
            String description
    ) throws StripeException;
}
