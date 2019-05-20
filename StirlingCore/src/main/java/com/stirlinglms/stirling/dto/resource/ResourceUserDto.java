package com.stirlinglms.stirling.dto.resource;

import com.stirlinglms.stirling.entity.user.User;
import com.stirlinglms.stirling.entity.user.grouping.UserType;
import lombok.Getter;

import javax.annotation.concurrent.Immutable;

@Getter
@Immutable
public final class ResourceUserDto {

    private final String displayName;
    private final String accountName;

    private final UserType type;
    private final String group;

    public ResourceUserDto(User user) {
        this.displayName = user.getDisplayName();
        this.accountName = user.getAccountName();

        this.type = user.getType();
        this.group = user.getGroup().getName();
    }
}
