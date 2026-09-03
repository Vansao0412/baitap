<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Tất cả sản phẩm</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/app.css">
</head>
<body>
    <header class="site-header">
        <div class="site-header-inner">
            <a class="brand" href="${pageContext.request.contextPath}/home">Cửa hàng</a>
            <nav class="nav">
                <a href="${pageContext.request.contextPath}/home">Trang chủ</a>
                <a class="active" href="${pageContext.request.contextPath}/product">Sản phẩm</a>
                <c:choose>
                    <c:when test="${sessionScope.loggedRole eq 'ADMIN'}">
                        <a href="${pageContext.request.contextPath}/admin/products">Quản trị</a>
                        <a href="${pageContext.request.contextPath}/profile">Hồ sơ</a>
                        <a href="${pageContext.request.contextPath}/logout">Đăng xuất</a>
                    </c:when>
                    <c:when test="${not empty sessionScope.loggedUsername}">
                        <a href="${pageContext.request.contextPath}/profile">Hồ sơ</a>
                        <a href="${pageContext.request.contextPath}/logout">Đăng xuất</a>
                    </c:when>
                    <c:otherwise>
                        <a href="${pageContext.request.contextPath}/login">Đăng nhập</a>
                    </c:otherwise>
                </c:choose>
            </nav>
        </div>
    </header>

    <main class="container">
        <div class="page-heading">
            <div>
                <p class="eyebrow">Danh sách</p>
                <h1>Tất cả sản phẩm</h1>
                <p class="subtitle">Hiển thị 6 sản phẩm trên mỗi trang.</p>
            </div>
        </div>

        <div class="product-grid">
            <c:forEach items="${products}" var="product">
                <article class="product-card">
                    <a href="${pageContext.request.contextPath}/product?id=${product.productId}">
                        <c:choose>
                            <c:when test="${not empty product.image and product.image.startsWith('upload:')}">
                                <img class="product-image"
                                     src="${pageContext.request.contextPath}/image?name=${product.image.substring(7)}"
                                     alt="Ảnh sản phẩm">
                            </c:when>
                            <c:when test="${not empty product.image}">
                                <img class="product-image"
                                     src="<c:out value='${product.image}' />"
                                     alt="Ảnh sản phẩm">
                            </c:when>
                            <c:otherwise>
                                <div class="product-image">Chưa có ảnh</div>
                            </c:otherwise>
                        </c:choose>
                    </a>

                    <div class="product-body">
                        <div class="product-meta">
                            <c:out value="${product.category.categoryName}" />
                        </div>
                        <h3>
                            <a href="${pageContext.request.contextPath}/product?id=${product.productId}">
                                <c:out value="${product.productName}" />
                            </a>
                        </h3>
                        <div class="price">
                            <fmt:formatNumber value="${product.price}" type="number" /> đ
                        </div>
                        <a class="btn btn-secondary btn-sm"
                           href="${pageContext.request.contextPath}/product?id=${product.productId}">
                            Chi tiết
                        </a>
                    </div>
                </article>
            </c:forEach>

            <c:if test="${empty products}">
                <div class="empty">Chưa có sản phẩm.</div>
            </c:if>
        </div>

        <div class="pagination">
            <c:forEach begin="1" end="${totalPages}" var="p">
                <a class="${p eq page ? 'active' : ''}"
                   href="${pageContext.request.contextPath}/product?page=${p}">
                    ${p}
                </a>
            </c:forEach>
        </div>
    </main>
</body>
</html>
