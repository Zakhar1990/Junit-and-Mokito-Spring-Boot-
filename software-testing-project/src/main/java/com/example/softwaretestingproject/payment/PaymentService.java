package com.example.softwaretestingproject.payment;

import com.example.softwaretestingproject.customer.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class PaymentService {

    private static List<Currency> ACCEPTED_CURRENCIES = List.of(Currency.USD,Currency.GBP);

    private final CustomerRepository customerRepository;
    private final PaymentRepository paymentRepository;
    private final  CardPaymentCharger cardPaymentCharger;

    public PaymentService(CustomerRepository customerRepository, PaymentRepository paymentRepository, CardPaymentCharger cardPaymentCharger) {
        this.customerRepository = customerRepository;
        this.paymentRepository = paymentRepository;
        this.cardPaymentCharger = cardPaymentCharger;
    }
    void chargeCard(UUID customerId, PaymentRequest paymentRequest){

        //1. Does customer exist if not throw
        boolean isCustomerFound = customerRepository.findById(customerId).isPresent();
        if (!isCustomerFound){
            throw new IllegalStateException(String.format("Customer with id [%s] not found", customerId));
        }

        // 2. Do we support the current if not throw
        boolean isCurrencySupported = ACCEPTED_CURRENCIES.contains(paymentRequest.getPayment().getCurrency());

        if (!isCurrencySupported){
            String  message = String.format(
                    "Currency [%s] not supported",
                    paymentRequest.getPayment().getCurrency());
                    throw new IllegalStateException(message);

        }

        // 3. Charge card

        CardPaymentCharge cardPaymentCharge = cardPaymentCharger.chargeCard(
                paymentRequest.getPayment().getSource(),
                paymentRequest.getPayment().getAmount(),
                paymentRequest.getPayment().getCurrency(),
                paymentRequest.getPayment().getDescription()
        );

        // 4. If not debited throw
        if (!cardPaymentCharge.isCardDebited){
            throw  new IllegalStateException(String.format("Card not debited for customer %s", customerId));
        }

        // 5. Insert payment
        paymentRequest.getPayment().setCustomerId(customerId);

        paymentRepository.save(paymentRequest.getPayment());
    }


}
