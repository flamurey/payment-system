package payment;

import payment.limits.PaymentLimit;

import java.util.*;

public interface PaymentSystem {
    /**
     * Check payment on limits and then set new {@link PaymentStatus}
     *
     * @param payment received payment, which checked on current limits
     * @return Either {@link PaymentStatus#READY_TO_EXECUTE} or {@link PaymentStatus#NEED_TO_CONFIRM},
     * which set up to the payment object
     */
    PaymentStatus acceptPayment(Payment payment);

    void addLimit(PaymentLimit limit);
}