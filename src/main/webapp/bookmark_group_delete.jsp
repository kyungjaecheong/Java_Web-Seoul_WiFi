<%@ page import="bookmark.BookmarkGroupDBTool" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String idParam = request.getParameter("id");
    String dbPath = application.getRealPath("/WEB-INF/db/wifiDatabase.db");

    int id = Integer.parseInt(idParam);

    try {
        boolean success = BookmarkGroupDBTool.deleteBookmarkGroup(dbPath, id);
        if (success) {
            response.sendRedirect("bookmark_group.jsp");
        } else {
            out.println("<p style='color: red;'>삭제 실패</p>");
        }
    } catch (Exception e) {
        e.printStackTrace(System.err);
        out.println("<p style='color: red;'>오류: " + e.getMessage() + "</p>");
    }
%>
