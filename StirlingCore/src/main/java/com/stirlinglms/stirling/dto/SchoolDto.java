package com.stirlinglms.stirling.dto;

import com.stirlinglms.stirling.entity.school.School;
import lombok.Getter;

import javax.annotation.concurrent.Immutable;

@Getter
@Immutable
public final class SchoolDto {

    private final long id;
    private final String name;

    private final String location;
    private final String phoneNumber;
    private final String emailAddress;
    
    public SchoolDto(School school) {
        this.id = school.getId();
        this.name = school.getName();

        this.location = school.getLocation();
        this.phoneNumber = school.getPhoneNumber();
        this.emailAddress = school.getEmailAddress();
    }
}
