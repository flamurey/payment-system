package payment.limits;

import payment.Payment;

import java.time.LocalTime;
import java.util.LongSummaryStatistics;

public class MaxCountOnDayLimit extends PeriodedPaymentLimit {
    private final long maxTotalCount;

    public MaxCountOnDayLimit(long maxTotalCount) {
        super(LocalTime.MIDNIGHT, LocalTime.MAX);
        this.maxTotalCount = maxTotalCount;
    }

    @Override
    protected boolean isPaymentExceed(Payment checkedPayment, LongSummaryStatistics statistics) {
        return statistics.getCount() + 1 > maxTotalCount;
    }

    @Override
    protected boolean isRegisteredPaymentMatchedToLimit(Payment registeredPayment, Payment checkedPayment) {
        return registeredPayment.isSameService(checkedPayment)
                && registeredPayment.isSameClient(checkedPayment);
    }
}
