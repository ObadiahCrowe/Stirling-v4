package com.stirlinglms.stirling.service.impl;

import com.google.common.collect.Sets;
import com.stirlinglms.stirling.entity.credential.Credential;
import com.stirlinglms.stirling.entity.credential.CredentialType;
import com.stirlinglms.stirling.entity.user.User;
import com.stirlinglms.stirling.repository.CredentialRepository;
import com.stirlinglms.stirling.service.CredentialService;
import com.stirlinglms.stirling.util.Pair;
import com.stirlinglms.stirling.util.UpdateLevel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class CredentialServiceImpl implements CredentialService {

    private static final String[] VALID_FIELDS = new String[] {
      "type",
      "credential"
    };

    private final CredentialRepository repository;

    @Autowired
    CredentialServiceImpl(CredentialRepository repository) {
        this.repository = repository;
    }

    @Override
    public Credential createCredential(User user, CredentialType type, Pair<char[], char[]> credential) {
        Credential cred = new Credential(user, type, credential);

        this.repository.save(cred);

        return cred;
    }

    @Override
    public Credential update(Credential entity, UpdateLevel level) throws Exception {
        return this.repository.performTransaction((repo) -> repo.save(entity));
    }

    @Override
    public Credential delete(Credential entity) {
        this.repository.delete(entity);

        return entity;
    }

    @Override
    public Credential getById(Long id) {
        return this.repository.findById(id).orElse(null);
    }

    @Override
    public Set<Credential> getAll() {
        return Sets.newConcurrentHashSet(this.repository.findAll());
    }

    @Override
    public CredentialRepository getRepository() {
        return this.repository;
    }
}
