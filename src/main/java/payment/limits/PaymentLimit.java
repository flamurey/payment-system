package payment.limits;

import payment.Payment;

import java.time.LocalDateTime;
import java.util.NavigableMap;

@FunctionalInterface
public interface PaymentLimit {
    boolean isPaymentExceeded(Payment payment, NavigableMap<LocalDateTime, Payment> registeredPayments);
}
