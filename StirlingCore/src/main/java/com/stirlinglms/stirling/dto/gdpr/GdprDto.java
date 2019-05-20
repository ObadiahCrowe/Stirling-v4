package com.stirlinglms.stirling.dto.gdpr;

import com.stirlinglms.stirling.dto.*;
import com.stirlinglms.stirling.dto.announcement.AnnouncementDto;
import com.stirlinglms.stirling.dto.assignment.AssignmentBaseDto;
import com.stirlinglms.stirling.entity.credential.Credential;
import com.stirlinglms.stirling.entity.email.EmailData;
import com.stirlinglms.stirling.entity.user.User;
import com.stirlinglms.stirling.entity.user.grouping.UserType;
import lombok.Getter;
import org.springframework.data.annotation.Immutable;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@Immutable
public final class GdprDto {

    private final String displayName;
    private final String accountName;

    private final Set<EmailDataDto> emailAddresses;

    private final UserType type;
    private final String group;

    private final UUID uuid;
    private final Set<CredentialDto> credentials;

    private final Set<AssignmentBaseDto> postedAssignments;
    private final Set<AssignmentUserDataDto> completedAssignments;

    private final Set<NoteDto> notes;
    private final Set<ClassPostDto> posts;
    private final Set<ClassHomeworkDto> homework;

    private final Set<ClassroomDto> classes;
    private final Set<AnnouncementDto> postedAnnouncements;
    private final Set<AnnouncementDto> receivedAnnouncements;

    private final Set<PaymentDto> payments;

    // TODO: 2019-01-20  
    public GdprDto(User user) {
        this.displayName = user.getDisplayName();
        this.accountName = user.getAccountName();

        this.emailAddresses = user.getEmailAddresses().stream().map(EmailData::getDto).collect(Collectors.toSet());

        this.type = user.getType();
        this.group = user.getGroup().getName();

        this.uuid = user.getUuid();
        this.credentials = user.getCredentials().stream().map(Credential::getDto).collect(Collectors.toSet());

        this.postedAssignments = null;
        this.completedAssignments = null;
        
        this.notes = null;
        this.posts = null;
        this.homework = null;
        
        this.classes = null;
        this.postedAnnouncements = null;
        this.receivedAnnouncements = null;
        
        this.payments = null;
    }
}
