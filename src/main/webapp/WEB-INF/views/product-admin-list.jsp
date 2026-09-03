<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Quản lý sản phẩm</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/app.css">
</head>
<body>
    <header class="topbar">
        <div class="topbar-inner">
            <a class="brand" href="${pageContext.request.contextPath}/admin/products">
                Quản lý sản phẩm
            </a>
            <nav class="nav">
                <a href="${pageContext.request.contextPath}/admin/categories">Danh mục</a>
                <a class="active" href="${pageContext.request.contextPath}/admin/products">Sản phẩm</a>
                <a href="${pageContext.request.contextPath}/home">Trang chủ</a>
                <a href="${pageContext.request.contextPath}/logout">Đăng xuất</a>
            </nav>
        </div>
    </header>

    <main class="container">
        <div class="page-heading">
            <div>
                <p class="eyebrow">Quản trị</p>
                <h1>Danh sách sản phẩm</h1>
            </div>
            <a class="btn btn-primary"
               href="${pageContext.request.contextPath}/admin/product/add">
                + Thêm sản phẩm
            </a>
        </div>

        <c:if test="${not empty param.message}">
            <div class="alert alert-success">Thao tác thành công.</div>
        </c:if>

        <section class="card">
            <div class="table-wrap">
                <table>
                    <thead>
                        <tr>
                            <th>Sản phẩm</th>
                            <th>Danh mục</th>
                            <th>Giá</th>
                            <th>Kho</th>
                            <th>Trạng thái</th>
                            <th>Thao tác</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach items="${products}" var="product">
                            <tr>
                                <td><strong><c:out value="${product.productName}" /></strong></td>
                                <td><c:out value="${product.category.categoryName}" /></td>
                                <td>
                                    <fmt:formatNumber value="${product.price}" type="number" /> đ
                                </td>
                                <td>${product.stock}</td>
                                <td>
                                    <span class="badge ${product.status eq 1 ? 'badge-success' : 'badge-muted'}">
                                        ${product.status eq 1 ? 'Hoạt động' : 'Khóa'}
                                    </span>
                                </td>
                                <td>
                                    <div class="actions">
                                        <a class="btn btn-secondary btn-sm"
                                           href="${pageContext.request.contextPath}/admin/product/edit?id=${product.productId}">
                                            Sửa
                                        </a>
                                        <a class="btn btn-danger btn-sm"
                                           href="${pageContext.request.contextPath}/admin/product/delete?id=${product.productId}"
                                           onclick="return confirm('Xóa sản phẩm này?')">
                                            Xóa
                                        </a>
                                    </div>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </section>

        <div class="pagination">
            <c:forEach begin="1" end="${totalPages}" var="p">
                <a class="${p eq page ? 'active' : ''}"
                   href="${pageContext.request.contextPath}/admin/products?page=${p}">
                    ${p}
                </a>
            </c:forEach>
        </div>
    </main>
</body>
</html>
