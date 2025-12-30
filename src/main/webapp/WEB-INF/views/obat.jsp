<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="id">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Data Obat</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/bootstrap.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/aos.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/styles.css">
</head>
<body class="gradient-bg">
<div class="d-flex min-vh-100">
    <div class="sidebar shadow-lg" data-aos="fade-right">
        <div class="sidebar-brand mb-3"><i class="fa-solid fa-pills me-2"></i>Manajemen Obat</div>
        <a href="${pageContext.request.contextPath}/dashboard" class="nav-link"><i class="fa-solid fa-gauge"></i> Dashboard</a>
        <a href="${pageContext.request.contextPath}/obat" class="nav-link active"><i class="fa-solid fa-capsules"></i> Data Obat</a>
        <a href="${pageContext.request.contextPath}/transaksi-masuk" class="nav-link"><i class="fa-solid fa-circle-down"></i> Obat Masuk</a>
        <a href="${pageContext.request.contextPath}/transaksi-keluar" class="nav-link"><i class="fa-solid fa-circle-up"></i> Obat Keluar</a>
        <a href="${pageContext.request.contextPath}/laporan" class="nav-link"><i class="fa-solid fa-chart-line"></i> Laporan</a>
        <div class="mt-auto pt-4">
            <a href="${pageContext.request.contextPath}/logout" class="nav-link text-danger"><i class="fa-solid fa-right-from-bracket"></i> Keluar</a>
        </div>
    </div>

    <div class="flex-grow-1 p-4 content-area">
        <div class="d-flex justify-content-between align-items-center mb-4">
            <div>
                <h3 class="text-white mb-0">Data Obat</h3>
                <small class="text-white-50">Kelola data obat</small>
            </div>
            <a href="${pageContext.request.contextPath}/obat" class="btn btn-outline-light btn-sm">Reset Form</a>
        </div>

        <div class="row g-3">
            <div class="col-lg-4" data-aos="fade-up">
                <div class="glass-card card">
                    <div class="card-header bg-transparent text-white fw-semibold">${editObat == null ? "Tambah Obat" : "Ubah Obat"}</div>
                    <div class="card-body">
                        <form method="post" action="${pageContext.request.contextPath}/obat">
                            <input type="hidden" name="action" value="${editObat == null ? "create" : "update"}">
                            <c:if test="${editObat != null}">
                                <input type="hidden" name="id" value="${editObat.id}">
                            </c:if>
                            <div class="mb-2">
                                <label class="form-label">Kode</label>
                                <input type="text" name="kodeObat" class="form-control" value="${editObat.kodeObat}" required>
                            </div>
                            <div class="mb-2">
                                <label class="form-label">Nama</label>
                                <input type="text" name="namaObat" class="form-control" value="${editObat.namaObat}" required>
                            </div>
                            <div class="mb-2">
                                <label class="form-label">Kategori</label>
                                <input type="text" name="kategori" class="form-control" value="${editObat.kategori}">
                            </div>
                            <div class="mb-2">
                                <label class="form-label">Satuan</label>
                                <input type="text" name="satuan" class="form-control" value="${editObat.satuan}" required>
                            </div>
                            <div class="mb-2">
                                <label class="form-label">Harga Beli</label>
                                <input type="number" step="0.01" name="hargaBeli" class="form-control" value="${editObat.hargaBeli}">
                            </div>
                            <div class="mb-2">
                                <label class="form-label">Stok Saat Ini</label>
                                <input type="number" name="stokSaatIni" class="form-control" value="${editObat.stokSaatIni}" required>
                            </div>
                            <div class="mb-2">
                                <label class="form-label">Stok Minimum</label>
                                <input type="number" name="stokMinimum" class="form-control" value="${editObat.stokMinimum}" required>
                            </div>
                            <div class="mb-2">
                                <label class="form-label">Kadaluarsa</label>
                                <input type="date" name="tanggalKadaluarsa" class="form-control" value="${editObat.tanggalKadaluarsa}">
                            </div>
                            <div class="mb-2">
                                <label class="form-label">Lokasi</label>
                                <input type="text" name="lokasiPenyimpanan" class="form-control" value="${editObat.lokasiPenyimpanan}">
                            </div>
                            <div class="mb-2">
                                <label class="form-label">Produsen</label>
                                <input type="text" name="produsen" class="form-control" value="${editObat.produsen}">
                            </div>
                            <div class="mb-2">
                                <label class="form-label">Deskripsi</label>
                                <textarea name="deskripsi" class="form-control" rows="2">${editObat.deskripsi}</textarea>
                            </div>
                            <button type="submit" class="btn btn-primary w-100 mt-2">${editObat == null ? "Simpan" : "Update"}</button>
                        </form>
                    </div>
                </div>
            </div>

            <div class="col-lg-8" data-aos="fade-up" data-aos-delay="100">
                <div class="glass-card card">
                    <div class="card-header bg-transparent text-white fw-semibold">Daftar Obat</div>
                    <div class="card-body p-0">
                        <div class="table-responsive">
                            <table class="table table-dark table-hover mb-0 align-middle">
                                <thead>
                                <tr>
                                    <th>Kode</th>
                                    <th>Nama</th>
                                    <th>Stok</th>
                                    <th>Min</th>
                                    <th>Satuan</th>
                                    <th>Aksi</th>
                                </tr>
                                </thead>
                                <tbody>
                                <c:forEach items="${obatList}" var="o">
                                    <tr>
                                        <td>${o.kodeObat}</td>
                                        <td>${o.namaObat}</td>
                                        <td>${o.stokSaatIni}</td>
                                        <td>${o.stokMinimum}</td>
                                        <td>${o.satuan}</td>
                                        <td>
                                            <a href="${pageContext.request.contextPath}/obat?id=${o.id}" class="btn btn-sm btn-outline-info"><i class="fa-regular fa-pen-to-square"></i></a>
                                            <form method="post" action="${pageContext.request.contextPath}/obat" class="d-inline" onsubmit="return confirm('Hapus data?');">
                                                <input type="hidden" name="action" value="delete">
                                                <input type="hidden" name="id" value="${o.id}">
                                                <button class="btn btn-sm btn-outline-danger"><i class="fa-regular fa-trash-can"></i></button>
                                            </form>
                                        </td>
                                    </tr>
                                </c:forEach>
                                <c:if test="${empty obatList}">
                                    <tr><td colspan="6" class="text-center text-white-50 py-3">Belum ada data</td></tr>
                                </c:if>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<script src="${pageContext.request.contextPath}/assets/js/bootstrap.bundle.min.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/aos.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/app.js"></script>
</body>
</html>
