package payment.limits

import payment.DefaultPaymentSystem
import payment.Payment
import payment.PaymentStatus
import payment.PaymentSystem
import spock.lang.Shared
import spock.lang.Specification

import java.time.LocalDate
import java.time.LocalTime

class MaxTotalPriceOnPeriodTest extends Specification {

    @Shared maxTotalPrice = 1000
    @Shared fromLimit = LocalTime.of(10, 0)
    @Shared toLimit = LocalTime.of(20, 0)
    @Shared day = LocalDate.of(2000, 1, 1)
    @Shared time = day.atTime(11, 0)

    def "payment belonging to period and exceeded total max price must have NEED_TO_CONFIRM status"() {
        def paymentSystem = new DefaultPaymentSystem()
        def payment1 = new Payment(500, "client1", "service1", time)
        def payment2 = new Payment(price2, "client1", service2, time2)

        def limit = new MaxPriceOnPeriodLimit(maxTotalPrice, fromLimit, toLimit)
        paymentSystem.addLimit(limit)

        when:
        paymentSystem.acceptPayment(payment1)
        paymentSystem.acceptPayment(payment2)

        then:
        payment2.status == status2

        where:
        price2 | time2                                 | service2   || status2
        500    | time                                  | "service1" || PaymentStatus.READY_TO_EXECUTE
        501    | time                                  | "service1" || PaymentStatus.NEED_TO_CONFIRM
        501    | time                                  | "ser2"     || PaymentStatus.READY_TO_EXECUTE
        501    | day.atTime(fromLimit)                 | "service1" || PaymentStatus.NEED_TO_CONFIRM
        501    | day.atTime(fromLimit.minusMinutes(1)) | "service1" || PaymentStatus.READY_TO_EXECUTE
        501    | day.atTime(toLimit)                   | "service1" || PaymentStatus.NEED_TO_CONFIRM
        501    | day.atTime(toLimit.plusMinutes(1))    | "service1" || PaymentStatus.READY_TO_EXECUTE
        1501   | time                                  | "ser2"     || PaymentStatus.NEED_TO_CONFIRM
    }
}
