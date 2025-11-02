package org.example.payment;

public interface PaymentProcessor {
    String getName();

    String id();
    PaymentResult processPayment(PaymentRequest paymentRequest);
}
