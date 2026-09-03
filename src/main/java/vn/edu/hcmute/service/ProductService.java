package vn.edu.hcmute.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import vn.edu.hcmute.dao.ProductDao;
import vn.edu.hcmute.entity.Product;

public class ProductService {
    private final ProductDao dao = new ProductDao();
    public Product insert(Product product) { validate(product); return dao.insert(product); }
    public Product update(Product product) { validate(product); return dao.update(product); }
    public void delete(int id) { dao.delete(id); }
    public Optional<Product> findById(int id) { return dao.findById(id); }
    public List<Product> findAll(int page, int size) { return dao.findAll(page, size); }
    public List<Product> findLatest(int limit) { return dao.findLatest(limit); }
    public int count() { return dao.count(); }

    private void validate(Product p) {
        if (p == null || p.getProductName() == null || p.getProductName().trim().isEmpty())
            throw new IllegalArgumentException("Tên sản phẩm không được để trống");
        p.setProductName(p.getProductName().trim());
        if (p.getProductName().length() > 150) throw new IllegalArgumentException("Tên sản phẩm tối đa 150 ký tự");
        if (p.getPrice() == null || p.getPrice().compareTo(BigDecimal.ZERO) < 0)
            throw new IllegalArgumentException("Giá sản phẩm không hợp lệ");
        if (p.getStock() < 0) throw new IllegalArgumentException("Số lượng không hợp lệ");
        if (p.getCategory() == null) throw new IllegalArgumentException("Vui lòng chọn danh mục");
    }
}
