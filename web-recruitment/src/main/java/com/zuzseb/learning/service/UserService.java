package com.zuzseb.learning.service;

import com.zuzseb.learning.model.User;

public interface UserService {
    User save(User user);
    boolean isEmailTaken(String email);
}
