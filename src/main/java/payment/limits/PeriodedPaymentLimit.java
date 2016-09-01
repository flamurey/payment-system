package payment.limits;

import payment.Payment;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.function.Predicate;

import static java.util.Objects.requireNonNull;

public abstract class PeriodedPaymentLimit extends StatisticsPaymentLimit {
    protected final LocalTime from;
    protected final LocalTime to;

    public PeriodedPaymentLimit(LocalTime from, LocalTime to) {
        requireNonNull(from);
        requireNonNull(to);
        this.from = from;
        this.to = to;
    }

    @Override
    protected Predicate<Payment> getPaymentsFilter(Payment checkedPayment) {
        return payment -> {
            if (!checkedPayment.isBetweenTo(from, to)) {
                return false;
            } else {
                LocalDate paymentDay = checkedPayment.getTime().toLocalDate();
                LocalDateTime fromDt = paymentDay.atTime(from);
                LocalDateTime toDt = paymentDay.atTime(to);
                return payment.isBetweenTo(fromDt, toDt) && isRegisteredPaymentMatchedToLimit(payment, checkedPayment);
            }
        };
    }

    protected abstract boolean isRegisteredPaymentMatchedToLimit(Payment registeredPayment, Payment checkedPayment);
}
