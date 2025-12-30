<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="id">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Laporan</title>
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
        <a href="${pageContext.request.contextPath}/transaksi-masuk" class="nav-link"><i class="fa-solid fa-circle-down"></i> Obat Masuk</a>
        <a href="${pageContext.request.contextPath}/transaksi-keluar" class="nav-link"><i class="fa-solid fa-circle-up"></i> Obat Keluar</a>
        <a href="${pageContext.request.contextPath}/laporan" class="nav-link active"><i class="fa-solid fa-chart-line"></i> Laporan</a>
        <div class="mt-auto pt-4"><a href="${pageContext.request.contextPath}/logout" class="nav-link text-danger"><i class="fa-solid fa-right-from-bracket"></i> Keluar</a></div>
    </div>

    <div class="flex-grow-1 p-4 content-area">
        <div class="d-flex justify-content-between align-items-center mb-4">
            <div>
                <h3 class="text-white mb-0">Laporan Stok & Transaksi</h3>
                <small class="text-white-50">Low stock & rekap bulanan</small>
            </div>
        </div>

        <div class="row g-3">
            <div class="col-lg-5" data-aos="fade-up">
                <div class="glass-card card">
                    <div class="card-header bg-transparent text-white fw-semibold">Stok Menipis</div>
                    <div class="card-body p-0">
                        <div class="table-responsive">
                            <table class="table table-dark table-hover mb-0 align-middle">
                                <thead>
                                <tr>
                                    <th>Kode</th>
                                    <th>Nama</th>
                                    <th>Stok</th>
                                    <th>Min</th>
                                </tr>
                                </thead>
                                <tbody>
                                <c:forEach items="${lowStock}" var="o">
                                    <tr>
                                        <td>${o.kodeObat}</td>
                                        <td>${o.namaObat}</td>
                                        <td><span class="badge bg-warning text-dark">${o.stokSaatIni}</span></td>
                                        <td>${o.stokMinimum}</td>
                                    </tr>
                                </c:forEach>
                                <c:if test="${empty lowStock}">
                                    <tr><td colspan="4" class="text-center text-white-50 py-3">Tidak ada stok menipis</td></tr>
                                </c:if>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>

            <div class="col-lg-7" data-aos="fade-up" data-aos-delay="100">
                <div class="glass-card card">
                    <div class="card-header bg-transparent text-white fw-semibold">Rekap Bulanan</div>
                    <div class="card-body p-0">
                        <div class="table-responsive">
                            <table class="table table-dark table-hover mb-0 align-middle">
                                <thead>
                                <tr>
                                    <th>Bulan</th>
                                    <th>Total Masuk</th>
                                    <th>Total Keluar</th>
                                </tr>
                                </thead>
                                <tbody>
                                <c:forEach items="${monthly}" var="m">
                                    <tr>
                                        <td>${m.bulan}</td>
                                        <td>${m.totalMasuk}</td>
                                        <td>${m.totalKeluar}</td>
                                    </tr>
                                </c:forEach>
                                <c:if test="${empty monthly}">
                                    <tr><td colspan="3" class="text-center text-white-50 py-3">Belum ada data</td></tr>
                                </c:if>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <div class="card glass-card mt-4" data-aos="fade-up" data-aos-delay="150">
            <div class="card-header bg-transparent text-white fw-semibold">Grafik Bulanan</div>
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
                type: 'bar',
                data: {
                    labels,
                    datasets: [
                        { label: 'Obat Masuk', data: masuk, backgroundColor: 'rgba(33,212,253,0.7)', borderColor: '#21d4fd', borderWidth: 1.5 },
                        { label: 'Obat Keluar', data: keluar, backgroundColor: 'rgba(255,126,179,0.7)', borderColor: '#ff7eb3', borderWidth: 1.5 }
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
