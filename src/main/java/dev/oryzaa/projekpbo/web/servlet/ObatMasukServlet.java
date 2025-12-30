package dev.oryzaa.projekpbo.web.servlet;

import dev.oryzaa.projekpbo.web.config.AppConfig;
import dev.oryzaa.projekpbo.web.dao.ObatDao;
import dev.oryzaa.projekpbo.web.dao.ObatMasukDao;
import dev.oryzaa.projekpbo.web.model.Obat;
import dev.oryzaa.projekpbo.web.model.ObatMasuk;
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
 * Servlet handling incoming medicine (Obat Masuk) transactions.
 */
@WebServlet(name = "ObatMasukServlet", urlPatterns = {"/transaksi-masuk"})
public class ObatMasukServlet extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(ObatMasukServlet.class);
    private final ObatMasukDao obatMasukDao = new ObatMasukDao();
    private final ObatDao obatDao = new ObatDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) 
            throws ServletException, IOException {
        try {
            List<ObatMasuk> list = obatMasukDao.findAll();
            List<Obat> obatList = obatDao.findAll();
            req.setAttribute("masukList", list);
            req.setAttribute("obatList", obatList);
            req.getRequestDispatcher("/WEB-INF/views/transaksi-masuk.jsp").forward(req, resp);
        } catch (SQLException e) {
            log.error("Failed to load incoming medicines", e);
            throw new ServletException("Failed to load incoming medicines", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) 
            throws ServletException, IOException {
        try {
            ObatMasuk data = new ObatMasuk();
            data.setIdObat(ParsingUtil.parseInt(req.getParameter("idObat"), 0));
            data.setJumlah(ParsingUtil.parseInt(req.getParameter("jumlah"), 0));
            
            String tanggalMasuk = req.getParameter("tanggalMasuk");
            if (ParsingUtil.isValid(tanggalMasuk)) {
                data.setTanggalMasuk(LocalDate.parse(tanggalMasuk));
            }
            
            data.setNomorBatch(req.getParameter("nomorBatch"));
            data.setSupplier(req.getParameter("supplier"));
            data.setKeterangan(req.getParameter("keterangan"));
            data.setCreatedBy(getCurrentUserId(req));
            
            obatMasukDao.create(data);
            log.info("Incoming medicine recorded");
            resp.sendRedirect(req.getContextPath() + "/transaksi-masuk");
            
        } catch (SQLException e) {
            log.error("Failed to save incoming medicine", e);
            throw new ServletException("Failed to save incoming medicine", e);
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
