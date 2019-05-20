package com.stirlinglms.stirling.entity.user;

import com.google.common.collect.Sets;
import com.stirlinglms.stirling.Stirling;
import com.stirlinglms.stirling.dto.UserDto;
import com.stirlinglms.stirling.entity.SaveableEntity;
import com.stirlinglms.stirling.entity.credential.Credential;
import com.stirlinglms.stirling.entity.email.EmailData;
import com.stirlinglms.stirling.entity.school.School;
import com.stirlinglms.stirling.entity.user.grouping.UserGroup;
import com.stirlinglms.stirling.entity.user.grouping.UserType;
import com.stirlinglms.stirling.repository.UserRepository;
import com.stirlinglms.stirling.service.UserService;
import com.stirlinglms.stirling.util.UpdateLevel;
import org.springframework.cloud.gcp.data.datastore.core.mapping.Entity;
import org.springframework.cloud.gcp.data.datastore.core.mapping.Reference;
import org.springframework.data.annotation.Id;

import java.util.Collections;
import java.util.Set;
import java.util.UUID;

@Entity(name = "users")
public class User implements SaveableEntity<User, UserDto, UserService, UserRepository> {

    @Id
    private Long id;

    private String displayName;
    private String accountName;
    private Set<EmailData> emailAddresses;

    //private Locale locale; Coming after MVP
    private UserType type;
    private UserGroup group;

    private UUID uuid;
    private String password;

    @Reference
    private Set<Credential> credentials;

    @Reference
    private School school;

    /**
     * For database instantiation only. Do not attempt from instantiate via this.
     */
    @Deprecated
    private User() {}

    public User(final String accountName, final String emailAddress, final String password) {
        this.displayName = accountName;
        this.accountName = accountName;
        this.emailAddresses = Sets.newConcurrentHashSet(Collections.singletonList(new EmailData(emailAddress)));

        this.type = UserType.VISITOR;
        this.group = UserGroup.UNGROUPED;

        this.uuid = UUID.randomUUID();
        this.password = password;

        this.credentials = Sets.newConcurrentHashSet();
        this.school = null;
    }

    public Long getId() {
        return this.id;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public String getAccountName() {
        return this.accountName;
    }

    public Set<EmailData> getEmailAddresses() {
        return Collections.unmodifiableSet(this.emailAddresses);
    }

    public UserType getType() {
        return this.type;
    }

    public UserGroup getGroup() {
        return this.group;
    }

    public UUID getUuid() {
        return this.uuid;
    }

    public Set<Credential> getCredentials() {
        return this.credentials;
    }

    public School getSchool() {
        return this.school;
    }

    public String getPassword() {
        return this.password;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setEmailAddresses(Set<EmailData> emailAddresses) {
        this.emailAddresses = emailAddresses;
    }

    public void setType(UserType type) {
        this.type = type;
    }

    public void setGroup(UserGroup group) {
        this.group = group;
    }

    public void setCredentials(Set<Credential> credentials) {
        this.credentials = credentials;
    }

    public void setSchool(School school) {
        this.school = school;
    }

    @Override
    public UserService getService() {
        return Stirling.get().getUserService();
    }

    @Override
    public UserDto getDto() {
        return new UserDto(this);
    }
}
