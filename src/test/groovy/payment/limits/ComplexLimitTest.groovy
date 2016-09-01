package payment.limits

import payment.DefaultPaymentSystem
import payment.Payment
import payment.PaymentStatus
import spock.lang.Specification

import java.time.LocalDate
import java.time.temporal.ChronoUnit

class ComplexLimitTest extends Specification {
    def day = LocalDate.of(2000, 1, 1)
    def time = day.atTime(11, 0)
    def client = "client1"
    def service = "service"
    def limit = new ComplexPaymentLimit(1000, 2, ChronoUnit.HOURS, 2)
    def paymentSystem = new DefaultPaymentSystem()

    def setup() {
        paymentSystem.addLimit(limit)
    }

    def "after exceeding price limit the status must be NEED_TO_CONFIRM"() {
        def payment = new Payment(700, client, service, time)

        when:
        def status = paymentSystem.acceptPayment(payment)
        then:
        status == PaymentStatus.READY_TO_EXECUTE

        when:
        status = paymentSystem.acceptPayment(new Payment(700, client + "2", service, time))
        then:
        status == PaymentStatus.READY_TO_EXECUTE

        when:
        status = paymentSystem.acceptPayment(new Payment(700, client, service, time.plusHours(3)))
        then:
        status == PaymentStatus.READY_TO_EXECUTE

        when:
        status = paymentSystem.acceptPayment(payment)
        then:
        status == PaymentStatus.NEED_TO_CONFIRM
    }

    def "after exceeding max count the status must be NEED_TO_CONFIRM"() {
        def payment = new Payment(100, client, service, time)

        when:
        paymentSystem.acceptPayment(payment)
        def status = paymentSystem.acceptPayment(payment)
        then:
        status == PaymentStatus.READY_TO_EXECUTE

        when:
        status = paymentSystem.acceptPayment(new Payment(700, client + "2", service, time))
        then:
        status == PaymentStatus.READY_TO_EXECUTE

        when:
        status = paymentSystem.acceptPayment(new Payment(700, client, service, time.plusHours(3)))
        then:
        status == PaymentStatus.READY_TO_EXECUTE

        when:
        status = paymentSystem.acceptPayment(payment)
        then:
        status == PaymentStatus.NEED_TO_CONFIRM
    }
}
