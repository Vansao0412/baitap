package vn.edu.hcmute.dao;

import java.util.Optional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NoResultException;
import vn.edu.hcmute.config.JpaConfig;
import vn.edu.hcmute.entity.User;

public class UserDAO implements IUserDao {

    @Override
    public User insert(User user) {
        return save(user, false);
    }

    @Override
    public User update(User user) {
        return save(user, true);
    }

    @Override
    public void delete(int userId) {
        try (EntityManager entityManager = createEntityManager()) {
            EntityTransaction transaction = entityManager.getTransaction();
            try {
                transaction.begin();
                User user = entityManager.find(User.class, userId);
                if (user != null) entityManager.remove(user);
                transaction.commit();
            } catch (RuntimeException exception) {
                if (transaction.isActive()) transaction.rollback();
                throw exception;
            }
        }
    }

    private User save(User user, boolean merge) {
        try (EntityManager entityManager = createEntityManager()) {
            EntityTransaction transaction = entityManager.getTransaction();
            try {
                transaction.begin();
                User result;
                if (merge) result = entityManager.merge(user);
                else { entityManager.persist(user); result = user; }
                transaction.commit();
                return result;
            } catch (RuntimeException exception) {
                if (transaction.isActive()) transaction.rollback();
                throw exception;
            }
        }
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return findOne("SELECT u FROM User u WHERE LOWER(u.username) = LOWER(:value)", username);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return findOne("SELECT u FROM User u WHERE LOWER(u.email) = LOWER(:value)", email);
    }

    public Optional<User> authenticate(String username, String password) {
        return findByUsername(username)
                .filter(User::isActive)
                .filter(user -> password.equals(user.getPasswordHash()));
    }

    private Optional<User> findOne(String jpql, String value) {
        try (EntityManager entityManager = createEntityManager()) {
            return Optional.of(entityManager.createQuery(jpql, User.class)
                    .setParameter("value", value.trim())
                    .getSingleResult());
        } catch (NoResultException exception) {
            return Optional.empty();
        }
    }

    protected EntityManager createEntityManager() {
        return JpaConfig.getEntityManager();
    }
}
