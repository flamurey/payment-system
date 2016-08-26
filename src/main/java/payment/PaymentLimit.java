package payment;

import java.util.Collection;

public interface PaymentLimit {
    boolean isPaymentExceeded(Payment payment, Collection<Payment> registered);
}
