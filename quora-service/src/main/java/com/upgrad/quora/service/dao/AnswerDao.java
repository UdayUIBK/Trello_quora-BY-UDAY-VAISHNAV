package com.upgrad.quora.service.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.upgrad.quora.service.entity.AnswerEntity;

@Repository
@Transactional
public class AnswerDao {

    @PersistenceContext
    private EntityManager entityManager;

    public AnswerEntity createAnswer(
            AnswerEntity answerEntity
    ) {

        entityManager.persist(answerEntity);

        return answerEntity;
    }

    public List<AnswerEntity> getAllAnswers(
            final String questionId
    ) {

        return entityManager.createQuery(
                "select a from AnswerEntity a where a.question.uuid = :uuid order by a.date desc",
                AnswerEntity.class
        )
                .setParameter("uuid", questionId)
                .getResultList();
    }

    public AnswerEntity getAnswerByUuid(
            final String answerId
    ) {

        try {

            return entityManager.createQuery(
                    "select a from AnswerEntity a where a.uuid = :uuid",
                    AnswerEntity.class
            )
                    .setParameter("uuid", answerId)
                    .getSingleResult();

        } catch (Exception e) {

            return null;
        }
    }

    public AnswerEntity updateAnswer(
            AnswerEntity answerEntity
    ) {

        entityManager.merge(answerEntity);

        return answerEntity;
    }

    public void deleteAnswer(
            AnswerEntity answerEntity
    ) {

        entityManager.remove(answerEntity);
    }
}