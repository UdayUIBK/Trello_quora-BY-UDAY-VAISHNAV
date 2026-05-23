package com.upgrad.quora.service.dao;

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
}