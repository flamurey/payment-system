package payment.limits

import payment.DefaultPaymentSystem
import payment.Payment
import payment.PaymentStatus
import spock.lang.Specification

import java.time.LocalDate
import java.time.LocalTime
import java.time.temporal.ChronoUnit

class LimitCombinationTest extends Specification {

    def "test max price combination"() {
        def client = "client"
        def service = "service"
        def day = LocalDate.now()
        def paypal = new DefaultPaymentSystem()
        paypal.addLimit(new MaxPriceOnPeriodLimit(5000, LocalTime.of(9, 0), LocalTime.of(23, 0)))
        paypal.addLimit(new MaxPriceOnTimespanLimit(3000, ChronoUnit.HOURS, 1))

        when:
        def status = paypal.acceptPayment(new Payment(2000, client, service, day.atTime(10, 0)))
        then:
        status == PaymentStatus.READY_TO_EXECUTE

        when: "total price for hour became 2000+2000 > 3000"
        status = paypal.acceptPayment(new Payment(2000, client, service, day.atTime(10, 30)))
        then:
        status == PaymentStatus.NEED_TO_CONFIRM

        //first limit work per service, regardless of client
        when:
        status = paypal.acceptPayment(new Payment(1, client + "2", service, day.atTime(10, 30)))
        then:
        status == PaymentStatus.NEED_TO_CONFIRM

        when:
        status = paypal.acceptPayment(new Payment(2000, client, service + "2", day.atTime(10, 30)))
        then:
        status == PaymentStatus.READY_TO_EXECUTE

        when: "after several hours second limit will cleaned"
        status = paypal.acceptPayment(new Payment(10, client, service, day.atTime(12, 30)))
        then:
        status == PaymentStatus.READY_TO_EXECUTE

        when: "total price on service became 2000+2000+10+1000 > 5000"
        status = paypal.acceptPayment(new Payment(1000, client, service, day.atTime(12, 30)))
        then:
        status == PaymentStatus.NEED_TO_CONFIRM

        when: "only second limit will enabled"
        status = paypal.acceptPayment(new Payment(2000, client, service, day.atTime(23, 30)))
        then:
        status == PaymentStatus.READY_TO_EXECUTE
    }
}
