package com.stirlinglms.stirling.integration.service.impl;

import com.stirlinglms.stirling.entity.user.User;
import com.stirlinglms.stirling.integration.entity.ImportableClass;
import com.stirlinglms.stirling.integration.entity.moodle.MoodleClass;
import com.stirlinglms.stirling.integration.service.MoodleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class MoodleServiceImpl implements MoodleService {

    @Autowired
    MoodleServiceImpl() {
        // TODO: 2019-01-23
    }

    @Override
    public Set<ImportableClass<Short>> getCoursesList(User user) {
        return null;
    }

    @Override
    public boolean validCredentials(User user) {
        return false;
    }

    @Override
    public MoodleClass getCourse(User user, ImportableClass<Short> identifier) {
        return null;
    }

    @Override
    public Set<MoodleClass> getCourses(User user) {
        return null;
    }
}
