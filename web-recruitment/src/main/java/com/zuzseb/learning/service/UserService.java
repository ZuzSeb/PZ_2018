package com.zuzseb.learning.service;

import com.zuzseb.learning.exception.ComparisonPasswordException;
import com.zuzseb.learning.exception.UserNotFoundException;
import com.zuzseb.learning.exception.WrongActualPasswordException;
import com.zuzseb.learning.model.Login;
import com.zuzseb.learning.model.Post;
import com.zuzseb.learning.model.PwdChange;
import com.zuzseb.learning.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    User save(User user);
    Optional<User> update(User user);
    Optional<User> merge(User user);
    boolean isEmailTaken(String email);
    User findByLogin(String login);
    boolean authenticate(Login login);
    Optional<User> getUserByLogin(String login);
    Optional<User> getUserByEmail(String login);
    void changePwd(String login, PwdChange pwdChange) throws WrongActualPasswordException, ComparisonPasswordException, UserNotFoundException;
    void deletePostFromUser(Post post);
    List<User> findByPost(Post post);
    void addUserPost(String login, Long postId);
    void deleteUserPost(String login, Long postId);
}
