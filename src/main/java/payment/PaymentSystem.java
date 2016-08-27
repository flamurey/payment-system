package payment;

import payment.limits.PaymentLimit;

import java.time.LocalDateTime;
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

    static PaymentSystem create() {
        return new DefaultPaymentSystem();
    }
}

class DefaultPaymentSystem implements PaymentSystem {

    private NavigableMap<LocalDateTime, Payment> payments = new TreeMap<>();

    private List<PaymentLimit> limits = new ArrayList<>();

    @Override
    public PaymentStatus acceptPayment(Payment payment) {
        boolean isExceeded = limits.stream().anyMatch(l -> l.isPaymentExceeded(payment, payments));
        payments.put(payment.getTime(), payment);
        payment.setStatus(isExceeded ? PaymentStatus.NEED_TO_CONFIRM : PaymentStatus.READY_TO_EXECUTE);
        return payment.getStatus();
    }

    @Override
    public void addLimit(PaymentLimit limit) {
        limits.add(limit);
    }
}
