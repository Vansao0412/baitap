<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Hồ sơ tài khoản</title>
</head>
<body>
    <main class="container">
        <div class="page-heading">
            <div>
                <p class="eyebrow">Tài khoản</p>
                <h1>Hồ sơ của tôi</h1>
                <p class="subtitle">Cập nhật thông tin cá nhân và ảnh đại diện.</p>
            </div>
        </div>

        <c:if test="${not empty message}">
            <div class="alert alert-success"><c:out value="${message}" /></div>
        </c:if>
        <c:if test="${not empty error}">
            <div class="alert alert-danger"><c:out value="${error}" /></div>
        </c:if>

        <section class="card profile-card">
            <div class="profile-avatar-wrap">
                <c:choose>
                    <c:when test="${not empty user.image and user.image.startsWith('upload:')}">
                        <img class="profile-avatar"
                             src="${pageContext.request.contextPath}/image?name=${user.image.substring(7)}"
                             alt="Ảnh đại diện">
                    </c:when>
                    <c:when test="${not empty user.image}">
                        <img class="profile-avatar" src="<c:out value='${user.image}' />" alt="Ảnh đại diện">
                    </c:when>
                    <c:otherwise>
                        <div class="profile-avatar profile-avatar-empty">
                            ${user.username.substring(0, 1).toUpperCase()}
                        </div>
                    </c:otherwise>
                </c:choose>
                <div>
                    <h2><c:out value="${user.fullName}" /></h2>
                    <p class="profile-username">@<c:out value="${user.username}" /></p>
                </div>
            </div>

            <form class="profile-form"
                  method="post"
                  action="${pageContext.request.contextPath}/profile"
                  enctype="multipart/form-data">
                <div class="form-grid">
                    <div class="field field-full">
                        <label class="label" for="fullName">Họ và tên</label>
                        <input class="input" id="fullName" name="fullName" type="text"
                               maxlength="100" required
                               value="<c:out value='${user.fullName}' />">
                    </div>
                    <div class="field">
                        <label class="label" for="username">Tên đăng nhập</label>
                        <input class="input" id="username" type="text" readonly
                               value="<c:out value='${user.username}' />">
                    </div>
                    <div class="field">
                        <label class="label" for="email">Email</label>
                        <input class="input" id="email" type="email" readonly value="<c:out value='${user.email}' />">
                    </div>
                    <div class="field field-full">
                        <label class="label" for="phone">Số điện thoại</label>
                        <input class="input" id="phone" name="phone" type="tel"
                               maxlength="20" value="<c:out value='${user.phone}' />"
                               placeholder="Ví dụ: 0901234567">
                    </div>
                    <div class="field field-full">
                        <label class="label" for="image">Ảnh đại diện</label>
                        <input class="input" id="image" name="image" type="file"
                               accept="image/jpeg,image/png,image/gif,image/webp">
                        <p class="hint">JPG, PNG, GIF hoặc WEBP; dung lượng tối đa 5 MB.</p>
                    </div>
                </div>
                <div class="form-actions">
                    <a class="btn btn-secondary" href="${pageContext.request.contextPath}/home">Hủy</a>
                    <button class="btn btn-primary" type="submit">Lưu thay đổi</button>
                </div>
            </form>
        </section>
    </main>
</body>
</html>
