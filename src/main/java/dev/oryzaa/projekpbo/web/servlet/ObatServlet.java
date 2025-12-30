package dev.oryzaa.projekpbo.web.servlet;

import dev.oryzaa.projekpbo.web.dao.ObatDao;
import dev.oryzaa.projekpbo.web.model.Obat;
import dev.oryzaa.projekpbo.web.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

@WebServlet(name = "ObatServlet", urlPatterns = {"/obat"})
public class ObatServlet extends HttpServlet {

    private final ObatDao obatDao = new ObatDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String id = req.getParameter("id");
            Obat edit = null;
            if (id != null && !id.isEmpty()) {
                edit = obatDao.findById(Integer.parseInt(id));
            }
            List<Obat> list = obatDao.findAll();
            req.setAttribute("obatList", list);
            req.setAttribute("editObat", edit);
            req.getRequestDispatcher("/WEB-INF/views/obat.jsp").forward(req, resp);
        } catch (SQLException e) {
            throw new ServletException("Gagal memuat data obat", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        if (action == null) {
            resp.sendRedirect(req.getContextPath() + "/obat");
            return;
        }

        try {
            switch (action) {
                case "create":
                    Obat baru = buildObatFromRequest(req);
                    baru.setStokSaatIni(parseInt(req.getParameter("stokSaatIni"), 0));
                    baru.setCreatedBy(currentUserId(req));
                    obatDao.create(baru);
                    break;
                case "update":
                    Obat update = buildObatFromRequest(req);
                    update.setId(Integer.parseInt(req.getParameter("id")));
                    update.setCreatedBy(currentUserId(req));
                    obatDao.update(update);
                    break;
                case "delete":
                    int id = Integer.parseInt(req.getParameter("id"));
                    obatDao.delete(id);
                    break;
                default:
                    break;
            }
            resp.sendRedirect(req.getContextPath() + "/obat");
        } catch (SQLException e) {
            throw new ServletException("Operasi obat gagal", e);
        }
    }

    private Obat buildObatFromRequest(HttpServletRequest req) {
        Obat o = new Obat();
        o.setKodeObat(req.getParameter("kodeObat"));
        o.setNamaObat(req.getParameter("namaObat"));
        o.setKategori(req.getParameter("kategori"));
        o.setSatuan(req.getParameter("satuan"));
        o.setDeskripsi(req.getParameter("deskripsi"));
        o.setHargaBeli(parseBigDecimal(req.getParameter("hargaBeli")));
        o.setStokSaatIni(parseInt(req.getParameter("stokSaatIni"), 0));
        o.setStokMinimum(parseInt(req.getParameter("stokMinimum"), 10));
        String kadaluarsa = req.getParameter("tanggalKadaluarsa");
        if (kadaluarsa != null && !kadaluarsa.isEmpty()) {
            o.setTanggalKadaluarsa(LocalDate.parse(kadaluarsa));
        }
        o.setLokasiPenyimpanan(req.getParameter("lokasiPenyimpanan"));
        o.setProdusen(req.getParameter("produsen"));
        return o;
    }

    private BigDecimal parseBigDecimal(String value) {
        try { return new BigDecimal(value); } catch (Exception e) { return BigDecimal.ZERO; }
    }

    private int parseInt(String value, int defaultVal) {
        try { return Integer.parseInt(value); } catch (Exception e) { return defaultVal; }
    }

    private Integer currentUserId(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        if (session == null) return null;
        User user = (User) session.getAttribute("user");
        return user != null ? user.getId() : null;
    }
}
