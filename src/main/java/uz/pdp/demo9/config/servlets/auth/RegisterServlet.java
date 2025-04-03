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


@MultipartConfig
@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String password = req.getParameter("password");
        String passwordRepeat = req.getParameter("passwordRepeat");
        if (!password.equals(passwordRepeat)) {
            return;
        }
        Part part = req.getPart("photo");
        Attachment attachment = new Attachment();
        attachment.setContent(part.getInputStream().readAllBytes());
        Attachment savedAttachment = AttachmentService.save(attachment);
        User user = new User(req);
        user.setPhotoId(savedAttachment.getId());
        UserService.save(user);
        resp.sendRedirect("/auth/login.jsp");
    }
}
