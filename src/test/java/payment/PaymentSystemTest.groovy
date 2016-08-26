package payment

import spock.lang.Specification

import java.time.LocalDateTime

class PaymentSystemTest extends Specification {

    def paymentSystem = PaymentSystem.create();
    def payment = new Payment(100000, "client1", "service1", LocalDateTime.now());
    def exceededLimit = { p, payments -> true }
    def noExceededLimit = { p, payments -> false }

    def "limitless payment systems must return READY_TO_EXECUTE status"() {
        when:
        def status = paymentSystem.acceptPayment(payment)

        then:
        status == PaymentStatus.READY_TO_EXECUTE
        payment.status == PaymentStatus.READY_TO_EXECUTE
    }

    def "if limit not exceed then status must be READY_TO_EXECUTE"() {
        paymentSystem.addLimit(noExceededLimit)

        when:
        def status = paymentSystem.acceptPayment(payment)

        then:
        status == PaymentStatus.READY_TO_EXECUTE
        payment.status == PaymentStatus.READY_TO_EXECUTE
    }

    def "if limit exceeded then status must be NEED_TO_CONFIRM"() {
        paymentSystem.addLimit(exceededLimit)
        paymentSystem.addLimit(noExceededLimit)

        when:
        def status = paymentSystem.acceptPayment(payment)

        then:
        status == PaymentStatus.NEED_TO_CONFIRM
        payment.status == PaymentStatus.NEED_TO_CONFIRM
    }
}
