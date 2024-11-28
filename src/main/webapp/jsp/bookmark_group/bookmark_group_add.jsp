<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>북마크 그룹 추가</title>
    <link rel="stylesheet" type="text/css" href="../../css/style.css">
</head>
<body>
<!-- 네비게이션 바 -->
<nav class="navbar">
    <div class="navbar-container">
        <h1 class="logo">Wi-Fi Service</h1>
        <ul class="nav-links">
            <li><a href="../home/home.jsp">Home</a></li>
            <li><a href="../history/history.jsp">위치 History</a></li>
            <li><a href="../bookmark/bookmark.jsp">WiFi Bookmark</a></li>
            <li><a href="bookmark_group.jsp">Bookmark 그룹 관리</a></li>
        </ul>
    </div>
</nav>
<main class="content">
    <form action="bookmark_group_add_action.jsp" method="post" class="bookmark-group-add-form">
        <h2>북마크 그룹 추가</h2>
        <label for="name">북마크 이름:</label>
        <input type="text" name="name" id="name" required>
        <label for="orderNo">순서:</label>
        <input type="number" name="orderNo" id="orderNo" required>
        <button type="submit">추가</button>
        <a href="bookmark_group.jsp" class="back-link">뒤로가기</a>
    </form>
</main>
</body>
</html>
