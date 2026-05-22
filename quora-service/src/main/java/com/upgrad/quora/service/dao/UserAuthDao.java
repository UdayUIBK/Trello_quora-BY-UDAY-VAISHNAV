package com.upgrad.quora.service.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.upgrad.quora.service.entity.UserAuthEntity;

@Repository
@Transactional
public class UserAuthDao {

    @PersistenceContext
    private EntityManager entityManager;

    public UserAuthEntity createUserAuth(UserAuthEntity userAuthEntity) {

        entityManager.persist(userAuthEntity);

        return userAuthEntity;
    }

    public UserAuthEntity getUserAuthToken(final String accessToken) {

        try {

            return entityManager.createQuery(
                    "select ua from UserAuthEntity ua where ua.accessToken = :token",
                    UserAuthEntity.class
            )
                    .setParameter("token", accessToken)
                    .getSingleResult();

        } catch (Exception e) {

            return null;
        }
    }

    public UserAuthEntity updateUserAuth(UserAuthEntity userAuthEntity) {

        entityManager.merge(userAuthEntity);

        return userAuthEntity;
    }
}