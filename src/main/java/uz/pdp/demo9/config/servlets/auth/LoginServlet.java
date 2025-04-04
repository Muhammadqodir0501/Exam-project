package uz.pdp.demo9.config.servlets.auth;

import uz.pdp.demo9.config.entity.User;
import uz.pdp.demo9.config.services.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        Optional<User> userOptional = null;
        try {
            userOptional = UserService.findByEmail(email);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        if(userOptional.isPresent()){
            User user = userOptional.get();
            if(user.getPassword().equals(password)){
                req.getSession().setAttribute("currentUser", user);
                resp.sendRedirect("/cabinet.jsp");
            }else{
                resp.sendRedirect("/auth/login.jsp");
            }
        }else{
            resp.sendRedirect("/auth/login.jsp");
        }
    }
}
