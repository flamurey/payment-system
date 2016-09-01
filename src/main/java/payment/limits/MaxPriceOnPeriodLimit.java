package payment.limits;

import payment.Payment;

import java.time.LocalTime;
import java.util.LongSummaryStatistics;

public class MaxPriceOnPeriodLimit extends PeriodedPaymentLimit{
    private final long maxTotalPrice;


    public MaxPriceOnPeriodLimit(long maxTotalPrice, LocalTime from, LocalTime to) {
        super(from, to);
        this.maxTotalPrice = maxTotalPrice;
    }

    @Override
    protected boolean isRegisteredPaymentMatchedToLimit(Payment registeredPayment, Payment checkedPayment) {
        return registeredPayment.isSameService(checkedPayment);
    }

    @Override
    protected boolean isPaymentExceed(Payment checkedPayment, LongSummaryStatistics statistics) {
        return statistics.getSum() + checkedPayment.getPrice() > maxTotalPrice;
    }
}
