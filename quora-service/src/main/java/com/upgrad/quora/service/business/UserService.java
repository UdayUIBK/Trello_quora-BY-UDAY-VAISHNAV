package com.upgrad.quora.service.business;

import java.time.ZonedDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.upgrad.quora.service.dao.UserAuthDao;
import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.entity.UserEntity;

@Service
public class UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private UserAuthDao userAuthDao;

    public UserEntity signup(UserEntity userEntity) {

        UserEntity existingUser = userDao.getUserByEmail(userEntity.getEmail());

        if (existingUser != null) {
            return null;
        }

        userEntity.setUuid(UUID.randomUUID().toString());

        return userDao.createUser(userEntity);
    }

    public UserAuthEntity signin(final String username, final String password) {

        UserEntity userEntity = userDao.getUserByUserName(username);

        if (userEntity == null) {
            return null;
        }

        if (!userEntity.getPassword().equals(password)) {
            return null;
        }

        UserAuthEntity userAuthEntity = new UserAuthEntity();

        userAuthEntity.setUuid(UUID.randomUUID().toString());

        userAuthEntity.setUser(userEntity);

        userAuthEntity.setAccessToken(UUID.randomUUID().toString());

        ZonedDateTime now = ZonedDateTime.now();

        userAuthEntity.setLoginAt(now);

        userAuthEntity.setExpiresAt(now.plusHours(8));

        return userAuthDao.createUserAuth(userAuthEntity);
    }
}