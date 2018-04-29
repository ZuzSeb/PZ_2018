package com.zuzseb.learning.service;

import com.zuzseb.learning.model.Login;
import com.zuzseb.learning.model.User;

import java.util.Optional;

public interface UserService {
    User save(User user);
    boolean isEmailTaken(String email);
    User findByLogin(String login);
    boolean authenticate(Login login);
    Optional<User> getUserByLogin(String login);
}
