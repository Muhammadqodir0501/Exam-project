package uz.pdp.demo9.config.servlets.file;

import uz.pdp.demo9.config.entity.Attachment;
import uz.pdp.demo9.config.services.AttachmentService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = "/file/*")
public class FileServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String url = req.getRequestURI();
        Integer fileId = Integer.parseInt(url.substring(url.lastIndexOf("/") + 1));
        Attachment attachment = AttachmentService.findById(fileId);
        resp.getOutputStream().write(attachment.getContent());
    }
}
