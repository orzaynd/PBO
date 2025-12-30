package dev.oryzaa.projekpbo.web.dao;

import dev.oryzaa.projekpbo.web.config.DBConnection;
import dev.oryzaa.projekpbo.web.model.Obat;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class ObatDao {

    public List<Obat> findAll() throws SQLException {
        String sql = "SELECT * FROM obat ORDER BY nama_obat";
        List<Obat> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(map(rs));
            }
        }
        return list;
    }

    public Obat findById(int id) throws SQLException {
        String sql = "SELECT * FROM obat WHERE id_obat = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return map(rs);
                }
            }
        }
        return null;
    }

    public void create(Obat obat) throws SQLException {
        String sql = "INSERT INTO obat (kode_obat, nama_obat, kategori, satuan, deskripsi, harga_beli, stok_saat_ini, stok_minimum, tanggal_kadaluarsa, lokasi_penyimpanan, produsen, created_by) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            fillStatement(obat, ps, true);
            ps.executeUpdate();
        }
    }

    public void update(Obat obat) throws SQLException {
        String sql = "UPDATE obat SET kode_obat=?, nama_obat=?, kategori=?, satuan=?, deskripsi=?, harga_beli=?, stok_saat_ini=?, stok_minimum=?, tanggal_kadaluarsa=?, lokasi_penyimpanan=?, produsen=?, updated_at=CURRENT_TIMESTAMP WHERE id_obat=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            int idx = fillStatement(obat, ps, false);
            ps.setInt(idx, obat.getId());
            ps.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM obat WHERE id_obat=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    public List<Obat> findLowStock() throws SQLException {
        String sql = "SELECT * FROM obat WHERE stok_saat_ini <= stok_minimum ORDER BY stok_saat_ini";
        List<Obat> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(map(rs));
            }
        }
        return list;
    }

    private int fillStatement(Obat o, PreparedStatement ps, boolean includeCreatedBy) throws SQLException {
        int idx = 1;
        ps.setString(idx++, o.getKodeObat());
        ps.setString(idx++, o.getNamaObat());
        ps.setString(idx++, o.getKategori());
        ps.setString(idx++, o.getSatuan());
        ps.setString(idx++, o.getDeskripsi());
        ps.setBigDecimal(idx++, o.getHargaBeli() != null ? o.getHargaBeli() : BigDecimal.ZERO);
        ps.setInt(idx++, o.getStokSaatIni());
        ps.setInt(idx++, o.getStokMinimum());
        if (o.getTanggalKadaluarsa() != null) {
            ps.setDate(idx++, Date.valueOf(o.getTanggalKadaluarsa()));
        } else {
            ps.setDate(idx++, null);
        }
        ps.setString(idx++, o.getLokasiPenyimpanan());
        ps.setString(idx++, o.getProdusen());
        if (includeCreatedBy) {
            if (o.getCreatedBy() != null) {
                ps.setInt(idx++, o.getCreatedBy());
            } else {
                ps.setNull(idx++, java.sql.Types.INTEGER);
            }
        }
        return idx;
    }

    private Obat map(ResultSet rs) throws SQLException {
        Obat o = new Obat();
        o.setId(rs.getInt("id_obat"));
        o.setKodeObat(rs.getString("kode_obat"));
        o.setNamaObat(rs.getString("nama_obat"));
        o.setKategori(rs.getString("kategori"));
        o.setSatuan(rs.getString("satuan"));
        o.setDeskripsi(rs.getString("deskripsi"));
        o.setHargaBeli(rs.getBigDecimal("harga_beli"));
        o.setStokSaatIni(rs.getInt("stok_saat_ini"));
        o.setStokMinimum(rs.getInt("stok_minimum"));
        Date tgl = rs.getDate("tanggal_kadaluarsa");
        if (tgl != null) { o.setTanggalKadaluarsa(tgl.toLocalDate()); }
        o.setLokasiPenyimpanan(rs.getString("lokasi_penyimpanan"));
        o.setProdusen(rs.getString("produsen"));
        Timestamp cAt = rs.getTimestamp("created_at");
        if (cAt != null) { o.setCreatedAt(cAt.toLocalDateTime()); }
        Timestamp uAt = rs.getTimestamp("updated_at");
        if (uAt != null) { o.setUpdatedAt(uAt.toLocalDateTime()); }
        int creator = rs.getInt("created_by");
        if (!rs.wasNull()) { o.setCreatedBy(creator); }
        return o;
    }
}
