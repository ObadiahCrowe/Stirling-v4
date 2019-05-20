package com.stirlinglms.stirling;

import com.google.gson.Gson;
import com.stirlinglms.stirling.entity.credential.Credential;
import com.stirlinglms.stirling.entity.credential.CredentialType;
import com.stirlinglms.stirling.entity.user.User;
import com.stirlinglms.stirling.integration.entity.ImportableClass;
import com.stirlinglms.stirling.integration.service.DaymapService;
import com.stirlinglms.stirling.service.*;
import com.stirlinglms.stirling.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@SpringBootApplication
public class StirlingLMS implements Stirling {

    // TODO: 2019-01-29 All resources except resource api should transfer dtos
    // TODO: 2019-01-29 Credenial APIs when scrapers are done.

    private static final AtomicReference<AnnouncementService> ANNOUNCEMENT_SERVICE = new AtomicReference<>(null);
    private static final AtomicReference<AssignmentService> ASSIGNMENT_SERVICE = new AtomicReference<>(null);
    private static final AtomicReference<AssignmentUserDataService> ASSIGNMENT_USER_DATA_SERVICE = new AtomicReference<>(null);
    private static final AtomicReference<ClassHomeworkService> HOMEWORK_SERVICE = new AtomicReference<>(null);
    private static final AtomicReference<ClassPostService> CLASS_POST_SERVICE = new AtomicReference<>(null);
    private static final AtomicReference<ClassroomService> CLASSROOM_SERVICE = new AtomicReference<>(null);
    private static final AtomicReference<ClassSectionService> CLASS_SECTION_SERVICE = new AtomicReference<>(null);
    private static final AtomicReference<CredentialService> CREDENTIAL_SERVICE = new AtomicReference<>(null);
    private static final AtomicReference<LessonService> LESSON_SERVICE = new AtomicReference<>(null);
    private static final AtomicReference<NoteService> NOTE_SERVICE = new AtomicReference<>(null);
    private static final AtomicReference<PaymentService> PAYMENT_SERVICE = new AtomicReference<>(null);
    private static final AtomicReference<ResourceService> RESOURCE_SERVICE = new AtomicReference<>(null);
    private static final AtomicReference<SchoolService> SCHOOL_SERVICE = new AtomicReference<>(null);
    private static final AtomicReference<TimeSlotService> TIME_SLOT_SERVICE = new AtomicReference<>(null);
    private static final AtomicReference<UserService> USER_SERVICE = new AtomicReference<>(null);

    @Autowired
    public StirlingLMS(
      AnnouncementService announcementService,
      AssignmentService assignmentService,
      AssignmentUserDataService assignmentUserDataService,
      ClassHomeworkService homeworkService,
      ClassPostService classPostService,
      ClassroomService classroomService,
      ClassSectionService classSectionService,
      CredentialService credentialService,
      LessonService lessonService,
      NoteService noteService,
      PaymentService paymentService,
      ResourceService resourceService,
      SchoolService schoolService,
      TimeSlotService timeSlotService,
      UserService userService, DaymapService daymapService) {
        ANNOUNCEMENT_SERVICE.compareAndSet(null, announcementService);
        ASSIGNMENT_SERVICE.compareAndSet(null, assignmentService);
        ASSIGNMENT_USER_DATA_SERVICE.compareAndSet(null, assignmentUserDataService);
        HOMEWORK_SERVICE.compareAndSet(null, homeworkService);
        CLASS_POST_SERVICE.compareAndSet(null, classPostService);
        CLASSROOM_SERVICE.compareAndSet(null, classroomService);
        CLASS_SECTION_SERVICE.compareAndSet(null, classSectionService);
        CREDENTIAL_SERVICE.compareAndSet(null, credentialService);
        LESSON_SERVICE.compareAndSet(null, lessonService);
        NOTE_SERVICE.compareAndSet(null, noteService);
        PAYMENT_SERVICE.compareAndSet(null, paymentService);
        RESOURCE_SERVICE.compareAndSet(null, resourceService);
        SCHOOL_SERVICE.compareAndSet(null, schoolService);
        TIME_SLOT_SERVICE.compareAndSet(null, timeSlotService);
        USER_SERVICE.compareAndSet(null, userService);

        Implementation.STIRLING.compareAndSet(null, this);
    }

    public static void main(String[] args) {
        SpringApplication.run(StirlingLMS.class);
    }

    @Override
    public InputStream getGoogleApiSecrets() {
        return StirlingLMS.class.getClassLoader().getResourceAsStream("client_secret.json");
    }

    @Override
    public AnnouncementService getAnnouncementService() {
        return ANNOUNCEMENT_SERVICE.get();
    }

    @Override
    public AssignmentService getAssignmentService() {
        return ASSIGNMENT_SERVICE.get();
    }

    @Override
    public AssignmentUserDataService getAssignmentUserDataService() {
        return ASSIGNMENT_USER_DATA_SERVICE.get();
    }

    @Override
    public ClassHomeworkService getHomeworkService() {
        return HOMEWORK_SERVICE.get();
    }

    @Override
    public ClassPostService getPostService() {
        return CLASS_POST_SERVICE.get();
    }

    @Override
    public ClassroomService getClassroomService() {
        return CLASSROOM_SERVICE.get();
    }

    @Override
    public ClassSectionService getSectionService() {
        return CLASS_SECTION_SERVICE.get();
    }

    @Override
    public CredentialService getCredentialService() {
        return CREDENTIAL_SERVICE.get();
    }

    @Override
    public LessonService getLessonService() {
        return LESSON_SERVICE.get();
    }

    @Override
    public NoteService getNoteService() {
        return NOTE_SERVICE.get();
    }

    @Override
    public PaymentService getPaymentService() {
        return PAYMENT_SERVICE.get();
    }

    @Override
    public ResourceService getResourceService() {
        return RESOURCE_SERVICE.get();
    }

    @Override
    public SchoolService getSchoolService() {
        return SCHOOL_SERVICE.get();
    }
    
    @Override
    public TimeSlotService getTimeSlotService() {
        return TIME_SLOT_SERVICE.get();
    }

    @Override
    public UserService getUserService() {
        return USER_SERVICE.get();
    }
}
