package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.QuestionRequest;
import com.upgrad.quora.api.model.QuestionResponse;

import com.upgrad.quora.service.business.QuestionService;

import com.upgrad.quora.service.entity.QuestionEntity;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @RequestMapping(
            method = RequestMethod.POST,
            path = "/question/create",
            consumes = "application/json",
            produces = "application/json"
    )

    public ResponseEntity<QuestionResponse> createQuestion(

            @RequestBody final QuestionRequest questionRequest,

            @RequestHeader("authorization")
                    final String authorization
    ) {

        QuestionEntity questionEntity = new QuestionEntity();

        questionEntity.setContent(questionRequest.getContent());

        QuestionEntity createdQuestion =
                questionService.createQuestion(
                        questionEntity,
                        authorization
                );

        if (createdQuestion == null) {

            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        QuestionResponse questionResponse =
                new QuestionResponse()
                        .id(createdQuestion.getUuid())
                        .status("QUESTION CREATED");

        return new ResponseEntity<>(
                questionResponse,
                HttpStatus.CREATED
        );
    }
}