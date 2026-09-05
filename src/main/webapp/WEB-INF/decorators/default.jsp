<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Cửa hàng</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/css/bootstrap.min.css"
          rel="stylesheet"
          integrity="sha384-sRIl4kxILFvY47J16cr9ZwB07vP4J8+LH7qKQnuqkuIAvNWLzeN8tE5YBujZqJLB"
          crossorigin="anonymous">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/app.css">
    <sitemesh:write property="head" />
</head>
<body>
    <header class="navbar navbar-expand-lg bg-body-tertiary border-bottom">
        <div class="container">
            <a class="navbar-brand fw-bold" href="${pageContext.request.contextPath}/home">
                Cửa hàng
            </a>
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse"
                    data-bs-target="#mainNavigation" aria-controls="mainNavigation"
                    aria-expanded="false" aria-label="Mở menu">
                <span class="navbar-toggler-icon"></span>
            </button>
            <nav id="mainNavigation" class="collapse navbar-collapse">
                <div class="navbar-nav ms-auto">
                    <a class="nav-link" href="${pageContext.request.contextPath}/home">Trang chủ</a>
                    <a class="nav-link" href="${pageContext.request.contextPath}/product">Sản phẩm</a>
                <c:choose>
                    <c:when test="${sessionScope.loggedRole eq 'ADMIN'}">
                        <a class="nav-link" href="${pageContext.request.contextPath}/admin/products">Quản trị</a>
                        <a class="nav-link" href="${pageContext.request.contextPath}/profile">Hồ sơ</a>
                        <a class="nav-link" href="${pageContext.request.contextPath}/logout">Đăng xuất</a>
                    </c:when>
                    <c:when test="${not empty sessionScope.loggedUsername}">
                        <a class="nav-link" href="${pageContext.request.contextPath}/profile">Hồ sơ</a>
                        <a class="nav-link" href="${pageContext.request.contextPath}/logout">Đăng xuất</a>
                    </c:when>
                    <c:otherwise>
                        <a class="nav-link" href="${pageContext.request.contextPath}/login">Đăng nhập</a>
                    </c:otherwise>
                </c:choose>
                </div>
            </nav>
        </div>
    </header>
    <sitemesh:write property="body" />
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/js/bootstrap.bundle.min.js"
            integrity="sha384-FKyoEForCGlyvwx9Hj09JcYn3nv7wiPVlz7YYjRwJrWVcXK/BmnVDxM+D2scQbITxI"
            crossorigin="anonymous"></script>
</body>
</html>
