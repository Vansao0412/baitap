<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<head><title><c:out value="${product.productName}" /></title></head>
<main class="container">
        <p>
            <a href="${pageContext.request.contextPath}/product">Quay lại sản phẩm</a>
        </p>

        <section class="card form-card detail-grid">
            <div>
                <c:choose>
                    <c:when test="${not empty product.image and product.image.startsWith('upload:')}">
                        <img class="detail-image"
                             src="${pageContext.request.contextPath}/image?name=${product.image.substring(7)}"
                             alt="Ảnh sản phẩm">
                    </c:when>
                    <c:when test="${not empty product.image}">
                        <img class="detail-image"
                             src="<c:out value='${product.image}' />"
                             alt="Ảnh sản phẩm">
                    </c:when>
                    <c:otherwise>
                        <div class="product-image">Chưa có ảnh</div>
                    </c:otherwise>
                </c:choose>
            </div>

            <div>
                <p class="eyebrow">
                    <c:out value="${product.category.categoryName}" />
                </p>
                <h1><c:out value="${product.productName}" /></h1>
                <div class="price">
                    <fmt:formatNumber value="${product.price}" type="number" /> đ
                </div>
                <p class="subtitle"><c:out value="${product.description}" /></p>
                <p><strong>Số lượng còn:</strong> ${product.stock}</p>
                <p>
                    <span class="badge ${product.status eq 1 ? 'badge-success' : 'badge-muted'}">
                        ${product.status eq 1 ? 'Đang bán' : 'Ngừng bán'}
                    </span>
                </p>
            </div>
        </section>
</main>
