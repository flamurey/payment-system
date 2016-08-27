package payment.limits;

import java.time.LocalTime;

public interface PaymentLimitFactory {

    /**
     * @param from inclusive
     * @param to exclusive
     */
    PaymentLimit createMaxPriceOnPeriodLimit(long maxPrice, LocalTime from, LocalTime to);
}
