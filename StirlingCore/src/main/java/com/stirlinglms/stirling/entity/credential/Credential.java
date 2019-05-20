package com.stirlinglms.stirling.entity.credential;

import com.stirlinglms.stirling.Stirling;
import com.stirlinglms.stirling.dto.CredentialDto;
import com.stirlinglms.stirling.entity.SaveableEntity;
import com.stirlinglms.stirling.entity.user.User;
import com.stirlinglms.stirling.repository.CredentialRepository;
import com.stirlinglms.stirling.service.CredentialService;
import com.stirlinglms.stirling.util.Pair;
import com.stirlinglms.stirling.util.UpdateLevel;
import org.springframework.cloud.gcp.data.datastore.core.mapping.Entity;
import org.springframework.cloud.gcp.data.datastore.core.mapping.Reference;
import org.springframework.data.annotation.Id;

@Entity(name = "credentials")
public class Credential implements SaveableEntity<Credential, CredentialDto, CredentialService, CredentialRepository> {
    
    @Id
    private Long id;

    @Reference
    private User user;

    private CredentialType type;
    private Pair<char[], char[]> credential;
    
    @Deprecated
    private Credential() {}
    
    public Credential(User user, CredentialType type, Pair<char[], char[]> credential) {
        this.user = user;
        this.type = type;
        this.credential = credential;
    }

    public Long getId() {
        return this.id;
    }

    public User getUser() {
        return this.user;
    }

    public CredentialType getType() {
        return this.type;
    }

    public Pair<char[], char[]> getCredential() {
        return this.credential;
    }

    public void setType(CredentialType type) {
        this.type = type;
    }

    public void setCredential(Pair<char[], char[]> credential) {
        this.credential = credential;
    }

    @Override
    public CredentialService getService() {
        return Stirling.get().getCredentialService();
    }

    @Override
    public CredentialDto getDto() {
        return new CredentialDto(this);
    }
}
