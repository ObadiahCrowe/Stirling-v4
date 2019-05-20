package com.stirlinglms.stirling.service;

import com.stirlinglms.stirling.entity.email.EmailData;
import com.stirlinglms.stirling.entity.user.User;
import com.stirlinglms.stirling.exception.EmailExistsException;
import com.stirlinglms.stirling.exception.InvalidInputException;
import com.stirlinglms.stirling.exception.UserExistsException;
import com.stirlinglms.stirling.repository.UserRepository;

import java.util.UUID;

public interface UserService extends RepositoryService<User, UserRepository> {

    User createUser(String username, String emailAddress, String password) throws UserExistsException, EmailExistsException, InvalidInputException;

    User getByUsername(String username) throws NullPointerException;

    User getByEmailAddress(String emailAddress);

    User getByUuid(UUID uuid);

    EmailData sendVerifyAddressEmail(User user, EmailData data) throws EmailExistsException;

    EmailData verifyEmailAddress(User user, String hash) throws IllegalStateException;

    EmailData verifyEmailAddress(String username, String hash) throws IllegalStateException;
}
