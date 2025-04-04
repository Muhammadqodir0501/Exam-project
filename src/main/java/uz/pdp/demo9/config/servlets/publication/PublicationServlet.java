package uz.pdp.demo9.config.servlets.publication;

import uz.pdp.demo9.config.entity.Attachment;
import uz.pdp.demo9.config.entity.Publication;
import uz.pdp.demo9.config.entity.User;
import uz.pdp.demo9.config.services.AttachmentService;
import uz.pdp.demo9.config.services.PublicationService;
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
import java.util.List;

@WebServlet(urlPatterns = "/publication/*")
@MultipartConfig
public class PublicationServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            System.out.println("PublicationServlet: Posting Publication");

            User currentUser = (User) req.getSession().getAttribute("currentUser");
            currentUser = UserService.findByEmail(currentUser.getEmail()).orElseThrow();
            if (currentUser == null) {
                resp.sendRedirect("/auth/login.jsp");
                return;
            }

            System.out.println(currentUser);

            Part part = req.getPart("photo");
            System.out.println("Photo part: " + (part != null ? "Received" : "Missing"));
            if (part == null) {
                System.out.println("No photo uploaded");
                resp.sendRedirect("/cabinet.jsp?error=photo_missing");
                return;
            }
            byte[] content = part.getInputStream().readAllBytes();
            System.out.println("Attachment content length: " + content.length);

            Attachment attachment = new Attachment();
            attachment.setContent(content);
            Attachment savedAttachment = AttachmentService.save(attachment);
            System.out.println("Saved Attachment ID: " + savedAttachment.getId());

            Publication publication = new Publication();
            publication.setUserId(currentUser.getId());
            publication.setPublicationPhotoId(savedAttachment.getId());
            publication.setTitle(req.getParameter("title"));
            publication.setDescription(req.getParameter("description"));
            PublicationService.save(publication, currentUser.getId());

            resp.sendRedirect("/cabinet.jsp");
        } catch (SQLException e) {
            System.err.println("SQL Error: " + e.getMessage());
            e.printStackTrace();
            resp.sendRedirect("/cabinet.jsp?error=sql_error");
        } catch (ServletException | IOException e) {
            System.err.println("Servlet/IO Error: " + e.getMessage());
            e.printStackTrace();
            resp.sendRedirect("/cabinet.jsp?error=publication_failed");
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String url = req.getRequestURI();
            if (url.equals(req.getContextPath() + "/publication")) {
                List<Publication> publications = PublicationService.findAll();
                req.setAttribute("publications", publications);
                req.getRequestDispatcher("/publication.jsp").forward(req, resp);
                return;
            }

            Integer fileId = Integer.parseInt(url.substring(url.lastIndexOf("/") + 1));
            Publication publication = PublicationService.findById(fileId).orElseThrow(() -> new ServletException("Publication not found"));
            Attachment attachment = AttachmentService.findById(publication.getPublicationPhotoId());
            if (attachment != null) {
                resp.setContentType("image/jpeg");
                resp.getOutputStream().write(attachment.getContent());
            } else {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (SQLException e) {
            System.err.println("SQL Error: " + e.getMessage());
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } catch (ServletException e) {
            System.err.println("Servlet Error: " + e.getMessage());
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

}
