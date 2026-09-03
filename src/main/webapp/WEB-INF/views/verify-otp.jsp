<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Xác thực OTP</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/app.css">
</head>
<body class="login-page">
    <main class="login-card">
        <div class="login-header">
            <h1>Kích hoạt tài khoản</h1>
            <p>Nhập mã OTP đã gửi đến email.</p>
        </div>

        <c:if test="${param.mailError eq '1'}">
            <div class="alert alert-danger">
                Tài khoản đã tạo nhưng chưa gửi được email. Hãy kiểm tra SMTP rồi gửi lại OTP.
            </div>
        </c:if>

        <c:if test="${not empty error}">
            <div class="alert alert-danger">
                <c:out value="${error}" />
            </div>
        </c:if>

        <c:if test="${not empty message}">
            <div class="alert alert-success">
                <c:out value="${message}" />
            </div>
        </c:if>

        <form method="post" action="${pageContext.request.contextPath}/verify-otp">
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

            <button class="btn btn-primary login-submit">Xác nhận</button>
            <button class="btn btn-secondary login-submit"
                    name="action" value="resend" formnovalidate>
                Gửi lại OTP
            </button>
        </form>

        <p class="login-switch">
            <a href="${pageContext.request.contextPath}/login">Quay lại đăng nhập</a>
        </p>
    </main>
</body>
</html>
