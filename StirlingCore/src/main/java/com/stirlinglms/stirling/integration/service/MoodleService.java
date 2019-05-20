package com.stirlinglms.stirling.integration.service;

import com.stirlinglms.stirling.integration.entity.moodle.MoodleClass;

import javax.annotation.concurrent.ThreadSafe;

@ThreadSafe
public interface MoodleService extends ClassService<Short, MoodleClass> {}
