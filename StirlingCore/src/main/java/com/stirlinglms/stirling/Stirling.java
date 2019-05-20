package com.stirlinglms.stirling;

import com.stirlinglms.stirling.service.*;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.concurrent.atomic.AtomicReference;

@Component
public interface Stirling {

    static Stirling get() {
        return Implementation.STIRLING.get();
    }

    InputStream getGoogleApiSecrets();

    AnnouncementService getAnnouncementService();

    AssignmentService getAssignmentService();

    AssignmentUserDataService getAssignmentUserDataService();

    ClassHomeworkService getHomeworkService();

    ClassPostService getPostService();

    ClassroomService getClassroomService();

    ClassSectionService getSectionService();

    CredentialService getCredentialService();

    LessonService getLessonService();

    NoteService getNoteService();

    PaymentService getPaymentService();

    ResourceService getResourceService();

    SchoolService getSchoolService();

    TimeSlotService getTimeSlotService();

    UserService getUserService();

    class Implementation {

        static final AtomicReference<Stirling> STIRLING = new AtomicReference<>(null);
    }
}
