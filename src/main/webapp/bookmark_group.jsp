<%@ page import="bookmark.BookmarkGroupDBTool" %>
<%@ page import="bookmark.BookmarkGroup" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <title>북마크 그룹 관리</title>
    <link rel="stylesheet" type="text/css" href="css/style.css">
</head>
<body>

<!-- 네비게이션 바 -->
<nav class="navbar">
    <div class="navbar-container">
        <h1 class="logo">Wi-Fi Service</h1>
        <ul class="nav-links">
            <li><a href="home.jsp">Home</a></li>
            <li><a href="history.jsp">위치 History</a></li>
            <li><a href="bookmark.jsp">WiFi Bookmark</a></li>
            <li><a href="bookmark_group.jsp">Bookmark 그룹 관리</a></li>
        </ul>
    </div>
</nav>

<main class="content">
    <h2>북마크 그룹 관리</h2>
    <div class="action-container">
        <a href="bookmark_group_add.jsp" class="add-button">북마크 그룹 추가</a>
        <form action="bookmark_group_delete_all.jsp" method="post" style="display: inline;">
            <button type="submit" class="delete-all-button">전체 삭제</button>
        </form>
    </div>
    <div class="table-container">
        <table>
            <thead>
            <tr>
                <th>ID</th>
                <th>북마크 이름</th>
                <th>순서</th>
                <th>등록일자</th>
                <th>수정일자</th>
                <th>비고</th>
            </tr>
            </thead>
            <tbody>
            <%
                String dbPath = application.getRealPath("/WEB-INF/db/wifiDatabase.db");
                try {
                    List<BookmarkGroup> groups = BookmarkGroupDBTool.getBookmarkGroups(dbPath);
                    for (BookmarkGroup group : groups) {
            %>
            <tr>
                <td><%= group.getId() %></td>
                <td><%= group.getName() %></td>
                <td><%= group.getOrderNo() %></td>
                <td><%= group.getRegisterDttm() %></td>
                <td><%= group.getUpdateDttm() %></td>
                <td class="action-cell">
                    <a href="bookmark_group_edit.jsp?id=<%= group.getId() %>">수정</a> |
                    <a href="bookmark_group_delete.jsp?id=<%= group.getId() %>">삭제</a>
                </td>
            </tr>
            <%
                    }
                } catch (Exception e) {
                    e.printStackTrace(System.err);
                    out.println("<tr><td colspan='6' style='color: red;'>오류가 발생했습니다: " + e.getMessage() + "</td></tr>");
                }
            %>
            </tbody>
        </table>
    </div>
</main>
</body>
</html>
