package com.upgrad.quora.service.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.upgrad.quora.service.entity.UserEntity;

@Repository
@Transactional
public class UserDao {

    @PersistenceContext
    private EntityManager entityManager;

    public UserEntity createUser(UserEntity userEntity) {

        entityManager.persist(userEntity);

        return userEntity;
    }

    public UserEntity getUserByEmail(final String email) {

        try {

            return entityManager
                    .createQuery(
                            "select u from UserEntity u where u.email = :email",
                            UserEntity.class
                    )
                    .setParameter("email", email)
                    .getSingleResult();

        } catch (Exception e) {

            return null;
        }
    }

    public UserEntity getUserByUserName(final String userName) {

        try {

            return entityManager
                    .createQuery(
                            "select u from UserEntity u where u.userName = :userName",
                            UserEntity.class
                    )
                    .setParameter("userName", userName)
                    .getSingleResult();

        } catch (Exception e) {

            return null;
        }
    }
}