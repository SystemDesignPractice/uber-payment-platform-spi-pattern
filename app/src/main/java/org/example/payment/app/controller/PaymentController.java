package org.example.payment.app.controller;

import org.example.payment.PaymentRequest;
import org.example.payment.PaymentResult;
import org.example.payment.app.PaymentProcessorRegistry;
import org.example.payment.app.service.CheckoutService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class PaymentController {
    private final PaymentProcessorRegistry registry;
    private final CheckoutService checkoutService;

    public PaymentController(PaymentProcessorRegistry registry, CheckoutService checkoutService) {
        this.registry = registry;
        this.checkoutService = checkoutService;
    }

    @GetMapping("/providers")
    public List<?> providers(){
        return registry.all().stream()
                .map(
                        p -> new Object(){
                            public final String id = p.id();
                            public final String name = p.getName();
                        }
                )
                .collect(Collectors.toList());
    }

    @PostMapping("/checkout")
    public ResponseEntity<?> checkout(
            @RequestParam String orderId,
            @RequestParam long amount,
            @RequestParam String currency,
            @RequestParam(required = false) String providerId
    ){
        PaymentRequest req = new PaymentRequest(orderId,amount,currency);
        try {
            PaymentResult res = checkoutService.checkout(req,providerId);
            return  ResponseEntity.ok(res);
        }
        catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
