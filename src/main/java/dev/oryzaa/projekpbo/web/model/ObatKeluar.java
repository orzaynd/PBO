package dev.oryzaa.projekpbo.web.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class ObatKeluar {
    private int id;
    private int idObat;
    private LocalDate tanggalKeluar;
    private int jumlah;
    private String tujuan;
    private String keterangan;
    private LocalDateTime createdAt;
    private Integer createdBy;
    private String namaObat;
    private String satuan;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getIdObat() { return idObat; }
    public void setIdObat(int idObat) { this.idObat = idObat; }
    public LocalDate getTanggalKeluar() { return tanggalKeluar; }
    public void setTanggalKeluar(LocalDate tanggalKeluar) { this.tanggalKeluar = tanggalKeluar; }
    public int getJumlah() { return jumlah; }
    public void setJumlah(int jumlah) { this.jumlah = jumlah; }
    public String getTujuan() { return tujuan; }
    public void setTujuan(String tujuan) { this.tujuan = tujuan; }
    public String getKeterangan() { return keterangan; }
    public void setKeterangan(String keterangan) { this.keterangan = keterangan; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public Integer getCreatedBy() { return createdBy; }
    public void setCreatedBy(Integer createdBy) { this.createdBy = createdBy; }
    public String getNamaObat() { return namaObat; }
    public void setNamaObat(String namaObat) { this.namaObat = namaObat; }
    public String getSatuan() { return satuan; }
    public void setSatuan(String satuan) { this.satuan = satuan; }
}
