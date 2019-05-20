package com.stirlinglms.stirling.dto;

import com.stirlinglms.stirling.entity.email.EmailData;
import lombok.Getter;

import javax.annotation.concurrent.Immutable;
import java.time.Instant;

@Getter
@Immutable
public final class EmailDataDto {

    private final String emailAddress;
    private final boolean verified;
    private final Instant verifiedOn;

    public EmailDataDto(EmailData data) {
        this.emailAddress = data.getAddress();
        this.verified = data.isVerified();
        this.verifiedOn = data.getVerifiedOn();
    }
}
