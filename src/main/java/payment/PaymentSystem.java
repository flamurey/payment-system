package payment;

public interface PaymentSystem {
    /**
     * Check payment on limits and then set new {@link PaymentStatus}
     * @param payment received payment, which checked on current limits
     * @return Either {@link PaymentStatus#READY_TO_EXECUTE} or {@link PaymentStatus#REIQURED_TO_CONFIRM},
     * which set up to the payment object
     */
    PaymentStatus acceptPlayment(Payment payment);

    void addLimit(PaymentLimit limit);
}
