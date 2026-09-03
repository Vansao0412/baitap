<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Quên mật khẩu</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/app.css">
</head>
<body class="login-page">
    <main class="login-card">
        <div class="login-header">
            <h1>Quên mật khẩu</h1>
            <p>Nhập email để nhận mã OTP.</p>
        </div>

        <c:if test="${not empty error}">
            <div class="alert alert-danger">
                <c:out value="${error}" />
            </div>
        </c:if>

        <form method="post" action="${pageContext.request.contextPath}/forgot-password">
            <div class="field">
                <label class="label">Email</label>
                <input class="input" type="email" name="email" required
                       value="<c:out value='${email}' />">
            </div>
            <button class="btn btn-primary login-submit">Gửi mã OTP</button>
        </form>

        <p class="login-switch">
            <a href="${pageContext.request.contextPath}/login">Quay lại đăng nhập</a>
        </p>
    </main>
</body>
</html>
