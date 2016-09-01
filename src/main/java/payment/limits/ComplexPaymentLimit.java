package payment.limits;

import payment.Payment;

import java.time.temporal.ChronoUnit;
import java.util.LongSummaryStatistics;

public class ComplexPaymentLimit extends TimespanPaymentLimit {
    private final long maxTotalPrice;
    private final long maxTotalCount;

    public ComplexPaymentLimit(long maxTotalPrice, long maxTotalCount, ChronoUnit timeUnit, long timespanLength) {
        super(timeUnit, timespanLength);
        this.maxTotalPrice = maxTotalPrice;
        this.maxTotalCount = maxTotalCount;
    }

    @Override
    protected boolean isPaymentExceed(Payment checkedPayment, LongSummaryStatistics statistics) {
        return statistics.getSum() + checkedPayment.getPrice() > maxTotalPrice
                || statistics.getCount() + 1 > maxTotalCount;
    }

    @Override
    protected boolean isRegisteredPaymentMatchedToLimit(Payment registeredPayment, Payment checkedPayment) {
        return registeredPayment.isSameClient(checkedPayment);
    }
}
