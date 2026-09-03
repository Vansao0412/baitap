package vn.edu.hcmute.dao;

import java.util.List;
import java.util.Optional;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import vn.edu.hcmute.config.JpaConfig;
import vn.edu.hcmute.entity.Product;

public class ProductDao implements IProductDao {
    @Override public Product insert(Product product) { return save(product, false); }
    @Override public Product update(Product product) { return save(product, true); }

    private Product save(Product product, boolean merge) {
        try (EntityManager em = createEntityManager()) {
            EntityTransaction tx = em.getTransaction();
            try {
                tx.begin();
                Product result;
                if (merge) result = em.merge(product); else { em.persist(product); result = product; }
                tx.commit(); return result;
            } catch (RuntimeException ex) { if (tx.isActive()) tx.rollback(); throw ex; }
        }
    }

    @Override public void delete(int id) {
        try (EntityManager em = createEntityManager()) {
            EntityTransaction tx = em.getTransaction();
            try { tx.begin(); Product product = em.find(Product.class, id);
                if (product == null) throw new IllegalArgumentException("Không tìm thấy sản phẩm");
                em.remove(product); tx.commit();
            } catch (RuntimeException ex) { if (tx.isActive()) tx.rollback(); throw ex; }
        }
    }

    @Override public Optional<Product> findById(int id) {
        try (EntityManager em = createEntityManager()) { return Optional.ofNullable(em.find(Product.class, id)); }
    }

    @Override public List<Product> findAll(int page, int pageSize) {
        try (EntityManager em = createEntityManager()) {
            return em.createQuery("SELECT p FROM Product p ORDER BY p.createdAt DESC, p.productId DESC", Product.class)
                    .setFirstResult(page * pageSize).setMaxResults(pageSize).getResultList();
        }
    }

    @Override public List<Product> findLatest(int limit) {
        try (EntityManager em = createEntityManager()) {
            return em.createQuery("SELECT p FROM Product p WHERE p.status = 1 ORDER BY p.createdAt DESC, p.productId DESC", Product.class)
                    .setMaxResults(limit).getResultList();
        }
    }

    @Override public int count() {
        try (EntityManager em = createEntityManager()) {
            return em.createQuery("SELECT COUNT(p) FROM Product p", Long.class).getSingleResult().intValue();
        }
    }

    protected EntityManager createEntityManager() { return JpaConfig.getEntityManager(); }
}
