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
        String idParam = request.getParameter("id");
        String dbPath = application.getRealPath("/WEB-INF/db/wifiDatabase.db");
        int id = Integer.parseInt(idParam);

        try {
            BookmarkGroup group = BookmarkGroupDBTool.getBookmarkGroups(dbPath).stream()
                    .filter(g -> g.getId() == id)
                    .findFirst()
                    .orElseThrow(() -> new Exception("ID에 해당하는 북마크 그룹을 찾을 수 없습니다."));
    %>
    <form action="bookmark_group_edit_action.jsp" method="post" class="bookmark-group-edit-form">
        <h2>북마크 그룹 수정</h2>
        <input type="hidden" name="id" value="<%= group.getId() %>">
        <label for="name">북마크 이름:</label>
        <input type="text" name="name" id="name" value="<%= group.getName() %>" required>
        <label for="orderNo">순서:</label>
        <input type="number" name="orderNo" id="orderNo" value="<%= group.getOrderNo() %>" required>
        <button type="submit">수정</button>
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
