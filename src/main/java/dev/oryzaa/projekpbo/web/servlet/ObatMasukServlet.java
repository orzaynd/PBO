package dev.oryzaa.projekpbo.web.servlet;

import dev.oryzaa.projekpbo.web.dao.ObatDao;
import dev.oryzaa.projekpbo.web.dao.ObatMasukDao;
import dev.oryzaa.projekpbo.web.model.Obat;
import dev.oryzaa.projekpbo.web.model.ObatMasuk;
import dev.oryzaa.projekpbo.web.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

@WebServlet(name = "ObatMasukServlet", urlPatterns = {"/transaksi-masuk"})
public class ObatMasukServlet extends HttpServlet {

    private final ObatMasukDao obatMasukDao = new ObatMasukDao();
    private final ObatDao obatDao = new ObatDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            List<ObatMasuk> list = obatMasukDao.findAll();
            List<Obat> obatList = obatDao.findAll();
            req.setAttribute("masukList", list);
            req.setAttribute("obatList", obatList);
            req.getRequestDispatcher("/WEB-INF/views/transaksi-masuk.jsp").forward(req, resp);
        } catch (SQLException e) {
            throw new ServletException("Gagal memuat obat masuk", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            ObatMasuk data = new ObatMasuk();
            data.setIdObat(Integer.parseInt(req.getParameter("idObat")));
            data.setTanggalMasuk(LocalDate.parse(req.getParameter("tanggalMasuk")));
            data.setJumlah(Integer.parseInt(req.getParameter("jumlah")));
            data.setNomorBatch(req.getParameter("nomorBatch"));
            data.setSupplier(req.getParameter("supplier"));
            data.setKeterangan(req.getParameter("keterangan"));
            data.setCreatedBy(currentUserId(req));
            obatMasukDao.create(data);
            resp.sendRedirect(req.getContextPath() + "/transaksi-masuk");
        } catch (SQLException e) {
            throw new ServletException("Gagal menyimpan obat masuk", e);
        }
    }

    private Integer currentUserId(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        if (session == null) return null;
        User user = (User) session.getAttribute("user");
        return user != null ? user.getId() : null;
    }
}
