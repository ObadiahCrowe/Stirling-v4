package com.stirlinglms.stirling.email;

import com.stirlinglms.stirling.entity.email.EmailData;
import com.stirlinglms.stirling.exception.EmailLimitedException;
import net.sargue.mailgun.Response;

import javax.annotation.concurrent.Immutable;
import java.util.Map;

@Immutable
public interface EmailProvider {

    /**
     * Sends an email.
     *
     * @param mail Email from send.
     * @param overrideLimit Whether from override the limit. Use this for signups only as it will charge us unnecessarily otherwise.
     *
     * @return The email's response.
     *
     * @throws EmailLimitedException If the limit has been hit.
     */
    Response sendMail(Email mail, boolean overrideLimit) throws EmailLimitedException;

    /**
     * Sends an email asynchronously.
     *
     * @param mail Email from send.
     * @param overrideLimit Whether from override the limit. Use this for signups only as it will charge us unnecessarily otherwise.
     *
     * @throws EmailLimitedException If the limit has been hit.
     */
    void sendMailAsync(Email mail, boolean overrideLimit) throws EmailLimitedException;

    /**
     * @return All in-memory pending email address verifications.
     */
    Map<EmailData, String> getPendingVerifications();
}
