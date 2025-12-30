package dev.oryzaa.projekpbo.web.servlet;

import dev.oryzaa.projekpbo.web.config.AppConfig;
import dev.oryzaa.projekpbo.web.dao.ObatDao;
import dev.oryzaa.projekpbo.web.dao.ObatKeluarDao;
import dev.oryzaa.projekpbo.web.model.Obat;
import dev.oryzaa.projekpbo.web.model.ObatKeluar;
import dev.oryzaa.projekpbo.web.model.User;
import dev.oryzaa.projekpbo.web.util.ParsingUtil;
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
import java.time.LocalDate;
import java.util.List;

/**
 * Servlet handling outgoing medicine (Obat Keluar) transactions.
 */
@WebServlet(name = "ObatKeluarServlet", urlPatterns = {"/transaksi-keluar"})
public class ObatKeluarServlet extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(ObatKeluarServlet.class);
    private final ObatKeluarDao obatKeluarDao = new ObatKeluarDao();
    private final ObatDao obatDao = new ObatDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) 
            throws ServletException, IOException {
        try {
            List<ObatKeluar> list = obatKeluarDao.findAll();
            List<Obat> obatList = obatDao.findAll();
            req.setAttribute("keluarList", list);
            req.setAttribute("obatList", obatList);
            req.getRequestDispatcher("/WEB-INF/views/transaksi-keluar.jsp").forward(req, resp);
        } catch (SQLException e) {
            log.error("Failed to load outgoing medicines", e);
            throw new ServletException("Failed to load outgoing medicines", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) 
            throws ServletException, IOException {
        try {
            ObatKeluar data = new ObatKeluar();
            data.setIdObat(ParsingUtil.parseInt(req.getParameter("idObat"), 0));
            data.setJumlah(ParsingUtil.parseInt(req.getParameter("jumlah"), 0));
            
            String tanggalKeluar = req.getParameter("tanggalKeluar");
            if (ParsingUtil.isValid(tanggalKeluar)) {
                data.setTanggalKeluar(LocalDate.parse(tanggalKeluar));
            }
            
            data.setTujuan(req.getParameter("tujuan"));
            data.setKeterangan(req.getParameter("keterangan"));
            data.setCreatedBy(getCurrentUserId(req));
            
            obatKeluarDao.create(data);
            log.info("Outgoing medicine recorded");
            resp.sendRedirect(req.getContextPath() + "/transaksi-keluar");
            
        } catch (SQLException e) {
            log.error("Failed to save outgoing medicine", e);
            throw new ServletException("Failed to save outgoing medicine", e);
        }
    }

    private Integer getCurrentUserId(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        if (session == null) {
            return null;
        }
        User user = (User) session.getAttribute(AppConfig.USER_SESSION_KEY);
        return user != null ? user.getId() : null;
    }
}
