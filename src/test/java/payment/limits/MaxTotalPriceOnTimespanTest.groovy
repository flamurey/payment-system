package payment.limits

import payment.Payment
import payment.PaymentStatus
import payment.PaymentSystem
import spock.lang.Shared
import spock.lang.Specification

import java.time.LocalDate
import java.time.temporal.ChronoUnit

class MaxTotalPriceOnTimespanTest extends Specification {
    @Shared maxTotalPrice = 1000
    @Shared day = LocalDate.of(2000, 1, 1)
    @Shared time = day.atTime(11, 0)

    def "if total price for given interval exceed max price then status must be NEED_TO_CONFIRM"() {
        def paymentSystem = PaymentSystem.create()
        def payment1 = new Payment(500, "client1", "service1", time)
        def payment2 = new Payment(price2, "client1", service2, time2)

        def limit = ConfigurablePaymentLimit.createMaxPriceOnTimespanLimit(maxTotalPrice, ChronoUnit.MINUTES, 60)
        paymentSystem.addLimit(limit)

        when:
        paymentSystem.acceptPayment(payment1)
        paymentSystem.acceptPayment(payment2)

        then:
        payment2.status == status2

        where:
        price2 | time2                | service2   || status2
        500    | time                 | "service1" || PaymentStatus.READY_TO_EXECUTE
        501    | time.minusMinutes(1) | "service1" || PaymentStatus.READY_TO_EXECUTE
        501    | time.plusMinutes(61) | "service1" || PaymentStatus.READY_TO_EXECUTE
        501    | time.plusMinutes(60) | "service1" || PaymentStatus.NEED_TO_CONFIRM
        501    | time.plusMinutes(60) | "ser2"     || PaymentStatus.READY_TO_EXECUTE
        1001   | time.plusMinutes(60) | "ser2"     || PaymentStatus.NEED_TO_CONFIRM
    }
}
