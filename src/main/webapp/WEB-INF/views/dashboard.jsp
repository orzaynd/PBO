<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="id">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Dashboard - Stok Obat</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/bootstrap.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/aos.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/styles.css">
</head>
<body class="gradient-bg">
<div class="d-flex min-vh-100">
    <div class="sidebar shadow-lg" data-aos="fade-right">
        <div class="sidebar-brand mb-3">
            <i class="fa-solid fa-pills me-2"></i>
            <span>Manajemen Obat</span>
        </div>
        <a href="${pageContext.request.contextPath}/dashboard" class="nav-link active"><i class="fa-solid fa-gauge"></i> Dashboard</a>
        <a href="${pageContext.request.contextPath}/obat" class="nav-link"><i class="fa-solid fa-capsules"></i> Data Obat</a>
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
                <h3 class="text-white mb-0">Dashboard</h3>
                <p class="text-white-50 mb-0">Ringkasan stok, transaksi, dan peringatan.</p>
            </div>
        </div>

        <div class="row g-3">
            <div class="col-md-3" data-aos="fade-up">
                <div class="info-card gradient-card-1">
                    <div class="d-flex justify-content-between align-items-center">
                        <div>
                            <p class="text-white-50 mb-1">Total Obat</p>
                            <h3 class="text-white mb-0">${stats.totalObat}</h3>
                        </div>
                        <i class="fa-solid fa-capsules fa-2x text-white"></i>
                    </div>
                </div>
            </div>
            <div class="col-md-3" data-aos="fade-up" data-aos-delay="50">
                <div class="info-card gradient-card-2">
                    <div class="d-flex justify-content-between align-items-center">
                        <div>
                            <p class="text-white-50 mb-1">Transaksi Masuk</p>
                            <h3 class="text-white mb-0">${stats.totalMasuk}</h3>
                        </div>
                        <i class="fa-solid fa-circle-down fa-2x text-white"></i>
                    </div>
                </div>
            </div>
            <div class="col-md-3" data-aos="fade-up" data-aos-delay="100">
                <div class="info-card gradient-card-3">
                    <div class="d-flex justify-content-between align-items-center">
                        <div>
                            <p class="text-white-50 mb-1">Transaksi Keluar</p>
                            <h3 class="text-white mb-0">${stats.totalKeluar}</h3>
                        </div>
                        <i class="fa-solid fa-circle-up fa-2x text-white"></i>
                    </div>
                </div>
            </div>
            <div class="col-md-3" data-aos="fade-up" data-aos-delay="150">
                <div class="info-card gradient-card-4">
                    <div class="d-flex justify-content-between align-items-center">
                        <div>
                            <p class="text-white-50 mb-1">Stok Menipis</p>
                            <h3 class="text-white mb-0">${stats.lowStock}</h3>
                        </div>
                        <i class="fa-solid fa-triangle-exclamation fa-2x text-white"></i>
                    </div>
                </div>
            </div>
        </div>

        <div class="card glass-card mt-4" data-aos="fade-up">
            <div class="card-header bg-transparent d-flex justify-content-between align-items-center">
                <div>
                    <h5 class="mb-0 text-white">Peringatan Stok</h5>
                    <small class="text-white-50">Obat dengan stok <= stok minimum</small>
                </div>
            </div>
            <div class="card-body p-0">
                <div class="table-responsive">
                    <table class="table table-dark table-hover mb-0 align-middle">
                        <thead>
                        <tr>
                            <th>Kode</th>
                            <th>Nama</th>
                            <th>Stok</th>
                            <th>Minimum</th>
                            <th>Kategori</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach items="${lowStock}" var="o">
                            <tr>
                                <td>${o.kodeObat}</td>
                                <td>${o.namaObat}</td>
                                <td><span class="badge bg-warning text-dark">${o.stokSaatIni}</span></td>
                                <td>${o.stokMinimum}</td>
                                <td>${o.kategori}</td>
                            </tr>
                        </c:forEach>
                        <c:if test="${empty lowStock}">
                            <tr><td colspan="5" class="text-center text-white-50 py-3">Tidak ada stok menipis.</td></tr>
                        </c:if>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>

        <div class="card glass-card mt-4" data-aos="fade-up">
            <div class="card-header bg-transparent d-flex justify-content-between align-items-center">
                <div>
                    <h5 class="mb-0 text-white">Perbandingan Masuk vs Keluar per Bulan</h5>
                    <small class="text-white-50">Memantau selisih stok yang masuk dan keluar setiap bulan</small>
                </div>
            </div>
            <div class="card-body">
                <canvas id="monthlyChart" height="120"></canvas>
            </div>
        </div>
    </div>
</div>

<script src="${pageContext.request.contextPath}/assets/js/bootstrap.bundle.min.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/aos.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/chart.umd.min.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/app.js"></script>
<script>
    (function(){
        const labels = [<c:forEach var="m" items="${monthly}" varStatus="s">'${m.bulan}'<c:if test="${!s.last}">,</c:if></c:forEach>];
        const masuk = [<c:forEach var="m" items="${monthly}" varStatus="s">${m.totalMasuk}<c:if test="${!s.last}">,</c:if></c:forEach>];
        const keluar = [<c:forEach var="m" items="${monthly}" varStatus="s">${m.totalKeluar}<c:if test="${!s.last}">,</c:if></c:forEach>];
        const ctx = document.getElementById('monthlyChart');
        if (ctx) {
            const styles = getComputedStyle(document.documentElement);
            const grid = styles.getPropertyValue('--chart-grid').trim() || 'rgba(255,255,255,0.12)';
            const text = styles.getPropertyValue('--chart-text').trim() || '#e5e7eb';
            new Chart(ctx, {
                type: 'line',
                data: {
                    labels,
                    datasets: [
                        {
                            label: 'Obat Masuk',
                            data: masuk,
                            borderColor: '#21d4fd',
                            backgroundColor: 'rgba(33,212,253,0.25)',
                            tension: 0.35,
                            fill: true,
                            borderWidth: 2.2,
                        },
                        {
                            label: 'Obat Keluar',
                            data: keluar,
                            borderColor: '#ff7eb3',
                            backgroundColor: 'rgba(255,126,179,0.25)',
                            tension: 0.35,
                            fill: true,
                            borderWidth: 2.2,
                        }
                    ]
                },
                options: {
                    plugins: { legend: { labels: { color: text } } },
                    scales: {
                        x: { ticks: { color: text }, grid: { color: grid } },
                        y: { ticks: { color: text }, grid: { color: grid } }
                    }
                }
            });
        }
    })();
</script>
</body>
</html>
