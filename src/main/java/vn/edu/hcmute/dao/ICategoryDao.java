package vn.edu.hcmute.dao;

import java.util.List;
import java.util.Optional;
import vn.edu.hcmute.entity.Category;

public interface ICategoryDao {
    Category insert(Category category);
    Category update(Category category);
    void delete(int categoryId);
    Optional<Category> findById(int categoryId);
    Optional<Category> findByCategoryName(String categoryName);
    List<Category> findAll();
    List<Category> searchByName(String keyword);
    List<Category> findAll(int page, int pageSize);
    int count();
}
