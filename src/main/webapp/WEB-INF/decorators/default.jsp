<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title><sitemesh:write property="title" /></title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/app.css">
    <sitemesh:write property="head" />
</head>
<body>
    <header class="topbar">
        <div class="topbar-inner">
            <a class="brand" href="${pageContext.request.contextPath}/home">
                <span class="brand-mark">C</span>
                <span>Cửa hàng</span>
            </a>
            <nav class="nav">
                <a href="${pageContext.request.contextPath}/home">Trang chủ</a>
                <a href="${pageContext.request.contextPath}/product">Sản phẩm</a>
                <c:choose>
                    <c:when test="${sessionScope.loggedRole eq 'ADMIN'}">
                        <a href="${pageContext.request.contextPath}/admin/products">Quản trị</a>
                        <a class="active" href="${pageContext.request.contextPath}/profile">Hồ sơ</a>
                        <a href="${pageContext.request.contextPath}/logout">Đăng xuất</a>
                    </c:when>
                    <c:when test="${not empty sessionScope.loggedUsername}">
                        <a class="active" href="${pageContext.request.contextPath}/profile">Hồ sơ</a>
                        <a href="${pageContext.request.contextPath}/logout">Đăng xuất</a>
                    </c:when>
                    <c:otherwise>
                        <a href="${pageContext.request.contextPath}/login">Đăng nhập</a>
                    </c:otherwise>
                </c:choose>
            </nav>
        </div>
    </header>
    <sitemesh:write property="body" />
</body>
</html>
