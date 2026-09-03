package vn.edu.hcmute.service;

import java.util.List;
import java.util.Optional;
import vn.edu.hcmute.entity.Category;

public interface ICategoryService {
    Category insert(Category category);
    Category update(Category category);
    void delete(int categoryId);
    Optional<Category> findById(int categoryId);
    List<Category> findAll();
    List<Category> searchByName(String keyword);
    List<Category> findAll(int page, int pageSize);
    int count();
}
