package dev.oryzaa.projekpbo.web.servlet;

import dev.oryzaa.projekpbo.web.dao.UserDao;
import dev.oryzaa.projekpbo.web.model.User;
import dev.oryzaa.projekpbo.web.util.PasswordUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet(name = "LoginServlet", urlPatterns = {"/login"})
public class LoginServlet extends HttpServlet {

    private final UserDao userDao = new UserDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");

        try {
            User user = userDao.findByUsername(username);
            if (user != null && PasswordUtil.matches(password, user.getPassword())) {
                HttpSession session = req.getSession(true);
                session.setAttribute("user", user);
                resp.sendRedirect(req.getContextPath() + "/dashboard");
                return;
            }
        } catch (SQLException e) {
            throw new ServletException("Login failed", e);
        }

        req.setAttribute("error", "Username atau password salah.");
        req.getRequestDispatcher("/login.jsp").forward(req, resp);
    }
}
