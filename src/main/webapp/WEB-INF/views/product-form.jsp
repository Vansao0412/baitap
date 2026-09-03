<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="editing" value="${product.productId gt 0}" />
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>${editing ? 'Sửa' : 'Thêm'} sản phẩm</title>
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
            </nav>
        </div>
    </header>

    <main class="form-shell">
        <div class="page-heading">
            <h1>${editing ? 'Sửa sản phẩm' : 'Thêm sản phẩm'}</h1>
        </div>

        <c:if test="${not empty error}">
            <div class="alert alert-danger">
                <c:out value="${error}" />
            </div>
        </c:if>

        <section class="card form-card">
            <form method="post" enctype="multipart/form-data"
                  action="${pageContext.request.contextPath}/admin/product/${editing ? 'update' : 'insert'}">

                <c:if test="${editing}">
                    <input type="hidden" name="id" value="${product.productId}">
                </c:if>

                <div class="form-grid">
                    <div class="field field-full">
                        <label class="label">Tên sản phẩm</label>
                        <input class="input" name="productName" maxlength="150" required
                               value="<c:out value='${product.productName}' />">
                    </div>

                    <div class="field">
                        <label class="label">Danh mục</label>
                        <select class="input" name="categoryId" required>
                            <option value="">-- Chọn danh mục --</option>
                            <c:forEach items="${categories}" var="category">
                                <option value="${category.categoryId}"
                                        ${product.category.categoryId eq category.categoryId ? 'selected' : ''}>
                                    <c:out value="${category.categoryName}" />
                                </option>
                            </c:forEach>
                        </select>
                    </div>

                    <div class="field">
                        <label class="label">Giá</label>
                        <input class="input" type="number" name="price" min="0" step="0.01"
                               required value="${product.price}">
                    </div>

                    <div class="field">
                        <label class="label">Số lượng</label>
                        <input class="input" type="number" name="stock" min="0"
                               required value="${product.stock}">
                    </div>

                    <div class="field">
                        <label class="label">Link ảnh</label>
                        <input class="input" name="image" maxlength="500"
                               value="<c:out value='${product.image}' />">
                    </div>

                    <div class="field">
                        <label class="label">Hoặc tải ảnh từ máy</label>
                        <input class="input" type="file" name="imageFile" accept="image/*">
                    </div>

                    <div class="field field-full">
                        <label class="label">Mô tả</label>
                        <textarea class="input" name="description" rows="5"><c:out value="${product.description}" /></textarea>
                    </div>

                    <div class="field field-full">
                        <label class="radio-card">
                            <input type="checkbox" name="status" value="1"
                                   ${product.status eq 1 ? 'checked' : ''}>
                            Đang bán
                        </label>
                    </div>
                </div>

                <div class="form-actions">
                    <a class="btn btn-secondary"
                       href="${pageContext.request.contextPath}/admin/products">
                        Hủy
                    </a>
                    <button class="btn btn-primary">Lưu sản phẩm</button>
                </div>
            </form>
        </section>
    </main>
</body>
</html>
