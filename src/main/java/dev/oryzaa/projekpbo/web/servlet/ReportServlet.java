package dev.oryzaa.projekpbo.web.servlet;

import dev.oryzaa.projekpbo.web.dao.ReportDao;
import dev.oryzaa.projekpbo.web.model.MonthlySummary;
import dev.oryzaa.projekpbo.web.model.Obat;
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
 * Servlet handling report generation and display.
 */
@WebServlet(name = "ReportServlet", urlPatterns = {"/laporan"})
public class ReportServlet extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(ReportServlet.class);
    private static final int LIMIT = 50;
    private final ReportDao reportDao = new ReportDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) 
            throws ServletException, IOException {
        try {
            List<Obat> lowStock = reportDao.findLowStock(LIMIT);
            List<MonthlySummary> monthly = reportDao.getMonthlySummary();
            req.setAttribute("lowStock", lowStock);
            req.setAttribute("monthly", monthly);
            req.getRequestDispatcher("/WEB-INF/views/laporan.jsp").forward(req, resp);
        } catch (SQLException e) {
            log.error("Failed to load report", e);
            throw new ServletException("Failed to load report", e);
        }
    }
}
