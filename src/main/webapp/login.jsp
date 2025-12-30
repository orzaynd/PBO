<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%! String cp(HttpServletRequest req){ return req.getContextPath(); } %>
<!DOCTYPE html>
<html lang="id">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Login - Stok Obat</title>
    <link rel="stylesheet" href="<%=cp(request)%>/css/bootstrap.min.css">
    <link rel="stylesheet" href="<%=cp(request)%>/css/all.min.css">
    <link rel="stylesheet" href="<%=cp(request)%>/css/aos.css">
    <link rel="stylesheet" href="<%=cp(request)%>/css/styles.css">
</head>
<body class="gradient-bg d-flex align-items-center justify-content-center min-vh-100">
<div class="glass-card p-4 shadow-lg" data-aos="fade-up" style="min-width: 340px; max-width: 420px;">
    <div class="text-center mb-3">
        <div class="icon-circle mb-2">
            <i class="fa-solid fa-pills"></i>
        </div>
        <h4 class="fw-bold text-white mb-0">Sistem Stok Obat</h4>
        <small class="text-white-50">Puskesmas Inventory</small>
    </div>
    <form method="post" action="<%=cp(request)%>/login" class="needs-validation" novalidate>
        <div class="mb-3">
            <label class="form-label text-white">Username</label>
            <div class="input-group">
                <span class="input-group-text"><i class="fa-regular fa-user"></i></span>
                <input type="text" name="username" class="form-control" placeholder="admin" required>
            </div>
        </div>
        <div class="mb-3">
            <label class="form-label text-white">Password</label>
            <div class="input-group">
                <span class="input-group-text"><i class="fa-solid fa-lock"></i></span>
                <input type="password" id="passwordInput" name="password" class="form-control" placeholder="••••••" required>
                <button type="button" class="input-group-text" id="togglePwd" aria-label="Tampilkan/Sembunyikan password">
                    <i class="fa-regular fa-eye"></i>
                </button>
            </div>
        </div>
        <% if (request.getAttribute("error") != null) { %>
            <div class="alert alert-danger py-2"><%=request.getAttribute("error")%></div>
        <% } %>
        <button type="submit" class="btn btn-primary w-100">Masuk</button>
    </form>
</div>

<script src="<%=cp(request)%>/assets/js/aos.js"></script>
<script>
  AOS.init();
  const pwd = document.getElementById('passwordInput');
  const toggle = document.getElementById('togglePwd');
  toggle.addEventListener('click', () => {
    const isPwd = pwd.type === 'password';
    pwd.type = isPwd ? 'text' : 'password';
    toggle.querySelector('i').className = isPwd ? 'fa-regular fa-eye-slash' : 'fa-regular fa-eye';
  });
</script>
</body>
</html>
