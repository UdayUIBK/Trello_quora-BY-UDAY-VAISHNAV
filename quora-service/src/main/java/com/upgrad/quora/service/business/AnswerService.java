package com.upgrad.quora.service.business;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.upgrad.quora.service.dao.AnswerDao;
import com.upgrad.quora.service.dao.QuestionDao;
import com.upgrad.quora.service.dao.UserAuthDao;
import com.upgrad.quora.service.entity.AnswerEntity;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserAuthEntity;

@Service
public class AnswerService {

    @Autowired
    private AnswerDao answerDao;

    @Autowired
    private QuestionDao questionDao;

    @Autowired
    private UserAuthDao userAuthDao;

    public AnswerEntity createAnswer(
            final String questionId,
            final String answer,
            final String authorization
    ) {

        UserAuthEntity userAuthEntity =
                userAuthDao.getUserAuthToken(authorization);

        if (userAuthEntity == null) {
            return null;
        }

        QuestionEntity questionEntity =
                questionDao.getQuestionByUuid(questionId);

        if (questionEntity == null) {
            return null;
        }

        AnswerEntity answerEntity = new AnswerEntity();

        answerEntity.setUuid(UUID.randomUUID().toString());

        answerEntity.setAns(answer);

        answerEntity.setDate(ZonedDateTime.now());

        answerEntity.setUser(userAuthEntity.getUser());

        answerEntity.setQuestion(questionEntity);

        return answerDao.createAnswer(answerEntity);
    }

    public List<AnswerEntity> getAllAnswers(
            final String questionId,
            final String authorization
    ) {

        UserAuthEntity userAuthEntity =
                userAuthDao.getUserAuthToken(authorization);

        if (userAuthEntity == null) {
            return null;
        }

        return answerDao.getAllAnswers(questionId);
    }

    public AnswerEntity editAnswer(
            final String answerId,
            final String answer,
            final String authorization
    ) {

        UserAuthEntity userAuthEntity =
                userAuthDao.getUserAuthToken(authorization);

        if (userAuthEntity == null) {
            return null;
        }

        AnswerEntity answerEntity =
                answerDao.getAnswerByUuid(answerId);

        if (answerEntity == null) {
            return null;
        }

        if (
                !answerEntity.getUser().getUuid()
                        .equals(userAuthEntity.getUser().getUuid())
                        &&
                        !userAuthEntity.getUser().getRole()
                                .equals("admin")
        ) {

            return null;
        }

        answerEntity.setAns(answer);

        return answerDao.updateAnswer(answerEntity);
    }

    public AnswerEntity deleteAnswer(
            final String answerId,
            final String authorization
    ) {

        UserAuthEntity userAuthEntity =
                userAuthDao.getUserAuthToken(authorization);

        if (userAuthEntity == null) {
            return null;
        }

        AnswerEntity answerEntity =
                answerDao.getAnswerByUuid(answerId);

        if (answerEntity == null) {
            return null;
        }

        if (
                !answerEntity.getUser().getUuid()
                        .equals(userAuthEntity.getUser().getUuid())
                        &&
                        !userAuthEntity.getUser().getRole()
                                .equals("admin")
        ) {

            return null;
        }

        answerDao.deleteAnswer(answerEntity);

        return answerEntity;
    }
}