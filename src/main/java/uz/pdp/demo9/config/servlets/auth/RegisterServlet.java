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

@WebServlet("/register")
@MultipartConfig
public class RegisterServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String firstName = req.getParameter("firstName");
            String lastName = req.getParameter("lastName");
            String email = req.getParameter("email");
            String password = req.getParameter("password");

            Part part = req.getPart("photo");
            if (part == null) {
                resp.sendRedirect("/auth/register.jsp?error=photo_missing");
                return;
            }
            byte[] content = part.getInputStream().readAllBytes();

            Attachment attachment = new Attachment();
            attachment.setContent(content);
            Attachment savedAttachment = AttachmentService.save(attachment);

            User user = new User();
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setEmail(email);
            user.setPassword(password);
            user.setPhotoId(savedAttachment.getId());

            UserService.save(user);
            resp.sendRedirect("/auth/login.jsp");
        } catch (SQLException e) {
            System.err.println("SQL Error: " + e.getMessage());
            e.printStackTrace();
            resp.sendRedirect("/auth/register.jsp?error=sql_error");
        } catch (ServletException | IOException e) {
            System.err.println("Servlet/IO Error: " + e.getMessage());
            e.printStackTrace();
            resp.sendRedirect("/auth/register.jsp?error=registration_failed");
        }
    }
}