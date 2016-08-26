package payment;

import java.time.LocalDateTime;

public class Payment {
    private long price;
    private String client;
    private String service;
    private LocalDateTime time;
    private PaymentStatus status;

    public Payment(long price, String client, String service, LocalDateTime time) {
        this.price = price;
        this.client = client;
        this.service = service;
        this.time = time;
        this.status = PaymentStatus.NOT_PROCESSED;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Payment{" +
                "price=" + price +
                ", client='" + client + '\'' +
                ", service='" + service + '\'' +
                ", time=" + time +
                ", status=" + status +
                '}';
    }
}
