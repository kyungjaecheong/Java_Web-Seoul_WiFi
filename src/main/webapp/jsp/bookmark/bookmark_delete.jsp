<%@ page import="bookmark.BookmarkDBTool" %>
<%@ page import="bookmark.Bookmark" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>북마크 삭제</title>
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
            <li><a href="bookmark.jsp">WiFi Bookmark</a></li>
            <li><a href="../bookmark_group/bookmark_group.jsp">Bookmark 그룹 관리</a></li>
        </ul>
    </div>
</nav>

<main class="content">
    <%
        String idParam = request.getParameter("id");
        String dbPath = application.getRealPath("/WEB-INF/db/wifiDatabase.db");
        int id = Integer.parseInt(idParam);

        try {
            Bookmark bookmark = BookmarkDBTool.getBookmarkById(dbPath, id);
    %>
    <div class="bookmark-delete-container">
        <h2>북마크 삭제</h2>
        <p>정말로 아래 북마크를 삭제하시겠습니까?</p>
        <table class="bookmark-info-table">
            <tr>
                <th>북마크 이름</th>
                <td><%= bookmark.getBookmarkGroupName() %></td>
            </tr>
            <tr>
                <th>와이파이명</th>
                <td><%= bookmark.getWifiName() %></td>
            </tr>
            <tr>
                <th>등록일자</th>
                <td><%= bookmark.getRegisterDttm() %></td>
            </tr>
        </table>
        <form action="bookmark_delete_action.jsp" method="post" class="delete-form">
            <input type="hidden" name="id" value="<%= bookmark.getId() %>">
            <button type="submit" class="delete-confirm-button">삭제</button>
            <a href="bookmark.jsp" class="back-link">취소</a>
        </form>
    </div>
    <%
        } catch (Exception e) {
            e.printStackTrace(System.err);
            out.println("<p style='color: red;'>오류: " + e.getMessage() + "</p>");
        }
    %>
</main>
</body>
</html>
