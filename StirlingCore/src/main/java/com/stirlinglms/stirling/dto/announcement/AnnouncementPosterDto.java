package com.stirlinglms.stirling.dto.announcement;

import com.stirlinglms.stirling.dto.UserDto;
import com.stirlinglms.stirling.entity.user.grouping.UserType;
import lombok.Getter;

import javax.annotation.concurrent.Immutable;

@Getter
@Immutable
public final class AnnouncementPosterDto {

    private final long id;
    private final String displayName;
    private final String accountName;

    private final UserType type;
    private final String group;

    public AnnouncementPosterDto(UserDto dto) {
        this.id = dto.getId();
        this.displayName = dto.getDisplayName();
        this.accountName = dto.getAccountName();

        this.type = dto.getType();
        this.group = dto.getGroup();
    }
}
