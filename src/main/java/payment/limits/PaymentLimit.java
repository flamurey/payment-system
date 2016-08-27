package payment.limits;

import payment.Payment;

import java.util.Collection;

@FunctionalInterface
public interface PaymentLimit {
    boolean isPaymentExceeded(Payment payment, Collection<Payment> registeredPayments);
}
