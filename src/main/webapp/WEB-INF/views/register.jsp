<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Đăng ký</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/app.css">
</head>
<body class="login-page">
    <main class="login-card register-card">
        <div class="login-header">
            <h1>Đăng ký tài khoản</h1>
            <p>Điền thông tin để tạo tài khoản mới.</p>
        </div>
        <c:if test="${not empty error}">
            <div class="alert alert-danger">
                <c:out value="${error}" />
            </div>
        </c:if>
        <form method="post" action="${pageContext.request.contextPath}/register">
            <div class="field">
                <label class="label" for="fullName">Họ và tên</label>
                <input class="input" id="fullName" name="fullName"
                       maxlength="100" required autofocus
                       value="<c:out value='${fullName}' />">
            </div>
            <div class="field">
                <label class="label" for="username">Tên đăng nhập</label>
                <input class="input" id="username" name="username"
                       minlength="4" maxlength="30" pattern="[A-Za-z0-9_]+"
                       required value="<c:out value='${username}' />">
                <p class="hint">Từ 4-30 ký tự, chỉ gồm chữ, số và dấu gạch dưới.</p>
            </div>
            <div class="field">
                <label class="label" for="email">Email</label>
                <input class="input" id="email" type="email" name="email"
                       maxlength="100" required value="<c:out value='${email}' />">
            </div>
            <div class="field">
                <label class="label" for="password">Mật khẩu</label>
                <input class="input" id="password" type="password" name="password" minlength="6" required>
            </div>
            <div class="field">
                <label class="label" for="confirmPassword">Xác nhận mật khẩu</label>
                <input class="input" id="confirmPassword" type="password" name="confirmPassword" minlength="6" required>
            </div>
            <button class="btn btn-primary login-submit" type="submit">Đăng ký</button>
        </form>
        <p class="login-switch">
            Đã có tài khoản?
            <a href="${pageContext.request.contextPath}/login">Đăng nhập</a>
        </p>
    </main>
</body>
</html>
