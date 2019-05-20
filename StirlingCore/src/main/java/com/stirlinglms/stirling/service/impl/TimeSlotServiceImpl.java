package com.stirlinglms.stirling.service.impl;

import com.stirlinglms.stirling.entity.classroom.time.TimeSlot;
import com.stirlinglms.stirling.entity.classroom.time.TimeSlotOccurrence;
import com.stirlinglms.stirling.entity.school.School;
import com.stirlinglms.stirling.repository.TimeSlotRepository;
import com.stirlinglms.stirling.service.TimeSlotService;
import com.stirlinglms.stirling.util.UpdateLevel;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@Service
public class TimeSlotServiceImpl implements TimeSlotService {

    @Override
    public TimeSlot createTimeSlot(School school, TimeSlotOccurrence... occurrences) {
        return null;
    }

    @Override
    public TimeSlot createTimeSlot(School school, Collection<TimeSlotOccurrence> occurrences) {
        return null;
    }

    @Override
    public TimeSlot createTimeSlot(School school, Set<TimeSlotOccurrence> occurrences) {
        return null;
    }

    @Override
    public TimeSlot createTimeSlot(School school, List<TimeSlotOccurrence> occurrences) {
        return null;
    }

    @Override
    public TimeSlot getOrCreate(School school, String day, String time) {
        return null;
    }

    @Override
    public TimeSlotRepository getRepository() {
        return null;
    }

    @Override
    public TimeSlot update(TimeSlot entity, UpdateLevel level) throws Exception {
        return null;
    }

    @Override
    public TimeSlot delete(TimeSlot entity) {
        return null;
    }

    @Override
    public TimeSlot getById(Long id) {
        return null;
    }

    @Override
    public Set<TimeSlot> getAll() {
        return null;
    }
}
