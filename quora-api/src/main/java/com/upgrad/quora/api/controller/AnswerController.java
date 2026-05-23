package com.upgrad.quora.api.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.upgrad.quora.api.model.AnswerDeleteResponse;
import com.upgrad.quora.api.model.AnswerDetailsResponse;
import com.upgrad.quora.api.model.AnswerEditRequest;
import com.upgrad.quora.api.model.AnswerEditResponse;
import com.upgrad.quora.api.model.AnswerRequest;
import com.upgrad.quora.api.model.AnswerResponse;

import com.upgrad.quora.service.business.AnswerService;

import com.upgrad.quora.service.entity.AnswerEntity;

@RestController
@RequestMapping("/")
public class AnswerController {

    @Autowired
    private AnswerService answerService;

    @RequestMapping(
            method = RequestMethod.POST,
            path = "/answer/create/{questionId}",
            consumes = "application/json",
            produces = "application/json"
    )

    public ResponseEntity<AnswerResponse> createAnswer(

            @PathVariable("questionId")
                    final String questionId,

            @RequestBody final AnswerRequest answerRequest,

            @RequestHeader("authorization")
                    final String authorization
    ) {

        AnswerEntity createdAnswer =
                answerService.createAnswer(
                        questionId,
                        answerRequest.getAnswer(),
                        authorization
                );

        if (createdAnswer == null) {

            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        AnswerResponse answerResponse =
                new AnswerResponse()
                        .id(createdAnswer.getUuid())
                        .status("ANSWER CREATED");

        return new ResponseEntity<>(
                answerResponse,
                HttpStatus.CREATED
        );
    }

    @RequestMapping(
            method = RequestMethod.GET,
            path = "/answer/all/{questionId}",
            produces = "application/json"
    )

    public ResponseEntity<List<AnswerDetailsResponse>>
    getAllAnswers(

            @PathVariable("questionId")
                    final String questionId,

            @RequestHeader("authorization")
                    final String authorization
    ) {

        List<AnswerEntity> answerEntities =
                answerService.getAllAnswers(
                        questionId,
                        authorization
                );

        if (answerEntities == null) {

            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        List<AnswerDetailsResponse> answerDetailsResponses =
                new ArrayList<>();

        for (AnswerEntity answerEntity : answerEntities) {

            AnswerDetailsResponse answerDetailsResponse =
                    new AnswerDetailsResponse()
                            .id(answerEntity.getUuid())
                            .answerContent(answerEntity.getAns())
                            .questionContent(
                                    answerEntity.getQuestion().getContent()
                            );

            answerDetailsResponses.add(answerDetailsResponse);
        }

        return new ResponseEntity<>(
                answerDetailsResponses,
                HttpStatus.OK
        );
    }

    @RequestMapping(
            method = RequestMethod.PUT,
            path = "/answer/edit/{answerId}",
            consumes = "application/json",
            produces = "application/json"
    )

    public ResponseEntity<AnswerEditResponse> editAnswer(

            @PathVariable("answerId")
                    final String answerId,

            @RequestBody final AnswerEditRequest answerEditRequest,

            @RequestHeader("authorization")
                    final String authorization
    ) {

        AnswerEntity updatedAnswer =
                answerService.editAnswer(
                        answerId,
                        answerEditRequest.getContent(),
                        authorization
                );

        if (updatedAnswer == null) {

            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        AnswerEditResponse answerEditResponse =
                new AnswerEditResponse()
                        .id(updatedAnswer.getUuid())
                        .status("ANSWER EDITED");

        return new ResponseEntity<>(
                answerEditResponse,
                HttpStatus.OK
        );
    }

    @RequestMapping(
            method = RequestMethod.DELETE,
            path = "/answer/delete/{answerId}",
            produces = "application/json"
    )

    public ResponseEntity<AnswerDeleteResponse> deleteAnswer(

            @PathVariable("answerId")
                    final String answerId,

            @RequestHeader("authorization")
                    final String authorization
    ) {

        AnswerEntity deletedAnswer =
                answerService.deleteAnswer(
                        answerId,
                        authorization
                );

        if (deletedAnswer == null) {

            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        AnswerDeleteResponse answerDeleteResponse =
                new AnswerDeleteResponse()
                        .id(deletedAnswer.getUuid())
                        .status("ANSWER DELETED");

        return new ResponseEntity<>(
                answerDeleteResponse,
                HttpStatus.OK
        );
    }
}