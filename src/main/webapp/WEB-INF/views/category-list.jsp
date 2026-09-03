<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Quản lý danh mục</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/app.css">
</head>
<body>
    <header class="topbar">
        <div class="topbar-inner">
            <a class="brand" href="${pageContext.request.contextPath}/admin/categories">
                Quản lý danh mục
            </a>
            <nav class="nav">
                <a class="active" href="${pageContext.request.contextPath}/admin/categories">Danh mục</a>
                <a href="${pageContext.request.contextPath}/admin/products">Sản phẩm</a>
                <a href="${pageContext.request.contextPath}/home">Trang chủ</a>
                <a href="${pageContext.request.contextPath}/logout">Đăng xuất</a>
            </nav>
        </div>
    </header>

    <main class="container">
        <div class="page-heading">
            <div>
                <p class="eyebrow">Quản trị</p>
                <h1>Quản lý danh mục</h1>
                <p class="subtitle">Tạo và quản lý các nhóm sản phẩm trong hệ thống.</p>
            </div>
            <a class="btn btn-primary"
               href="${pageContext.request.contextPath}/admin/category/add">
                + Thêm danh mục
            </a>
        </div>

        <c:set var="activeCount" value="0" />
        <c:forEach items="${categories}" var="item">
            <c:if test="${item.status eq 1}">
                <c:set var="activeCount" value="${activeCount + 1}" />
            </c:if>
        </c:forEach>

        <section class="stats">
            <div class="stat-card">
                <div class="stat-label">Tổng danh mục</div>
                <div class="stat-value">${categories.size()}</div>
                <div class="stat-note">Trong kết quả hiện tại</div>
            </div>
            <div class="stat-card">
                <div class="stat-label">Đang hoạt động</div>
                <div class="stat-value">${activeCount}</div>
                <div class="stat-note">Có thể hiển thị công khai</div>
            </div>
            <div class="stat-card">
                <div class="stat-label">Đang khóa</div>
                <div class="stat-value">${categories.size() - activeCount}</div>
                <div class="stat-note">Tạm thời không hoạt động</div>
            </div>
        </section>

        <c:if test="${param.message eq 'success'}">
            <div class="alert alert-success">Danh mục đã được lưu thành công.</div>
        </c:if>
        <c:if test="${param.message eq 'deleted'}">
            <div class="alert alert-success">Danh mục đã được xóa.</div>
        </c:if>

        <section class="card">
            <div class="toolbar">
                <form class="search" method="get"
                      action="${pageContext.request.contextPath}/admin/categories">
                    <input class="input" name="keyword"
                           value="<c:out value='${keyword}' />"
                           placeholder="Tìm theo tên danh mục...">
                    <button class="btn btn-primary" type="submit">Tìm kiếm</button>
                    <c:if test="${not empty keyword}">
                        <a class="btn btn-secondary"
                           href="${pageContext.request.contextPath}/admin/categories">
                            Đặt lại
                        </a>
                    </c:if>
                </form>
            </div>

            <div class="table-wrap">
                <table>
                    <thead>
                        <tr>
                            <th>Danh mục</th>
                            <th>Trạng thái</th>
                            <th style="text-align: right">Thao tác</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach items="${categories}" var="category">
                            <tr>
                                <td>
                                    <div class="category-cell">
                                        <c:choose>
                                            <c:when test="${not empty category.images and category.images.startsWith('upload:')}">
                                                <img class="thumb"
                                                     src="${pageContext.request.contextPath}/image?name=${category.images.substring(7)}"
                                                     alt="Ảnh danh mục">
                                            </c:when>
                                            <c:when test="${not empty category.images}">
                                                <img class="thumb"
                                                     src="<c:out value='${category.images}' />"
                                                     alt="Ảnh danh mục">
                                            </c:when>
                                            <c:otherwise>
                                                <span class="thumb thumb-empty">Ảnh</span>
                                            </c:otherwise>
                                        </c:choose>
                                        <div>
                                            <div class="category-name">
                                                <c:out value="${category.categoryName}" />
                                            </div>
                                            <div class="category-id">Mã #${category.categoryId}</div>
                                        </div>
                                    </div>
                                </td>
                                <td>
                                    <c:choose>
                                        <c:when test="${category.status eq 1}">
                                            <span class="badge badge-success">Hoạt động</span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="badge badge-muted">Đang khóa</span>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td>
                                    <div class="actions">
                                        <a class="btn btn-secondary btn-sm"
                                           href="${pageContext.request.contextPath}/admin/category/edit?id=${category.categoryId}">
                                            Sửa
                                        </a>
                                        <a class="btn btn-danger btn-sm"
                                           href="${pageContext.request.contextPath}/admin/category/delete?id=${category.categoryId}"
                                           onclick="return confirm('Bạn chắc chắn muốn xóa danh mục này?')">
                                            Xóa
                                        </a>
                                    </div>
                                </td>
                            </tr>
                        </c:forEach>

                        <c:if test="${empty categories}">
                            <tr>
                                <td colspan="3">
                                    <div class="empty">
                                        <strong>Không tìm thấy danh mục</strong>
                                        <p>Hãy thử từ khóa khác hoặc tạo danh mục mới.</p>
                                    </div>
                                </td>
                            </tr>
                        </c:if>
                    </tbody>
                </table>
            </div>
        </section>
    </main>
</body>
</html>
