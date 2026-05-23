package com.upgrad.quora.api.controller;

import java.util.ArrayList;
import java.util.List;

import com.upgrad.quora.api.model.QuestionDeleteResponse;
import com.upgrad.quora.api.model.QuestionDetailsResponse;
import com.upgrad.quora.api.model.QuestionEditRequest;
import com.upgrad.quora.api.model.QuestionEditResponse;
import com.upgrad.quora.api.model.QuestionRequest;
import com.upgrad.quora.api.model.QuestionResponse;

import com.upgrad.quora.service.business.QuestionService;

import com.upgrad.quora.service.entity.QuestionEntity;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.PathVariable;
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

    @RequestMapping(
            method = RequestMethod.GET,
            path = "/question/all",
            produces = "application/json"
    )

    public ResponseEntity<List<QuestionDetailsResponse>>
    getAllQuestions(

            @RequestHeader("authorization")
                    final String authorization
    ) {

        List<QuestionEntity> questionEntities =
                questionService.getAllQuestions(authorization);

        if (questionEntities == null) {

            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        List<QuestionDetailsResponse> questionDetailsResponses =
                new ArrayList<>();

        for (QuestionEntity questionEntity : questionEntities) {

            QuestionDetailsResponse questionDetailsResponse =
                    new QuestionDetailsResponse()
                            .id(questionEntity.getUuid())
                            .content(questionEntity.getContent());

            questionDetailsResponses.add(questionDetailsResponse);
        }

        return new ResponseEntity<>(
                questionDetailsResponses,
                HttpStatus.OK
        );
    }

    @RequestMapping(
            method = RequestMethod.PUT,
            path = "/question/edit/{questionId}",
            consumes = "application/json",
            produces = "application/json"
    )

    public ResponseEntity<QuestionEditResponse> editQuestion(

            @PathVariable("questionId")
                    final String questionId,

            @RequestBody final QuestionEditRequest questionEditRequest,

            @RequestHeader("authorization")
                    final String authorization
    ) {

        QuestionEntity updatedQuestion =
                questionService.editQuestion(
                        questionId,
                        questionEditRequest.getContent(),
                        authorization
                );

        if (updatedQuestion == null) {

            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        QuestionEditResponse questionEditResponse =
                new QuestionEditResponse()
                        .id(updatedQuestion.getUuid())
                        .status("QUESTION EDITED");

        return new ResponseEntity<>(
                questionEditResponse,
                HttpStatus.OK
        );
    }

    @RequestMapping(
            method = RequestMethod.DELETE,
            path = "/question/delete/{questionId}",
            produces = "application/json"
    )

    public ResponseEntity<QuestionDeleteResponse> deleteQuestion(

            @PathVariable("questionId")
                    final String questionId,

            @RequestHeader("authorization")
                    final String authorization
    ) {

        QuestionEntity deletedQuestion =
                questionService.deleteQuestion(
                        questionId,
                        authorization
                );

        if (deletedQuestion == null) {

            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        QuestionDeleteResponse questionDeleteResponse =
                new QuestionDeleteResponse()
                        .id(deletedQuestion.getUuid())
                        .status("QUESTION DELETED");

        return new ResponseEntity<>(
                questionDeleteResponse,
                HttpStatus.OK
        );
    }
}