package com.stirlinglms.stirling.dto;

import com.stirlinglms.stirling.entity.email.EmailData;
import com.stirlinglms.stirling.entity.user.User;
import com.stirlinglms.stirling.entity.user.grouping.UserType;
import lombok.Getter;

import javax.annotation.concurrent.Immutable;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Immutable
public final class UserDto {

    private final long id;
    private final String displayName;
    private final String accountName;

    private final UserType type;
    private final String group;

    private final Set<EmailDataDto> emailData;
    private final SchoolDto school;

    public UserDto(User user) {
        this.id = user.getId();
        this.displayName = user.getDisplayName();
        this.accountName = user.getAccountName();

        this.type = user.getType();
        this.group = user.getGroup().getName();

        this.emailData = user.getEmailAddresses().stream().map(EmailData::getDto).collect(Collectors.toSet());
        this.school = user.getSchool() == null ? null : user.getSchool().getDto();
    }
}
