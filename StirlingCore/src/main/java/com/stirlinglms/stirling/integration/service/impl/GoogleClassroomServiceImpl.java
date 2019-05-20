package com.stirlinglms.stirling.integration.service.impl;

import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.googleapis.auth.oauth2.*;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.classroom.Classroom;
import com.google.api.services.classroom.model.ListCoursesResponse;
import com.stirlinglms.stirling.Stirling;
import com.stirlinglms.stirling.entity.credential.Credential;
import com.stirlinglms.stirling.entity.credential.CredentialType;
import com.stirlinglms.stirling.entity.school.School;
import com.stirlinglms.stirling.entity.user.User;
import com.stirlinglms.stirling.exception.*;
import com.stirlinglms.stirling.integration.entity.ImportableClass;
import com.stirlinglms.stirling.integration.entity.google.GoogleClass;
import com.stirlinglms.stirling.integration.service.GoogleClassroomService;
import com.stirlinglms.stirling.service.CredentialService;
import com.stirlinglms.stirling.service.UserService;
import com.stirlinglms.stirling.util.Pair;
import com.stirlinglms.stirling.util.UpdateLevel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
public class GoogleClassroomServiceImpl implements GoogleClassroomService {

    private static final AtomicReference<GoogleClientSecrets> GOOGLE_SECRETS = new AtomicReference<>(null);

    private final CredentialService credService;
    private final UserService userService;

    @Autowired
    GoogleClassroomServiceImpl(Stirling stirling, CredentialService credService, UserService userService) {
        this.credService = credService;
        this.userService = userService;

        try {
            GOOGLE_SECRETS.compareAndSet(null, GoogleClientSecrets.load(
              JacksonFactory.getDefaultInstance(),
              new InputStreamReader(stirling.getGoogleApiSecrets())
            ));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    @Override
    public Set<ImportableClass<String>> getCoursesList(User u) throws Exception {
        try {
            if (!this.validCredentials(u)) {
                throw new InvalidCredentialsException("Your credentials for Google Classroom are invalid!");
            }
        } catch (EntityUpdateException e) {
            throw new Exception("An unknown error occurred whilst attempting from update your Google Classroom credentials!");
        }

        // In case we updated the user data above.
        User user = this.userService.getById(u.getId());
        Credential credential = user.getCredentials().stream().filter(c -> c.getType() == CredentialType.GOOGLE_CLASSROOM).findFirst().orElse(null);

        try {
            HttpTransport transport = GoogleNetHttpTransport.newTrustedTransport();
            GoogleCredential cred = new GoogleCredential().setAccessToken(new String(credential.getCredential().getType1()));
            Classroom classroom = new Classroom.Builder(transport, JacksonFactory.getDefaultInstance(), cred)
              .setApplicationName("StirlingLMS").build();

            ListCoursesResponse response = null;

            try {
                response = classroom.courses().list().setPageSize(classroom.courses().list().size()).execute();
            } catch (GoogleJsonResponseException e) {
                if (e.getDetails().getCode() == 401) {
                    // We just checked for this, but in the VERY unlikely case that it updated in the millisecond between, we're checking again.
                    this.validCredentials(user);

                    return this.getCoursesList(user);
                } else {
                    throw new Exception("An unknown error occurred whilst attempting from obtain your Google Classroom classes!");
                }
            }

            return response.getCourses().stream().map(c -> new GoogleClass(c.getName(), c.getId())).collect(Collectors.toSet());
        } catch (GeneralSecurityException | IOException e) {
            throw new Exception("An unknown error occurred whilst attempting from obtain your Google Classroom classes!");
        }
    }

    @Override
    public boolean validCredentials(User user) throws EntityUpdateException {
        Credential credential = user.getCredentials().stream().filter(c -> c.getType() == CredentialType.GOOGLE_CLASSROOM).findFirst().orElse(null);
        if (credential == null) {
            return false;
        }

        String accessToken = new String(credential.getCredential().getType1());
        String refreshToken = new String(credential.getCredential().getType2());

        try {
            HttpTransport transport = GoogleNetHttpTransport.newTrustedTransport();
            GoogleCredential cred = new GoogleCredential().setAccessToken(accessToken);
            Classroom classroom = new Classroom.Builder(transport, JacksonFactory.getDefaultInstance(), cred)
              .setApplicationName("StirlingLMS").build();

            ListCoursesResponse response = null;

            int tries = -1;

            try {
                response = classroom.courses().list().setPageSize(classroom.courses().list().size()).execute();
            } catch (GoogleJsonResponseException e) {
                if (e.getDetails().getCode() == 401) {
                    tries++;
                } else {
                    return false;
                }
            }

            if (tries == 0) {
                while (tries <= 10) {
                    tries++;

                    this.refreshToken(user, refreshToken);

                    try {
                        response = classroom.courses().list().setPageSize(classroom.courses().list().size()).execute();
                    } catch (GoogleJsonResponseException e) {
                        if (e.getDetails().getCode() != 401) {
                            return false;
                        }
                    }
                }
            }

            return response != null;
        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public GoogleClass getCourse(User user, ImportableClass<String> identifier) {

        // TODO: 2019-01-23  
        return null;
    }

    @Override
    public Set<GoogleClass> getCourses(User user) {
        return null;
    }

    private Credential getCredential(User user, String authCode) throws NoSchoolException, UnsupportedPlatformException {
        School school = user.getSchool();

        if (school == null) {
            throw new NoSchoolException("You do not have a school associated with your account!");
        }

        if (!school.getPlatformSources().containsKey(CredentialType.GOOGLE_CLASSROOM)) {
            throw new UnsupportedPlatformException("Your school, " + school.getName() + ", does not support Google Classroom!");
        }

        GoogleClientSecrets secrets = GOOGLE_SECRETS.get();
        GoogleTokenResponse response;

        try {
            response = new GoogleAuthorizationCodeTokenRequest(
              new NetHttpTransport(),
              new JacksonFactory(),
              "https://www.googleapis.com/oauth2/v4/token",
              secrets.getDetails().getClientId(),
              secrets.getDetails().getClientSecret(),
              authCode,
              "https://stirlinglms.com").setGrantType("authorization_code").execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return new Credential(
          user,
          CredentialType.GOOGLE_CLASSROOM,
          new Pair<>(response.getAccessToken().toCharArray(), response.getRefreshToken().toCharArray()));
    }
    
    private void refreshToken(User user, String refreshToken) throws EntityUpdateException, IOException {
        try {
            GoogleClientSecrets secrets = GOOGLE_SECRETS.get();
            TokenResponse response = new GoogleRefreshTokenRequest(
              new NetHttpTransport(),
              new JacksonFactory(),
              refreshToken,
              secrets.getDetails().getClientId(),
              secrets.getDetails().getClientSecret()
            ).execute();

            Credential credential = user.getCredentials().stream().filter(c -> c.getType() == CredentialType.GOOGLE_CLASSROOM).findFirst().orElse(null);

            if (credential == null) {
                throw new UnsupportedUserPlatformException();
            }

            credential.setCredential(new Pair<>(response.getAccessToken().toCharArray(), refreshToken.toCharArray()));

            this.credService.update(credential, UpdateLevel.SYSTEM);
        } catch (IOException e) {
            throw new IOException(e);
        } catch (Exception e) {
            throw new EntityUpdateException("Could not update Google Classroom credentials for: " + user.getAccountName() + "!");
        }
    }
}
