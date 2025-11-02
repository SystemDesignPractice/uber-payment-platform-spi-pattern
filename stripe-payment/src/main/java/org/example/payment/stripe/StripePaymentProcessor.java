package org.example.payment.stripe;

import org.example.payment.PaymentProcessor;
import org.example.payment.PaymentRequest;
import org.example.payment.PaymentResult;

public class StripePaymentProcessor implements PaymentProcessor {

    @Override
    public String getName() {
        return "Stripe (simulated)";
    }

    @Override
    public String id() {
        return "stripe";
    }

    @Override
    public PaymentResult processPayment(PaymentRequest paymentRequest) {
        String tx = "tx-stripe_"+System.currentTimeMillis();
        return new PaymentResult(true,tx,"Stripe Payment processed successfully");
    }

}
