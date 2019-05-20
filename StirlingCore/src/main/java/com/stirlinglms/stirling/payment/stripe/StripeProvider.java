package com.stirlinglms.stirling.payment.stripe;

import com.google.common.collect.Maps;
import com.stirlinglms.stirling.entity.email.EmailData;
import com.stirlinglms.stirling.entity.payment.Payment;
import com.stirlinglms.stirling.entity.user.User;
import com.stirlinglms.stirling.payment.PaymentProvider;
import com.stirlinglms.stirling.payment.PaymentRequest;
import com.stirlinglms.stirling.service.PaymentService;
import com.stirlinglms.stirling.util.response.Response;
import com.stirlinglms.stirling.util.response.ResponseCode;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class StripeProvider implements PaymentProvider {

    private static final String API_KEY = "";
    private static final int CONNECTION_TIMEOUT = 30_000;
    private static final int READ_TIMEOUT = 90_000;

    private final PaymentService service;

    @Autowired
    StripeProvider(PaymentService service) {
        this.service = service;

        Stripe.apiKey = API_KEY;
        Stripe.setConnectTimeout(CONNECTION_TIMEOUT);
        Stripe.setReadTimeout(READ_TIMEOUT);
    }

    @Override
    public Response<Payment> createPayment(User user, PaymentRequest request) {
        try {
            Map<String, Object> params = Maps.newConcurrentMap();

            params.put("amount", request.getCost());
            params.put("currency", "AUD");
            params.put("description", request.getDescription() == null ? "Payment" : request.getDescription());
            params.put("source", request.getGatewayToken());

            user.getEmailAddresses().stream()
              .filter(EmailData::isVerified).findFirst()
              .ifPresent(data -> params.put("receipt_email", data.getAddress()));

            return new Response<>(ResponseCode.SUCCESS, "Payment, " + request.getDescription() + ", completed!",
              this.service.createPayment(user, Charge.create(params)));
        } catch (StripeException e) {
            return new Response<>(ResponseCode.ERROR, e.getMessage(), null);
        }
    }

    @Override
    public Response<Payment> createRecurringPayment(User user, PaymentRequest request, long delay) {
        return null;
    }

    @Override
    public Response<Payment> refundPayment(Payment payment) {
        return null;
    }
}
