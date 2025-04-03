package uz.pdp.demo9.config;

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
            "/register"));
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String url = request.getRequestURI();
        if (openPages.contains(url)) {
            filterChain.doFilter(request, response);
            return;
        }

        Object user = request.getSession().getAttribute("currentUser");
        if (user == null) {
            return;
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }
}
