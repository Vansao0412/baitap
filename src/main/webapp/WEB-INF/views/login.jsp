<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Đăng nhập</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/app.css">
</head>
<body class="login-page">
    <main class="login-card">
        <div class="login-header">
            <h1>Đăng nhập</h1>
            <p>Nhập tài khoản để tiếp tục.</p>
        </div>
        <c:if test="${param.registered eq '1'}">
            <div class="alert alert-success">Đăng ký thành công. Bạn có thể đăng nhập.</div>
        </c:if>
        <c:if test="${param.reset eq '1'}">
            <div class="alert alert-success">Đã đổi mật khẩu thành công.</div>
        </c:if>
        <c:if test="${param.logout eq '1'}">
            <div class="alert alert-success">Bạn đã đăng xuất.</div>
        </c:if>
        <form method="post" action="${pageContext.request.contextPath}/login">
            <div class="field">
                <label class="label" for="username">Tên đăng nhập</label>
                <input class="input" id="username" name="username" autocomplete="username" required autofocus>
            </div>
            <div class="field">
                <label class="label" for="password">Mật khẩu</label>
                <input class="input" id="password" type="password" name="password" autocomplete="current-password" required>
            </div>
            <button class="btn btn-primary login-submit" type="submit">Đăng nhập</button>
        </form>
        <p class="login-switch">
            <a href="${pageContext.request.contextPath}/forgot-password">Quên mật khẩu?</a>
        </p>
        <p class="login-switch">
            Chưa có tài khoản?
            <a href="${pageContext.request.contextPath}/register">Đăng ký</a>
        </p>
    </main>
</body>
</html>
