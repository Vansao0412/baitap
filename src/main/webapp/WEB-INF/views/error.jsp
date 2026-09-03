<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Đăng nhập thất bại</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/app.css">
</head>
<body>
    <main class="result-page">
        <section class="card result-card">
            <div class="result-icon danger">!</div>
            <p class="eyebrow" style="color: #dc2626">Không thể đăng nhập</p>
            <h1>Thông tin chưa chính xác</h1>
            <p class="subtitle">
                Tên đăng nhập hoặc mật khẩu không đúng. Vui lòng kiểm tra và thử lại.
            </p>
            <div class="result-actions">
                <a class="btn btn-primary" href="${pageContext.request.contextPath}/login">
                    Quay lại đăng nhập
                </a>
                <a class="btn btn-secondary" href="${pageContext.request.contextPath}/home">
                    Trang chủ
                </a>
            </div>
        </section>
    </main>
</body>
</html>
