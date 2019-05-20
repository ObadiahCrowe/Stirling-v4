package com.stirlinglms.stirling.entity.school;

import com.stirlinglms.stirling.Stirling;
import com.stirlinglms.stirling.dto.SchoolDto;
import com.stirlinglms.stirling.entity.SaveableEntity;
import com.stirlinglms.stirling.entity.classroom.time.TimeSlot;
import com.stirlinglms.stirling.entity.credential.CredentialType;
import com.stirlinglms.stirling.repository.SchoolRepository;
import com.stirlinglms.stirling.service.SchoolService;
import com.stirlinglms.stirling.util.UpdateLevel;
import org.springframework.cloud.gcp.data.datastore.core.mapping.Entity;
import org.springframework.cloud.gcp.data.datastore.core.mapping.Reference;
import org.springframework.data.annotation.Id;

import javax.annotation.concurrent.Immutable;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

@Immutable
@Entity(name = "schools")
public class School implements SaveableEntity<School, SchoolDto, SchoolService, SchoolRepository> {

    @Id
    private Long id;

    private String name;

    private String location;
    private String phoneNumber;
    private String emailAddress;

    private CredentialType primaryPlatform;
    private Map<CredentialType, String> platformSources;

    @Reference
    private Set<TimeSlot> timeSlots;

    //private Subscription subscription;
    //private Instant appliedOn;

    @Deprecated
    private School() {}

    public School(String name, String location) {
        this.name = name;

        this.location = location;
        this.phoneNumber = "";
        this.emailAddress = "";
    }

    public School(String name, String location, String phoneNumber, String emailAddress) {
        this.name = name;

        this.location = location;
        this.phoneNumber = phoneNumber;
        this.emailAddress = emailAddress;
    }

    public School(String name, String location, String phoneNumber, String emailAddress, CredentialType primaryPlatform,
                  Map<CredentialType, String> platformSources, Set<TimeSlot> timeSlots) {
        this.name = name;

        this.location = location;
        this.phoneNumber = phoneNumber;
        this.emailAddress = emailAddress;

        this.primaryPlatform = primaryPlatform;
        this.platformSources = platformSources;
        this.timeSlots = timeSlots;
    }

    public Long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getLocation() {
        return this.location;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public String getEmailAddress() {
        return this.emailAddress;
    }

    public CredentialType getPrimaryPlatform() {
        return this.primaryPlatform;
    }

    public Map<CredentialType, String> getPlatformSources() {
        return this.platformSources;
    }

    public Set<TimeSlot> getTimeSlots() {
        return this.timeSlots;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public void setPrimaryPlatform(CredentialType primaryPlatform) {
        this.primaryPlatform = primaryPlatform;
    }

    public void setPlatformSources(Map<CredentialType, String> platformSources) {
        this.platformSources = platformSources;
    }

    public void setTimeSlots(Set<TimeSlot> timeSlots) {
        this.timeSlots = timeSlots;
    }

    @Override
    public SchoolService getService() {
        return Stirling.get().getSchoolService();
    }

    @Override
    public SchoolDto getDto() {
        return new SchoolDto(this);
    }
}
