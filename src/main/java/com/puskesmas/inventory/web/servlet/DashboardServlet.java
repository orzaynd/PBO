package com.puskesmas.inventory.web.servlet;

import com.puskesmas.inventory.web.dao.ObatDao;
import com.puskesmas.inventory.web.dao.ReportDao;
import com.puskesmas.inventory.web.model.DashboardStats;
import com.puskesmas.inventory.web.model.Obat;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * Servlet handling dashboard display.
 */
@WebServlet(name = "DashboardServlet", urlPatterns = {"/dashboard"})
public class DashboardServlet extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(DashboardServlet.class);
    private final ReportDao reportDao = new ReportDao();
    private final ObatDao obatDao = new ObatDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) 
            throws ServletException, IOException {
        
        try {
            DashboardStats stats = reportDao.getDashboardStats();
            List<Obat> lowStock = obatDao.findLowStock();
            
            req.setAttribute("stats", stats);
            req.setAttribute("lowStock", lowStock);
            req.setAttribute("monthly", reportDao.getMonthlySummary());
            req.getRequestDispatcher("/WEB-INF/views/dashboard.jsp").forward(req, resp);
            
        } catch (SQLException e) {
            log.error("Failed to load dashboard", e);
            throw new ServletException("Failed to load dashboard", e);
        }
    }
}
