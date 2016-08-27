package payment.limits;

import payment.Payment;

import java.util.Collection;
import java.util.NavigableSet;

@FunctionalInterface
public interface PaymentLimit {
    boolean isPaymentExceeded(Payment payment, Collection<Payment> registeredPayments);
}
