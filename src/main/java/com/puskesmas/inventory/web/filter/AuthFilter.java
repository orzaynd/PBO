package com.puskesmas.inventory.web.filter;

import com.puskesmas.inventory.web.config.AppConfig;
import com.puskesmas.inventory.web.model.User;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Authentication filter to protect resources from unauthorized access.
 */
@WebFilter(filterName = "AuthFilter", urlPatterns = {"/*"})
public class AuthFilter implements Filter {

    private static final Logger log = LoggerFactory.getLogger(AuthFilter.class);

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) 
            throws IOException, ServletException {
        
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        String path = req.getRequestURI();
        
        if (isPublicResource(path)) {
            chain.doFilter(request, response);
            return;
        }

        HttpSession session = req.getSession(false);
        User user = session != null ? (User) session.getAttribute(AppConfig.USER_SESSION_KEY) : null;
        
        if (user == null) {
            log.debug("Unauthorized access attempt to: {}", path);
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }
        
        chain.doFilter(request, response);
    }

    private boolean isPublicResource(String path) {
        return path.contains("/css/") || 
               path.contains("/js/") || 
               path.contains("/img/") || 
               path.contains("/lib/") ||
               path.endsWith(".css") || 
               path.endsWith(".js") || 
               path.endsWith(".png") || 
               path.endsWith(".jpg") ||
               path.endsWith(".jpeg") ||
               path.endsWith(".gif") ||
               path.endsWith(".svg") ||
               path.endsWith(".woff") ||
               path.endsWith(".woff2") ||
               path.endsWith("/login") || 
               path.endsWith("login.jsp") ||
               path.endsWith("/logout") ||
               path.endsWith("/index.jsp");
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void destroy() {
    }
}
