<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="id">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Obat Masuk</title>
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
        <a href="${pageContext.request.contextPath}/obat" class="nav-link"><i class="fa-solid fa-capsules"></i> Data Obat</a>
        <a href="${pageContext.request.contextPath}/transaksi-masuk" class="nav-link active"><i class="fa-solid fa-circle-down"></i> Obat Masuk</a>
        <a href="${pageContext.request.contextPath}/transaksi-keluar" class="nav-link"><i class="fa-solid fa-circle-up"></i> Obat Keluar</a>
        <a href="${pageContext.request.contextPath}/laporan" class="nav-link"><i class="fa-solid fa-chart-line"></i> Laporan</a>
        <div class="mt-auto pt-4"><a href="${pageContext.request.contextPath}/logout" class="nav-link text-danger"><i class="fa-solid fa-right-from-bracket"></i> Keluar</a></div>
    </div>

    <div class="flex-grow-1 p-4 content-area">
        <div class="d-flex justify-content-between align-items-center mb-4">
            <div>
                <h3 class="text-white mb-0">Transaksi Obat Masuk</h3>
                <small class="text-white-50">Catat restock / pembelian</small>
            </div>
        </div>

        <div class="row g-3">
            <div class="col-lg-4" data-aos="fade-up">
                <div class="glass-card card">
                    <div class="card-header bg-transparent text-white fw-semibold">Tambah Obat Masuk</div>
                    <div class="card-body">
                        <form method="post" action="${pageContext.request.contextPath}/transaksi-masuk">
                            <div class="mb-2">
                                <label class="form-label">Obat</label>
                                <select name="idObat" class="form-select" required>
                                    <c:forEach items="${obatList}" var="o">
                                        <option value="${o.id}">${o.namaObat} (${o.kodeObat})</option>
                                    </c:forEach>
                                </select>
                            </div>
                            <div class="mb-2">
                                <label class="form-label">Tanggal Masuk</label>
                                <input type="date" name="tanggalMasuk" class="form-control" required>
                            </div>
                            <div class="mb-2">
                                <label class="form-label">Jumlah</label>
                                <input type="number" name="jumlah" class="form-control" required>
                            </div>
                            <div class="mb-2">
                                <label class="form-label">Nomor Batch</label>
                                <input type="text" name="nomorBatch" class="form-control">
                            </div>
                            <div class="mb-2">
                                <label class="form-label">Supplier</label>
                                <input type="text" name="supplier" class="form-control">
                            </div>
                            <div class="mb-2">
                                <label class="form-label">Keterangan</label>
                                <textarea name="keterangan" class="form-control" rows="2"></textarea>
                            </div>
                            <button class="btn btn-primary w-100 mt-2" type="submit">Simpan</button>
                        </form>
                    </div>
                </div>
            </div>

            <div class="col-lg-8" data-aos="fade-up" data-aos-delay="100">
                <div class="glass-card card">
                    <div class="card-header bg-transparent text-white fw-semibold">Riwayat Obat Masuk</div>
                    <div class="card-body p-0">
                        <div class="table-responsive">
                            <table class="table table-dark table-hover mb-0 align-middle">
                                <thead>
                                <tr>
                                    <th>Tanggal</th>
                                    <th>Obat</th>
                                    <th>Jumlah</th>
                                    <th>Supplier</th>
                                    <th>Batch</th>
                                </tr>
                                </thead>
                                <tbody>
                                <c:forEach items="${masukList}" var="m">
                                    <tr>
                                        <td>${m.tanggalMasuk}</td>
                                        <td>${m.namaObat}</td>
                                        <td>${m.jumlah} ${m.satuan}</td>
                                        <td>${m.supplier}</td>
                                        <td>${m.nomorBatch}</td>
                                    </tr>
                                </c:forEach>
                                <c:if test="${empty masukList}">
                                    <tr><td colspan="5" class="text-center text-white-50 py-3">Belum ada transaksi</td></tr>
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
