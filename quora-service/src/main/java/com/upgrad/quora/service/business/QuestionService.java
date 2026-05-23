package com.upgrad.quora.service.business;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.upgrad.quora.service.dao.QuestionDao;
import com.upgrad.quora.service.dao.UserAuthDao;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserAuthEntity;

@Service
public class QuestionService {

    @Autowired
    private QuestionDao questionDao;

    @Autowired
    private UserAuthDao userAuthDao;

    public QuestionEntity createQuestion(
            final QuestionEntity questionEntity,
            final String authorization
    ) {

        UserAuthEntity userAuthEntity =
                userAuthDao.getUserAuthToken(authorization);

        if (userAuthEntity == null) {
            return null;
        }

        questionEntity.setUuid(UUID.randomUUID().toString());

        questionEntity.setDate(ZonedDateTime.now());

        questionEntity.setUser(userAuthEntity.getUser());

        return questionDao.createQuestion(questionEntity);
    }

    public List<QuestionEntity> getAllQuestions(
            final String authorization
    ) {

        UserAuthEntity userAuthEntity =
                userAuthDao.getUserAuthToken(authorization);

        if (userAuthEntity == null) {
            return null;
        }

        return questionDao.getAllQuestions();
    }

    public QuestionEntity editQuestion(
            final String questionId,
            final String content,
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

        questionEntity.setContent(content);

        return questionDao.updateQuestion(questionEntity);
    }

    public QuestionEntity deleteQuestion(
            final String questionId,
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

        questionDao.deleteQuestion(questionEntity);

        return questionEntity;
    }
}