package com.stirlinglms.stirling.controller;

import com.stirlinglms.stirling.dto.SchoolDto;
import com.stirlinglms.stirling.entity.school.School;
import com.stirlinglms.stirling.service.SchoolService;
import com.stirlinglms.stirling.util.response.Response;
import com.stirlinglms.stirling.util.response.ResponseCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.stream.Collectors;

@RestController
public class SchoolController {

    private final SchoolService schoolService;

    @Autowired
    SchoolController(SchoolService schoolService) {
        this.schoolService = schoolService;
    }

    @PostMapping(value = "/v1/school")
    @PreAuthorize(value = "hasAuthority('DEV')")
    public Response<SchoolDto> postSchool(@RequestParam("name") String name,
                                          @RequestParam("location") String location,
                                          @RequestParam(value = "phoneNumber", required = false) String phoneNumber,
                                          @RequestParam(value = "emailAddress", required = false) String emailAddress) {
        try {
            if (phoneNumber == null || emailAddress == null) {
                return new Response<>(ResponseCode.SUCCESS, "School created.", this.schoolService.createSchool(name, location).getDto());
            }

            return new Response<>(ResponseCode.SUCCESS, "School created.", this.schoolService.createSchool(name, location, phoneNumber, emailAddress).getDto());
        } catch (Exception e) {
            return new Response<>(ResponseCode.ERROR, e.getMessage(), null);
        }
    }

    @GetMapping(value = "/v1/school/{id}")
    public Response<SchoolDto> getSchool(@PathVariable("id") long id) {
        try {
            School school = this.schoolService.getById(id);

            if (school == null) {
                return new Response<>(ResponseCode.ERROR, "School not found.", null);
            }

            return new Response<>(ResponseCode.SUCCESS, "School found.", school.getDto());
        } catch (Exception e) {
            return new Response<>(ResponseCode.ERROR, e.getMessage(), null);
        }
    }

    @GetMapping(value = "/v1/school/all")
    public Response<Set<SchoolDto>> getSchoolAll() {
        try {
            return new Response<>(ResponseCode.SUCCESS, "Schools found.",
              this.schoolService.getAll().stream().map(School::getDto).collect(Collectors.toSet()));
        } catch (Exception e) {
            return new Response<>(ResponseCode.ERROR, e.getMessage(), null);
        }
    }

    @DeleteMapping(value = "/v1/school/{id}")
    @PreAuthorize(value = "hasAuthority('DEV')")
    public Response<SchoolDto> deleteSchool(@PathVariable("id") long id) {
        try {
            School school = this.schoolService.getById(id);

            if (school == null) {
                return new Response<>(ResponseCode.ERROR, "School not found.", null);
            }

            return new Response<>(ResponseCode.SUCCESS, "School deleted.", this.schoolService.delete(school).getDto());
        } catch (Exception e) {
            return new Response<>(ResponseCode.ERROR, e.getMessage(), null);
        }
    }

    @PatchMapping(value = "/v1/school/{id}")
    @PreAuthorize(value = "hasAuthority('DEV')")
    public Response<SchoolDto> patchSchool(
      @PathVariable("id") long id,
      @RequestParam("field") String field,
      @RequestParam("value") Object value) {
        try {
            School school = this.schoolService.getById(id);

            if (school == null) {
                return new Response<>(ResponseCode.ERROR, "School not found.", null);
            }

            // this.schoolService.update(school, field, value, UpdateLevel.ADMIN).getDto()
            return new Response<>(ResponseCode.SUCCESS, "School updated.", null);
        } catch (Exception e) {
            return new Response<>(ResponseCode.ERROR, e.getMessage(), null);
        }
    }
}
