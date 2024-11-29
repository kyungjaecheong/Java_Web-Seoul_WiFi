<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>북마크 그룹 추가</title>
    <!-- 스타일시트 연결 -->
    <link rel="stylesheet" type="text/css" href="../../css/style.css">
</head>
<body>
<!-- 네비게이션 바 -->
<nav class="navbar">
    <div class="navbar-container">
        <h1 class="logo">Wi-Fi Service</h1>
        <!-- 네비게이션 링크 -->
        <ul class="nav-links">
            <li><a href="../home/home.jsp">Home</a></li>
            <li><a href="../history/history.jsp">위치 History</a></li>
            <li><a href="../bookmark/bookmark.jsp">WiFi Bookmark</a></li>
            <li><a href="bookmark_group.jsp">Bookmark 그룹 관리</a></li>
        </ul>
    </div>
</nav>
<main class="content">
    <!-- 북마크 그룹 추가 폼 -->
    <form action="bookmark_group_add_action.jsp" method="post" class="bookmark-group-add-form">
        <h2>북마크 그룹 추가</h2>  <!-- 페이지 제목 -->

        <!-- 북마크 그룹 이름 입력 -->
        <label for="name">북마크 이름:</label>
        <input type="text" name="name" id="name" required>

        <!-- 순서 입력 -->
        <label for="orderNo">순서:</label>
        <input type="number" name="orderNo" id="orderNo" required>

        <!-- 추가 버튼 -->
        <button type="submit">추가</button>

        <!-- 뒤로가기 링크 -->
        <a href="bookmark_group.jsp" class="back-link">뒤로가기</a>
    </form>
</main>
</body>
</html>
