package vn.edu.hcmute.service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Optional;

import vn.edu.hcmute.dao.UserDAO;
import vn.edu.hcmute.entity.User;
import vn.edu.hcmute.util.PasswordUtil;

public class UserService {
    private static final String ACTIVATE = "ACTIVATE";
    private static final String RESET = "RESET";
    private static final int MAX_OTP_ATTEMPTS = 5;
    private final UserDAO userDAO = new UserDAO();
    private final MailService mailService = new MailService();
    private final SecureRandom random = new SecureRandom();

    public Optional<User> login(String username, String password) {
        if (username == null || password == null) return Optional.empty();
        return userDAO.authenticate(username.trim(), password);
    }

    public Optional<User> findById(int userId) {
        return userDAO.findById(userId);
    }

    public Optional<User> findByUsername(String username) {
        if (username == null || username.isBlank()) return Optional.empty();
        return userDAO.findByUsername(username.trim());
    }

    public User updateProfile(int userId, String fullName, String phone, String image) {
        User user = userDAO.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy tài khoản"));
        fullName = normalize(fullName);
        phone = normalize(phone);
        image = normalize(image);

        if (fullName.isEmpty() || fullName.length() > 100)
            throw new IllegalArgumentException("Họ tên không được để trống và tối đa 100 ký tự");
        if (!phone.isEmpty() && !phone.matches("[0-9+() .-]{7,20}"))
            throw new IllegalArgumentException("Số điện thoại không hợp lệ");
        if (image.length() > 500)
            throw new IllegalArgumentException("Tên ảnh quá dài");

        user.setFullName(fullName);
        user.setPhone(phone.isEmpty() ? null : phone);
        user.setImage(image.isEmpty() ? null : image);
        return userDAO.update(user);
    }

    public User register(String fullName, String username, String email, String password, String confirmPassword) {
        fullName = normalize(fullName);
        username = normalize(username);
        email = normalize(email).toLowerCase();
        validateRegistration(fullName, username, email, password, confirmPassword);

        User user = new User(username, password, fullName, email);
        user.setActive(false);
        user.setRole(User.ROLE_USER);
        String otp = issueOtp(user, ACTIVATE);
        userDAO.insert(user);
        try {
            mailService.sendOtp(email, otp, ACTIVATE);
        } catch (MailDeliveryException exception) {
            userDAO.delete(user.getUserId());
            throw exception;
        }
        return user;
    }

    public void resendActivationOtp(String email) {
        User user = findByEmail(email);
        if (user.isActive()) throw new IllegalArgumentException("Tài khoản đã được kích hoạt");
        String otp = issueOtp(user, ACTIVATE);
        userDAO.update(user);
        mailService.sendOtp(user.getEmail(), otp, ACTIVATE);
    }

    public void activate(String email, String otp) {
        User user = findByEmail(email);
        verifyOtp(user, otp, ACTIVATE);
        user.setActive(true);
        clearOtp(user);
        userDAO.update(user);
    }

    public void requestPasswordReset(String email) {
        User user = findByEmail(email);
        if (!user.isActive()) throw new IllegalArgumentException("Tài khoản chưa được kích hoạt");
        String otp = issueOtp(user, RESET);
        userDAO.update(user);
        mailService.sendOtp(user.getEmail(), otp, RESET);
    }

    public void resetPassword(String email, String otp, String password, String confirmPassword) {
        User user = findByEmail(email);
        verifyOtp(user, otp, RESET);
        if (password == null || password.length() < 6)
            throw new IllegalArgumentException("Mật khẩu phải có ít nhất 6 ký tự");
        if (!password.equals(confirmPassword))
            throw new IllegalArgumentException("Mật khẩu xác nhận không khớp");
        user.setPasswordHash(password);
        clearOtp(user);
        userDAO.update(user);
    }

    private void validateRegistration(String fullName, String username, String email,
            String password, String confirmPassword) {
        if (fullName.isEmpty() || fullName.length() > 100)
            throw new IllegalArgumentException("Họ tên không được để trống và tối đa 100 ký tự");
        if (!username.matches("[A-Za-z0-9_]{4,30}"))
            throw new IllegalArgumentException("Tên đăng nhập gồm 4-30 chữ, số hoặc dấu gạch dưới");
        if ("admin".equalsIgnoreCase(username))
            throw new IllegalArgumentException("Tên đăng nhập đã tồn tại");
        if (!email.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$") || email.length() > 100)
            throw new IllegalArgumentException("Email không hợp lệ");
        if (password == null || password.length() < 6)
            throw new IllegalArgumentException("Mật khẩu phải có ít nhất 6 ký tự");
        if (!password.equals(confirmPassword))
            throw new IllegalArgumentException("Mật khẩu xác nhận không khớp");

        userDAO.findByUsername(username).ifPresent(existing -> {
            if (existing.isActive())
                throw new IllegalArgumentException("Tên đăng nhập đã tồn tại");
            userDAO.delete(existing.getUserId());
        });

        userDAO.findByEmail(email).ifPresent(existing -> {
            if (existing.isActive())
                throw new IllegalArgumentException("Email đã được sử dụng");
            userDAO.delete(existing.getUserId());
        });
    }

    private String issueOtp(User user, String purpose) {
        String otp = String.format("%06d", random.nextInt(1_000_000));
        user.setOtpHash(PasswordUtil.hash(otp));
        user.setOtpPurpose(purpose);
        user.setOtpExpiresAt(LocalDateTime.now().plusMinutes(10));
        user.setOtpAttempts(0);
        return otp;
    }

    private void verifyOtp(User user, String otp, String purpose) {
        if (!purpose.equals(user.getOtpPurpose()) || user.getOtpHash() == null)
            throw new IllegalArgumentException("Yêu cầu OTP không hợp lệ");
        if (user.getOtpExpiresAt() == null || LocalDateTime.now().isAfter(user.getOtpExpiresAt()))
            throw new IllegalArgumentException("Mã OTP đã hết hạn");
        if (user.getOtpAttempts() >= MAX_OTP_ATTEMPTS)
            throw new IllegalArgumentException("Bạn đã nhập sai quá số lần cho phép");
        if (!PasswordUtil.verify(normalize(otp), user.getOtpHash())) {
            user.setOtpAttempts(user.getOtpAttempts() + 1);
            userDAO.update(user);
            throw new IllegalArgumentException("Mã OTP không chính xác");
        }
    }

    private User findByEmail(String email) {
        return userDAO.findByEmail(normalize(email).toLowerCase())
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy tài khoản với email này"));
    }

    private void clearOtp(User user) {
        user.setOtpHash(null);
        user.setOtpPurpose(null);
        user.setOtpExpiresAt(null);
        user.setOtpAttempts(0);
    }

    private String normalize(String value) { return value == null ? "" : value.trim(); }
}
