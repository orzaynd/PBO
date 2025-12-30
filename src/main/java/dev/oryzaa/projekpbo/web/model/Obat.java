package dev.oryzaa.projekpbo.web.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Obat (Medicine) entity.
 */
public class Obat {
    
    private int id;
    private String kodeObat;
    private String namaObat;
    private String kategori;
    private String satuan;
    private String deskripsi;
    private BigDecimal hargaBeli;
    private int stokSaatIni;
    private int stokMinimum;
    private LocalDate tanggalKadaluarsa;
    private String lokasiPenyimpanan;
    private String produsen;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer createdBy;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getKodeObat() {
        return kodeObat;
    }

    public void setKodeObat(String kodeObat) {
        this.kodeObat = kodeObat;
    }

    public String getNamaObat() {
        return namaObat;
    }

    public void setNamaObat(String namaObat) {
        this.namaObat = namaObat;
    }

    public String getKategori() {
        return kategori;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }

    public String getSatuan() {
        return satuan;
    }

    public void setSatuan(String satuan) {
        this.satuan = satuan;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public BigDecimal getHargaBeli() {
        return hargaBeli;
    }

    public void setHargaBeli(BigDecimal hargaBeli) {
        this.hargaBeli = hargaBeli;
    }

    public int getStokSaatIni() {
        return stokSaatIni;
    }

    public void setStokSaatIni(int stokSaatIni) {
        this.stokSaatIni = stokSaatIni;
    }

    public int getStokMinimum() {
        return stokMinimum;
    }

    public void setStokMinimum(int stokMinimum) {
        this.stokMinimum = stokMinimum;
    }

    public LocalDate getTanggalKadaluarsa() {
        return tanggalKadaluarsa;
    }

    public void setTanggalKadaluarsa(LocalDate tanggalKadaluarsa) {
        this.tanggalKadaluarsa = tanggalKadaluarsa;
    }

    public String getLokasiPenyimpanan() {
        return lokasiPenyimpanan;
    }

    public void setLokasiPenyimpanan(String lokasiPenyimpanan) {
        this.lokasiPenyimpanan = lokasiPenyimpanan;
    }

    public String getProdusen() {
        return produsen;
    }

    public void setProdusen(String produsen) {
        this.produsen = produsen;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Integer getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    public boolean isLowStock() {
        return stokSaatIni <= stokMinimum;
    }

    public boolean isExpired(LocalDate today) {
        return tanggalKadaluarsa != null && tanggalKadaluarsa.isBefore(today);
    }

    @Override
    public String toString() {
        return "Obat{" +
                "id=" + id +
                ", kodeObat='" + kodeObat + '\'' +
                ", namaObat='" + namaObat + '\'' +
                ", kategori='" + kategori + '\'' +
                ", stokSaatIni=" + stokSaatIni +
                '}';
    }
}
