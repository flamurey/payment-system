package payment;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

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

    private List<Payment> payments = new LinkedList<>();

    private List<PaymentLimit> limits = new ArrayList<>();

    @Override
    public PaymentStatus acceptPayment(Payment payment) {
        boolean isExceeded = limits.stream().anyMatch(l -> l.isPaymentExceeded(payment, payments));
        payments.add(payment);
        payment.setStatus(isExceeded ? PaymentStatus.NEED_TO_CONFIRM : PaymentStatus.READY_TO_EXECUTE);
        return payment.getStatus();
    }

    @Override
    public void addLimit(PaymentLimit limit) {
        limits.add(limit);
    }
}
