package com.upgrad.quora.service.business;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.UserEntity;

@Service
public class UserService {

    @Autowired
    private UserDao userDao;

    public UserEntity signup(UserEntity userEntity) {

        UserEntity existingUser = userDao.getUserByEmail(userEntity.getEmail());

        if (existingUser != null) {
            return null;
        }

        userEntity.setUuid(UUID.randomUUID().toString());

        return userDao.createUser(userEntity);
    }
}