package com.stirlinglms.stirling.service;

import com.stirlinglms.stirling.entity.payment.Payment;
import com.stirlinglms.stirling.entity.user.User;
import com.stirlinglms.stirling.repository.PaymentRepository;
import com.stripe.model.Charge;

import java.util.Set;

public interface PaymentService extends RepositoryService<Payment, PaymentRepository> {

    Payment createPayment(User user, String transactionId, long cost, boolean paid);

    Payment createPayment(User user, Charge charge);

    Payment getByTransactionId(String transactionId);

    Set<Payment> getByUser(User user);
}
