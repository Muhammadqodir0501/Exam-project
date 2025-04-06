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
import javax.servlet.http.HttpSession;
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
        String loginType = req.getParameter("loginType"); // "admin" or "user"
        try {
            Optional<User> userOptional = UserService.findByEmail(email);
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                if (user.getPassword().equals(password)) {
                    HttpSession session = req.getSession();
                    session.setAttribute("currentUser", user);
                    session.setAttribute("loginType", loginType); // Set login type (admin or user)
                    System.out.println("LoginServlet: User logged in as " + loginType + ", set currentUser in session: " + user.getEmail());
                    List<Publication> publications = PublicationService.findAll();
                    for (Publication publication : publications) {
                        List<Comment> comments = CommentService.findByPublicationId(publication.getId());
                        publication.setComments(comments);
                    }
                    System.out.println("LoginServlet: Fetched " + publications.size() + " publications after login");
                    req.setAttribute("publications", publications);
                    System.out.println("LoginServlet: Forwarding to cabinet.jsp");
                    req.getRequestDispatcher("/cabinet.jsp").forward(req, resp);
                } else {
                    System.out.println("LoginServlet: Invalid password, redirecting to login page");
                    String redirectPage = "admin".equals(loginType) ? "/auth/login.jsp" : "/auth/user_login.jsp";
                    resp.sendRedirect(redirectPage + "?error=invalid_credentials");
                }
            } else {
                System.out.println("LoginServlet: User not found, redirecting to login page");
                String redirectPage = "admin".equals(loginType) ? "/auth/login.jsp" : "/auth/user_login.jsp";
                resp.sendRedirect(redirectPage + "?error=user_not_found");
            }
        } catch (SQLException e) {
            System.err.println("SQL Error: " + e.getMessage());
            e.printStackTrace();
            String redirectPage = "admin".equals(loginType) ? "/auth/login.jsp" : "/auth/user_login.jsp";
            resp.sendRedirect(redirectPage + "?error=sql_error");
        }
    }
}
