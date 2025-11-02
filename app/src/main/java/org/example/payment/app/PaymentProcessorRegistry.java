package org.example.payment.app;

import jakarta.annotation.PostConstruct;
import org.example.payment.PaymentProcessor;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class PaymentProcessorRegistry {

    private final Map<String, PaymentProcessor> processors = new HashMap<>();

    @PostConstruct
    public void init(){
        ServiceLoader<PaymentProcessor> loader = ServiceLoader.load(PaymentProcessor.class);
        for(PaymentProcessor processor:loader){
            if(processor == null) continue;
            processors.putIfAbsent(processor.id(),processor);
            System.out.println("Discovered Payment Processor: "+processor.id()+" "+processor.getName());
        }
    }

    public Optional<PaymentProcessor> getProcessorById(String id){
        return Optional.ofNullable(processors.get(id));
    }

    public List<PaymentProcessor> all(){
        return new ArrayList<>(processors.values());
    }
}
