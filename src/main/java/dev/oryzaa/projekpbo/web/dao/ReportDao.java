package dev.oryzaa.projekpbo.web.dao;

import dev.oryzaa.projekpbo.web.model.DashboardStats;
import dev.oryzaa.projekpbo.web.model.MonthlySummary;
import dev.oryzaa.projekpbo.web.model.Obat;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Data Access Object for reporting and dashboard data.
 */
public class ReportDao extends BaseDao {

    /**
     * Get dashboard statistics.
     */
    public DashboardStats getDashboardStats() throws SQLException {
        DashboardStats stats = new DashboardStats();
        
        try (Connection conn = getConnection()) {
            stats.setTotalObat(count(conn, "SELECT COUNT(*) FROM obat"));
            stats.setTotalMasuk(count(conn, "SELECT COUNT(*) FROM obat_masuk"));
            stats.setTotalKeluar(count(conn, "SELECT COUNT(*) FROM obat_keluar"));
            stats.setLowStock(count(conn, 
                "SELECT COUNT(*) FROM obat WHERE stok_saat_ini <= stok_minimum"));
        }
        
        return stats;
    }

    /**
     * Get low stock medicines.
     */
    public List<Obat> findLowStock(int limit) throws SQLException {
        String sql = "SELECT id_obat, kode_obat, nama_obat, kategori, satuan, " +
                "stok_saat_ini, stok_minimum FROM obat " +
                "WHERE stok_saat_ini <= stok_minimum " +
                "ORDER BY stok_saat_ini ASC LIMIT ?";
        List<Obat> list = new ArrayList<>();
        
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, limit);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Obat o = new Obat();
                    o.setId(rs.getInt("id_obat"));
                    o.setKodeObat(rs.getString("kode_obat"));
                    o.setNamaObat(rs.getString("nama_obat"));
                    o.setKategori(rs.getString("kategori"));
                    o.setSatuan(rs.getString("satuan"));
                    o.setStokSaatIni(rs.getInt("stok_saat_ini"));
                    o.setStokMinimum(rs.getInt("stok_minimum"));
                    list.add(o);
                }
            }
        }
        
        return list;
    }

    /**
     * Get monthly summary of incoming and outgoing medicines.
     */
    public List<MonthlySummary> getMonthlySummary() throws SQLException {
        Map<YearMonth, MonthlySummary> map = new HashMap<>();
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM");

        String masukSql = "SELECT to_char(date_trunc('month', tanggal_masuk), 'YYYY-MM') " +
                "AS bulan, SUM(jumlah) AS total FROM obat_masuk GROUP BY 1 ORDER BY 1";
        
        String keluarSql = "SELECT to_char(date_trunc('month', tanggal_keluar), 'YYYY-MM') " +
                "AS bulan, SUM(jumlah) AS total FROM obat_keluar GROUP BY 1 ORDER BY 1";

        try (Connection conn = getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement(masukSql);
                 ResultSet rs = ps.executeQuery()) {
                
                while (rs.next()) {
                    YearMonth ym = YearMonth.parse(rs.getString("bulan"), fmt);
                    MonthlySummary sum = map.getOrDefault(ym, new MonthlySummary());
                    sum.setBulan(ym);
                    sum.setTotalMasuk(rs.getLong("total"));
                    map.put(ym, sum);
                }
            }

            try (PreparedStatement ps = conn.prepareStatement(keluarSql);
                 ResultSet rs = ps.executeQuery()) {
                
                while (rs.next()) {
                    YearMonth ym = YearMonth.parse(rs.getString("bulan"), fmt);
                    MonthlySummary sum = map.getOrDefault(ym, new MonthlySummary());
                    sum.setBulan(ym);
                    sum.setTotalKeluar(rs.getLong("total"));
                    map.put(ym, sum);
                }
            }
        }

        List<MonthlySummary> list = new ArrayList<>(map.values());
        list.sort((a, b) -> a.getBulan().compareTo(b.getBulan()));
        return list;
    }

    private long count(Connection conn, String sql) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            if (rs.next()) {
                return rs.getLong(1);
            }
        }
        return 0L;
    }
}
