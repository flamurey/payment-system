package payment.limits;

import payment.Payment;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.function.Predicate;

import static java.util.Objects.requireNonNull;

public abstract class TimespanPaymentLimit extends StatisticsPaymentLimit {
    protected final ChronoUnit timeUnit;
    protected final long timespanLength;

    public TimespanPaymentLimit(ChronoUnit timeUnit, long timespanLength) {
        requireNonNull(timeUnit);
        this.timeUnit = timeUnit;
        this.timespanLength = timespanLength;
    }

    @Override
    protected Predicate<Payment> getPaymentsFilter(Payment checkedPayment) {
        return payment -> {
            LocalDateTime fromDt = checkedPayment.getTime().minus(timespanLength, timeUnit);
            LocalDateTime toDt = checkedPayment.getTime();
            return payment.isBetweenTo(fromDt, toDt) && isRegisteredPaymentMatchedToLimit(payment, checkedPayment);
        };
    }

    protected abstract boolean isRegisteredPaymentMatchedToLimit(Payment registeredPayment, Payment checkedPayment);
}
