package com.stirlinglms.stirling.integration.service.impl;

import com.gargoylesoftware.htmlunit.DefaultCredentialsProvider;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.stirlinglms.stirling.concurrent.PoolSpec;
import com.stirlinglms.stirling.concurrent.ThreadPool;
import com.stirlinglms.stirling.entity.classroom.AttendanceStatus;
import com.stirlinglms.stirling.entity.classroom.time.TimeSlot;
import com.stirlinglms.stirling.entity.credential.Credential;
import com.stirlinglms.stirling.entity.credential.CredentialType;
import com.stirlinglms.stirling.entity.user.User;
import com.stirlinglms.stirling.integration.entity.ImportableClass;
import com.stirlinglms.stirling.integration.entity.daymap.DaymapClass;
import com.stirlinglms.stirling.integration.service.DaymapService;
import com.stirlinglms.stirling.service.TimeSlotService;
import com.stirlinglms.stirling.util.Quadlet;
import com.stirlinglms.stirling.util.Tuple;
import com.stirlinglms.stirling.util.WebClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

@Service
public class DaymapServiceImpl implements DaymapService {

    private static final ThreadPool SCRAPER = ThreadPool.createForSpec(PoolSpec.INTEGRATION);

    private static final String AUTH_PREFIX = "curric\\";

    private static final int PROVIDER_PORT = -1;
    private static final String PROVIDER_WORKSTATION = "localhost";
    private static final String PROVIDER_DOMAIN = "curric";

    private static final String DAYMAP_DAYPLAN = "https://daymap.gihs.sa.edu.au/daymap/student/dayplan.aspx";

    private static final String XPATH_GET_COURSES = "//*[@id=\"ctl00_cp_divStudent\"]/div/table/tbody/tr[1]/td[3]/div";
    private static final String XPATH_GET_SLOT_ROOM = "//*[@id=\"divEvents\"]/div";

    private final TimeSlotService timeSlotService;

    @Autowired
    DaymapServiceImpl(TimeSlotService timeSlotService) {
        this.timeSlotService = timeSlotService;
    }

    @Override
    public Set<ImportableClass<Integer>> getCoursesList(User user) {
        Authenticator.setDefault(this.getAuthenticator(user));

        if (!this.validCredentials(user)) {
            return Sets.newConcurrentHashSet();
        }

        Set<ImportableClass<Integer>> classes = Sets.newConcurrentHashSet();
        WebClient client = new WebClient(this.getProvider(user));

        try {
            HtmlPage dayplan = client.getPage(DAYMAP_DAYPLAN);
            HtmlDivision linkList = (HtmlDivision) dayplan.getByXPath(XPATH_GET_COURSES).get(0);

            linkList.getChildElements().forEach(dom -> classes.add(new DaymapClass(
              dom.getTextContent(),
              Integer.valueOf(dom.getAttributes().getNamedItem("href").getNodeValue().replace("plans/class.aspx?id=", ""))
            )));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return classes;
    }

    @Override
    public boolean validCredentials(User user) {
        Authenticator.setDefault(this.getAuthenticator(user));
        WebClient client = new WebClient(this.getProvider(user));

        try {
            // Seems redundant, but also does checks on the page type.
            HtmlPage page = client.getPage(DAYMAP_DAYPLAN);
            return true;
        } catch (Exception ignored) {}

        return false;
    }

    @Override
    public DaymapClass getCourse(User user, ImportableClass<Integer> identifier) {
        Authenticator.setDefault(this.getAuthenticator(user));

        if (!this.validCredentials(user)) {
            return null;
        }

        final DefaultCredentialsProvider provider = this.getProvider(user);
        DaymapClass daymapClass = (DaymapClass) identifier;

        // Time Slot and Room
        Future<Quadlet<TimeSlot, String, Integer, AttendanceStatus>> tsi = SCRAPER.submit(() -> {
            WebClient slotClient = new WebClient(provider);

            try {
                HtmlPage page = slotClient.getPage(DAYMAP_DAYPLAN);
                HtmlDivision div = (HtmlDivision) page.getByXPath(XPATH_GET_SLOT_ROOM).get(0);

                String dataId = "";
                int dataAttendance = -1;
                String room = "";
                String slot = "";
                String day = "";
                boolean found = false;

                for (DomElement e : div.getChildElements()) {
                    if (e.getAttribute("class").equalsIgnoreCase("L ditm")) {
                        dataId = e.getAttribute("data-id"); // Daymap Class Id
                        dataAttendance = Integer.valueOf(e.getAttribute("data-attendance"));

                        int i = 0;
                        System.out.println("STARTING\n");
                        System.out.println(daymapClass.getName());
                        for (DomElement element : e.getChildElements()) {
                            System.out.println(i);
                            System.out.println(element.getTextContent());
                            if (element.getTextContent().contains(daymapClass.getName())) {
                                System.out.println("in room");
                                room = element.getTextContent().replace(daymapClass.getName(), "").replace(" ", ""); // Room
                            } else if (element.getAttribute("class").equalsIgnoreCase("t")) {
                                slot = element.getTextContent(); // Slot
                            }

                            i++;
                        }
                        System.out.println("ENDING");

                        found = true;
                    } else if (e.getAttribute("class").equalsIgnoreCase("diaryDay")) {
                        if (!found) {
                            day = e.getTextContent().split(",")[0];
                        }
                    }
                }

                System.out.println("time: " + slot);
                System.out.println("day: " + day);

                TimeSlot timeSlot = this.timeSlotService.getOrCreate(user.getSchool(), day, slot);

                System.out.println(new Gson().toJson(timeSlot));

                int data = -1;
                try {
                    data = Integer.valueOf(dataId);
                } catch (NumberFormatException e) {}

                AttendanceStatus status = AttendanceStatus.ROLL_NOT_MARKED;
                switch (dataAttendance) {
                    case 0:
                        status = AttendanceStatus.ROLL_NOT_MARKED;
                        break;
                    case 2:
                        status = AttendanceStatus.PRESENT;
                        break;
                }

                return new Quadlet<>(timeSlot, room, data, status);
            } catch (Exception e) {
                e.printStackTrace();
                return new Quadlet<>(null, "", 0, null);
            }
        });

        try {
            System.out.println(new Gson().toJson(tsi.get()));
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public Set<DaymapClass> getCourses(User user) {
        return this.getCoursesList(user).stream().map(c -> this.getCourse(user, c)).collect(Collectors.toSet());
    }

    private Authenticator getAuthenticator(User user) {
        Optional<Credential> cred = user.getCredentials().stream().filter(c -> c.getType() == CredentialType.DAYMAP).findFirst();
        if (!cred.isPresent()) {
            throw new IllegalStateException("You have not added any Daymap credentials to your account.");
        }

        return new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(
                  AUTH_PREFIX + new String(cred.get().getCredential().getType1()),
                  cred.get().getCredential().getType2()
                );
            }
        };
    }

    private DefaultCredentialsProvider getProvider(User user) {
        Credential credential = user.getCredentials().stream().filter(c -> c.getType() == CredentialType.DAYMAP)
          .findFirst().orElseThrow(() -> new IllegalStateException("You have not added any Daymap credentials to your account."));

        return new DefaultCredentialsProvider() {{
            addNTLMCredentials(
              new String(credential.getCredential().getType1()),
              new String(credential.getCredential().getType2()),
              null,
              PROVIDER_PORT,
              PROVIDER_WORKSTATION,
              PROVIDER_DOMAIN
            );
        }};
    }
}
