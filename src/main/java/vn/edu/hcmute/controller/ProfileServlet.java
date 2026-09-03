package vn.edu.hcmute.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;

import vn.edu.hcmute.entity.User;
import vn.edu.hcmute.service.UserService;

@WebServlet("/profile")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024,
        maxFileSize = 5 * 1024 * 1024,
        maxRequestSize = 6 * 1024 * 1024)
public class ProfileServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Set<String> IMAGE_EXTENSIONS = Set.of("jpg", "jpeg", "png", "gif", "webp");
    private UserService userService;

    @Override
    public void init() throws ServletException {
        userService = new UserService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Optional<User> currentUser = getCurrentUser(request.getSession(false));
        if (currentUser.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        request.setAttribute("user", currentUser.get());
        if ("1".equals(request.getParameter("updated"))) {
            request.setAttribute("message", "Cập nhật hồ sơ thành công");
        }
        request.getRequestDispatcher("/WEB-INF/views/profile.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        Optional<User> currentUser = getCurrentUser(request.getSession(false));
        if (currentUser.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        User user = currentUser.get();
        String oldImage = user.getImage();
        String image = oldImage;
        String fullName = request.getParameter("fullName");
        String phone = request.getParameter("phone");
        Path savedFile = null;

        try {
            Part imagePart = isMultipart(request) ? request.getPart("image") : null;
            if (imagePart != null && imagePart.getSize() > 0) {
                image = saveImage(imagePart);
                savedFile = uploadedFile(image);
            }

            User updated = userService.updateProfile(user.getUserId(), fullName, phone, image);
            if (savedFile != null) deleteOldImage(oldImage, image);
            request.getSession().setAttribute("loggedUserId", updated.getUserId());
            response.sendRedirect(request.getContextPath() + "/profile?updated=1");
        } catch (IllegalArgumentException | IllegalStateException exception) {
            if (savedFile != null) Files.deleteIfExists(savedFile);
            user.setFullName(fullName == null ? "" : fullName.trim());
            user.setPhone(phone == null ? "" : phone.trim());
            request.setAttribute("user", user);
            request.setAttribute("error", exception.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/profile.jsp").forward(request, response);
        }
    }

    private Optional<User> getCurrentUser(HttpSession session) {
        if (session == null) return Optional.empty();
        Object idValue = session.getAttribute("loggedUserId");
        if (idValue != null) {
            try {
                Optional<User> user = userService.findById(Integer.parseInt(idValue.toString()));
                if (user.isPresent() && user.get().isActive()) return user;
            } catch (NumberFormatException ignored) {
                // Older sessions do not have the numeric id yet.
            }
        }
        Object username = session.getAttribute("loggedUsername");
        return userService.findByUsername(username == null ? null : username.toString())
                .filter(User::isActive);
    }

    private boolean isMultipart(HttpServletRequest request) {
        String contentType = request.getContentType();
        return contentType != null && contentType.toLowerCase(Locale.ROOT).startsWith("multipart/");
    }

    private String saveImage(Part imagePart) throws IOException {
        String contentType = imagePart.getContentType();
        if (contentType != null && !contentType.isBlank()
                && !contentType.toLowerCase(Locale.ROOT).startsWith("image/")
                && !"application/octet-stream".equalsIgnoreCase(contentType)) {
            throw new IllegalArgumentException("Chỉ được chọn tệp hình ảnh");
        }

        String originalName = imagePart.getSubmittedFileName();
        if (originalName == null || originalName.isBlank()) {
            throw new IllegalArgumentException("Vui lòng chọn ảnh");
        }
        int dot = originalName.lastIndexOf('.');
        String extension = dot >= 0 ? originalName.substring(dot + 1).toLowerCase(Locale.ROOT) : "";
        if (!IMAGE_EXTENSIONS.contains(extension)) {
            throw new IllegalArgumentException("Ảnh phải có định dạng JPG, PNG, GIF hoặc WEBP");
        }

        String fileName = UUID.randomUUID() + "." + extension;
        Path target = uploadedFile("upload:" + fileName);
        Files.createDirectories(target.getParent());
        try (var input = imagePart.getInputStream()) {
            Files.copy(input, target, StandardCopyOption.REPLACE_EXISTING);
        }
        return "upload:" + fileName;
    }

    private Path uploadedFile(String image) {
        String fileName = image == null || !image.startsWith("upload:") ? "" : image.substring(7);
        return getUploadDirectory().resolve(fileName).normalize();
    }

    private Path getUploadDirectory() {
        return Paths.get(System.getProperty("catalina.base", System.getProperty("java.io.tmpdir")),
                "jpa-category-uploads").toAbsolutePath().normalize();
    }

    private void deleteOldImage(String oldImage, String newImage) throws IOException {
        if (oldImage == null || !oldImage.startsWith("upload:") || oldImage.equals(newImage)) return;
        String oldName = oldImage.substring(7);
        if (oldName.matches("[a-zA-Z0-9-]+\\.(jpg|jpeg|png|gif|webp)")) {
            Files.deleteIfExists(uploadedFile(oldImage));
        }
    }
}
