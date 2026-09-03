<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Đặt lại mật khẩu</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/app.css">
</head>
<body class="login-page">
    <main class="login-card">
        <div class="login-header">
            <h1>Đặt lại mật khẩu</h1>
            <p>Nhập mã OTP và mật khẩu mới.</p>
        </div>

        <c:if test="${not empty error}">
            <div class="alert alert-danger">
                <c:out value="${error}" />
            </div>
        </c:if>

        <form method="post" action="${pageContext.request.contextPath}/reset-password">
            <div class="field">
                <label class="label">Email</label>
                <input class="input" type="email" name="email" required
                       value="<c:out value='${not empty email ? email : param.email}' />">
            </div>

            <div class="field">
                <label class="label">Mã OTP</label>
                <input class="input otp-input" name="otp" inputmode="numeric"
                       pattern="[0-9]{6}" maxlength="6" required>
            </div>

            <div class="field">
                <label class="label">Mật khẩu mới</label>
                <input class="input" type="password" name="password"
                       minlength="6" required>
            </div>

            <div class="field">
                <label class="label">Xác nhận mật khẩu</label>
                <input class="input" type="password" name="confirmPassword"
                       minlength="6" required>
            </div>

            <button class="btn btn-primary login-submit">Đổi mật khẩu</button>
        </form>
    </main>
</body>
</html>
