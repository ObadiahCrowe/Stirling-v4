package com.stirlinglms.stirling.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.stirlinglms.stirling.Stirling;
import com.stirlinglms.stirling.entity.announcement.Announcement;
import com.stirlinglms.stirling.entity.classroom.Classroom;
import com.stirlinglms.stirling.entity.classroom.assignment.Assignment;
import com.stirlinglms.stirling.entity.classroom.assignment.AssignmentUserData;
import com.stirlinglms.stirling.entity.classroom.assignment.Result;
import com.stirlinglms.stirling.entity.classroom.info.ClassHomework;
import com.stirlinglms.stirling.entity.classroom.info.ClassPost;
import com.stirlinglms.stirling.entity.classroom.info.ClassSection;
import com.stirlinglms.stirling.entity.classroom.lesson.Lesson;
import com.stirlinglms.stirling.entity.classroom.time.TimeSlot;
import com.stirlinglms.stirling.entity.credential.Credential;
import com.stirlinglms.stirling.entity.email.EmailData;
import com.stirlinglms.stirling.entity.note.Note;
import com.stirlinglms.stirling.entity.res.Resource;
import com.stirlinglms.stirling.entity.school.School;
import com.stirlinglms.stirling.entity.user.User;
import com.stirlinglms.stirling.entity.user.grouping.UserGroup;
import com.stirlinglms.stirling.util.Pair;
import org.springframework.cloud.gcp.data.datastore.core.convert.DatastoreCustomConversions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;

import java.util.Arrays;

@Configuration
public class DatastoreConfig {

    private static final Gson GSON = new GsonBuilder().disableHtmlEscaping().serializeNulls().setPrettyPrinting().create();

    // Result
    private static final Converter<Result, String> RESULT_STRING = new Converter<Result, String>() {
        @Override
        public String convert(Result result) {
            return GSON.toJson(result);
        }
    };
    private static final Converter<String, Result> STRING_RESULT = new Converter<String, Result>() {
        @Override
        public Result convert(String s) {
            return GSON.fromJson(s, Result.class);
        }
    };

    // Email Data
    private static final Converter<EmailData, String> EMAIL_DATA_STRING = new Converter<EmailData, String>() {
        @Override
        public String convert(EmailData emailData) {
            return GSON.toJson(emailData);
        }
    };
    private static final Converter<String, EmailData> STRING_EMAIL_DATA = new Converter<String, EmailData>() {
        @Override
        public EmailData convert(String s) {
            return GSON.fromJson(s, EmailData.class);
        }
    };

    // Time Slot
    private static final Converter<TimeSlot, String> TIME_SLOT_STRING = new Converter<TimeSlot, String>() {
        @Override
        public String convert(TimeSlot timeSlot) {
            return GSON.toJson(timeSlot);
        }
    };
    private static final Converter<String, TimeSlot> STRING_TIME_SLOT = new Converter<String, TimeSlot>() {
        @Override
        public TimeSlot convert(String s) {
            return GSON.fromJson(s, TimeSlot.class);
        }
    };

    // User Group
    private static final Converter<UserGroup, String> USER_GROUP_STRING = new Converter<UserGroup, String>() {
        @Override
        public String convert(UserGroup userGroup) {
            return GSON.toJson(userGroup);
        }
    };
    private static final Converter<String, UserGroup> STRING_USER_GROUP = new Converter<String, UserGroup>() {
        @Override
        public UserGroup convert(String s) {
            return GSON.fromJson(s, UserGroup.class);
        }
    };

    // Announcement
    private static final Converter<Announcement, Long> ANNOUNCEMENT_LONG = new Converter<Announcement, Long>() {
        @Override
        public Long convert(Announcement announcement) {
            return announcement.getId();
        }
    };
    private static final Converter<Long, Announcement> LONG_ANNOUNCEMENT = new Converter<Long, Announcement>() {
        @Override
        public Announcement convert(Long aLong) {
            return Stirling.get().getAnnouncementService().getById(aLong);
        }
    };

    // Assignment
    private static final Converter<Assignment, Long> ASSIGNMENT_LONG = new Converter<Assignment, Long>() {
        @Override
        public Long convert(Assignment assignment) {
            return assignment.getId();
        }
    };
    private static final Converter<Long, Assignment> LONG_ASSIGNMENT = new Converter<Long, Assignment>() {
        @Override
        public Assignment convert(Long aLong) {
            return Stirling.get().getAssignmentService().getById(aLong);
        }
    };

    // Assignment User Data
    private static final Converter<AssignmentUserData, Long> ASSIGNMENT_USER_DATA_LONG = new Converter<AssignmentUserData, Long>() {
        @Override
        public Long convert(AssignmentUserData assignmentUserData) {
            return assignmentUserData.getId();
        }
    };
    private static final Converter<Long, AssignmentUserData> LONG_ASSIGNMENT_USER_DATA = new Converter<Long, AssignmentUserData>() {
        @Override
        public AssignmentUserData convert(Long aLong) {
            return Stirling.get().getAssignmentUserDataService().getById(aLong);
        }
    };

    // Homework
    private static final Converter<ClassHomework, Long> HOMEWORK_LONG = new Converter<ClassHomework, Long>() {
        @Override
        public Long convert(ClassHomework classHomework) {
            return classHomework.getId();
        }
    };
    private static final Converter<Long, ClassHomework> LONG_HOMEWORK = new Converter<Long, ClassHomework>() {
        @Override
        public ClassHomework convert(Long aLong) {
            return Stirling.get().getHomeworkService().getById(aLong);
        }
    };

    // Post
    private static final Converter<ClassPost, Long> POST_LONG = new Converter<ClassPost, Long>() {
        @Override
        public Long convert(ClassPost classPost) {
            return classPost.getId();
        }
    };
    private static final Converter<Long, ClassPost> LONG_POST = new Converter<Long, ClassPost>() {
        @Override
        public ClassPost convert(Long aLong) {
            return Stirling.get().getPostService().getById(aLong);
        }
    };

    // Section
    private static final Converter<ClassSection, Long> SECTION_LONG = new Converter<ClassSection, Long>() {
        @Override
        public Long convert(ClassSection classSection) {
            return classSection.getId();
        }
    };
    private static final Converter<Long, ClassSection> LONG_SECTION = new Converter<Long, ClassSection>() {
        @Override
        public ClassSection convert(Long aLong) {
            return Stirling.get().getSectionService().getById(aLong);
        }
    };

    // Lesson
    private static final Converter<Lesson, Long> LESSON_LONG = new Converter<Lesson, Long>() {
        @Override
        public Long convert(Lesson lesson) {
            return lesson.getId();
        }
    };
    private static final Converter<Long, Lesson> LONG_LESSON = new Converter<Long, Lesson>() {
        @Override
        public Lesson convert(Long aLong) {
            return Stirling.get().getLessonService().getById(aLong);
        }
    };

    // Classroom
    private static final Converter<Classroom, Long> CLASSROOM_LONG = new Converter<Classroom, Long>() {
        @Override
        public Long convert(Classroom classroom) {
            return classroom.getId();
        }
    };
    private static final Converter<Long, Classroom> LONG_CLASSROOM = new Converter<Long, Classroom>() {
        @Override
        public Classroom convert(Long aLong) {
            return Stirling.get().getClassroomService().getById(aLong);
        }
    };

    // Credential
    private static final Converter<Credential, Long> CREDENTIAL_LONG = new Converter<Credential, Long>() {
        @Override
        public Long convert(Credential credential) {
            return credential.getId();
        }
    };
    private static final Converter<Long, Credential> LONG_CREDENTIAL = new Converter<Long, Credential>() {
        @Override
        public Credential convert(Long aLong) {
            return Stirling.get().getCredentialService().getById(aLong);
        }
    };

    // Note
    private static final Converter<Note, Long> NOTE_LONG = new Converter<Note, Long>() {
        @Override
        public Long convert(Note note) {
            return note.getId();
        }
    };
    private static final Converter<Long, Note> LONG_NOTE = new Converter<Long, Note>() {
        @Override
        public Note convert(Long aLong) {
            return Stirling.get().getNoteService().getById(aLong);
        }
    };

    // Resource
    private static final Converter<Resource, Long> RESOURCE_LONG = new Converter<Resource, Long>() {
        @Override
        public Long convert(Resource resource) {
            return resource.getId();
        }
    };
    private static final Converter<Long, Resource> LONG_RESOURCE = new Converter<Long, Resource>() {
        @Override
        public Resource convert(Long aLong) {
            return Stirling.get().getResourceService().getById(aLong);
        }
    };

    // School
    private static final Converter<School, Long> SCHOOL_LONG = new Converter<School, Long>() {
        @Override
        public Long convert(School school) {
            return school.getId();
        }
    };
    private static final Converter<Long, School> LONG_SCHOOL = new Converter<Long, School>() {
        @Override
        public School convert(Long aLong) {
            return Stirling.get().getSchoolService().getById(aLong);
        }
    };

    // User
    private static final Converter<User, Long> USER_LONG = new Converter<User, Long>() {
        @Override
        public Long convert(User user) {
            return user.getId();
        }
    };
    private static final Converter<Long, User> LONG_USER = new Converter<Long, User>() {
        @Override
        public User convert(Long aLong) {
            return Stirling.get().getUserService().getById(aLong);
        }
    };

    // Pair
    private static final Converter<Pair<char[], char[]>, String> PAIR_STRING = new Converter<Pair<char[], char[]>, String>() {
        @Override
        public String convert(Pair<char[], char[]> pair) {
            JsonObject obj = new JsonObject();

            obj.addProperty(new String(pair.getType1()), new String(pair.getType2()));

            return obj.toString();
        }
    };
    private static final Converter<String, Pair<char[], char[]>> STRING_PAIR = new Converter<String, Pair<char[], char[]>>() {
        @Override
        public Pair<char[], char[]> convert(String s) {
            String[] parts = s
              .replace("{", "")
              .replace("\"", "")
              .replace("}", "")
              .split(":");

            return new Pair<>(parts[0].toCharArray(), parts[1].toCharArray());
        }
    };

    @Bean
    public DatastoreCustomConversions datastoreCustomConversions() {
        return new DatastoreCustomConversions(Arrays.asList(
          RESULT_STRING,
          STRING_RESULT,

          EMAIL_DATA_STRING,
          STRING_EMAIL_DATA,

          TIME_SLOT_STRING,
          STRING_TIME_SLOT,

          USER_GROUP_STRING,
          STRING_USER_GROUP,

          ANNOUNCEMENT_LONG,
          LONG_ANNOUNCEMENT,

          ASSIGNMENT_LONG,
          LONG_ASSIGNMENT,

          ASSIGNMENT_USER_DATA_LONG,
          LONG_ASSIGNMENT_USER_DATA,

          HOMEWORK_LONG,
          LONG_HOMEWORK,

          POST_LONG,
          LONG_POST,

          SECTION_LONG,
          LONG_SECTION,

          LESSON_LONG,
          LONG_LESSON,

          CLASSROOM_LONG,
          LONG_CLASSROOM,

          CREDENTIAL_LONG,
          LONG_CREDENTIAL,

          NOTE_LONG,
          LONG_NOTE,

          RESOURCE_LONG,
          LONG_RESOURCE,

          SCHOOL_LONG,
          LONG_SCHOOL,

          USER_LONG,
          LONG_USER,

          PAIR_STRING,
          STRING_PAIR
        ));
    }
}
