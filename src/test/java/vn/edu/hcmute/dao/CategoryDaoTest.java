package vn.edu.hcmute.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import vn.edu.hcmute.entity.Category;

public class CategoryDaoTest {
    private static final EntityManagerFactory FACTORY = createFactory();
    private CategoryDao dao;

    @Before
    public void resetDatabase() {
        try (EntityManager entityManager = FACTORY.createEntityManager()) {
            entityManager.getTransaction().begin();
            entityManager.createQuery("DELETE FROM Category").executeUpdate();
            entityManager.getTransaction().commit();
        }
        dao = new CategoryDao() {
            @Override protected EntityManager createEntityManager() { return FACTORY.createEntityManager(); }
        };
    }

    @Test
    public void crudAndSearchWork() {
        Category mobile = dao.insert(new Category("Điện thoại", "phone.png", 1));
        Category book = dao.insert(new Category("Sách", null, 0));
        assertEquals(2, dao.count());
        assertTrue(dao.findById(mobile.getCategoryId()).isPresent());
        assertEquals(1, dao.searchByName("thoại").size());

        mobile.setCategoryName("Điện thoại thông minh");
        dao.update(mobile);
        assertTrue(dao.findByCategoryName("điện thoại thông minh").isPresent());
        dao.delete(book.getCategoryId());
        assertEquals(1, dao.count());
        assertFalse(dao.findById(book.getCategoryId()).isPresent());
    }

    @AfterClass
    public static void closeFactory() { FACTORY.close(); }

    private static EntityManagerFactory createFactory() {
        Map<String, Object> properties = new HashMap<>();
        properties.put("jakarta.persistence.jdbc.driver", "org.h2.Driver");
        properties.put("jakarta.persistence.jdbc.url", "jdbc:h2:mem:categorytest;DB_CLOSE_DELAY=-1;MODE=MSSQLServer");
        properties.put("jakarta.persistence.jdbc.user", "sa");
        properties.put("jakarta.persistence.jdbc.password", "");
        properties.put("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
        properties.put("hibernate.hbm2ddl.auto", "create-drop");
        properties.put("hibernate.show_sql", "false");
        return Persistence.createEntityManagerFactory("category-crud-pu", properties);
    }
}
