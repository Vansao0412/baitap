package vn.edu.hcmute.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/image")
public class ImageServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String fileName = request.getParameter("name");
        if (fileName == null || !fileName.matches("[a-zA-Z0-9-]+(\\.[a-zA-Z0-9]{1,10})?")) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        Path uploadDirectory = Paths.get(System.getProperty("catalina.base", System.getProperty("java.io.tmpdir")),
                "jpa-category-uploads");
        Path file = uploadDirectory.resolve(fileName);
        if (!Files.isRegularFile(file)) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        response.setContentType(Files.probeContentType(file));
        Files.copy(file, response.getOutputStream());
    }
}
