package com.example.softwaretestingproject.payment.stripe;

import com.example.softwaretestingproject.payment.CardPaymentCharge;
import com.example.softwaretestingproject.payment.CardPaymentCharger;
import com.example.softwaretestingproject.payment.Currency;
import com.stripe.exception.StripeException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
@Service
@ConditionalOnProperty(
        value = "stripe.enabled",
        havingValue = "false"
)
public class MockStripeService implements CardPaymentCharger {
    @Override
    public CardPaymentCharge chargeCard(String cardSource,
                                        BigDecimal amount,
                                        Currency currency,
                                        String description) throws StripeException {

        return new CardPaymentCharge(true);
    }
}
