<%@ page import="bookmark.BookmarkDBTool" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String dbPath = application.getRealPath("/WEB-INF/db/wifiDatabase.db");
    try {
        boolean success = BookmarkDBTool.deleteAllBookmarks(dbPath);
        if (success) {
            response.sendRedirect("bookmark.jsp");
        } else {
            out.println("<p style='color: red;'>전체 삭제에 실패했습니다.</p>");
        }
    } catch (Exception e) {
        e.printStackTrace(System.err);
        out.println("<p style='color: red;'>오류 발생: " + e.getMessage() + "</p>");
    }
%>
