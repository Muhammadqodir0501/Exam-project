package uz.pdp.demo9.config;

import uz.pdp.demo9.config.entity.User;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@WebFilter("/*")
public class MyFilter implements Filter {

    Set<String> openPages = new HashSet<>(Set.of(
            "/auth/login.jsp",
            "/auth/register.jsp",
            "/login",
            "/register",
            "/publication.jsp"));

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        String uri = req.getRequestURI();
        System.out.println("MyFilter: Processing URI: " + uri);

        // Allow image requests to bypass authentication check
        if (uri.startsWith(req.getContextPath() + "/publication/") && uri.matches(".*/\\d+$")) {
            System.out.println("MyFilter: Allowing image request: " + uri);
            chain.doFilter(request, response);
            return;
        }

        if (openPages.contains(uri)) {
            System.out.println("MyFilter: Allowing access to open page: " + uri);
            chain.doFilter(request, response);
            return;
        }

        User currentUser = (User) req.getSession().getAttribute("currentUser");
        if (currentUser == null) {
            System.out.println("MyFilter: User not logged in, redirecting to login");
            resp.sendRedirect("/auth/login.jsp");
        } else {
            System.out.println("MyFilter: User logged in, proceeding");
            chain.doFilter(request, response);
        }
    }
}
