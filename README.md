# 🧩 Java SPI (Service Provider Interface) Pattern — Explained with Real-World Examples

## 📘 What is the SPI Pattern?

The **Service Provider Interface (SPI)** pattern in Java is a mechanism that allows developers to build **extensible and pluggable systems**.  

It enables you to:
- Define a **service interface** in one module (the “contract”),  
- Implement that service in multiple independent modules (the “providers”),  
- And load those implementations **dynamically at runtime** — without modifying or recompiling your core application.  

This pattern is the foundation of Java’s **plugin architecture**.

---

### ⚙️ How it Works

1. **Define a Service Interface**

   ```java
   public interface PaymentService {
       String getName();
       String id();
       PaymentResult processPayment(PaymentRequest request);
   }
   ```

2. **Provide Implementations**

   ```java
   public class StripePaymentService implements PaymentService {
       public String getName() { return "stripe"; }
       public String id() { return "stripe"; }
       public PaymentResult processPayment(PaymentRequest request) {
           String tx = "tx-stripe_"+System.currentTimeMillis();
           return new PaymentResult(true,tx,"Stripe Payment processed successfully");
       }
   }
   ```

3. **Register the Implementation**

   In `META-INF/services/<fully-qualified-interface-name>`:

   ```
   org.example.payment.PaymentProcessor
   ```

   File content:
   ```
   org.example.payment.stripe.StripePaymentProcessor
   ```

4. **Discover and Load Dynamically**

   ```java
   ServiceLoader<PaymentProcessor> loader = ServiceLoader.load(PaymentProcessor.class);
   for (PaymentProcessor processor : loader) {
       System.out.println(processor.getName());
   }
   ```

The `ServiceLoader` scans all JARs in the classpath for this `META-INF/services` file and automatically loads all available implementations.  

No reflection, no manual wiring — pure runtime discovery.

---

## 🧠 Real-World Example — TwelveMonkeys ImageIO

[TwelveMonkeys ImageIO](https://github.com/haraldk/TwelveMonkeys) is a widely used open-source library that adds support for additional image formats in Java (e.g., TIFF, PSD, JPEG2000, HEIF).

### 🧩 The Problem
The default Java `ImageIO` API can only handle a few formats like PNG, JPEG, and GIF.  
But image processing applications often need more formats — TIFF, PSD, BMP, etc.

### 💡 The Solution — SPI-Based Plugin Architecture

The **Java ImageIO API** defines extension points (SPIs) for image readers and writers:

- `javax.imageio.spi.ImageReaderSpi`
- `javax.imageio.spi.ImageWriterSpi`

These are abstract classes that define **how a reader or writer should behave**.  

TwelveMonkeys implements these interfaces for new formats:

```java
public class TIFFImageReaderSpi extends ImageReaderSpi {
    @Override
    public ImageReader createReaderInstance(Object extension) {
        return new TIFFImageReader(this);
    }
}
```

Then, they register them using the **SPI pattern**:

**`META-INF/services/javax.imageio.spi.ImageReaderSpi`**
```
com.twelvemonkeys.imageio.plugins.tiff.TIFFImageReaderSpi
com.twelvemonkeys.imageio.plugins.psd.PSDImageReaderSpi
```

Now, when you write:

```java
BufferedImage img = ImageIO.read(new File("photo.tif"));
```

Java’s `ImageIO`:
1. Uses `ServiceLoader` under the hood to find all available `ImageReaderSpi` implementations on the classpath,  
2. Sees that TwelveMonkeys provides a `TIFFImageReaderSpi`,  
3. Automatically uses that reader to decode `.tif` files.  

No hardcoding, no manual registration.  
You can drop new JARs (plugins) for new formats, and ImageIO discovers them automatically.

This is the SPI pattern **in real-world production use** — providing extensibility, modularity, and decoupling.

---

## 💳 Demo Project — Uber Payments Platform (Spring Boot + Gradle)

This project is a hands-on demonstration of the **SPI pattern** using a simplified payment processing system inspired by **Uber’s Payments Platform**.

### 🧩 Problem Statement

Uber supports multiple payment gateways — Stripe, PayPal, Razorpay, etc.  
Each gateway has its own API and rules, and new gateways are often added.  

**Challenge:**  
How do we design a flexible system that can support new payment providers without changing or recompiling the core application?

### 💡 Solution

Use the **Java SPI pattern** to dynamically load payment providers at runtime.

---

## 🏗️ Project Structure

```
uber-payments-platform-spi-pattern/
│
├── core/
│   ├── PaymentRequest.java
│   └── PaymentProcessor.java
│   └── PaymentResult.java
│
├── stripe-payment/
│   ├── StripePaymentProcessor.java
│   └── META-INF/services/org.example.payment.PaymentProcessor
│
├── paypal-payment/
│   ├── PaypalPaymentProcessor.java
│   └── META-INF/services/org.example.payment.PaymentProcessor
│
└── app/
    ├── PaymentProcessorRegisty.java
    ├── PaymentController.java
    ├── PaymentService.java
    └── build.gradle
```

---

## ⚙️ How It Works

1. **`core`** defines the `PaymentProcessor` interface.

2. **`stripe-payment`** and **`paypal-payment`** modules each implement `PaymentProcessor` and register their classes via `META-INF/services`.

3. **`app`** is a Spring Boot application that uses the `PaymentProcessor` to process payments.  
   When you run it, it automatically discovers all payment providers on the classpath.

---

## ▶️ How to Run

### 🧰 Requirements
- Java 17+  
- Gradle 8.5+  

### 🧩 Build the Project

```bash
./gradlew clean build
```

### 🚀 Run the Application

```bash
./gradlew :app:bootRun
```

### 🖥️ Expected Output

```
GET - http://localhost:8080/api/providers

--- Returns all providers ---
```


```
POST - http://localhost:8080/api/checkout?orderId=ord123&amount=30&currency=USD&providerId=stripe

--- Processer payment using StripePaymentProcessor ---
```

---

## 🧠 What You Learned

| Concept | Description |
|----------|-------------|
| **SPI Pattern** | Defines contracts (interfaces) and allows independent modules to provide implementations dynamically. |
| **ServiceLoader** | Core Java class that discovers implementations registered under `META-INF/services`. |
| **Extensibility** | You can add new payment providers or image formats by just adding new JARs — no code change in the main app. |
| **Real-World Usage** | The same principle powers Java ImageIO (TwelveMonkeys), JDBC driver loading, and Spring Boot autoconfiguration. |

---

## 🧩 References

- [Java ServiceLoader Documentation](https://docs.oracle.com/javase/8/docs/api/java/util/ServiceLoader.html)
- [TwelveMonkeys ImageIO GitHub Repository](https://github.com/haraldk/TwelveMonkeys)
- [Spring Boot Plugin System](https://docs.spring.io/spring-boot/docs/current/reference/html/using.html#using.build-systems.gradle)
- [Uber Engineering Blog — Payments Platform](https://www.uber.com/en-IN/blog/engineering/)

---

### 💬 Summary

The **Java SPI pattern** gives your applications a **plugin architecture**.  
It’s a key reason frameworks like **Spring Boot**, **JDBC**, and **TwelveMonkeys ImageIO** are so modular and extensible.

Our **Uber Payments Platform demo** shows how you can use the same principle in your own Spring Boot apps — to make systems open for extension, but closed for modification.

---
