package payment.limits;

import payment.Payment;

import java.time.temporal.ChronoUnit;
import java.util.LongSummaryStatistics;

public class MaxPriceOnTimespanLimit extends TimespanPaymentLimit {
    private final long maxTotalPrice;

    public MaxPriceOnTimespanLimit(long maxTotalPrice, ChronoUnit timeUnit, long timespanLength) {
        super(timeUnit, timespanLength);
        this.maxTotalPrice = maxTotalPrice;
    }

    @Override
    protected boolean isPaymentExceed(Payment checkedPayment, LongSummaryStatistics statistics) {
        return statistics.getSum() + checkedPayment.getPrice() > maxTotalPrice;
    }

    @Override
    protected boolean isRegisteredPaymentMatchedToLimit(Payment registeredPayment, Payment checkedPayment) {
        return registeredPayment.isSameService(checkedPayment);
    }
}
