package com.stirlinglms.stirling.controller;

import com.stirlinglms.stirling.dto.EmailDataDto;
import com.stirlinglms.stirling.dto.UserDto;
import com.stirlinglms.stirling.entity.SpringEntity;
import com.stirlinglms.stirling.entity.email.EmailData;
import com.stirlinglms.stirling.entity.user.User;
import com.stirlinglms.stirling.exception.EmailExistsException;
import com.stirlinglms.stirling.exception.InvalidInputException;
import com.stirlinglms.stirling.exception.UserExistsException;
import com.stirlinglms.stirling.service.ResourceService;
import com.stirlinglms.stirling.service.UserService;
import com.stirlinglms.stirling.util.UpdateLevel;
import com.stirlinglms.stirling.util.response.Response;
import com.stirlinglms.stirling.util.response.ResponseCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    private final UserService userService;
    private final ResourceService resourceService;

    @Autowired
    UserController(UserService userService, ResourceService resourceService) {
        this.userService = userService;
        this.resourceService = resourceService;
    }

    @PostMapping(value = "/v1/user")
    public Response<UserDto> postUser(@RequestParam("username") String username,
                                      @RequestParam("emailAddress") String emailAddress,
                                      @RequestParam("password") String password) {
        try {
            return new Response<>(ResponseCode.SUCCESS, "User created.",
              this.userService.createUser(username, emailAddress, password).getDto());
        } catch (UserExistsException | EmailExistsException | InvalidInputException e) {
            return new Response<>(ResponseCode.ERROR, e.getMessage(), null);
        }
    }

    @GetMapping(value = "/v1/user")
    public Response<UserDto> getUser() {
        try {
            SpringEntity entity = (SpringEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            return new Response<>(ResponseCode.SUCCESS, "User found.", this.userService.getByUsername(entity.getUsername()).getDto());
        } catch (NullPointerException e) {
            return new Response<>(ResponseCode.ERROR, "User not found.", null);
        }
    }

    @DeleteMapping(value = "/v1/user")
    public Response<UserDto> deleteUser() {
        try {
            SpringEntity entity = (SpringEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            return new Response<>(ResponseCode.SUCCESS, "User deleted.",
              this.userService.delete(this.userService.getByUsername(entity.getUsername())).getDto());
        } catch (NullPointerException e) {
            return new Response<>(ResponseCode.ERROR, "User not found.", null);
        }
    }

    @PatchMapping(value = "/v1/user")
    public Response<UserDto> patchUser(@RequestParam("field") String field,
                                       @RequestParam("value") Object value) {
        try {
            SpringEntity entity = (SpringEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            User user = this.userService.getByUsername(entity.getUsername());

            if (user == null) {
                throw new NullPointerException("User not found.");
            }

            //this.userService.update(user, field, value, UpdateLevel.USER).getDto()
            return new Response<>(ResponseCode.SUCCESS, "User updated.", null);
        } catch (Exception e) {
            return new Response<>(ResponseCode.ERROR, e.getMessage(), null);
        }
    }

    @PostMapping(value = "/v1/email/verify")
    public Response<EmailDataDto> postEmailVerify(@RequestParam("username") String username, @RequestParam("hash") String hash) {
        try {
            return new Response<>(ResponseCode.SUCCESS, "Verified email.", this.userService.verifyEmailAddress(username, hash).getDto());
        } catch (Exception e) {
            return new Response<>(ResponseCode.ERROR, e.getMessage(), null);
        }
    }

    @GetMapping(value = "/v1/email/verify")
    public Response<EmailDataDto> getEmailVerify(@RequestParam("emailAddress") String address) {
        try {
            SpringEntity entity = (SpringEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            User user = this.userService.getByUsername(entity.getUsername());

            EmailData data = user.getEmailAddresses().stream().filter(d -> d.getAddress().equalsIgnoreCase(address)).findFirst().orElse(null);

            if (data == null) {
                data = new EmailData(address);
            }

            user.getEmailAddresses().add(data);

            this.userService.sendVerifyAddressEmail(user, data);
            this.userService.update(user, UpdateLevel.SYSTEM);

            return new Response<>(ResponseCode.SUCCESS, "Sent verification email.", data.getDto());
        } catch (Exception e) {
            return new Response<>(ResponseCode.ERROR, e.getMessage(), null);
        }
    }
}
