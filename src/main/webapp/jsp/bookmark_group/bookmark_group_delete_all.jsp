<%@ page import="bookmark.BookmarkGroupDBTool" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    String dbPath = application.getRealPath("/WEB-INF/db/wifiDatabase.db");

    try {
        boolean success = BookmarkGroupDBTool.deleteAllBookmarkGroups(dbPath);
        if (success) {
            response.sendRedirect("bookmark_group.jsp");
        } else {
            out.println("<p>전체 삭제에 실패했습니다.</p>");
        }
    } catch (Exception e) {
        e.printStackTrace(System.err);
        out.println("<p>오류: " + e.getMessage() + "</p>");
    }
%>
