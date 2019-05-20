package com.stirlinglms.stirling.entity.payment;

import com.stirlinglms.stirling.Stirling;
import com.stirlinglms.stirling.dto.PaymentDto;
import com.stirlinglms.stirling.entity.SaveableEntity;
import com.stirlinglms.stirling.entity.user.User;
import com.stirlinglms.stirling.repository.PaymentRepository;
import com.stirlinglms.stirling.service.PaymentService;
import com.stirlinglms.stirling.util.UpdateLevel;
import com.stripe.model.Charge;
import org.springframework.cloud.gcp.data.datastore.core.mapping.Entity;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Immutable;

import java.time.Instant;

@Immutable
@Entity(name = "payments")
public class Payment implements SaveableEntity<Payment, PaymentDto, PaymentService, PaymentRepository> {

    @Id
    private Long id;

    private User user;

    private String transactionId;
    private Instant createdOn;

    private long cost;
    private boolean paid;
    private boolean refunded;

    private String desc;
    private String receiptUrl;

    @Deprecated
    private Payment() {}

    public Payment(User user, String transactionId, long cost, boolean paid) {
        this.user = user;
        this.transactionId  = transactionId;
        this.createdOn = Instant.now();

        this.cost = cost;
        this.paid = paid;
        this.refunded = false;

        this.desc = "";
        this.receiptUrl = "";
    }

    public Payment(User user, Charge charge) {
        this.user = user;
        this.transactionId = charge.getId();
        this.createdOn = Instant.ofEpochMilli(charge.getCreated());

        this.cost = charge.getAmount();
        this.paid = charge.getPaid();
        this.refunded = charge.getRefunded();

        this.desc = charge.getDescription();
        this.receiptUrl = charge.getReceiptUrl();
    }

    public Long getId() {
        return this.id;
    }

    public User getUser() {
        return this.user;
    }

    public String getTransactionId() {
        return this.transactionId;
    }

    public Instant getCreatedOn() {
        return this.createdOn;
    }

    public long getCost() {
        return this.cost;
    }

    public boolean isPaid() {
        return this.paid;
    }

    public boolean isRefunded() {
        return this.refunded;
    }

    public String getDesc() {
        return this.desc;
    }

    public String getReceiptUrl() {
        return this.receiptUrl;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }

    public void setRefunded(boolean refunded) {
        this.refunded = refunded;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setReceiptUrl(String receiptUrl) {
        this.receiptUrl = receiptUrl;
    }

    @Override
    public PaymentService getService() {
        return Stirling.get().getPaymentService();
    }

    @Override
    public PaymentDto getDto() {
        return new PaymentDto(this);
    }
}
