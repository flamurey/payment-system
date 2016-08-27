package payment.limits;

import payment.Payment;

import java.time.LocalTime;

public class QuickPaymentLimitFactory implements PaymentLimitFactory {
    /**
     * @param from inclusive
     * @param to exclusive
     */
    @Override
    public PaymentLimit createMaxPriceOnPeriodLimit(long maxPrice, LocalTime from, LocalTime to) {
        return (payment, registeredPayments) -> {
            if (!payment.isBetweenTo(from, to)) {
                return false;
            }
            long currentTotalPrice = registeredPayments.stream()
                    .filter(p -> p.isSameDay(payment)
                            && p.isBetweenTo(from, to)
                            && p.isSameService(payment))
                    .mapToLong(Payment::getPrice)
                    .sum();
            return currentTotalPrice + payment.getPrice() > maxPrice;
        };
    }
}
