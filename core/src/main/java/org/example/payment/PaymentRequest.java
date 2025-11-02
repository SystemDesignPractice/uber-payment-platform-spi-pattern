package org.example.payment;

public class PaymentRequest {
    private final String orderId;
    private final double amount;
    private final String currency;

    public PaymentRequest(String orderId, double amount, String currency) {
        this.orderId = orderId;
        this.amount = amount;
        this.currency = currency;
    }

    public String getOrderId() {
        return orderId;
    }

    public double getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }
}
