package com.stirlinglms.stirling.payment;

import com.stirlinglms.stirling.entity.payment.Payment;
import com.stirlinglms.stirling.entity.user.User;
import com.stirlinglms.stirling.util.response.Response;

public interface PaymentProvider {

    Response<Payment> createPayment(User user, PaymentRequest request);

    Response<Payment> createRecurringPayment(User user, PaymentRequest request, long delay);

    Response<Payment> refundPayment(Payment payment);
}
