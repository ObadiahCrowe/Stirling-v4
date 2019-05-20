package com.stirlinglms.stirling.repository;

import com.stirlinglms.stirling.entity.payment.Payment;
import com.stirlinglms.stirling.entity.user.User;
import org.springframework.cloud.gcp.data.datastore.repository.DatastoreRepository;

import java.util.List;

public interface PaymentRepository extends DatastoreRepository<Payment, Long> {

    Payment findByTransactionId(String transactionId);

    List<Payment> findAllByUser(User user);
}
