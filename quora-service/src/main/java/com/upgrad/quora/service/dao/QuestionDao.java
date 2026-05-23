package com.upgrad.quora.service.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.upgrad.quora.service.entity.QuestionEntity;

@Repository
@Transactional
public class QuestionDao {

    @PersistenceContext
    private EntityManager entityManager;

    public QuestionEntity createQuestion(
            QuestionEntity questionEntity
    ) {

        entityManager.persist(questionEntity);

        return questionEntity;
    }

    public List<QuestionEntity> getAllQuestions() {

        return entityManager.createQuery(
                "select q from QuestionEntity q order by q.date desc",
                QuestionEntity.class
        ).getResultList();
    }

    public QuestionEntity getQuestionByUuid(
            final String questionUuid
    ) {

        try {

            return entityManager.createQuery(
                    "select q from QuestionEntity q where q.uuid = :uuid",
                    QuestionEntity.class
            )
                    .setParameter("uuid", questionUuid)
                    .getSingleResult();

        } catch (Exception e) {

            return null;
        }
    }

    public QuestionEntity updateQuestion(
            QuestionEntity questionEntity
    ) {

        entityManager.merge(questionEntity);

        return questionEntity;
    }

    public void deleteQuestion(
            QuestionEntity questionEntity
    ) {

        entityManager.remove(questionEntity);
    }
}