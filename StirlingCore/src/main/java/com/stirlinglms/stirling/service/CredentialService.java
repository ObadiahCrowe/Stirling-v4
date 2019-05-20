package com.stirlinglms.stirling.service;

import com.stirlinglms.stirling.entity.credential.Credential;
import com.stirlinglms.stirling.entity.credential.CredentialType;
import com.stirlinglms.stirling.entity.user.User;
import com.stirlinglms.stirling.repository.CredentialRepository;
import com.stirlinglms.stirling.util.Pair;

public interface CredentialService extends RepositoryService<Credential, CredentialRepository> {

    Credential createCredential(User user, CredentialType type, Pair<char[], char[]> credential);
}
