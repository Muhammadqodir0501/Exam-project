package uz.pdp.demo9.config.servlets.publication;

import uz.pdp.demo9.config.entity.Attachment;
import uz.pdp.demo9.config.entity.Comment;
import uz.pdp.demo9.config.entity.Publication;
import uz.pdp.demo9.config.entity.User;
import uz.pdp.demo9.config.services.AttachmentService;
import uz.pdp.demo9.config.services.CommentService;
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
                resp.sendRedirect("/cabinet?error=photo_missing");
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

            System.out.println("Publication saved, redirecting to /cabinet");
            resp.sendRedirect("/cabinet");
        } catch (SQLException e) {
            System.err.println("SQL Error: " + e.getMessage());
            e.printStackTrace();
            resp.sendRedirect("/cabinet?error=sql_error");
        } catch (ServletException | IOException e) {
            System.err.println("Servlet/IO Error: " + e.getMessage());
            e.printStackTrace();
            resp.sendRedirect("/cabinet?error=publication_failed");
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String url = req.getRequestURI();
            if (url.equals(req.getContextPath() + "/publication")) {
                List<Publication> publications = PublicationService.findAll();
                for (Publication publication : publications) {
                    List<Comment> comments = CommentService.findByPublicationId(publication.getId());
                    publication.setComments(comments);
                }
                req.setAttribute("publications", publications);
                req.getRequestDispatcher("/publication.jsp").forward(req, resp);
                return;
            }

            Integer fileId = Integer.parseInt(url.substring(url.lastIndexOf("/") + 1));
            System.out.println("PublicationServlet: Fetching image for attachment ID: " + fileId);

            Attachment attachment = AttachmentService.findById(fileId);
            if (attachment != null) {
                resp.setContentType("image/jpeg");
                resp.getOutputStream().write(attachment.getContent());
            } else {
                System.out.println("PublicationServlet: Attachment not found for ID: " + fileId);
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (SQLException e) {
            System.err.println("SQL Error: " + e.getMessage());
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } catch (NumberFormatException e) {
            System.err.println("Invalid ID format: " + e.getMessage());
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

}
