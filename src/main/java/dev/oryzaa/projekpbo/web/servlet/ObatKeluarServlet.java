package dev.oryzaa.projekpbo.web.servlet;

import dev.oryzaa.projekpbo.web.dao.ObatDao;
import dev.oryzaa.projekpbo.web.dao.ObatKeluarDao;
import dev.oryzaa.projekpbo.web.model.Obat;
import dev.oryzaa.projekpbo.web.model.ObatKeluar;
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

@WebServlet(name = "ObatKeluarServlet", urlPatterns = {"/transaksi-keluar"})
public class ObatKeluarServlet extends HttpServlet {

    private final ObatKeluarDao obatKeluarDao = new ObatKeluarDao();
    private final ObatDao obatDao = new ObatDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            List<ObatKeluar> list = obatKeluarDao.findAll();
            List<Obat> obatList = obatDao.findAll();
            req.setAttribute("keluarList", list);
            req.setAttribute("obatList", obatList);
            req.getRequestDispatcher("/WEB-INF/views/transaksi-keluar.jsp").forward(req, resp);
        } catch (SQLException e) {
            throw new ServletException("Gagal memuat obat keluar", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            ObatKeluar data = new ObatKeluar();
            data.setIdObat(Integer.parseInt(req.getParameter("idObat")));
            data.setTanggalKeluar(LocalDate.parse(req.getParameter("tanggalKeluar")));
            data.setJumlah(Integer.parseInt(req.getParameter("jumlah")));
            data.setTujuan(req.getParameter("tujuan"));
            data.setKeterangan(req.getParameter("keterangan"));
            data.setCreatedBy(currentUserId(req));
            obatKeluarDao.create(data);
            resp.sendRedirect(req.getContextPath() + "/transaksi-keluar");
        } catch (SQLException e) {
            throw new ServletException("Gagal menyimpan obat keluar", e);
        }
    }

    private Integer currentUserId(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        if (session == null) return null;
        User user = (User) session.getAttribute("user");
        return user != null ? user.getId() : null;
    }
}
