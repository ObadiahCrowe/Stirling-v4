package com.stirlinglms.stirling.payment.stripe;

import lombok.Data;

@Data
public class StripeRequest {

    private String description;
    private int amount;
    private String currency;
    private String stripeEmail;
    private String stripeToken;
}
