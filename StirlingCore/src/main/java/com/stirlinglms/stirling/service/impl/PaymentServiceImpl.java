package com.stirlinglms.stirling.service.impl;

import com.google.common.collect.Sets;
import com.stirlinglms.stirling.entity.payment.Payment;
import com.stirlinglms.stirling.entity.user.User;
import com.stirlinglms.stirling.repository.PaymentRepository;
import com.stirlinglms.stirling.service.PaymentService;
import com.stirlinglms.stirling.util.UpdateLevel;
import com.stripe.model.Charge;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Set;

@Service
public class PaymentServiceImpl implements PaymentService {

    private static final String[] VALID_FIELDS = new String[] {
      "paid",
      "refunded",
      "receiptUrl",
      "desc"
    };

    private final PaymentRepository repository;

    @Autowired
    PaymentServiceImpl(PaymentRepository repository) {
        this.repository = repository;
    }

    @Override
    public Payment createPayment(User user, String transactionId, long cost, boolean paid) {
        Payment payment = new Payment(user, transactionId, cost, paid);

        this.repository.save(payment);

        return payment;
    }

    @Override
    public Payment createPayment(User user, Charge charge) {
        Payment payment = new Payment(user, charge);

        this.repository.save(payment);

        return payment;
    }

    @Override
    public Payment getByTransactionId(String transactionId) {
        return this.repository.findByTransactionId(transactionId);
    }

    @Override
    public Set<Payment> getByUser(User user) {
        return Collections.unmodifiableSet(Sets.newConcurrentHashSet(this.repository.findAllByUser(user)));
    }

    @Override
    public Payment update(Payment entity, UpdateLevel level) throws Exception {
        return this.repository.performTransaction((repo) -> repo.save(entity));
    }

    @Override
    public Payment delete(Payment entity) {
        this.repository.delete(entity);

        return entity;
    }

    @Override
    public Payment getById(Long id) {
        return this.repository.findById(id).orElse(null);
    }

    @Override
    public Set<Payment> getAll() {
        return Collections.unmodifiableSet(Sets.newConcurrentHashSet(this.repository.findAll()));
    }

    @Override
    public PaymentRepository getRepository() {
        return this.repository;
    }
}
