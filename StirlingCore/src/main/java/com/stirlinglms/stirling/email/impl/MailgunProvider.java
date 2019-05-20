package com.stirlinglms.stirling.email.impl;

import com.google.common.collect.Maps;
import com.stirlinglms.stirling.email.Email;
import com.stirlinglms.stirling.email.EmailProvider;
import com.stirlinglms.stirling.entity.email.EmailData;
import com.stirlinglms.stirling.exception.EmailLimitedException;
import net.sargue.mailgun.Configuration;
import net.sargue.mailgun.MailBuilder;
import net.sargue.mailgun.Response;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

@Component
public class MailgunProvider implements EmailProvider {

    // Data, Hash
    private static final Map<EmailData, String> PENDING_VERIFICATIONS = Maps.newConcurrentMap();
    private static final AtomicReference<Configuration> CONFIG = new AtomicReference<>(null);
    private static final long PROVIDER_LIMIT_MONTHLY = 10_000L;

    private AtomicLong sentMonthly = new AtomicLong(0L);

    MailgunProvider() {
        CONFIG.compareAndSet(null, new Configuration(
          "mail.stirlinglms.com",
          "",
          "StirlingLMS <admin@stirlinglms.com>"
        ));
    }

    public Response sendMail(Email email, boolean overrideLimit) throws EmailLimitedException {
        if (!overrideLimit) {
            this.runEmailChecks();
        }

        return this.getMail(email).build().send();
    }

    @Override
    public void sendMailAsync(Email email, boolean overrideLimit) throws EmailLimitedException {
        if (!overrideLimit) {
            this.runEmailChecks();
        }

        this.getMail(email).build().sendAsync();
    }

    @Override
    public Map<EmailData, String> getPendingVerifications() {
        return PENDING_VERIFICATIONS;
    }

    private MailBuilder getMail(Email email) {
        MailBuilder builder = new MailBuilder(CONFIG.get());

        builder.subject(email.getSubject());
        builder.html(email.getContent());
        builder.from(CONFIG.get().from());

        email.getTo().forEach(builder::to);
        email.getCc().forEach(builder::cc);
        email.getBcc().forEach(builder::bcc);

        return builder;
    }

    private void runEmailChecks() throws EmailLimitedException {
        if (this.sentMonthly.get() >= PROVIDER_LIMIT_MONTHLY) {
            throw new EmailLimitedException();
        }

        this.sentMonthly.incrementAndGet();
    }
}
