package vn.edu.hcmute.dao;

import java.util.List;
import java.util.Optional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NoResultException;
import vn.edu.hcmute.config.JpaConfig;
import vn.edu.hcmute.entity.Category;

public class CategoryDao implements ICategoryDao {
    @Override
    public Category insert(Category category) {
        return executeInTransaction(entityManager -> {
            entityManager.persist(category);
            return category;
        });
    }

    @Override
    public Category update(Category category) {
        return executeInTransaction(entityManager -> entityManager.merge(category));
    }

    @Override
    public void delete(int categoryId) {
        executeInTransaction(entityManager -> {
            Category category = entityManager.find(Category.class, categoryId);
            if (category == null) {
                throw new IllegalArgumentException("Không tìm thấy danh mục có mã " + categoryId);
            }
            entityManager.remove(category);
            return null;
        });
    }

    @Override
    public Optional<Category> findById(int categoryId) {
        try (EntityManager entityManager = createEntityManager()) {
            return Optional.ofNullable(entityManager.find(Category.class, categoryId));
        }
    }

    @Override
    public Optional<Category> findByCategoryName(String categoryName) {
        try (EntityManager entityManager = createEntityManager()) {
            return Optional.of(entityManager.createQuery(
                    "SELECT c FROM Category c WHERE LOWER(c.categoryName) = LOWER(:name)", Category.class)
                    .setParameter("name", categoryName.trim())
                    .getSingleResult());
        } catch (NoResultException exception) {
            return Optional.empty();
        }
    }

    @Override
    public List<Category> findAll() {
        try (EntityManager entityManager = createEntityManager()) {
            return entityManager.createNamedQuery("Category.findAll", Category.class).getResultList();
        }
    }

    @Override
    public List<Category> searchByName(String keyword) {
        try (EntityManager entityManager = createEntityManager()) {
            return entityManager.createQuery(
                    "SELECT c FROM Category c WHERE LOWER(c.categoryName) LIKE LOWER(:keyword) ORDER BY c.categoryId",
                    Category.class)
                    .setParameter("keyword", "%" + keyword.trim() + "%")
                    .getResultList();
        }
    }

    @Override
    public List<Category> findAll(int page, int pageSize) {
        if (page < 0 || pageSize <= 0) {
            throw new IllegalArgumentException("Trang và kích thước trang không hợp lệ");
        }
        try (EntityManager entityManager = createEntityManager()) {
            return entityManager.createNamedQuery("Category.findAll", Category.class)
                    .setFirstResult(page * pageSize)
                    .setMaxResults(pageSize)
                    .getResultList();
        }
    }

    @Override
    public int count() {
        try (EntityManager entityManager = createEntityManager()) {
            return entityManager.createQuery("SELECT COUNT(c) FROM Category c", Long.class)
                    .getSingleResult().intValue();
        }
    }

    private <T> T executeInTransaction(TransactionCallback<T> callback) {
        try (EntityManager entityManager = createEntityManager()) {
            EntityTransaction transaction = entityManager.getTransaction();
            try {
                transaction.begin();
                T result = callback.apply(entityManager);
                transaction.commit();
                return result;
            } catch (RuntimeException exception) {
                if (transaction.isActive()) {
                    transaction.rollback();
                }
                throw exception;
            }
        }
    }

    protected EntityManager createEntityManager() {
        return JpaConfig.getEntityManager();
    }

    @FunctionalInterface
    private interface TransactionCallback<T> {
        T apply(EntityManager entityManager);
    }
}
