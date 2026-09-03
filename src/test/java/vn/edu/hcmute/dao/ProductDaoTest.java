package vn.edu.hcmute.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import vn.edu.hcmute.entity.Category;
import vn.edu.hcmute.entity.Product;

public class ProductDaoTest {
    private static final EntityManagerFactory FACTORY = createFactory();
    private ProductDao productDao;
    private Category category;

    @Before
    public void resetDatabase() {
        try (EntityManager entityManager = FACTORY.createEntityManager()) {
            entityManager.getTransaction().begin();
            entityManager.createQuery("DELETE FROM Product").executeUpdate();
            entityManager.createQuery("DELETE FROM Category").executeUpdate();
            category = new Category("Laptop", null, 1);
            entityManager.persist(category);
            entityManager.getTransaction().commit();
        }
        productDao = new ProductDao() {
            @Override protected EntityManager createEntityManager() { return FACTORY.createEntityManager(); }
        };
    }

    @Test
    public void crudPaginationAndCategoryRelationWork() {
        Product product = sampleProduct("Máy tính", 1);
        productDao.insert(product);

        assertEquals(1, productDao.count());
        assertEquals(1, productDao.findLatest(10).size());
        assertEquals(category.getCategoryId(), productDao.findById(product.getProductId())
                .orElseThrow().getCategory().getCategoryId());

        product.setProductName("Máy tính mới");
        productDao.update(product);
        assertEquals("Máy tính mới", productDao.findAll(0, 6).get(0).getProductName());

        productDao.delete(product.getProductId());
        assertTrue(productDao.findById(product.getProductId()).isEmpty());
    }

    private Product sampleProduct(String name, int status) {
        Product product = new Product();
        product.setProductName(name);
        product.setDescription("Sản phẩm kiểm thử");
        product.setPrice(new BigDecimal("1000000"));
        product.setStock(5);
        product.setStatus(status);
        product.setCategory(category);
        return product;
    }

    @AfterClass
    public static void closeFactory() { FACTORY.close(); }

    private static EntityManagerFactory createFactory() {
        Map<String, Object> properties = new HashMap<>();
        properties.put("jakarta.persistence.jdbc.driver", "org.h2.Driver");
        properties.put("jakarta.persistence.jdbc.url", "jdbc:h2:mem:producttest;DB_CLOSE_DELAY=-1;MODE=MSSQLServer");
        properties.put("jakarta.persistence.jdbc.user", "sa");
        properties.put("jakarta.persistence.jdbc.password", "");
        properties.put("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
        properties.put("hibernate.hbm2ddl.auto", "create-drop");
        properties.put("hibernate.show_sql", "false");
        return Persistence.createEntityManagerFactory("category-crud-pu", properties);
    }
}
