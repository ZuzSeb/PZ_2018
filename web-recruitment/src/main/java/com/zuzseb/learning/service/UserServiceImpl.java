package com.zuzseb.learning.service;

import com.zuzseb.learning.model.Login;
import com.zuzseb.learning.model.User;
import com.zuzseb.learning.repository.UserRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.Optional;

@Repository
public class UserServiceImpl implements UserService {
    private static final Logger LOGGER = Logger.getLogger(UserServiceImpl.class);
    @PersistenceContext
    private EntityManager em;

    @Autowired
    private UserRepository userRepository;

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public boolean isEmailTaken(String email) {
        Query query = em.createQuery("select u from User u where u.email = :email");
        query.setParameter("email", email);
        return !query.getResultList().isEmpty();
    }

    @Override
    public User findByLogin(String login) {
        TypedQuery<User> query = em.createQuery("select u from User u where u.login = :login", User.class);
        query.setParameter("login", login);
        return query.getSingleResult();
    }

    @Override
    public boolean authenticate(Login login) {
        Optional<User> foundUser = getUserByLogin(login.getLogin());
        return foundUser.map(user -> user.getPassword().equals(login.getPwd())).orElse(false);
    }

    @Override
    public Optional<User> getUserByLogin(String login) {
        try {
            return Optional.of(em.createNamedQuery("getUserByLogin", User.class).setParameter("login", login).getSingleResult());
        } catch (Exception e) {
            LOGGER.warn(getClass().getSimpleName() + "#getUserByLogin() method failed.", e);
        }
        return Optional.empty();
    }
}
