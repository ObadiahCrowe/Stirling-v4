package com.stirlinglms.stirling.payment;

import lombok.Getter;

import javax.annotation.concurrent.Immutable;

@Getter
@Immutable
public class PaymentRequest {

    private final String gatewayEmail;
    private final String gatewayToken;

    private final String description;
    private final int cost;

    public PaymentRequest(String gatewayEmail, String gatewayToken, String description, int cost) {
        this.gatewayEmail = gatewayEmail;
        this.gatewayToken = gatewayToken;
        this.description = description;
        this.cost = cost;
    }

}
