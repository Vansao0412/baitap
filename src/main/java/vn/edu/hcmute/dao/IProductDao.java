package vn.edu.hcmute.dao;

import java.util.List;
import java.util.Optional;
import vn.edu.hcmute.entity.Product;

public interface IProductDao {
    Product insert(Product product);
    Product update(Product product);
    void delete(int id);
    Optional<Product> findById(int id);
    List<Product> findAll(int page, int pageSize);
    List<Product> findLatest(int limit);
    int count();
}
