package uz.pdp.demo9.config.servlets.auth;

import uz.pdp.demo9.config.entity.Comment;
import uz.pdp.demo9.config.entity.Publication;
import uz.pdp.demo9.config.entity.User;
import uz.pdp.demo9.config.services.CommentService;
import uz.pdp.demo9.config.services.PublicationService;
import uz.pdp.demo9.config.services.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        try {
            Optional<User> userOptional = UserService.findByEmail(email);
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                if (user.getPassword().equals(password)) {
                    req.getSession().setAttribute("currentUser", user);
                    List<Publication> publications = PublicationService.findAll();
                    for (Publication publication : publications) {
                        List<Comment> comments = CommentService.findByPublicationId(publication.getId());
                        publication.setComments(comments);
                    }
                    System.out.println("Fetched " + publications.size() + " publications after login:");
                    publications.forEach(publication -> System.out.println(publication));
                    req.setAttribute("publications", publications);
                    req.getRequestDispatcher("/cabinet.jsp").forward(req, resp);
                } else {
                    resp.sendRedirect("/auth/login.jsp");
                }
            } else {
                resp.sendRedirect("/auth/login.jsp");
            }
        } catch (SQLException e) {
            System.err.println("SQL Error: " + e.getMessage());
            e.printStackTrace();
            resp.sendRedirect("/auth/login.jsp?error=sql_error");
        }
    }
}
