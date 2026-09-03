package vn.edu.hcmute.controller;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.edu.hcmute.service.ProductService;

@WebServlet("/product")
public class ProductServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final int PAGE_SIZE = 6;
    private final ProductService service = new ProductService();

    @Override protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String id = req.getParameter("id");
        if (id != null && !id.isBlank()) {
            try {
                req.setAttribute("product", service.findById(Integer.parseInt(id))
                        .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy sản phẩm")));
                req.getRequestDispatcher("/WEB-INF/views/product-detail.jsp").forward(req, resp);
            } catch (IllegalArgumentException ex) { resp.sendError(404, ex.getMessage()); }
            return;
        }
        int page = 1;
        try { page = Math.max(1, Integer.parseInt(req.getParameter("page"))); } catch (Exception ignored) { }
        int totalPages = Math.max(1, (int) Math.ceil(service.count() / (double) PAGE_SIZE));
        if (page > totalPages) page = totalPages;
        req.setAttribute("products", service.findAll(page - 1, PAGE_SIZE));
        req.setAttribute("page", page);
        req.setAttribute("totalPages", totalPages);
        req.getRequestDispatcher("/WEB-INF/views/product-list.jsp").forward(req, resp);
    }
}
