package uz.pdp.demo9.config.servlets;


import uz.pdp.demo9.config.entity.Comment;
import uz.pdp.demo9.config.entity.User;
import uz.pdp.demo9.config.services.CommentService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;

@WebServlet("/comment")
public class CommentServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            User currentUser = (User) req.getSession().getAttribute("currentUser");
            if (currentUser == null) {
                resp.sendRedirect("/auth/login.jsp");
                return;
            }

            Integer publicationId = Integer.parseInt(req.getParameter("publicationId"));
            String content = req.getParameter("content");

            Comment comment = new Comment();
            comment.setUserId(currentUser.getId());
            comment.setPublicationId(publicationId);
            comment.setContent(content);
            comment.setCreatedAt(new Date());

            CommentService.save(comment);
            resp.sendRedirect("/cabinet");
        } catch (SQLException e) {
            System.err.println("SQL Error: " + e.getMessage());
            e.printStackTrace();
            resp.sendRedirect("/cabinet?error=sql_error");
        }
    }
}