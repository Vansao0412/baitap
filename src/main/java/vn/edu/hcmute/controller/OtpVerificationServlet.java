package vn.edu.hcmute.controller;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.edu.hcmute.service.MailDeliveryException;
import vn.edu.hcmute.service.UserService;

@WebServlet("/verify-otp")
public class OtpVerificationServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final UserService userService = new UserService();

    @Override protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/verify-otp.jsp").forward(request, response);
    }

    @Override protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String email = request.getParameter("email");
        try {
            if ("resend".equals(request.getParameter("action"))) {
                userService.resendActivationOtp(email);
                request.setAttribute("message", "Đã gửi lại mã OTP.");
            } else {
                userService.activate(email, request.getParameter("otp"));
                response.sendRedirect(request.getContextPath() + "/login?registered=1");
                return;
            }
        } catch (IllegalArgumentException | MailDeliveryException exception) {
            request.setAttribute("error", exception.getMessage());
        }
        request.setAttribute("email", email);
        request.getRequestDispatcher("/WEB-INF/views/verify-otp.jsp").forward(request, response);
    }
}
