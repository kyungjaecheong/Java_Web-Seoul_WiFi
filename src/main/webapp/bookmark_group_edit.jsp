<%@ page import="bookmark.BookmarkGroupDBTool" %>
<%@ page import="bookmark.BookmarkGroup" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>북마크 그룹 수정</title>
</head>
<body>
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
<form action="bookmark_group_edit_action.jsp" method="post">
    <input type="hidden" name="id" value="<%= group.getId() %>">
    <label for="name">북마크 이름:</label>
    <input type="text" name="name" id="name" value="<%= group.getName() %>" required>
    <label for="orderNo">순서:</label>
    <input type="number" name="orderNo" id="orderNo" value="<%= group.getOrderNo() %>" required>
    <button type="submit">수정</button>
</form>
<%
    } catch (Exception e) {
        e.printStackTrace(System.err);
        out.println("<p style='color: red;'>오류: " + e.getMessage() + "</p>");
    }
%>
<a href="bookmark_group.jsp">뒤로가기</a>
</body>
</html>
