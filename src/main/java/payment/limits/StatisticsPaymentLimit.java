package payment.limits;

import payment.Payment;

import java.util.Collection;
import java.util.LongSummaryStatistics;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public abstract class StatisticsPaymentLimit implements PaymentLimit {

    protected abstract Predicate<Payment> getPaymentsFilter(Payment checkedPayment);

    protected abstract boolean isPaymentExceed(Payment checkedPayment, LongSummaryStatistics statistics);

    @Override
    public boolean isPaymentExceeded(Payment payment, Collection<Payment> registeredPayments) {
        LongSummaryStatistics statistics = registeredPayments.stream()
                .filter(getPaymentsFilter(payment))
                .collect(Collectors.summarizingLong(Payment::getPrice));
        return isPaymentExceed(payment, statistics);
    }
}
