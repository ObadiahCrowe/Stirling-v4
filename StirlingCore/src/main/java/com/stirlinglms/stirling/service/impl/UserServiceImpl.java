package com.stirlinglms.stirling.service.impl;

import com.google.common.collect.Sets;
import com.stirlinglms.stirling.email.Email;
import com.stirlinglms.stirling.email.EmailProvider;
import com.stirlinglms.stirling.entity.email.EmailData;
import com.stirlinglms.stirling.entity.user.User;
import com.stirlinglms.stirling.exception.*;
import com.stirlinglms.stirling.repository.UserRepository;
import com.stirlinglms.stirling.service.UserService;
import com.stirlinglms.stirling.util.UpdateLevel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private static final String[] VALID_FIELDS = new String[] {
      "displayName",
      "emailAddresses",
      "type",
      "group",
      "password",
      "credentials",
      "school"
    };

    private final UserRepository repository;
    private final PasswordEncoder encoder;
    private final EmailProvider emailProvider;

    @Autowired
    UserServiceImpl(UserRepository repository, PasswordEncoder encoder, EmailProvider emailProvider) {
        this.repository = repository;
        this.encoder = encoder;
        this.emailProvider = emailProvider;
    }

    @Override
    public User createUser(String username, String emailAddress, String password) throws UserExistsException, EmailExistsException, InvalidInputException {
        if (this.getByUsername(username) != null) {
            throw new UserExistsException(username);
        }

        if (this.getByEmailAddress(emailAddress) != null) {
            throw new EmailExistsException(emailAddress);
        }

        if (password.length() < 8) {
            throw new InvalidInputException("Password is too short! Minimum length is: 8.");
        }

        User user = new User(username, emailAddress, this.encoder.encode(password));

        this.repository.save(user);

        EmailData data = user.getEmailAddresses().stream().findFirst().orElseThrow(() -> new NullPointerException("Email data does not exist."));

        this.sendVerifyAddressEmail(user, data);

        return user;
    }

    @Override
    public User getByUsername(String username) throws NullPointerException {
        return this.getAll().stream().filter(u -> u.getAccountName().equalsIgnoreCase(username)).findFirst().orElse(null);
    }

    @Override
    public User update(User entity, UpdateLevel level) throws Exception {
        return this.repository.save(entity);
    }

    @Override
    public User delete(User entity) {
        this.repository.delete(entity);

        return entity;
    }

    @Override
    public User getById(Long id) {
        return this.repository.findById(id).orElse(null);
    }

    @Override
    public User getByEmailAddress(String emailAddress) {
        return this.getAll().stream()
          .filter(u -> u.getEmailAddresses().stream()
            .filter(e -> e.getAddress().equalsIgnoreCase(emailAddress) && e.isVerified())
            .findFirst()
            .orElse(null) != null)
          .findFirst()
          .orElse(null);
    }

    @Override
    public User getByUuid(UUID uuid) {
        return this.getAll().stream().filter(u -> u.getUuid().equals(uuid)).findFirst().orElse(null);
    }

    @Override
    public EmailData sendVerifyAddressEmail(User user, EmailData data) throws EmailExistsException {
        User possible = this.getByEmailAddress(data.getAddress());
        if (possible != null && !possible.getId().equals(user.getId())) {
            throw new EmailExistsException(data.getAddress());
        }

        if (data.isVerified()) {
            throw new IllegalStateException("Email has already been verified!");
        }

        String hash = this.genHash(user.getAccountName());
        this.emailProvider.getPendingVerifications()
          .put(data, hash);

        try {
            this.emailProvider.sendMailAsync(
              new Email.Builder("Verify your email")
                .setContent("Verify your Stirling account by clicking the following link: <br/><br/><a href=\"https://api.stirlinglms.com/v1/email/verify?username=" +
                  user.getAccountName() + "&hash=" + hash + "\">Click here</a><br/><br/>Thank you")
                .addTo(data.getAddress())
                .build(),
              true);
        } catch (EmailLimitedException | EmailNoContentException | EmailNoRecipientException e) {
            e.printStackTrace();

            throw new NullPointerException(e.getMessage());
        }

        return data;
    }

    @Override
    public EmailData verifyEmailAddress(User user, String hash) throws IllegalStateException {
        return this.verifyEmailAddress(user.getAccountName(), hash);
    }

    @Override
    public EmailData verifyEmailAddress(String username, String hash) throws IllegalStateException {
        EmailData data = this.emailProvider.getPendingVerifications().entrySet().stream()
          .filter(e -> e.getValue().equals(hash))
          .map(Map.Entry::getKey)
          .findFirst()
          .orElse(null);

        if (data == null) {
            throw new IllegalStateException("This hash is expired.");
        }

        data.setVerified(true);

        if (!this.encoder.matches(username, hash)) {
            throw new IllegalStateException("Request was not sent from this account!");
        }

        User user = this.getByUsername(username);
        Set<EmailData> set = user.getEmailAddresses().stream()
          .filter(d -> !d.getAddress().equalsIgnoreCase(data.getAddress()))
          .collect(Collectors.toSet());

        set.add(data);
        user.setEmailAddresses(set);

        this.repository.performTransaction((repo) -> {
            repo.save(user);
            return null;
        });

        return data;
    }

    @Override
    public Set<User> getAll() {
        return Collections.unmodifiableSet(Sets.newConcurrentHashSet(this.repository.findAll()));
    }

    @Override
    public UserRepository getRepository() {
        return this.repository;
    }

    private String genHash(String username) {
        return this.encoder.encode(username);
    }
}
