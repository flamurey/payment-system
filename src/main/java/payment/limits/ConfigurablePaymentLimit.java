package payment.limits;

import payment.Payment;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.LongSummaryStatistics;
import java.util.NavigableMap;
import java.util.stream.Collectors;

public class ConfigurablePaymentLimit implements PaymentLimit {
    private LocalTime from;
    private LocalTime to;
    private ChronoUnit timespanUnit;
    private long timespanLength;
    private long maxTotalPrice;
    private long maxTotalCount;
    private boolean sameClient;
    private boolean sameService;

    @Override
    public boolean isPaymentExceeded(Payment payment, NavigableMap<LocalDateTime, Payment> registeredPayments) {
        Collection<Payment> fetchedByTimePayments;

        if (from != null && to != null) {
            if (!payment.isBetweenTo(from, to)) {
                return false;
            } else {
                LocalDate paymentDay = payment.getTime().toLocalDate();
                LocalDateTime fromDt = paymentDay.atTime(from);
                LocalDateTime toDt = paymentDay.atTime(to);
                fetchedByTimePayments = registeredPayments.subMap(fromDt, true, toDt, false).values();
            }
        } else {
            if (timespanUnit == null) {
                throw new IllegalStateException("Either from and to fields or timespanUnit field must be initialized");
            }
            LocalDateTime fromDt = payment.getTime().minus(timespanLength, timespanUnit);
            fetchedByTimePayments = registeredPayments.subMap(fromDt, true, payment.getTime(), false).values();
        }

        LongSummaryStatistics statistics = fetchedByTimePayments.stream()
                .filter(p -> {
                    boolean clientFilter = !sameClient || p.isSameClient(payment);
                    boolean serviceFilter = !sameService || p.isSameService(payment);
                    return clientFilter && serviceFilter;
                })
                .collect(Collectors.summarizingLong(Payment::getPrice));
        boolean priceExceeded = maxTotalPrice > 0 && statistics.getSum() + payment.getPrice() > maxTotalPrice;
        boolean volumeExceeded = maxTotalCount > 0 && statistics.getCount() + 1 > maxTotalCount;
        return priceExceeded || volumeExceeded;
    }

    public static PaymentLimit createMaxPriceOnPeriodLimit(long maxTotalPrice, LocalTime from, LocalTime to) {
        ConfigurablePaymentLimit.Builder builder = new Builder(from, to);
        return builder.setMaxTotalPrice(maxTotalPrice)
                .setSameServiceRestriction(true)
                .build();
    }

    public static PaymentLimit createMaxPriceOnTimespanLimit(long maxTotalPrice, ChronoUnit timeUnit, long timespanLength) {
        ConfigurablePaymentLimit.Builder builder = new Builder(timeUnit, timespanLength);
        return builder.setMaxTotalPrice(maxTotalPrice)
                .setSameServiceRestriction(true)
                .build();
    }

    public static class Builder {
        private LocalTime from;
        private LocalTime to;
        private ChronoUnit timespanUnit;
        private long timespanLength;
        private long maxTotalPrice;
        private long maxTotalCount;
        private boolean sameClient;
        private boolean sameService;

        public Builder(LocalTime from, LocalTime to) {
            this.from = from;
            this.to = to;
        }

        public Builder(ChronoUnit unit, long length) {
            this.timespanUnit = unit;
            this.timespanLength = length;
        }

        public Builder setMaxTotalPrice(long price) {
            this.maxTotalPrice = price;
            return this;
        }

        public Builder setMaxTotalCount(long count) {
            this.maxTotalCount = count;
            return this;
        }

        public Builder setSameClientRestriction(boolean enable) {
            this.sameClient = enable;
            return this;
        }

        public Builder setSameServiceRestriction(boolean enable) {
            this.sameService = enable;
            return this;
        }

        public PaymentLimit build() {
            ConfigurablePaymentLimit limit = new ConfigurablePaymentLimit();
            limit.from = from;
            limit.to = to;
            limit.timespanUnit = timespanUnit;
            limit.timespanLength = timespanLength;
            limit.maxTotalPrice = maxTotalPrice;
            limit.maxTotalCount = maxTotalCount;
            limit.sameClient = sameClient;
            limit.sameService = sameService;
            return limit;
        }
    }
}
