package vn.edu.hcmute.controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import vn.edu.hcmute.entity.User;
import vn.edu.hcmute.service.UserService;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private UserService userService;

    @Override
    public void init() throws ServletException {
        userService = new UserService();
    }

    @Override
    protected void doGet(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        request.getRequestDispatcher("/WEB-INF/views/login.jsp")
               .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request,
                           HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        User user = userService.login(username, password).orElse(null);

        if (user != null) {
            HttpSession session = request.getSession();
            request.changeSessionId();
            session.setAttribute("loggedUsername", user.getUsername());
            session.setAttribute("loggedRole", user.getRole());
            session.setAttribute("loggedUserId", user.getUserId());

            if (User.ROLE_ADMIN.equals(user.getRole())) {
                response.sendRedirect(request.getContextPath() + "/admin/products");
            } else {
                response.sendRedirect(request.getContextPath() + "/home");
            }

        } else {

            request.getRequestDispatcher("/WEB-INF/views/error.jsp")
                   .forward(request, response);
        }
    }
}
