package org.example.payment.paypal;

import org.example.payment.PaymentRequest;
import org.example.payment.PaymentResult;
import org.example.payment.PaymentProcessor;

public class PaypalPaymentProcessor implements PaymentProcessor {

    @Override
    public String getName() {
        return "Paypal (Simulated)";
    }

    @Override
    public String id() {
        return "paypal";
    }

    @Override
    public PaymentResult processPayment(PaymentRequest paymentRequest) {
        String tx = "tx-paypal_"+System.currentTimeMillis();
        return new PaymentResult(true,tx,"Paypal Payment processed successfully");
    }

}
