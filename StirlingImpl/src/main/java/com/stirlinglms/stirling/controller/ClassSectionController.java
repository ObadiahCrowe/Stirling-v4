package com.stirlinglms.stirling.controller;

import com.stirlinglms.stirling.service.ClassSectionService;
import com.stirlinglms.stirling.service.ClassroomService;
import com.stirlinglms.stirling.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ClassSectionController {

    private final ClassSectionService sectionService;
    private final ClassroomService classroomService;
    private final UserService userService;

    @Autowired
    ClassSectionController(ClassSectionService sectionService, ClassroomService classroomService, UserService userService) {
        this.sectionService = sectionService;
        this.classroomService = classroomService;
        this.userService = userService;
    }
}
