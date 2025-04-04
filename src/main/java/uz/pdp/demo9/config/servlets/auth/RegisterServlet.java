package uz.pdp.demo9.config.servlets.auth;

import uz.pdp.demo9.config.entity.Attachment;
import uz.pdp.demo9.config.entity.User;
import uz.pdp.demo9.config.services.AttachmentService;
import uz.pdp.demo9.config.services.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.sql.SQLException;

@MultipartConfig
@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            System.out.println("RegisterServlet: Starting registration");

            String password = req.getParameter("password");
            String passwordRepeat = req.getParameter("passwordRepeat");
            System.out.println("Password: " + password + ", Password Repeat: " + passwordRepeat);

            if (!password.equals(passwordRepeat)) {
                System.out.println("Passwords do not match");
                resp.sendRedirect("/auth/register.jsp?error=password_mismatch");
                return;
            }
            if (password == null || password.length() <= 4) {
                System.out.println("Password too short or null");
                resp.sendRedirect("/auth/register.jsp?error=password_too_short");
                return;
            }

            Part part = req.getPart("photo");
            System.out.println("Photo part: " + (part != null ? "Received" : "Missing"));
            if (part == null) {
                System.out.println("No photo uploaded");
                resp.sendRedirect("/auth/register.jsp?error=photo_missing");
                return;
            }
            byte[] content = part.getInputStream().readAllBytes();
            System.out.println("Attachment content length: " + content.length);
            if (content.length == 0) {
                System.out.println("Photo content is empty");
            }

            Attachment attachment = new Attachment();
            attachment.setContent(content);
            Attachment savedAttachment = AttachmentService.save(attachment);
            System.out.println("Saved Attachment ID: " + savedAttachment.getId());

            User user = new User(req);
            System.out.println("User created: " + user);
            if (user.getEmail() == null) {
                System.out.println("Email is null");
                resp.sendRedirect("/auth/register.jsp?error=email_missing");
                return;
            }
            user.setPhotoId(savedAttachment.getId());
            System.out.println("User to save: " + user);

            User savedUser = UserService.save(user);
            System.out.println("Saved User ID: " + savedUser.getId());

            resp.sendRedirect("/auth/login.jsp");
        } catch (SQLException e) {
            System.err.println("SQL Error during registration: " + e.getMessage());
            e.printStackTrace();
            resp.sendRedirect("/auth/register.jsp?error=sql_error");
        } catch (ServletException | IOException e) {
            System.err.println("Servlet/IO Error during registration: " + e.getMessage());
            e.printStackTrace();
            resp.sendRedirect("/auth/register.jsp?error=registration_failed");
        }
    }
}