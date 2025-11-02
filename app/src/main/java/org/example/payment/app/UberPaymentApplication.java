package org.example.payment.app;


import org.example.payment.PaymentProcessor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ServiceLoader;

@SpringBootApplication
public class UberPaymentApplication {

    public static void main(String[] args) {
        SpringApplication.run(UberPaymentApplication.class);
    }

}
