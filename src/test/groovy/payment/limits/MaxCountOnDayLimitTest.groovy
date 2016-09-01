package payment.limits

import payment.DefaultPaymentSystem
import payment.Payment
import payment.PaymentStatus
import payment.PaymentSystem
import spock.lang.Specification

import java.time.LocalDate

class MaxCountOnDayLimitTest extends Specification {
    def day = LocalDate.of(2000, 1, 1)
    def time = day.atTime(11, 0)
    def client = "client1"
    def service = "service"

    def "if total count of payments for given interval exceed max count then status must be NEED_TO_CONFIRM"() {

        def paymentSystem = new DefaultPaymentSystem()
        def payment = new Payment(500, client, service, time)
        def maxCount = 10

        def limit = new MaxCountOnDayLimit(maxCount)
        paymentSystem.addLimit(limit)

        when: "add max count payments "
        def status = null
        maxCount.times {
            status = paymentSystem.acceptPayment(payment)
        }
        then: "limit not exceed"
        status == PaymentStatus.READY_TO_EXECUTE

        when: "add payment for another day"
        status = paymentSystem.acceptPayment(new Payment(1000, client, service, time.minusDays(1)))
        then: "limit till not exceeded"
        status == PaymentStatus.READY_TO_EXECUTE

        when: "add payment with another service"
        status = paymentSystem.acceptPayment(new Payment(1000, client, service + "2", time))
        then: "limit till not exceeded"
        status == PaymentStatus.READY_TO_EXECUTE

        when: "add payment with another client"
        status = paymentSystem.acceptPayment(new Payment(1000, client + "2", service + "2", time))
        then: "limit till not exceeded"
        status == PaymentStatus.READY_TO_EXECUTE

        when: "add additional payment with same service and same client and same day"
        status = paymentSystem.acceptPayment(new Payment(1000, client, service, time))
        then: "limit will exceeded"
        status == PaymentStatus.NEED_TO_CONFIRM
    }
}
