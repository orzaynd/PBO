package com.puskesmas.inventory.web.model;

public class DashboardStats {
    private long totalObat;
    private long totalMasuk;
    private long totalKeluar;
    private long lowStock;

    public long getTotalObat() { return totalObat; }
    public void setTotalObat(long totalObat) { this.totalObat = totalObat; }
    public long getTotalMasuk() { return totalMasuk; }
    public void setTotalMasuk(long totalMasuk) { this.totalMasuk = totalMasuk; }
    public long getTotalKeluar() { return totalKeluar; }
    public void setTotalKeluar(long totalKeluar) { this.totalKeluar = totalKeluar; }
    public long getLowStock() { return lowStock; }
    public void setLowStock(long lowStock) { this.lowStock = lowStock; }
}
