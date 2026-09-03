package vn.edu.hcmute.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import vn.edu.hcmute.entity.User;

public class UserDaoTest {
    private static final EntityManagerFactory FACTORY = createFactory();
    private UserDAO userDAO;

    @Before
    public void resetDatabase() {
        try (EntityManager entityManager = FACTORY.createEntityManager()) {
            entityManager.getTransaction().begin();
            entityManager.createQuery("DELETE FROM User").executeUpdate();
            entityManager.getTransaction().commit();
        }

        userDAO = new UserDAO() {
            @Override
            protected EntityManager createEntityManager() {
                return FACTORY.createEntityManager();
            }
        };
    }

    @Test
    public void loginReturnsUserRoleFromDatabase() {
        User admin = new User("admin_test", "secret123",
                "Admin", "admin@test.local");
        admin.setActive(true);
        admin.setRole(User.ROLE_ADMIN);
        userDAO.insert(admin);

        User result = userDAO.authenticate("admin_test", "secret123").orElseThrow();

        assertEquals(User.ROLE_ADMIN, result.getRole());
        assertTrue(userDAO.authenticate("admin_test", "wrong-password").isEmpty());
    }

    @AfterClass
    public static void closeFactory() {
        FACTORY.close();
    }

    private static EntityManagerFactory createFactory() {
        Map<String, Object> properties = new HashMap<>();
        properties.put("jakarta.persistence.jdbc.driver", "org.h2.Driver");
        properties.put("jakarta.persistence.jdbc.url",
                "jdbc:h2:mem:usertest;DB_CLOSE_DELAY=-1;MODE=MSSQLServer");
        properties.put("jakarta.persistence.jdbc.user", "sa");
        properties.put("jakarta.persistence.jdbc.password", "");
        properties.put("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
        properties.put("hibernate.hbm2ddl.auto", "create-drop");
        properties.put("hibernate.show_sql", "false");
        return Persistence.createEntityManagerFactory("category-crud-pu", properties);
    }
}
