package com.stirlinglms.stirling.entity.email;

import com.stirlinglms.stirling.dto.EmailDataDto;
import com.stirlinglms.stirling.entity.Entity;

import javax.annotation.concurrent.Immutable;
import java.time.Instant;

@Immutable
public class EmailData implements Entity<EmailDataDto> {

    private String address;
    private Instant verifiedOn;

    @Deprecated
    private EmailData() {}

    public EmailData(String address) {
        this.address = address;
        this.verifiedOn = null;
    }

    public EmailData(String address, boolean verified) {
        this.address = address;
        this.verifiedOn = verified ? Instant.now() : null;
    }

    public String getAddress() {
        return this.address;
    }

    public boolean isVerified() {
        return this.verifiedOn != null;
    }

    public void setVerified(boolean verified) {
        this.verifiedOn = verified ? Instant.now() : null;
    }

    public Instant getVerifiedOn() {
        return this.verifiedOn;
    }

    @Override
    public EmailDataDto getDto() {
        return new EmailDataDto(this);
    }
}
