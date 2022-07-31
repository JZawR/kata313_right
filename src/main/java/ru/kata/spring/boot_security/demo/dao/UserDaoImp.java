package ru.kata.spring.boot_security.demo.dao;

import org.springframework.stereotype.Repository;
import ru.kata.spring.boot_security.demo.model.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class UserDaoImp implements UserDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void addUser(User user) {
        entityManager
                .persist(user);
    }

    @Override
    public void update(User user) {
        entityManager
                .merge(user);
    }

    @Override
    public List<User> listUsers() {
        return entityManager
                .createQuery("FROM User", User.class)
                .getResultList();
    }

    @Override
    public void deleteUser(long userId) {
        entityManager
                .remove(getUserById(userId));
    }

    @Override
    public User getUserById(long id) {
        return entityManager
                .createQuery("FROM User WHERE id =:id", User.class)
                .setParameter("id", id)
                .getSingleResult();
    }

    @Override
    public User getUserByLogin(String login) {
        return  entityManager
                .createQuery("FROM User u JOIN FETCH u.roles roles where u.login = :login", User.class)
                .setParameter("login", login)
                .getSingleResult();
    }
}
