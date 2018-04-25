package com.zuzseb.learning.service;

import com.zuzseb.learning.model.User;
import com.zuzseb.learning.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class UserServiceImpl implements UserService {

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
        TypedQuery<User> query = em.createQuery("select u from User u where u.email = :email", User.class);
        query.setParameter("email", login);
        return query.getSingleResult();
    }
}
