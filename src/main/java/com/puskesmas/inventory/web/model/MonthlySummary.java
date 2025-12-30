package com.puskesmas.inventory.web.model;

import java.time.YearMonth;

public class MonthlySummary {
    private YearMonth bulan;
    private long totalMasuk;
    private long totalKeluar;

    public YearMonth getBulan() { return bulan; }
    public void setBulan(YearMonth bulan) { this.bulan = bulan; }
    public long getTotalMasuk() { return totalMasuk; }
    public void setTotalMasuk(long totalMasuk) { this.totalMasuk = totalMasuk; }
    public long getTotalKeluar() { return totalKeluar; }
    public void setTotalKeluar(long totalKeluar) { this.totalKeluar = totalKeluar; }
}
