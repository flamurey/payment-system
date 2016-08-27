package payment;

import java.time.LocalDateTime;
import java.time.LocalTime;

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

    public String getClient() {
        return client;
    }

    public String getService() {
        return service;
    }

    public LocalDateTime getTime() {
        return time;
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

    public boolean isSameDay(Payment other) {
        return getTime().toLocalDate().equals(other.getTime().toLocalDate());
    }

    public boolean isSameService(Payment other) {
        return this.service.equals(other.getService());
    }

    public boolean isSameClient(Payment other) {
        return this.client.equals(other.getClient());
    }

    public boolean isBetweenTo(LocalTime from, LocalTime to) {
        LocalTime time = this.time.toLocalTime();
        return time.compareTo(from) >= 0 && time.isBefore(to);
    }
}
