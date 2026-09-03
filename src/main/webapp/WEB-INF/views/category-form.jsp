<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="editing" value="${not empty category and category.categoryId gt 0}" />
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>${editing ? 'Chỉnh sửa' : 'Thêm'} danh mục</title>
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
                <a href="${pageContext.request.contextPath}/logout">Đăng xuất</a>
            </nav>
        </div>
    </header>

    <main class="form-shell">
        <div class="page-heading">
            <div>
                <p class="eyebrow">${editing ? 'Cập nhật' : 'Tạo mới'}</p>
                <h1>${editing ? 'Chỉnh sửa danh mục' : 'Thêm danh mục'}</h1>
                <p class="subtitle">Điền thông tin bên dưới rồi lưu thay đổi.</p>
            </div>
        </div>

        <c:if test="${not empty error}">
            <div class="alert alert-danger">
                <c:out value="${error}" />
            </div>
        </c:if>

        <section class="card form-card">
            <form method="post" enctype="multipart/form-data"
                  action="${pageContext.request.contextPath}/admin/category/${editing ? 'update' : 'insert'}">

                <c:if test="${editing}">
                    <input type="hidden" name="id" value="${category.categoryId}">
                </c:if>

                <div class="form-grid">
                    <div class="field field-full">
                        <label class="label" for="categoryName">Tên danh mục *</label>
                        <input class="input" id="categoryName" name="categoryName"
                               maxlength="50" required autofocus
                               value="<c:out value='${category.categoryName}' />"
                               placeholder="Ví dụ: Điện thoại">
                        <p class="hint">Tối đa 50 ký tự và không được trùng tên.</p>
                    </div>

                    <div class="field">
                        <label class="label" for="images">Đường dẫn ảnh</label>
                        <input class="input" id="images" name="images" maxlength="500"
                               value="<c:out value='${category.images}' />"
                               placeholder="https://example.com/image.jpg">
                        <p class="hint">Dùng URL ảnh có sẵn trên Internet.</p>
                    </div>

                    <div class="field">
                        <label class="label" for="imageFile">Tải ảnh từ máy</label>
                        <input class="input" id="imageFile" name="imageFile"
                               type="file" accept="image/*">
                        <p class="hint">JPG, PNG hoặc WEBP; tối đa 5 MB.</p>
                    </div>

                    <div class="field field-full">
                        <span class="label">Trạng thái</span>
                        <div class="radio-group">
                            <label class="radio-card">
                                <input type="radio" name="status" value="1"
                                       ${empty category or category.status eq 1 ? 'checked' : ''}>
                                <span>
                                    <strong>Hoạt động</strong><br>
                                    <small>Có thể sử dụng ngay</small>
                                </span>
                            </label>

                            <label class="radio-card">
                                <input type="radio" name="status" value="0"
                                       ${not empty category and category.status eq 0 ? 'checked' : ''}>
                                <span>
                                    <strong>Đang khóa</strong><br>
                                    <small>Tạm ẩn danh mục</small>
                                </span>
                            </label>
                        </div>
                    </div>
                </div>

                <div class="form-actions">
                    <a class="btn btn-secondary"
                       href="${pageContext.request.contextPath}/admin/categories">
                        Hủy
                    </a>
                    <button class="btn btn-primary" type="submit">
                        ${editing ? 'Lưu thay đổi' : 'Tạo danh mục'}
                    </button>
                </div>
            </form>
        </section>
    </main>
</body>
</html>
