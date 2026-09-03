package vn.edu.hcmute.controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.edu.hcmute.service.UserService;
import vn.edu.hcmute.service.MailDeliveryException;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private UserService userService;

    @Override
    public void init() {
        userService = new UserService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        try {
            userService.register(
                    request.getParameter("fullName"),
                    request.getParameter("username"),
                    request.getParameter("email"),
                    request.getParameter("password"),
                    request.getParameter("confirmPassword"));
            response.sendRedirect(request.getContextPath() + "/verify-otp?email="
                    + java.net.URLEncoder.encode(request.getParameter("email"), java.nio.charset.StandardCharsets.UTF_8));
        } catch (MailDeliveryException exception) {
            response.sendRedirect(request.getContextPath() + "/verify-otp?mailError=1&email="
                    + java.net.URLEncoder.encode(request.getParameter("email"), java.nio.charset.StandardCharsets.UTF_8));
        } catch (IllegalArgumentException exception) {
            request.setAttribute("error", exception.getMessage());
            request.setAttribute("fullName", request.getParameter("fullName"));
            request.setAttribute("username", request.getParameter("username"));
            request.setAttribute("email", request.getParameter("email"));
            request.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(request, response);
        }
    }
}
