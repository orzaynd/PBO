package dev.oryzaa.projekpbo.web.dao;

import dev.oryzaa.projekpbo.web.config.DBConnection;
import dev.oryzaa.projekpbo.web.model.ObatMasuk;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class ObatMasukDao {

    public List<ObatMasuk> findAll() throws SQLException {
        String sql = "SELECT om.*, o.nama_obat, o.satuan FROM obat_masuk om JOIN obat o ON om.id_obat = o.id_obat ORDER BY om.tanggal_masuk DESC";
        List<ObatMasuk> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(map(rs));
            }
        }
        return list;
    }

    public void create(ObatMasuk data) throws SQLException {
        String sql = "INSERT INTO obat_masuk (id_obat, tanggal_masuk, jumlah, nomor_batch, supplier, keterangan, created_by) VALUES (?,?,?,?,?,?,?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, data.getIdObat());
            ps.setDate(2, Date.valueOf(data.getTanggalMasuk()));
            ps.setInt(3, data.getJumlah());
            ps.setString(4, data.getNomorBatch());
            ps.setString(5, data.getSupplier());
            ps.setString(6, data.getKeterangan());
            if (data.getCreatedBy() != null) {
                ps.setInt(7, data.getCreatedBy());
            } else {
                ps.setNull(7, java.sql.Types.INTEGER);
            }
            ps.executeUpdate();
        }
    }

    private ObatMasuk map(ResultSet rs) throws SQLException {
        ObatMasuk m = new ObatMasuk();
        m.setId(rs.getInt("id_obat_masuk"));
        m.setIdObat(rs.getInt("id_obat"));
        Date tgl = rs.getDate("tanggal_masuk");
        if (tgl != null) { m.setTanggalMasuk(tgl.toLocalDate()); }
        m.setJumlah(rs.getInt("jumlah"));
        m.setNomorBatch(rs.getString("nomor_batch"));
        m.setSupplier(rs.getString("supplier"));
        m.setKeterangan(rs.getString("keterangan"));
        Timestamp cAt = rs.getTimestamp("created_at");
        if (cAt != null) { m.setCreatedAt(cAt.toLocalDateTime()); }
        int creator = rs.getInt("created_by");
        if (!rs.wasNull()) { m.setCreatedBy(creator); }
        m.setNamaObat(rs.getString("nama_obat"));
        m.setSatuan(rs.getString("satuan"));
        return m;
    }
}
