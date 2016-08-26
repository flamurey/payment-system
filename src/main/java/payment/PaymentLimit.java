package payment;

import java.util.Collection;

@FunctionalInterface
public interface PaymentLimit {
    boolean isPaymentExceeded(Payment payment, Collection<Payment> registered);
}
