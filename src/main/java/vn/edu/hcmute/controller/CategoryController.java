package vn.edu.hcmute.controller;

import java.io.IOException;
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
import vn.edu.hcmute.entity.Category;
import vn.edu.hcmute.service.CategoryService;
import vn.edu.hcmute.service.ICategoryService;

@MultipartConfig(maxFileSize = 5 * 1024 * 1024, maxRequestSize = 6 * 1024 * 1024)
@WebServlet(urlPatterns = {
        "/admin/categories", "/admin/category/add", "/admin/category/insert",
        "/admin/category/edit", "/admin/category/update", "/admin/category/delete"
})
public class CategoryController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final ICategoryService categoryService = new CategoryService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String path = request.getServletPath();
        try {
            switch (path) {
                case "/admin/categories" -> showList(request, response);
                case "/admin/category/add" -> request.getRequestDispatcher("/WEB-INF/views/category-form.jsp")
                        .forward(request, response);
                case "/admin/category/edit" -> showEdit(request, response);
                case "/admin/category/delete" -> delete(request, response);
                default -> response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (IllegalArgumentException exception) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, exception.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        try {
            if ("/admin/category/insert".equals(request.getServletPath())) {
                categoryService.insert(readCategory(request, null));
            } else if ("/admin/category/update".equals(request.getServletPath())) {
                int id = requiredId(request);
                Category existing = categoryService.findById(id)
                        .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy danh mục"));
                categoryService.update(readCategory(request, existing));
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
            response.sendRedirect(request.getContextPath() + "/admin/categories?message=success");
        } catch (IllegalArgumentException exception) {
            request.setAttribute("error", exception.getMessage());
            request.setAttribute("category", categoryFromRequest(request));
            request.getRequestDispatcher("/WEB-INF/views/category-form.jsp").forward(request, response);
        }
    }

    private void showList(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String keyword = request.getParameter("keyword");
        request.setAttribute("categories", keyword == null || keyword.isBlank()
                ? categoryService.findAll() : categoryService.searchByName(keyword));
        request.setAttribute("keyword", keyword == null ? "" : keyword);
        request.getRequestDispatcher("/WEB-INF/views/category-list.jsp").forward(request, response);
    }

    private void showEdit(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Category category = categoryService.findById(requiredId(request))
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy danh mục"));
        request.setAttribute("category", category);
        request.getRequestDispatcher("/WEB-INF/views/category-form.jsp").forward(request, response);
    }

    private void delete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        categoryService.delete(requiredId(request));
        response.sendRedirect(request.getContextPath() + "/admin/categories?message=deleted");
    }

    private Category readCategory(HttpServletRequest request, Category existing)
            throws IOException, ServletException {
        Category category = existing == null ? new Category() : existing;
        category.setCategoryName(request.getParameter("categoryName"));
        category.setStatus("1".equals(request.getParameter("status")) ? 1 : 0);

        String imageUrl = request.getParameter("images");
        Part imagePart = request.getPart("imageFile");
        if (imagePart != null && imagePart.getSize() > 0 && imagePart.getSubmittedFileName() != null) {
            category.setImages(saveFile(imagePart));
        } else if (imageUrl != null && !imageUrl.trim().isEmpty()) {
            category.setImages(imageUrl.trim());
        }
        return category;
    }

    private Category categoryFromRequest(HttpServletRequest request) {
        Category category = new Category();
        String id = request.getParameter("categoryId");
        if (id != null && id.matches("\\d+")) category.setCategoryId(Integer.parseInt(id));
        category.setCategoryName(request.getParameter("categoryName"));
        category.setImages(request.getParameter("images"));
        category.setStatus("1".equals(request.getParameter("status")) ? 1 : 0);
        return category;
    }

    private int requiredId(HttpServletRequest request) {
        try {
            return Integer.parseInt(request.getParameter("id"));
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException("Mã danh mục không hợp lệ");
        }
    }

    private String saveFile(Part part) throws IOException {
        String submittedName = Paths.get(part.getSubmittedFileName()).getFileName().toString();
        String extension = submittedName.contains(".")
                ? submittedName.substring(submittedName.lastIndexOf('.')) : "";
        String fileName = UUID.randomUUID() + extension;
        Path uploadDirectory = Paths.get(System.getProperty("catalina.base", System.getProperty("java.io.tmpdir")),
                "jpa-category-uploads");
        Files.createDirectories(uploadDirectory);
        part.write(uploadDirectory.resolve(fileName).toString());
        return "upload:" + fileName;
    }
}
