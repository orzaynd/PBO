package com.puskesmas.inventory.web.dao;

import com.puskesmas.inventory.web.model.ObatKeluar;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for ObatKeluar (Outgoing Medicine) entity.
 */
public class ObatKeluarDao extends BaseDao {

    /**
     * Get all outgoing medicine records.
     */
    public List<ObatKeluar> findAll() throws SQLException {
        String sql = "SELECT ok.*, o.nama_obat, o.satuan FROM obat_keluar ok " +
                "JOIN obat o ON ok.id_obat = o.id_obat " +
                "ORDER BY ok.tanggal_keluar DESC";
        List<ObatKeluar> list = new ArrayList<>();
        
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                list.add(mapResultSet(rs));
            }
        }
        return list;
    }

    /**
     * Create a new outgoing medicine record.
     */
    public void create(ObatKeluar data) throws SQLException {
        String sql = "INSERT INTO obat_keluar (id_obat, tanggal_keluar, jumlah, tujuan, " +
                "keterangan, created_by) VALUES (?,?,?,?,?,?)";
        
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, data.getIdObat());
            ps.setDate(2, Date.valueOf(data.getTanggalKeluar()));
            ps.setInt(3, data.getJumlah());
            ps.setString(4, data.getTujuan());
            ps.setString(5, data.getKeterangan());
            
            if (data.getCreatedBy() != null) {
                ps.setInt(6, data.getCreatedBy());
            } else {
                ps.setNull(6, java.sql.Types.INTEGER);
            }
            
            ps.executeUpdate();
            log.info("Outgoing medicine recorded: {} qty {}", data.getNamaObat(), data.getJumlah());
        }
    }

    private ObatKeluar mapResultSet(ResultSet rs) throws SQLException {
        ObatKeluar k = new ObatKeluar();
        k.setId(rs.getInt("id_obat_keluar"));
        k.setIdObat(rs.getInt("id_obat"));
        
        Date tglKeluar = rs.getDate("tanggal_keluar");
        if (tglKeluar != null) {
            k.setTanggalKeluar(tglKeluar.toLocalDate());
        }
        
        k.setJumlah(rs.getInt("jumlah"));
        k.setTujuan(rs.getString("tujuan"));
        k.setKeterangan(rs.getString("keterangan"));
        
        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            k.setCreatedAt(createdAt.toLocalDateTime());
        }
        
        int createdBy = rs.getInt("created_by");
        if (!rs.wasNull()) {
            k.setCreatedBy(createdBy);
        }
        
        k.setNamaObat(rs.getString("nama_obat"));
        k.setSatuan(rs.getString("satuan"));
        
        return k;
    }
}
