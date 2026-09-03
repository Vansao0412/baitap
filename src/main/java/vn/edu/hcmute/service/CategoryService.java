package vn.edu.hcmute.service;

import java.util.List;
import java.util.Optional;

import vn.edu.hcmute.dao.CategoryDao;
import vn.edu.hcmute.dao.ICategoryDao;
import vn.edu.hcmute.entity.Category;

public class CategoryService implements ICategoryService {
    private final ICategoryDao categoryDao;

    public CategoryService() {
        this(new CategoryDao());
    }

    public CategoryService(ICategoryDao categoryDao) {
        this.categoryDao = categoryDao;
    }

    @Override
    public Category insert(Category category) {
        validate(category);
        if (categoryDao.findByCategoryName(category.getCategoryName()).isPresent()) {
            throw new IllegalArgumentException("Tên danh mục đã tồn tại");
        }
        return categoryDao.insert(category);
    }

    @Override
    public Category update(Category category) {
        validate(category);
        Category current = categoryDao.findById(category.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy danh mục"));
        categoryDao.findByCategoryName(category.getCategoryName())
                .filter(found -> found.getCategoryId() != current.getCategoryId())
                .ifPresent(found -> { throw new IllegalArgumentException("Tên danh mục đã tồn tại"); });
        return categoryDao.update(category);
    }

    @Override public void delete(int categoryId) { categoryDao.delete(categoryId); }
    @Override public Optional<Category> findById(int categoryId) { return categoryDao.findById(categoryId); }
    @Override public List<Category> findAll() { return categoryDao.findAll(); }
    @Override public List<Category> searchByName(String keyword) { return categoryDao.searchByName(keyword == null ? "" : keyword); }
    @Override public List<Category> findAll(int page, int pageSize) { return categoryDao.findAll(page, pageSize); }
    @Override public int count() { return categoryDao.count(); }

    private void validate(Category category) {
        if (category == null || category.getCategoryName() == null || category.getCategoryName().trim().isEmpty()) {
            throw new IllegalArgumentException("Tên danh mục không được để trống");
        }
        category.setCategoryName(category.getCategoryName().trim());
        if (category.getCategoryName().length() > 50) {
            throw new IllegalArgumentException("Tên danh mục tối đa 50 ký tự");
        }
        if (category.getStatus() != 0 && category.getStatus() != 1) {
            throw new IllegalArgumentException("Trạng thái không hợp lệ");
        }
    }
}
