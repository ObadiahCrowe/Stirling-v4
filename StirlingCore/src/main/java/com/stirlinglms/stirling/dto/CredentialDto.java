package com.stirlinglms.stirling.dto;

import com.stirlinglms.stirling.entity.credential.Credential;
import com.stirlinglms.stirling.entity.credential.CredentialType;
import lombok.Getter;

import javax.annotation.concurrent.Immutable;

@Getter
@Immutable
public final class CredentialDto {

    private final CredentialType type;
    private final boolean present;

    public CredentialDto(Credential credential) {
        this.type = credential.getType();
        this.present = ((credential.getCredential().getType1().length > 0) && (credential.getCredential().getType2().length > 0));
    }
}
