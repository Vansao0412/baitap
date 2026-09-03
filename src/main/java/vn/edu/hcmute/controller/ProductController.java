package vn.edu.hcmute.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import vn.edu.hcmute.entity.Product;
import vn.edu.hcmute.service.CategoryService;
import vn.edu.hcmute.service.ProductService;

@MultipartConfig(maxFileSize = 5 * 1024 * 1024, maxRequestSize = 6 * 1024 * 1024)
@WebServlet(urlPatterns = { "/admin/products", "/admin/product/add", "/admin/product/insert",
        "/admin/product/edit", "/admin/product/update", "/admin/product/delete" })
public class ProductController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final ProductService productService = new ProductService();
    private final CategoryService categoryService = new CategoryService();

    @Override protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String path = req.getServletPath();
        try {
            if ("/admin/products".equals(path)) showList(req, resp);
            else if ("/admin/product/add".equals(path)) {
                Product product = new Product();
                product.setStatus(1);
                showForm(req, resp, product);
            }
            else if ("/admin/product/edit".equals(path)) showForm(req, resp, productService.findById(id(req))
                    .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy sản phẩm")));
            else if ("/admin/product/delete".equals(path)) {
                productService.delete(id(req));
                resp.sendRedirect(req.getContextPath() + "/admin/products?message=deleted");
            } else resp.sendError(404);
        } catch (IllegalArgumentException ex) { resp.sendError(400, ex.getMessage()); }
    }

    @Override protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        boolean editing = "/admin/product/update".equals(req.getServletPath());
        try {
            Product product = editing ? productService.findById(id(req))
                    .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy sản phẩm")) : new Product();
            readProduct(req, product);
            if (editing) productService.update(product); else productService.insert(product);
            resp.sendRedirect(req.getContextPath() + "/admin/products?message=success");
        } catch (RuntimeException ex) {
            req.setAttribute("error", ex.getMessage());
            Product product = new Product();
            product.setProductName(req.getParameter("productName"));
            product.setDescription(req.getParameter("description"));
            req.setAttribute("product", product);
            req.setAttribute("categories", categoryService.findAll());
            req.getRequestDispatcher("/WEB-INF/views/product-form.jsp").forward(req, resp);
        }
    }

    private void showList(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int page = Math.max(1, intOrDefault(req.getParameter("page"), 1));
        int pageSize = 10;
        int total = productService.count();
        req.setAttribute("products", productService.findAll(page - 1, pageSize));
        req.setAttribute("page", page);
        req.setAttribute("totalPages", Math.max(1, (int) Math.ceil(total / (double) pageSize)));
        req.getRequestDispatcher("/WEB-INF/views/product-admin-list.jsp").forward(req, resp);
    }

    private void showForm(HttpServletRequest req, HttpServletResponse resp, Product product)
            throws ServletException, IOException {
        req.setAttribute("product", product);
        req.setAttribute("categories", categoryService.findAll());
        req.getRequestDispatcher("/WEB-INF/views/product-form.jsp").forward(req, resp);
    }

    private void readProduct(HttpServletRequest req, Product product) throws IOException, ServletException {
        product.setProductName(req.getParameter("productName"));
        product.setDescription(req.getParameter("description"));
        product.setPrice(new BigDecimal(req.getParameter("price")));
        product.setStock(Integer.parseInt(req.getParameter("stock")));
        product.setStatus("1".equals(req.getParameter("status")) ? 1 : 0);
        product.setCategory(categoryService.findById(Integer.parseInt(req.getParameter("categoryId")))
                .orElseThrow(() -> new IllegalArgumentException("Danh mục không tồn tại")));
        String imageUrl = req.getParameter("image");
        Part part = req.getPart("imageFile");
        if (part != null && part.getSize() > 0 && part.getSubmittedFileName() != null) {
            product.setImage(saveFile(part));
        } else if (imageUrl != null && !imageUrl.isBlank()) {
            product.setImage(imageUrl.trim());
        }
    }

    private String saveFile(Part part) throws IOException {
        String submitted = Paths.get(part.getSubmittedFileName()).getFileName().toString();
        String extension = submitted.contains(".") ? submitted.substring(submitted.lastIndexOf('.')) : "";
        String fileName = UUID.randomUUID() + extension;
        Path dir = Paths.get(System.getProperty("catalina.base", System.getProperty("java.io.tmpdir")), "jpa-category-uploads");
        Files.createDirectories(dir);
        part.write(dir.resolve(fileName).toString());
        return "upload:" + fileName;
    }

    private int id(HttpServletRequest req) {
        return Integer.parseInt(req.getParameter("id"));
    }

    private int intOrDefault(String value, int fallback) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException exception) {
            return fallback;
        }
    }
}
