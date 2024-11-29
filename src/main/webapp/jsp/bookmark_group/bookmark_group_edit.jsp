<%@ page import="bookmark.BookmarkGroupDBTool" %>
<%@ page import="bookmark.BookmarkGroup" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>북마크 그룹 수정</title>
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
<!-- 메인 컨텐츠 -->
<main class="content">
    <%
        // URL로 전달된 그룹 ID를 가져옴
        String idParam = request.getParameter("id");
        String dbPath = application.getRealPath("/WEB-INF/db/wifiDatabase.db");
        int id = Integer.parseInt(idParam); // ID를 정수로 변환

        try {
            // DB에서 해당 ID의 북마크 그룹 정보를 가져옴
            BookmarkGroup group = BookmarkGroupDBTool.getBookmarkGroups(dbPath).stream()
                    .filter(g -> g.getId() == id)   // ID와 일치하는 그룹 필터링
                    .findFirst()    // 첫 번째 결과 반환
                    // 결과가 없으면 예외 발생
                    .orElseThrow(() -> new Exception("ID에 해당하는 북마크 그룹을 찾을 수 없습니다."));
    %>
    <!-- 그룹 수정 폼 -->
    <form action="bookmark_group_edit_action.jsp" method="post" class="bookmark-group-edit-form">
        <h2>북마크 그룹 수정</h2>
        <!-- 그룹 ID 숨김 필드 -->
        <input type="hidden" name="id" value="<%= group.getId() %>">
        <!-- 그룹 이름 -->
        <label for="name">북마크 이름:</label>
        <input type="text" name="name" id="name" value="<%= group.getName() %>" required>
        <!-- 순서 -->
        <label for="orderNo">순서:</label>
        <input type="number" name="orderNo" id="orderNo" value="<%= group.getOrderNo() %>" required>
        <!-- 수정 버튼 -->
        <button type="submit">수정</button>
        <!-- 뒤로가기 링크 -->
        <a href="bookmark_group.jsp" class="back-link">뒤로가기</a>
    </form>
    <%
        } catch (Exception e) {
            e.printStackTrace(System.err);
            out.println("<p style='color: red;'>오류: " + e.getMessage() + "</p>");
        }
    %>
</main>
</body>
</html>
