package uz.pdp.demo9.config.servlets;

import uz.pdp.demo9.config.entity.Comment;
import uz.pdp.demo9.config.entity.Publication;
import uz.pdp.demo9.config.entity.User;
import uz.pdp.demo9.config.services.CommentService;
import uz.pdp.demo9.config.services.PublicationService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/cabinet")
public class CabinetServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            System.out.println("CabinetServlet: doGet called for /cabinet");
            User currentUser = (User) req.getSession().getAttribute("currentUser");
            if (currentUser == null) {
                System.out.println("User not logged in, redirecting to login");
                resp.sendRedirect("/auth/login.jsp");
                return;
            }

            List<Publication> publications = PublicationService.findAll();
            for (Publication publication : publications) {
                List<Comment> comments = CommentService.findByPublicationId(publication.getId());
                publication.setComments(comments);
            }
            System.out.println("Fetched " + publications.size() + " publications:");
            publications.forEach(publication -> System.out.println(publication));
            req.setAttribute("publications", publications);
            System.out.println("Forwarding to cabinet.jsp with publications");
            req.getRequestDispatcher("/cabinet.jsp").forward(req, resp);
        } catch (SQLException e) {
            System.err.println("SQL Error: " + e.getMessage());
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}