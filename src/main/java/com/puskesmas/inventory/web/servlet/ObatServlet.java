package com.puskesmas.inventory.web.servlet;

import com.puskesmas.inventory.web.config.AppConfig;
import com.puskesmas.inventory.web.dao.ObatDao;
import com.puskesmas.inventory.web.model.Obat;
import com.puskesmas.inventory.web.model.User;
import com.puskesmas.inventory.web.util.ParsingUtil;
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
 * Servlet handling medicine (Obat) CRUD operations.
 */
@WebServlet(name = "ObatServlet", urlPatterns = {"/obat"})
public class ObatServlet extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(ObatServlet.class);
    private final ObatDao obatDao = new ObatDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) 
            throws ServletException, IOException {
        
        try {
            String id = req.getParameter("id");
            Obat edit = null;
            
            if (id != null && !id.isEmpty()) {
                edit = obatDao.findById(ParsingUtil.parseInt(id, 0));
            }
            
            List<Obat> list = obatDao.findAll();
            req.setAttribute("obatList", list);
            req.setAttribute("editObat", edit);
            req.getRequestDispatcher("/WEB-INF/views/obat.jsp").forward(req, resp);
            
        } catch (SQLException e) {
            log.error("Failed to load medicines", e);
            throw new ServletException("Failed to load medicines", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) 
            throws ServletException, IOException {
        
        String action = req.getParameter("action");
        
        if (action == null || action.isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/obat");
            return;
        }

        try {
            Integer userId = getCurrentUserId(req);
            
            switch (action) {
                case "create":
                    Obat newObat = buildObatFromRequest(req);
                    newObat.setCreatedBy(userId);
                    obatDao.create(newObat);
                    log.info("Medicine created: {}", newObat.getNamaObat());
                    break;
                    
                case "update":
                    Obat updateObat = buildObatFromRequest(req);
                    updateObat.setId(ParsingUtil.parseInt(req.getParameter("id"), 0));
                    updateObat.setCreatedBy(userId);
                    obatDao.update(updateObat);
                    log.info("Medicine updated: {}", updateObat.getNamaObat());
                    break;
                    
                case "delete":
                    int deleteId = ParsingUtil.parseInt(req.getParameter("id"), 0);
                    obatDao.delete(deleteId);
                    log.info("Medicine deleted: ID {}", deleteId);
                    break;
                    
                default:
                    log.warn("Unknown action: {}", action);
            }
            
            resp.sendRedirect(req.getContextPath() + "/obat");
            
        } catch (SQLException e) {
            log.error("Medicine operation failed", e);
            throw new ServletException("Medicine operation failed", e);
        }
    }

    private Obat buildObatFromRequest(HttpServletRequest req) {
        Obat obat = new Obat();
        obat.setKodeObat(req.getParameter("kodeObat"));
        obat.setNamaObat(req.getParameter("namaObat"));
        obat.setKategori(req.getParameter("kategori"));
        obat.setSatuan(req.getParameter("satuan"));
        obat.setDeskripsi(req.getParameter("deskripsi"));
        obat.setHargaBeli(ParsingUtil.parseBigDecimal(req.getParameter("hargaBeli")));
        obat.setStokSaatIni(ParsingUtil.parseInt(req.getParameter("stokSaatIni"), 0));
        obat.setStokMinimum(ParsingUtil.parseInt(req.getParameter("stokMinimum"), 10));
        
        String expiryDate = req.getParameter("tanggalKadaluarsa");
        if (ParsingUtil.isValid(expiryDate)) {
            try {
                obat.setTanggalKadaluarsa(LocalDate.parse(expiryDate));
            } catch (Exception e) {
                log.warn("Invalid expiry date format: {}", expiryDate);
            }
        }
        
        obat.setLokasiPenyimpanan(req.getParameter("lokasiPenyimpanan"));
        obat.setProdusen(req.getParameter("produsen"));
        
        return obat;
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
