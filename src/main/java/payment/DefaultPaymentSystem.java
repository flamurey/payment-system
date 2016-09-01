package payment;

import payment.limits.PaymentLimit;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class DefaultPaymentSystem implements PaymentSystem {

    private Collection<Payment> payments = new LinkedList<>();

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
