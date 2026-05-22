package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.SigninResponse;
import com.upgrad.quora.api.model.SignoutResponse;
import com.upgrad.quora.api.model.SignupUserRequest;
import com.upgrad.quora.api.model.SignupUserResponse;

import com.upgrad.quora.service.business.UserService;
import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.entity.UserEntity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(
            method = RequestMethod.POST,
            path = "/user/signup",
            consumes = "application/json",
            produces = "application/json"
    )

    public ResponseEntity<SignupUserResponse> signup(
            @RequestBody final SignupUserRequest signupUserRequest
    ) {

        UserEntity userEntity = new UserEntity();

        userEntity.setFirstName(signupUserRequest.getFirstName());
        userEntity.setLastName(signupUserRequest.getLastName());
        userEntity.setUserName(signupUserRequest.getUserName());
        userEntity.setEmail(signupUserRequest.getEmailAddress());

        String salt = UUID.randomUUID().toString();

        userEntity.setSalt(salt);

        userEntity.setPassword(signupUserRequest.getPassword());

        userEntity.setCountry(signupUserRequest.getCountry());
        userEntity.setAboutMe(signupUserRequest.getAboutMe());
        userEntity.setDob(signupUserRequest.getDob());
        userEntity.setContactNumber(signupUserRequest.getContactNumber());

        userEntity.setRole("nonadmin");

        UserEntity createdUser = userService.signup(userEntity);

        if (createdUser == null) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        SignupUserResponse signupUserResponse =
                new SignupUserResponse()
                        .id(createdUser.getUuid())
                        .status("USER SUCCESSFULLY REGISTERED");

        return new ResponseEntity<>(signupUserResponse, HttpStatus.CREATED);
    }

    @RequestMapping(
            method = RequestMethod.POST,
            path = "/user/signin",
            produces = "application/json"
    )

    public ResponseEntity<SigninResponse> signin(
            @RequestHeader("authorization") final String authorization
    ) {

        String[] credentials = authorization.split(":");

        String username = credentials[0];

        String password = credentials[1];

        UserAuthEntity userAuthEntity = userService.signin(username, password);

        if (userAuthEntity == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        SigninResponse signinResponse = new SigninResponse()
                .id(userAuthEntity.getUser().getUuid())
                .message("SIGNED IN SUCCESSFULLY");

        return new ResponseEntity<>(signinResponse, HttpStatus.OK);
    }

    @RequestMapping(
            method = RequestMethod.POST,
            path = "/user/signout",
            produces = "application/json"
    )

    public ResponseEntity<SignoutResponse> signout(
            @RequestHeader("authorization") final String authorization
    ) {

        UserAuthEntity userAuthEntity =
                userService.signout(authorization);

        if (userAuthEntity == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        SignoutResponse signoutResponse = new SignoutResponse()
                .id(userAuthEntity.getUser().getUuid())
                .message("SIGNED OUT SUCCESSFULLY");

        return new ResponseEntity<>(signoutResponse, HttpStatus.OK);
    }
}