package org.springframework.boot.cloudstorage.services;

import org.springframework.boot.cloudstorage.mapper.UserMapper;
import org.springframework.boot.cloudstorage.model.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;

@Service
public class UserService {
    private UserMapper userMapper;
    private HashService hashService;

    public UserService(UserMapper userMapper, HashService hashService) {
        this.userMapper = userMapper;
        this.hashService = hashService;
    }

    public boolean isUserAvailable(String userName) {
        return userMapper.getUserByName(userName) == null;
    }

    public int createUser(User user) {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        String encodedSalt = Base64.getEncoder().encodeToString(salt);
        String hashedPassword = hashService.getHashedValue(user.getPassword(), encodedSalt);
        return userMapper.insert(new User(null, user.getUserName(), encodedSalt, hashedPassword, user.getFirstName(), user.getLastName()));
    }

    public User getUser(String userName) {
        return userMapper.getUserByName(userName);
    }

    public Integer getCurrentUserId() {
        String userName = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();

        return getUser(userName).getUserId();
    }
}
