package dev.oryzaa.projekpbo.web.servlet;

import dev.oryzaa.projekpbo.web.dao.ObatDao;
import dev.oryzaa.projekpbo.web.dao.ReportDao;
import dev.oryzaa.projekpbo.web.model.DashboardStats;
import dev.oryzaa.projekpbo.web.model.Obat;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name = "DashboardServlet", urlPatterns = {"/dashboard"})
public class DashboardServlet extends HttpServlet {

    private final ReportDao reportDao = new ReportDao();
    private final ObatDao obatDao = new ObatDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            DashboardStats stats = reportDao.getDashboardStats();
            List<Obat> lowStock = obatDao.findLowStock();
            req.setAttribute("stats", stats);
            req.setAttribute("lowStock", lowStock);
            req.setAttribute("monthly", reportDao.getMonthlySummary());
            req.getRequestDispatcher("/WEB-INF/views/dashboard.jsp").forward(req, resp);
        } catch (SQLException e) {
            throw new ServletException("Gagal memuat dashboard", e);
        }
    }
}
