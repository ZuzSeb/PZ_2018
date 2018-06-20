package com.zuzseb.learning.service;

import com.zuzseb.learning.exception.ComparisonPasswordException;
import com.zuzseb.learning.exception.UserNotFoundException;
import com.zuzseb.learning.exception.WrongActualPasswordException;
import com.zuzseb.learning.model.Login;
import com.zuzseb.learning.model.Post;
import com.zuzseb.learning.model.PwdChange;
import com.zuzseb.learning.model.User;
import com.zuzseb.learning.repository.PostRepository;
import com.zuzseb.learning.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.Iterator;
import java.util.Objects;
import java.util.Optional;

@Repository
public class UserServiceImpl implements UserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);
    @PersistenceContext
    private EntityManager em;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PostRepository postRepository;

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public Optional<User> update(User user) {
        try {
            Optional<User> oldUser = getUserByLogin(user.getLogin());
            if (oldUser.isPresent()) {
                Optional<User> foundUser = getUserByEmail(user.getEmail());
                if (foundUser.isPresent()) {
                    if (foundUser.get().getLogin().equals(oldUser.get().getLogin())) {
                        oldUser.get().setFirstName(user.getFirstName());
                        oldUser.get().setLastName(user.getLastName());
                        oldUser.get().setEmail(user.getEmail());
                        oldUser.get().setDescription(user.getDescription());
                        return Optional.of(em.merge(oldUser.get()));
                    }
                } else {
                    return Optional.of(em.merge(user));
                }
            } else {
                LOGGER.warn("Error: trying to update user which does not exist.");
            }
        } catch (Exception e) {
            LOGGER.warn(getClass().getSimpleName() + "#update()", e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> merge(User user) {
        try {
            return Optional.of(em.merge(user));
        } catch (Exception e) {
            LOGGER.warn(getClass().getSimpleName() + "#merge() method failed.", e);
        }
        return Optional.empty();
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

    @Override
    public Optional<User> getUserByEmail(String email) {
        try {
            return Optional.of(em.createNamedQuery("getUserByEmail", User.class).setParameter("email", email).getSingleResult());
        } catch (Exception e) {
            LOGGER.warn(getClass().getSimpleName() + "#getUserByEmail() method failed.", e);
        }
        return Optional.empty();
    }

    @Override
    @Transactional
    public void changePwd(String login, PwdChange pwdChange) throws WrongActualPasswordException, ComparisonPasswordException, UserNotFoundException {
        Optional<User> foundUser = getUserByLogin(login);
        if (foundUser.isPresent()) {
            User user = foundUser.get();
            if (!pwdChange.getNewPwd().equals(pwdChange.getNewPwd2())) {
                throw new ComparisonPasswordException("New password field do not match retype new password field.");
            }
            if (!user.getPassword().equals(pwdChange.getOldPwd())) {
                throw new WrongActualPasswordException(String.format("Wrong password for user: %s.", login));
            }
            user.setPassword(pwdChange.getNewPwd());
            em.merge(user);
        } else {
            throw new UserNotFoundException(String.format("User [%s] not found.", login));
        }
    }

    @Override
    @Transactional
    public void addUserPost(String login, Long postId) {
        User foundUser = findByLogin(login);
        Post postToApply = postRepository.findOne(postId);
        foundUser.getPosts().add(postToApply);
        em.merge(foundUser);
    }

    @Override
    @Transactional
    public void deleteUserPost(String login, Long postId) {
        User foundUser = findByLogin(login);
        Iterator<Post> iterator = foundUser.getPosts().iterator();
        while (iterator.hasNext()) {
            Post next = iterator.next();
            if (Objects.equals(postId, next.getId())) {
                iterator.remove();
                break;
            }
        }
        em.merge(foundUser);
    }

}
