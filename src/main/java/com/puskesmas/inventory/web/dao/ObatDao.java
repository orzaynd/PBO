package com.puskesmas.inventory.web.dao;

import com.puskesmas.inventory.web.model.Obat;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Obat entity.
 */
public class ObatDao extends BaseDao {

    /**
     * Get all medicines.
     */
    public List<Obat> findAll() throws SQLException {
        String sql = "SELECT * FROM obat ORDER BY nama_obat ASC";
        List<Obat> list = new ArrayList<>();
        
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
     * Get medicine by ID.
     */
    public Obat findById(int id) throws SQLException {
        String sql = "SELECT * FROM obat WHERE id_obat = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSet(rs);
                }
            }
        }
        return null;
    }

    /**
     * Create a new medicine record.
     */
    public void create(Obat obat) throws SQLException {
        String sql = "INSERT INTO obat (kode_obat, nama_obat, kategori, satuan, deskripsi, " +
                "harga_beli, stok_saat_ini, stok_minimum, tanggal_kadaluarsa, " +
                "lokasi_penyimpanan, produsen, created_by) " +
                "VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
        
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            fillStatement(obat, ps, true);
            ps.executeUpdate();
            log.info("Medicine created: {}", obat.getNamaObat());
        }
    }

    /**
     * Update an existing medicine record.
     */
    public void update(Obat obat) throws SQLException {
        String sql = "UPDATE obat SET kode_obat=?, nama_obat=?, kategori=?, satuan=?, " +
                "deskripsi=?, harga_beli=?, stok_saat_ini=?, stok_minimum=?, " +
                "tanggal_kadaluarsa=?, lokasi_penyimpanan=?, produsen=?, " +
                "updated_at=CURRENT_TIMESTAMP WHERE id_obat=?";
        
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            int idx = fillStatement(obat, ps, false);
            ps.setInt(idx, obat.getId());
            ps.executeUpdate();
            log.info("Medicine updated: {} (ID: {})", obat.getNamaObat(), obat.getId());
        }
    }

    /**
     * Delete a medicine record.
     */
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM obat WHERE id_obat=?";
        
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, id);
            ps.executeUpdate();
            log.info("Medicine deleted: ID {}", id);
        }
    }

    /**
     * Get medicines with low stock.
     */
    public List<Obat> findLowStock() throws SQLException {
        String sql = "SELECT * FROM obat WHERE stok_saat_ini <= stok_minimum " +
                "ORDER BY stok_saat_ini ASC";
        List<Obat> list = new ArrayList<>();
        
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                list.add(mapResultSet(rs));
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
            ps.setNull(idx++, java.sql.Types.DATE);
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

    private Obat mapResultSet(ResultSet rs) throws SQLException {
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
        
        Date tglKadaluarsa = rs.getDate("tanggal_kadaluarsa");
        if (tglKadaluarsa != null) {
            o.setTanggalKadaluarsa(tglKadaluarsa.toLocalDate());
        }
        
        o.setLokasiPenyimpanan(rs.getString("lokasi_penyimpanan"));
        o.setProdusen(rs.getString("produsen"));
        
        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            o.setCreatedAt(createdAt.toLocalDateTime());
        }
        
        Timestamp updatedAt = rs.getTimestamp("updated_at");
        if (updatedAt != null) {
            o.setUpdatedAt(updatedAt.toLocalDateTime());
        }
        
        int createdBy = rs.getInt("created_by");
        if (!rs.wasNull()) {
            o.setCreatedBy(createdBy);
        }
        
        return o;
    }
}
