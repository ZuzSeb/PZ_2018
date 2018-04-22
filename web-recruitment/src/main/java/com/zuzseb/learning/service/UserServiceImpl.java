package com.zuzseb.learning.service;

import com.zuzseb.learning.model.User;
import com.zuzseb.learning.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

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
}
