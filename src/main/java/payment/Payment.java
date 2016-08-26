package payment;

import java.time.LocalDateTime;

public class Payment {
    private long price;
    private String client;
    private String service;
    private LocalDateTime time;
    private PaymentStatus status;
}
