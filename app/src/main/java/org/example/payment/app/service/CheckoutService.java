package org.example.payment.app.service;

import org.example.payment.PaymentProcessor;
import org.example.payment.PaymentRequest;
import org.example.payment.PaymentResult;
import org.example.payment.app.PaymentProcessorRegistry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class CheckoutService {

    private final PaymentProcessorRegistry registry;
    private final String defaultProviderId;

    public CheckoutService(PaymentProcessorRegistry registry, @Value("$payment.default:stripe") String defaultProviderId) {
        this.registry = registry;
        this.defaultProviderId = defaultProviderId;
    }

    public PaymentResult checkout(PaymentRequest request, String selectedProviderId){
        String providerId = (selectedProviderId != null && !selectedProviderId.isEmpty()) ? selectedProviderId : defaultProviderId;
        PaymentProcessor p = registry.getProcessorById(providerId).orElseThrow(
                () -> new IllegalArgumentException("No payment processor found for id : "+selectedProviderId)
        );
        return p.processPayment(request);
    }
}
