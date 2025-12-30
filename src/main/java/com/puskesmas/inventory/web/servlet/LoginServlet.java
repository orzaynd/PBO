package com.puskesmas.inventory.web.servlet;

import com.puskesmas.inventory.web.config.AppConfig;
import com.puskesmas.inventory.web.dao.UserDao;
import com.puskesmas.inventory.web.model.User;
import com.puskesmas.inventory.web.util.PasswordUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Servlet handling user login.
 */
@WebServlet(name = "LoginServlet", urlPatterns = {"/login"})
public class LoginServlet extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(LoginServlet.class);
    private final UserDao userDao = new UserDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) 
            throws ServletException, IOException {
        req.getRequestDispatcher("/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) 
            throws ServletException, IOException {
        
        String username = req.getParameter("username");
        String password = req.getParameter("password");

        if (!isValidInput(username, password)) {
            req.setAttribute(AppConfig.ERROR_ATTRIBUTE_KEY, "Username and password are required.");
            req.getRequestDispatcher("/login.jsp").forward(req, resp);
            return;
        }

        try {
            User user = userDao.findByUsername(username);
            
            if (user != null && PasswordUtil.matches(password, user.getPassword())) {
                HttpSession session = req.getSession(true);
                session.setAttribute(AppConfig.USER_SESSION_KEY, user);
                log.info("User logged in: {}", username);
                resp.sendRedirect(req.getContextPath() + "/dashboard");
                return;
            }
            
            log.warn("Failed login attempt for username: {}", username);
            req.setAttribute(AppConfig.ERROR_ATTRIBUTE_KEY, "Invalid username or password.");
            req.getRequestDispatcher("/login.jsp").forward(req, resp);
            
        } catch (SQLException e) {
            log.error("Database error during login", e);
            throw new ServletException("Login failed", e);
        }
    }

    private boolean isValidInput(String username, String password) {
        return username != null && !username.trim().isEmpty() &&
               password != null && !password.isEmpty();
    }
}
